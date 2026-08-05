const ORDER_SERVICE_URL = "http://localhost:8081";
const PAYMENT_SERVICE_URL = "http://localhost:8082";
const DELIVERY_SERVICE_URL = "http://localhost:8083";
const SESSION_KEY = "foodOrderingUser";

function getCurrentUser() {
    try {
        return JSON.parse(sessionStorage.getItem(SESSION_KEY) || "null");
    } catch (_error) {
        sessionStorage.removeItem(SESSION_KEY);
        return null;
    }
}

function setCurrentUser(user) {
    sessionStorage.setItem(SESSION_KEY, JSON.stringify(user));
}

function logout() {
    sessionStorage.clear();
    window.location.replace("index.html");
}

function requireRole(role) {
    const user = getCurrentUser();
    if (!user) {
        window.location.replace("index.html");
        return null;
    }
    if (user.role !== role) {
        window.location.replace(user.role === "STAFF" ? "staff.html" : "customer.html");
        return null;
    }
    return user;
}

async function requestJson(url, options = {}) {
    const response = await fetch(url, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        }
    });
    const text = await response.text();
    let body = null;
    try {
        body = text ? JSON.parse(text) : null;
    } catch (_error) {
        body = { message: text };
    }
    if (!response.ok) {
        throw new Error(body?.message || body?.error || `Request failed (${response.status})`);
    }
    return body;
}

async function optionalJson(url) {
    const response = await fetch(url);
    if (response.status === 404) {
        return null;
    }
    const text = await response.text();
    let body = null;
    try {
        body = text ? JSON.parse(text) : null;
    } catch (_error) {
        body = { message: text };
    }
    if (!response.ok) {
        throw new Error(body?.message || body?.error || `Request failed (${response.status})`);
    }
    return body;
}

function showMessage(elementId, message, type = "") {
    const element = document.getElementById(elementId);
    if (!element) return;
    element.textContent = message || "";
    element.className = `message ${type}`.trim();
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function formatDate(value) {
    if (!value) return "—";
    const date = new Date(value);
    return Number.isNaN(date.getTime()) ? escapeHtml(value) : date.toLocaleString();
}

function formatAmount(value) {
    const amount = Number(value);
    return Number.isFinite(amount) ? amount.toFixed(2) : "—";
}

function formatCurrency(value) {
    const amount = Number(value);
    return Number.isFinite(amount) ? `RM ${amount.toFixed(2)}` : "RM --";
}

function statusClass(value) {
    return `status-${String(value || "unknown").toLowerCase().replace(/[^a-z0-9]+/g, "-")}`;
}

document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("login-form");
    if (!loginForm) return;

    const existingUser = getCurrentUser();
    if (existingUser) {
        window.location.replace(existingUser.role === "STAFF" ? "staff.html" : "customer.html");
        return;
    }

    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        showMessage("login-message", "Signing in…");
        const formData = new FormData(loginForm);
        try {
            const user = await requestJson(`${ORDER_SERVICE_URL}/api/auth/login`, {
                method: "POST",
                body: JSON.stringify({
                    email: formData.get("email"),
                    password: formData.get("password")
                })
            });
            setCurrentUser(user);
            window.location.href = user.role === "STAFF" ? "staff.html" : "customer.html";
        } catch (error) {
            showMessage("login-message", error.message, "error");
        }
    });
});
