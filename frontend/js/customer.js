const customer = requireRole("CUSTOMER");

const MENU_ITEMS = [
    {
        id: "chicken-rice",
        name: "Chicken Rice",
        category: "Rice bowl",
        description: "Tender chicken, fragrant rice, and house soy.",
        price: 2.00,
        badge: "Popular"
    },
    {
        id: "nasi-lemak",
        name: "Nasi Lemak",
        category: "Local favourite",
        description: "Coconut rice with sambal, egg, and crunchy anchovies.",
        price: 7.50,
        badge: "Local"
    },
    {
        id: "mee-goreng",
        name: "Mee Goreng",
        category: "Wok favourite",
        description: "Smoky wok-fried noodles with vegetables and egg.",
        price: 6.50,
        badge: "Wok fired"
    },
    {
        id: "fish-and-chips",
        name: "Fish & Chips",
        category: "House special",
        description: "Crisp fish, golden fries, and a bright herb dip.",
        price: 12.90,
        badge: "Special"
    },
    {
        id: "beef-burger",
        name: "Beef Burger",
        category: "Handheld",
        description: "Grilled beef, melted cheese, lettuce, and pickles.",
        price: 10.90,
        badge: "Classic"
    },
    {
        id: "iced-lemon-tea",
        name: "Iced Lemon Tea",
        category: "Cold drink",
        description: "Freshly brewed tea with lemon and a little sparkle.",
        price: 2.50,
        badge: "Cool down"
    }
];

const cart = new Map();

if (customer) {
    document.addEventListener("DOMContentLoaded", () => {
        document.getElementById("customer-name").textContent = customer.name;
        document.getElementById("logout-button").addEventListener("click", logout);
        document.getElementById("refresh-orders").addEventListener("click", loadCustomerOrders);
        document.getElementById("customer-tabs").addEventListener("click", handleCustomerTabChange);
        document.getElementById("menu-grid").addEventListener("click", handleMenuAction);
        document.getElementById("checkout-form").addEventListener("submit", createOrder);

        renderMenu();
        renderCheckout();
        loadCustomerOrders();
    });
}

function handleCustomerTabChange(event) {
    const tab = event.target.closest("[data-customer-tab]");
    if (!tab) return;
    activateCustomerTab(tab.dataset.customerTab);
}

function activateCustomerTab(tabName) {
    document.querySelectorAll("[data-customer-tab]").forEach(tab => {
        const isActive = tab.dataset.customerTab === tabName;
        tab.setAttribute("aria-selected", String(isActive));
    });

    document.querySelectorAll(".customer-tab-panel").forEach(panel => {
        panel.hidden = panel.id !== `customer-${tabName}-panel`;
    });
}

function updateCustomerOrderCount(count) {
    const countElement = document.getElementById("customer-orders-count");
    if (countElement) countElement.textContent = count;
}

function getCartItems() {
    return MENU_ITEMS
        .filter(item => cart.has(item.id))
        .map(item => ({ item, quantity: cart.get(item.id) }));
}

function getCartTotal(cartItems) {
    return Number(cartItems
        .reduce((sum, entry) => sum + entry.item.price * entry.quantity, 0)
        .toFixed(2));
}

function renderMenu() {
    const menuGrid = document.getElementById("menu-grid");
    if (!menuGrid) return;

    menuGrid.innerHTML = MENU_ITEMS.map(item => {
        const quantity = cart.get(item.id) || 0;
        const isSelected = quantity > 0;
        const footer = isSelected
            ? `
                <div class="menu-card__selected-control">
                    <div class="quantity-control menu-quantity" aria-label="${escapeHtml(item.name)} quantity">
                        <button class="quantity-button" type="button" data-menu-action="decrease" data-menu-item="${escapeHtml(item.id)}" aria-label="Decrease ${escapeHtml(item.name)} quantity">−</button>
                        <output class="quantity-value" aria-live="polite">${quantity}</output>
                        <button class="quantity-button" type="button" data-menu-action="increase" data-menu-item="${escapeHtml(item.id)}" aria-label="Increase ${escapeHtml(item.name)} quantity">+</button>
                    </div>
                    <span class="menu-card__selected-label">In order</span>
                </div>
            `
            : `
                <button class="button menu-card__choose" type="button" data-menu-action="increase" data-menu-item="${escapeHtml(item.id)}">
                    Add to order
                </button>
            `;

        return `
            <article class="menu-card ${isSelected ? "selected" : ""}">
                <div class="menu-card__top">
                    <span class="menu-card__badge">${escapeHtml(item.badge)}</span>
                    <span class="menu-card__price">${formatCurrency(item.price)}</span>
                </div>
                <div class="menu-card__body">
                    <span class="menu-card__category">${escapeHtml(item.category)}</span>
                    <h3>${escapeHtml(item.name)}</h3>
                    <p>${escapeHtml(item.description)}</p>
                </div>
                <div class="menu-card__footer">${footer}</div>
            </article>
        `;
    }).join("");
}

function handleMenuAction(event) {
    const control = event.target.closest("[data-menu-action]");
    if (!control) return;

    const itemId = control.dataset.menuItem;
    const currentQuantity = cart.get(itemId) || 0;
    const nextQuantity = control.dataset.menuAction === "increase"
        ? Math.min(currentQuantity + 1, 10)
        : Math.max(currentQuantity - 1, 0);

    if (nextQuantity === 0) {
        cart.delete(itemId);
    } else {
        cart.set(itemId, nextQuantity);
    }

    renderMenu();
    renderCheckout();
}

