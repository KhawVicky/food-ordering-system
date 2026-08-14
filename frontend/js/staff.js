const staff = requireRole("STAFF");

if (staff) {
    // Set up the staff page.
    document.addEventListener("DOMContentLoaded", () => {
        document.getElementById("staff-name").textContent = staff.name;
        document.getElementById("logout-button").addEventListener("click", logout);
        document.getElementById("refresh-all").addEventListener("click", loadAll);
        document.getElementById("refresh-orders").addEventListener("click", loadOrders);
        document.getElementById("refresh-payments").addEventListener("click", loadPayments);
        document.getElementById("refresh-deliveries").addEventListener("click", refreshDeliveryViews);
        document.querySelector(".staff-tabs").addEventListener("click", handleTabChange);
        document.getElementById("payments-body").addEventListener("click", handlePaymentAction);
        document.getElementById("deliveries-body").addEventListener("click", handleDeliveryAction);
        loadAll();
    });
}

// Handle staff tab clicks.
function handleTabChange(event) {
    const tab = event.target.closest("[data-tab]");
    if (!tab) return;
    activateTab(tab.dataset.tab);
}

// Show the selected staff tab.
function activateTab(tabName) {
    document.querySelectorAll(".staff-tab").forEach(tab => {
        const isActive = tab.dataset.tab === tabName;
        tab.setAttribute("aria-selected", String(isActive));
    });

    document.querySelectorAll(".staff-tab-panel").forEach(panel => {
        panel.hidden = panel.id !== `staff-${tabName}-panel`;
    });
}

// Load all staff data.
async function loadAll() {
    setSyncNote("Refreshing...");
    await Promise.all([loadOrders(), loadPayments(), loadDeliveries()]);
    setSyncNote(`Updated ${new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}`);
}

// Show the last refresh message.
function setSyncNote(message) {
    const element = document.getElementById("staff-sync");
    if (element) element.textContent = message;
}

// Update a tab count.
function updateTabCount(tabName, count) {
    const countElement = document.getElementById(`${tabName}-count`);
    if (countElement) countElement.textContent = count;
}

// Check if a payment cannot change.
function isPaymentLocked(status) {
    return status === "COMPLETED" || status === "FAILED";
}

// Draw the payment status control.
function renderPaymentStatus(payment) {
    const status = payment.paymentStatus || "PENDING";
    if (isPaymentLocked(status)) {
        return `<span class="status-pill ${statusClass(status)}">${escapeHtml(formatDisplayLabel(status))}</span>`;
    }

    return `
        <select class="table-select" data-payment-status="${escapeHtml(payment.paymentId)}" aria-label="Payment status">
            ${["PENDING", "COMPLETED", "FAILED"].map(option => `<option value="${option}" ${option === status ? "selected" : ""}>${formatDisplayLabel(option)}</option>`).join("")}
        </select>
    `;
}

// Draw the payment action button.
function renderPaymentAction(payment) {
    return isPaymentLocked(payment.paymentStatus)
        ? '<span class="table-lock">Locked</span>'
        : `<button class="button small" data-payment-action="${escapeHtml(payment.paymentId)}" type="button">Save</button>`;
}

// Draw the rider assignment control.
function renderRiderControl(delivery) {
    const status = delivery.deliveryStatus || "PENDING_ASSIGNMENT";
    const riderName = delivery.riderName || "Not assigned";
    if (status !== "PENDING_ASSIGNMENT") {
        return `<span class="readonly-value">${escapeHtml(riderName)}</span>`;
    }

    return `
        <div class="action-row">
            <input data-rider-name="${escapeHtml(delivery.deliveryId)}" value="${escapeHtml(delivery.riderName || "")}" placeholder="Rider name" aria-label="Rider name">
            <button class="button small" data-rider-action="${escapeHtml(delivery.deliveryId)}" type="button">Assign rider</button>
        </div>
    `;
}

