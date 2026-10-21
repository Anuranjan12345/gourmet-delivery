
const API_BASE_URL = 'api';

// Toast Notification System
const Toast = {
    show: (message, type = 'success') => {
        let container = document.getElementById('toast-container');

        if (!container) {
            container = document.createElement('div');
            container.id = 'toast-container';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `toast ${type}`;

        const icon = type === 'success' ? '✓' : '✕';

        const iconSpan = document.createElement('span');
        iconSpan.style.fontWeight = 'bold';
        iconSpan.style.fontSize = '1.2rem';
        iconSpan.textContent = icon;

        const messageSpan = document.createElement('span');
        messageSpan.textContent = message;

        toast.append(iconSpan, messageSpan);
        container.appendChild(toast);

        setTimeout(() => {
            toast.classList.add('fade-out');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }
};

// Backend API helper
async function apiFetch(endpoint, options = {}) {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

    if (!response.ok) {
        throw new Error(`Backend request failed: ${response.status}`);
    }

    const contentType = response.headers.get('content-type');

    if (contentType && contentType.includes('application/json')) {
        return await response.json();
    }

    return null;
}

// Authentication
const AuthService = {
    login: async (email, password) => {
        const formData = new URLSearchParams();
        formData.append('email', email);
        formData.append('password', password);

        const data = await apiFetch('/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        });

        localStorage.setItem('user', JSON.stringify(data));
        localStorage.setItem('token', 'logged-in');

        if (data.id != null) {
            localStorage.setItem('userId', String(data.id));
        }

        return data;
    },

    signup: async (name, email, password) => {
        const formData = new URLSearchParams();
        formData.append('name', name);
        formData.append('email', email);
        formData.append('password', password);

        return await apiFetch('/auth/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        });
    },

    logout: () => {
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
        window.location.href = 'login.html';
    },

    getUser: () => {
        const user = localStorage.getItem('user');

        if (!user || user === 'undefined') {
            return null;
        }

        try {
            return JSON.parse(user);
        } catch (error) {
            console.error('Invalid stored user data:', error);
            return null;
        }
    },

    isAuthenticated: () => {
        return !!localStorage.getItem('token');
    }
};

// Cart Management
const CartService = {
    getUserId: () => {
        const storedId = localStorage.getItem('userId');

        if (storedId) {
            return storedId;
        }

        const user = AuthService.getUser();

        if (user && user.id != null) {
            const userId = String(user.id);
            localStorage.setItem('userId', userId);
            return userId;
        }

        return null;
    },

    getCart: async () => {
        const userId = CartService.getUserId();

        if (!userId) {
            return [];
        }

        return await apiFetch(`/cart/${userId}`);
    },

    addToCart: async (item) => {
        const userId = CartService.getUserId();

        if (!userId) {
            Toast.show('Please login first', 'error');
            return;
        }

        const formData = new URLSearchParams();
        formData.append('userId', userId);
        formData.append('foodId', item.id);
        formData.append('quantity', '1');

        await apiFetch('/cart', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        });

        await updateCartCount();

        // Keep only one success notification here.
        Toast.show(`${item.name} added to cart`);
    },

    removeFromCart: async (cartId) => {
        await apiFetch(`/cart/${cartId}`, {
            method: 'DELETE'
        });

        await updateCartCount();
    },

    updateQuantity: async (cartId, quantity) => {
        if (quantity <= 0) {
            return CartService.removeFromCart(cartId);
        }

        const formData = new URLSearchParams();
        formData.append('cartId', cartId);
        formData.append('quantity', String(quantity));

        await apiFetch('/cart', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        });

        await updateCartCount();
    },

    clearCart: async () => {
        const userId = CartService.getUserId();

        if (!userId) {
            return;
        }

        await apiFetch(`/cart/clear/${userId}`, {
            method: 'DELETE'
        });

        await updateCartCount();
    }
};

// Navbar
function updateNavbar() {
    const user = AuthService.getUser();
    const navContainer = document.getElementById('navbar-links');

    if (!navContainer) {
        return;
    }

    if (user) {
        navContainer.innerHTML = `
            <li>
                <a href="index.html" class="nav-link">Home</a>
            </li>
            <li>
                <a href="cart.html" class="nav-link">
                    🛒 Cart
                    <span id="cart-count" class="badge">0</span>
                </a>
            </li>
            <li>
                <a href="#" class="nav-link" id="profile-link">
                    👤 Profile
                </a>
            </li>
        `;

        updateCartCount();
    } else {
        navContainer.innerHTML = `
            <li>
                <a href="login.html" class="btn btn-sm btn-secondary">Login</a>
            </li>
            <li>
                <a href="signup.html" class="btn btn-sm btn-primary">Sign Up</a>
            </li>
        `;
    }
}

// Update the cart badge
async function updateCartCount() {
    const countSpan = document.getElementById('cart-count');

    if (!countSpan) {
        return;
    }

    try {
        const cart = await CartService.getCart();

        const total = cart.reduce(
            (sum, item) => sum + Number(item.quantity || 0),
            0
        );

        countSpan.textContent = total;
        countSpan.style.display = total > 0 ? 'inline-block' : 'none';
    } catch (error) {
        console.error('Failed to update cart count:', error);
        countSpan.textContent = '0';
        countSpan.style.display = 'none';
    }
}

// Redirect unauthenticated users
function requireAuth() {
    if (!AuthService.isAuthenticated()) {
        window.location.href = 'login.html';
    }
}

// Load and display cart contents
async function loadCart() {
    const container = document.getElementById('cart-container');

    if (!container) {
        return;
    }

    container.innerHTML = '<p>Loading cart...</p>';

    try {
        const cart = await CartService.getCart();

        console.log('Cart data received:', cart);

        if (!Array.isArray(cart) || cart.length === 0) {
            container.innerHTML = `
                <p>Your cart is empty.</p>
                <a href="menu.html">Browse food</a>
            `;
            return;
        }

        // Temporary diagnostic display until CartServlet returns food details.
        container.innerHTML = '';

        const list = document.createElement('div');
        list.className = 'cart-items';

        cart.forEach(item => {
            const row = document.createElement('div');
            row.className = 'cart-item';

            const details = document.createElement('p');
            details.textContent =
                `Food ID: ${item.foodId} | Quantity: ${item.quantity}`;

            row.appendChild(details);
            list.appendChild(row);
        });

        container.appendChild(list);

    } catch (error) {
        console.error('Failed to load cart:', error);
        container.innerHTML = `
            <p>Unable to load your cart. Please try again.</p>
        `;
    }
}

// Initialize the page
document.addEventListener('DOMContentLoaded', () => {
    updateNavbar();

    // Only load cart data on pages that contain the cart container.
    if (document.getElementById('cart-container')) {
        loadCart();
    }
});
