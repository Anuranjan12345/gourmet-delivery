# Manual Testing Checklist

Run these tests after changing code, rebuilding the WAR, or redeploying the application.

## Main Pages

- [ ] Home page loads and navigation links work.
- [ ] Signup validates input and creates an account.
- [ ] Login succeeds with valid credentials.
- [ ] Invalid login is handled without exposing sensitive details.
- [ ] Food menu displays food names, prices, and images.
- [ ] Add to Cart works for more than one food item.
- [ ] Cart quantity controls update the displayed quantities and totals.
- [ ] Removing an item works.
- [ ] Empty-cart state displays correctly.
- [ ] Checkout displays the expected cart summary.
- [ ] A delivery address is required as intended.
- [ ] Successful checkout creates an order.
- [ ] My Orders displays the newly created order and its items.
- [ ] Profile page displays the correct user's details.
- [ ] Logout and access restrictions behave as intended, if implemented.

## Regression Rule

After a code change, test the affected feature first. Then retest the main user journey:

Login → Menu → Cart → Checkout → My Orders → Profile.

Do not publish a change if a previously working feature has regressed. Record the failed step and the error before making another change.
