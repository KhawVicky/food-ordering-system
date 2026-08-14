const ORDER_SERVICE_URL = "http://localhost:8081";
const PAYMENT_SERVICE_URL = "http://localhost:8082";
const DELIVERY_SERVICE_URL = "http://localhost:8083";
const SESSION_KEY = "foodOrderingUser";

// Read the current user from the session.
function getCurrentUser() {
    try {
        return JSON.parse(sessionStorage.getItem(SESSION_KEY) || "null");
    } catch (_error) {
        sessionStorage.removeItem(SESSION_KEY);
        return null;
    }
}

// Save the current user in the session.
function setCurrentUser(user) {
    sessionStorage.setItem(SESSION_KEY, JSON.stringify(user));
}

// Clear the session and go to login.
function logout() {
    sessionStorage.clear();
    window.location.replace("index.html");
}

// Check if the user has the needed role.
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

// Send a JSON request and read the result.
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

// Read JSON when the resource may be missing.
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

// Show a message on the page.
function showMessage(elementId, message, type = "") {
    const element = document.getElementById(elementId);
    if (!element) return;
    element.textContent = message || "";
    element.className = `message ${type}`.trim();
}

// Make text safe for HTML.
function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

// Format a date for display.
function formatDate(value) {
    if (!value) return "—";
    const date = new Date(value);
    return Number.isNaN(date.getTime()) ? escapeHtml(value) : date.toLocaleString();
}

// Format a number with two decimals.
function formatAmount(value) {
    const amount = Number(value);
    return Number.isFinite(amount) ? amount.toFixed(2) : "—";
}

// Format an amount as Malaysian Ringgit.
function formatCurrency(value) {
    const amount = Number(value);
    return Number.isFinite(amount) ? `RM ${amount.toFixed(2)}` : "RM --";
}

// Build a CSS class from a status.
function statusClass(value) {
    return `status-${String(value || "unknown").toLowerCase().replace(/[^a-z0-9]+/g, "-")}`;
}

// Format a system value for display.
function formatDisplayLabel(value) {
    return String(value ?? "")
        .toLowerCase()
        .replace(/[_-]+/g, " ")
        .replace(/\b\w/g, letter => letter.toUpperCase());
}

// Set up the authentication page.
document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("login-form");
    const registerForm = document.getElementById("register-form");
    if (!loginForm || !registerForm) return;

    const authCopy = document.getElementById("auth-copy");
    const demoBox = document.getElementById("demo-box");
    const registerSubmit = document.getElementById("register-submit");

    // Switch between login and register forms.
    function setAuthMode(mode) {
        const isRegister = mode === "register";
        loginForm.hidden = isRegister;
        registerForm.hidden = !isRegister;
        demoBox.hidden = isRegister;
        authCopy.textContent = isRegister
            ? "Create a customer account and start ordering in a few seconds."
            : "Sign in to choose from today's menu or manage the fulfilment workflow.";
        document.getElementById("page-title").textContent = isRegister ? "Join the table." : "Food, sorted.";
        showMessage("login-message", "");
        showMessage("register-message", "");
    }

    const existingUser = getCurrentUser();
    if (existingUser) {
        window.location.replace(existingUser.role === "STAFF" ? "staff.html" : "customer.html");
        return;
    }

    // Send login details when the form is submitted.
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

    // Send register details when the form is submitted.
    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        showMessage("register-message", "Creating your account...");
        registerSubmit.disabled = true;
        const formData = new FormData(registerForm);
        try {
            const user = await requestJson(`${ORDER_SERVICE_URL}/api/auth/register`, {
                method: "POST",
                body: JSON.stringify({
                    name: formData.get("name"),
                    email: formData.get("email"),
                    password: formData.get("password")
                })
            });
            setCurrentUser(user);
            window.location.href = "customer.html";
        } catch (error) {
            registerSubmit.disabled = false;
            showMessage("register-message", error.message, "error");
        }
    });

    // Open the register form when clicked.
    document.getElementById("show-register").addEventListener("click", () => setAuthMode("register"));
    // Open the login form when clicked.
    document.getElementById("show-login").addEventListener("click", () => setAuthMode("login"));
});