function renderCheckout() {
    const emptyState = document.getElementById("checkout-empty");
    const selectedState = document.getElementById("checkout-selected");
    const checkoutButton = document.getElementById("checkout-button");
    const checkoutButtonTotal = document.getElementById("checkout-button-total");
    const checkoutItems = document.getElementById("checkout-items");
    const cartCount = document.getElementById("cart-count");

    if (!emptyState || !selectedState || !checkoutButton || !checkoutButtonTotal || !checkoutItems || !cartCount) return;

    const cartItems = getCartItems();
    const hasItems = cartItems.length > 0;
    const totalQuantity = cartItems.reduce((sum, entry) => sum + entry.quantity, 0);
    const totalAmount = getCartTotal(cartItems);

    emptyState.hidden = hasItems;
    selectedState.hidden = !hasItems;
    checkoutButton.disabled = !hasItems;
    checkoutButtonTotal.textContent = formatCurrency(totalAmount);

    if (!hasItems) {
        cartCount.textContent = "0 items";
        checkoutItems.innerHTML = "";
        return;
    }

    cartCount.textContent = `${totalQuantity} ${totalQuantity === 1 ? "item" : "items"}`;
    checkoutItems.innerHTML = cartItems.map(({ item, quantity }) => `
        <div class="checkout-item">
            <div class="checkout-item__copy">
                <strong>${escapeHtml(item.name)}</strong>
                <span>${quantity} × ${formatCurrency(item.price)}</span>
            </div>
            <strong>${formatCurrency(item.price * quantity)}</strong>
        </div>
    `).join("");
    document.getElementById("order-total").textContent = formatCurrency(totalAmount);
}

async function createOrder(event) {
    event.preventDefault();
    const cartItems = getCartItems();
    if (!cartItems.length) {
        showMessage("customer-message", "Add one or more menu items before checking out.", "error");
        return;
    }

    const form = event.currentTarget;
    const formData = new FormData(form);
    const totalQuantity = cartItems.reduce((sum, entry) => sum + entry.quantity, 0);
    const totalAmount = getCartTotal(cartItems);
    const itemSummary = cartItems
        .map(({ item, quantity }) => `${item.name} x${quantity}`)
        .join(" + ");

    showMessage("customer-message", "Checking out...");

    try {
        await requestJson(`${ORDER_SERVICE_URL}/api/orders`, {
            method: "POST",
            body: JSON.stringify({
                customerId: Number(customer.userId),
                customerName: customer.name,
                customerEmail: customer.email,
                foodItem: itemSummary,
                quantity: totalQuantity,
                totalAmount,
                paymentMethod: formData.get("paymentMethod"),
                deliveryAddress: formData.get("deliveryAddress")
            })
        });

        form.reset();
        cart.clear();
        renderMenu();
        renderCheckout();
        showMessage("customer-message", "Order placed. Payment and delivery updates will appear here.", "success");
        await loadCustomerOrders();
    } catch (error) {
        showMessage("customer-message", error.message, "error");
    }
}

async function loadCustomerOrders() {
    const body = document.getElementById("orders-body");
    body.innerHTML = '<tr><td colspan="9" class="empty-cell">Loading orders...</td></tr>';

    try {
        const orders = await requestJson(`${ORDER_SERVICE_URL}/api/orders/customer/${customer.userId}`);
        updateCustomerOrderCount(orders.length);
        if (!orders.length) {
            body.innerHTML = '<tr><td colspan="9" class="empty-cell">No orders yet. Your next meal starts above.</td></tr>';
            return;
        }

        const enrichedOrders = await Promise.all(orders.map(async order => {
            const [payment, delivery] = await Promise.all([
                optionalJson(`${PAYMENT_SERVICE_URL}/api/payments/order/${order.orderId}`),
                optionalJson(`${DELIVERY_SERVICE_URL}/api/deliveries/order/${order.orderId}`)
            ]);
            return { order, payment, delivery };
        }));

        body.innerHTML = enrichedOrders.map(({ order, payment, delivery }) => {
            const paymentStatus = payment?.paymentStatus || "WAITING";
            const deliveryStatus = delivery?.deliveryStatus
                || (paymentStatus === "FAILED" ? "CANCELLED" : "WAITING");
            return `
                <tr>
                    <td><strong>#${escapeHtml(order.orderId)}</strong></td>
                    <td class="items-cell">${escapeHtml(order.foodItem)}</td>
                    <td>${escapeHtml(order.quantity)}</td>
                    <td><strong>${formatCurrency(order.totalAmount)}</strong></td>
                    <td><span class="status-pill ${statusClass(order.orderStatus)}">${escapeHtml(order.orderStatus)}</span></td>
                    <td><span class="status-pill ${statusClass(paymentStatus)}">${escapeHtml(paymentStatus)}</span></td>
                    <td><span class="status-pill ${statusClass(deliveryStatus)}">${escapeHtml(deliveryStatus)}</span></td>
                    <td>${escapeHtml(delivery?.riderName || "Not assigned")}</td>
                    <td>${formatDate(order.createdAt)}</td>
                </tr>
            `;
        }).join("");
    } catch (error) {
        updateCustomerOrderCount(0);
        body.innerHTML = `<tr><td colspan="9" class="empty-cell">${escapeHtml(error.message)}</td></tr>`;
    }
}
