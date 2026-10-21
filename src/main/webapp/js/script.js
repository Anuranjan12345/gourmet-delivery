
const API_BASE_URL = 'api';


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

        const iconElement = document.createElement('span');
        iconElement.style.fontWeight = 'bold';
        iconElement.style.fontSize = '1.2rem';
        iconElement.textContent = icon;

        const messageElement = document.createElement('span');
        messageElement.textContent = message;

        toast.append(iconElement, messageElement);
        container.appendChild(toast);

        setTimeout(() => {
            toast.classList.add('fade-out');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }
};

async function apiFetch(endpoint, options = {}) {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(
            `Backend request failed: ${response.status} ${errorText}`
        );
    }

    const contentType = response.headers.get('content-type');

    if (contentType && contentType.includes('application/json')) {
        return await response.json();
    }

    return null;
}
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

        if (data && data.id != null) {
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
            console.error('Failed to read saved user:', error);
            return null;
        }
    },

    isAuthenticated: () => {
        return !!localStorage.getItem('token');
    }
};

const CartService = {
    getUserId: () => {
        const userId = localStorage.getItem('userId');

        if (userId) {
            return userId;
        }

        const user = AuthService.getUser();

        if (user && user.id != null) {
            return String(user.id);
        }

        return null;
    },

    getCart: async () => {
        const userId = CartService.getUserId();

        if (!userId) {
            return [];
        }

        const cart = await apiFetch(`/cart/${encodeURIComponent(userId)}`);

        return Array.isArray(cart) ? cart : [];
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
        return await CartService.removeFromCart(cartId);
    }

    const endpoint =
        `/cart?cartId=${encodeURIComponent(cartId)}` +
        `&quantity=${encodeURIComponent(quantity)}`;

    await apiFetch(endpoint, {
        method: 'PUT'
    });

    await updateCartCount();
},


    clearCart: async () => {
        const userId = CartService.getUserId();

        if (!userId) {
            return;
        }

        // This endpoint must exist in the backend to work.
        await apiFetch(`/cart/clear/${encodeURIComponent(userId)}`, {
            method: 'DELETE'
        });

        await updateCartCount();
    }
};

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
        <a href="orders.html" class="nav-link">
            📦 My Orders
        </a>
    </li>

    <li>
        <a href="profile.html" class="nav-link" id="profile-link">
            👤 Profile
        </a>
    </li>