// Draw the delivery status control.
function renderDeliveryStatus(delivery) {
    const status = delivery.deliveryStatus || "PENDING_ASSIGNMENT";
    if (status === "DELIVERED") {
        return `<span class="status-pill ${statusClass(status)}">${escapeHtml(formatDisplayLabel(status))}</span>`;
    }

    return `
        <select class="table-select" data-delivery-status="${escapeHtml(delivery.deliveryId)}" aria-label="Delivery status">
            ${["PENDING_ASSIGNMENT", "RIDER_ASSIGNED", "PICKED_UP", "DELIVERED"].map(option => `<option value="${option}" ${option === status ? "selected" : ""}>${formatDisplayLabel(option)}</option>`).join("")}
        </select>
    `;
}

// Draw the delivery action button.
function renderDeliveryAction(delivery) {
    return delivery.deliveryStatus === "DELIVERED"
        ? '<span class="table-lock">Locked</span>'
        : `<button class="button small" data-delivery-action="${escapeHtml(delivery.deliveryId)}" type="button">Save status</button>`;
}

// Choose the latest order progress for the staff order table.
function getOrderProgressStatus(order, payment, delivery) {
    if (delivery?.deliveryStatus) return delivery.deliveryStatus;
    if (payment?.paymentStatus === "FAILED") return "FAILED";
    return order.orderStatus;
}

// Refresh both delivery progress and the order summary.
async function refreshDeliveryViews() {
    await Promise.all([loadDeliveries(), loadOrders()]);
}

// Load all orders for staff.
async function loadOrders() {
    const body = document.getElementById("staff-orders-body");
    body.innerHTML = '<tr><td colspan="8" class="empty-cell">Loading orders...</td></tr>';

    try {
        const [orders, payments, deliveries] = await Promise.all([
            requestJson(`${ORDER_SERVICE_URL}/api/orders`),
            requestJson(`${PAYMENT_SERVICE_URL}/api/payments`).catch(() => []),
            requestJson(`${DELIVERY_SERVICE_URL}/api/deliveries`).catch(() => [])
        ]);
        const paymentsByOrder = new Map(payments.map(payment => [String(payment.orderId), payment]));
        const deliveriesByOrder = new Map(deliveries.map(delivery => [String(delivery.orderId), delivery]));
        updateTabCount("orders", orders.length);
        body.innerHTML = orders.length ? orders.map(order => `
            <tr>
                <td><strong>#${escapeHtml(order.orderId)}</strong></td>
                <td>${escapeHtml(order.customerName)}</td>
                <td class="items-cell">${escapeHtml(order.foodItem)}</td>
                <td>${escapeHtml(order.quantity)}</td>
                <td><strong>${formatCurrency(order.totalAmount)}</strong></td>
                <td>${renderOrderProgressStatus(
                    order,
                    paymentsByOrder.get(String(order.orderId)),
                    deliveriesByOrder.get(String(order.orderId))
                )}</td>
                <td>${renderOrderPaymentStatus(paymentsByOrder.get(String(order.orderId)))}</td>
                <td>${formatDate(order.createdAt)}</td>
            </tr>
        `).join("") : '<tr><td colspan="8" class="empty-cell">No orders yet.</td></tr>';
        return orders;
    } catch (error) {
        updateTabCount("orders", 0);
        body.innerHTML = `<tr><td colspan="8" class="empty-cell">${escapeHtml(error.message)}</td></tr>`;
        return [];
    }
}

// Draw the latest order or delivery status.
function renderOrderProgressStatus(order, payment, delivery) {
    const status = getOrderProgressStatus(order, payment, delivery);
    return `<span class="status-pill ${statusClass(status)}">${escapeHtml(formatDisplayLabel(status))}</span>`;
}

// Draw the payment status for an order.
function renderOrderPaymentStatus(payment) {
    const status = payment?.paymentStatus || "WAITING";
    return `<span class="status-pill ${statusClass(status)}">${escapeHtml(formatDisplayLabel(status))}</span>`;
}