`;
        updateCartCount();
    } else {
        navContainer.innerHTML = `
            <li>
                <a href="login.html" class="btn btn-sm btn-secondary">
                    Login
                </a>
            </li>
            <li>
                <a href="signup.html" class="btn btn-sm btn-primary">
                    Sign Up
                </a>
            </li>
        `;
    }
}

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


function requireAuth() {
    if (!AuthService.isAuthenticated()) {
        window.location.href = 'login.html';
    }
}


function escapeHTML(value) {
    return String(value ?? '').replace(/[&<>"']/g, character => {
        const entities = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#39;'
        };

        return entities[character];
    });
}
function formatPrice(value) {
    const price = Number(value);

    if (!Number.isFinite(price)) {
        return '₹0.00';
    }

    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        minimumFractionDigits: 2
    }).format(price);
}
async function loadCart() {
    const container = document.getElementById('cart-container');

    if (!container) {
        return;
    }

    container.innerHTML = '<p>Loading your cart...</p>';

   
try {
    const cart = await CartService.getCart();

    if (!cart || cart.length === 0) {
        container.innerHTML = `
            <div class="cart-empty-state">
                <div class="cart-empty-icon" aria-hidden="true">
                    🛒
                </div>

                <p class="eyebrow">A LITTLE SOMETHING IS MISSING</p>

                <h2>Your cart is waiting for something delicious!</h2>

                <p>
                    Looks like you haven't added anything yet.
                    Explore our restaurants and discover your next favourite meal.
                </p>

                <a href="index.html" class="cart-browse-button">
                    Browse Restaurants <span aria-hidden="true">→</span>
                </a>

                
            </div>
        `;

        return;
    }


        let total = 0;

        const itemsHTML = cart.map(item => {
            const quantity = Number(item.quantity || 0);
            const price = Number(item.foodPrice || 0);
            const subtotal = Number.isFinite(Number(item.subtotal))
                ? Number(item.subtotal)
                : price * quantity;

            total += subtotal;

           const foodName = String(item.foodName || '').toLowerCase();

const imageUrl = item.foodImageUrl
    ? escapeHTML(item.foodImageUrl)
    : '';
            const imageHTML = imageUrl
                ? `<img
                        class="cart-food-image"
                        src="${imageUrl}"
                        alt="${escapeHTML(item.foodName || 'Food item')}"
                        onerror="this.style.display='none'"
                   >`
                : '';

            return `
                <article class="cart-item" data-cart-id="${Number(item.id)}">
                    ${imageHTML}

                    <div class="cart-item-details">
                        <h3>${escapeHTML(item.foodName || `Food #${item.foodId}`)}</h3>

                        <p>${escapeHTML(item.foodDescription || '')}</p>

                        <p class="cart-item-price">
                            ${formatPrice(price)}
                        </p>

                        <div class="cart-quantity-controls">
                            <button
                                type="button"
                                class="quantity-btn"
                                data-action="decrease"
                                data-cart-id="${Number(item.id)}"
                                data-quantity="${quantity}"
                                aria-label="Decrease quantity"
                            >−</button>

                            <span class="cart-quantity">${quantity}</span>

                            <button
                                type="button"
                                class="quantity-btn"
                                data-action="increase"
                                data-cart-id="${Number(item.id)}"
                                data-quantity="${quantity}"
                                aria-label="Increase quantity"
                            >+</button>
                        </div>
                    </div>

                    <div class="cart-item-subtotal">
                        <strong>${formatPrice(subtotal)}</strong>

                        <button
                            type="button"
                            class="remove-cart-item"
                            data-action="remove"
                            data-cart-id="${Number(item.id)}"
                        >Remove</button>
                    </div>
                </article>
            `;
        }).join('');

        container.innerHTML = `
            <div class="cart-items">
                ${itemsHTML}
            </div>

            <section class="cart-summary">
                <h2>Order Summary</h2>

                <div class="cart-total-row">
                    <span>Total</span>
                    <strong>${formatPrice(total)}</strong>
                </div>

               <a href="menu.html" class="btn btn-secondary">
    Continue Shopping
</a>

<a href="checkout.html" class="btn btn-primary">
    Proceed to Checkout
</a>
            </section>
        `;

    } catch (error) {
        console.error('Failed to load cart:', error);

        container.innerHTML = `
            <div class="cart-error">
                <p>We couldn't load your cart.</p>
                <button type="button" id="retry-cart" class="btn btn-primary">
                    Try Again
                </button>
            </div>
        `;
    }
}

document.addEventListener('click', async event => {
    const button = event.target.closest('[data-action]');

    if (!button) {
        return;
    }

    const action = button.dataset.action;
    const cartId = button.dataset.cartId;

    if (!cartId) {
        return;
    }

    button.disabled = true;

    try {
        if (action === 'remove') {
            await CartService.removeFromCart(cartId);
            Toast.show('Item removed from cart');
        } else if (action === 'increase' || action === 'decrease') {
            const currentQuantity = Number(button.dataset.quantity || 1);

            const newQuantity = action === 'increase'
                ? currentQuantity + 1
                : currentQuantity - 1;

            if (newQuantity <= 0) {
                await CartService.removeFromCart(cartId);
                Toast.show('Item removed from cart');
            } else {
                await CartService.updateQuantity(cartId, newQuantity);
                Toast.show('Cart quantity updated');
            }
        } else {
            return;
        }

        await loadCart();
    } catch (error) {
        console.error('Cart action failed:', error);
        Toast.show('Unable to update cart. Please try again.', 'error');
    } finally {
        button.disabled = false;
    }
});

document.addEventListener('click', event => {
    if (event.target.closest('#retry-cart')) {
        loadCart();
    }
});


document.addEventListener('DOMContentLoaded', () => {
    updateNavbar();

    if (document.getElementById('cart-container')) {
        loadCart();
    }
});