// Load all payments for staff.
async function loadPayments() {
    const body = document.getElementById("payments-body");
    body.innerHTML = '<tr><td colspan="7" class="empty-cell">Loading payments...</td></tr>';

    try {
        const payments = await requestJson(`${PAYMENT_SERVICE_URL}/api/payments`);
        updateTabCount("payments", payments.length);
        body.innerHTML = payments.length ? payments.map(payment => `
            <tr>
                <td class="id-cell" title="${escapeHtml(payment.paymentId)}">${escapeHtml(payment.paymentId.slice(0, 8))}...</td>
                <td>#${escapeHtml(payment.orderId)}</td>
                <td><strong>${formatCurrency(payment.amount)}</strong></td>
                <td>${escapeHtml(payment.paymentMethod)}</td>
                <td>${renderPaymentStatus(payment)}</td>
                <td>${formatDate(payment.paidAt)}</td>
                <td>${renderPaymentAction(payment)}</td>
            </tr>
        `).join("") : '<tr><td colspan="7" class="empty-cell">No payments yet.</td></tr>';
        return payments;
    } catch (error) {
        updateTabCount("payments", 0);
        body.innerHTML = `<tr><td colspan="7" class="empty-cell">${escapeHtml(error.message)}</td></tr>`;
        return [];
    }
}

// Load all deliveries for staff.
async function loadDeliveries() {
    const body = document.getElementById("deliveries-body");
    body.innerHTML = '<tr><td colspan="6" class="empty-cell">Loading deliveries...</td></tr>';

    try {
        const deliveries = await requestJson(`${DELIVERY_SERVICE_URL}/api/deliveries`);
        updateTabCount("delivery", deliveries.length);
        body.innerHTML = deliveries.length ? deliveries.map(delivery => `
            <tr>
                <td class="id-cell" title="${escapeHtml(delivery.deliveryId)}">${escapeHtml(delivery.deliveryId.slice(0, 8))}...</td>
                <td>#${escapeHtml(delivery.orderId)}</td>
                <td class="address-cell">${escapeHtml(delivery.deliveryAddress)}</td>
                <td>${renderRiderControl(delivery)}</td>
                <td>${renderDeliveryStatus(delivery)}</td>
                <td>${renderDeliveryAction(delivery)}</td>
            </tr>
        `).join("") : '<tr><td colspan="6" class="empty-cell">No delivery tasks yet.</td></tr>';
        return deliveries;
    } catch (error) {
        updateTabCount("delivery", 0);
        body.innerHTML = `<tr><td colspan="6" class="empty-cell">${escapeHtml(error.message)}</td></tr>`;
        return [];
    }
}

// Save a payment status change.
async function handlePaymentAction(event) {
    const button = event.target.closest("[data-payment-action]");
    if (!button) return;

    const paymentId = button.dataset.paymentAction;
    const select = document.querySelector(`[data-payment-status="${CSS.escape(paymentId)}"]`);
    showMessage("staff-message", "Updating payment...");

    try {
        await requestJson(`${PAYMENT_SERVICE_URL}/api/payments/${encodeURIComponent(paymentId)}/status`, {
            method: "PUT",
            body: JSON.stringify({ status: select.value })
        });
        showMessage("staff-message", "Payment updated.", "success");
        await Promise.all([loadPayments(), loadOrders(), loadDeliveries()]);
    } catch (error) {
        showMessage("staff-message", error.message, "error");
    }
}

// Save a rider or delivery status change.
async function handleDeliveryAction(event) {
    const riderButton = event.target.closest("[data-rider-action]");
    const statusButton = event.target.closest("[data-delivery-action]");
    if (!riderButton && !statusButton) return;

    try {
        if (riderButton) {
            const deliveryId = riderButton.dataset.riderAction;
            const input = document.querySelector(`[data-rider-name="${CSS.escape(deliveryId)}"]`);
            showMessage("staff-message", "Assigning rider...");
            await requestJson(`${DELIVERY_SERVICE_URL}/api/deliveries/${encodeURIComponent(deliveryId)}/assign-rider`, {
                method: "PUT",
                body: JSON.stringify({ riderName: input.value })
            });
            showMessage("staff-message", "Rider assigned.", "success");
        } else {
            const deliveryId = statusButton.dataset.deliveryAction;
            const select = document.querySelector(`[data-delivery-status="${CSS.escape(deliveryId)}"]`);
            showMessage("staff-message", "Updating delivery...");
            await requestJson(`${DELIVERY_SERVICE_URL}/api/deliveries/${encodeURIComponent(deliveryId)}/status`, {
                method: "PUT",
                body: JSON.stringify({ status: select.value })
            });
            showMessage("staff-message", "Delivery updated.", "success");
        }
        await refreshDeliveryViews();
    } catch (error) {
        showMessage("staff-message", error.message, "error");
    }
}
