// Global variables
let currentPage = 0;
let currentSize = 12;
let currentSort = 'name,asc';
let currentFilters = {};
let cart = null;

// API base URL
const API_BASE = '/api';

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    loadVehicles();
    loadCart();
    
    // Setup event listeners
    document.getElementById('search-input').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            searchVehicles();
        }
    });
    
    document.getElementById('checkout-form').addEventListener('submit', function(e) {
        e.preventDefault();
        submitOrder();
    });
});

// Vehicle loading and display functions
async function loadVehicles(page = 0) {
    showLoading(true);
    currentPage = page;
    
    try {
        const sortParts = currentSort.split(',');
        const params = new URLSearchParams({
            page: page,
            size: currentSize,
            sortBy: sortParts[0],
            sortDir: sortParts[1],
            ...currentFilters
        });
        
        const response = await fetch(`${API_BASE}/vehicles/search?${params}`);
        const data = await response.json();
        
        displayVehicles(data.content);
        updatePagination(data);
    } catch (error) {
        console.error('Error loading vehicles:', error);
        showError('Failed to load vehicles');
    } finally {
        showLoading(false);
    }
}

function displayVehicles(vehicles) {
    const grid = document.getElementById('vehicle-grid');
    
    if (vehicles.length === 0) {
        grid.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-car"></i>
                <h3>No vehicles found</h3>
                <p>Try adjusting your search criteria</p>
            </div>
        `;
        return;
    }
    
    grid.innerHTML = vehicles.map(vehicle => `
        <div class="vehicle-card">
            <img src="/image/${vehicle.image}" alt="${vehicle.name}" class="vehicle-image" 
                 onerror="this.src='/image/placeholder.jpg'">
            <div class="vehicle-info">
                <h3 class="vehicle-name">${vehicle.name}</h3>
                <p class="vehicle-type">${vehicle.type}</p>
                <p class="vehicle-price">$${vehicle.price.toLocaleString()}</p>
                <button onclick="addToCart(${vehicle.id})" class="add-to-cart-btn">
                    <i class="fas fa-cart-plus"></i> Add to Cart
                </button>
            </div>
        </div>
    `).join('');
}

function updatePagination(pageData) {
    const pagination = document.getElementById('pagination');
    const totalPages = pageData.totalPages;
    const currentPage = pageData.number;
    
    if (totalPages <= 1) {
        pagination.innerHTML = '';
        return;
    }
    
    let paginationHTML = '';
    
    // Previous button
    paginationHTML += `
        <button class="page-btn" ${currentPage === 0 ? 'disabled' : ''} 
                onclick="loadVehicles(${currentPage - 1})">
            <i class="fas fa-chevron-left"></i> Previous
        </button>
    `;
    
    // Page numbers
    const startPage = Math.max(0, currentPage - 2);
    const endPage = Math.min(totalPages - 1, currentPage + 2);
    
    for (let i = startPage; i <= endPage; i++) {
        paginationHTML += `
            <button class="page-btn ${i === currentPage ? 'active' : ''}" 
                    onclick="loadVehicles(${i})">
                ${i + 1}
            </button>
        `;
    }
    
    // Next button
    paginationHTML += `
        <button class="page-btn" ${currentPage === totalPages - 1 ? 'disabled' : ''} 
                onclick="loadVehicles(${currentPage + 1})">
            Next <i class="fas fa-chevron-right"></i>
        </button>
    `;
    
    pagination.innerHTML = paginationHTML;
}

// Search and filter functions
function searchVehicles() {
    const query = document.getElementById('search-input').value.trim();
    if (query) {
        currentFilters.name = query;
    } else {
        delete currentFilters.name;
    }
    loadVehicles(0);
}

function filterByType(type) {
    currentFilters.type = type;
    loadVehicles(0);
    showVehicles();
}

function applyFilters() {
    const minPrice = document.getElementById('min-price').value;
    const maxPrice = document.getElementById('max-price').value;
    const sortSelect = document.getElementById('sort-select').value;
    
    if (minPrice) currentFilters.minPrice = minPrice;
    else delete currentFilters.minPrice;
    
    if (maxPrice) currentFilters.maxPrice = maxPrice;
    else delete currentFilters.maxPrice;
    
    currentSort = sortSelect;
    loadVehicles(0);
}

function clearFilters() {
    currentFilters = {};
    document.getElementById('search-input').value = '';
    document.getElementById('min-price').value = '';
    document.getElementById('max-price').value = '';
    document.getElementById('sort-select').value = 'name,asc';
    currentSort = 'name,asc';
    loadVehicles(0);
}

// Cart functions
async function loadCart() {
    try {
        const response = await fetch(`${API_BASE}/cart`);
        cart = await response.json();
        updateCartUI();
    } catch (error) {
        console.error('Error loading cart:', error);
        cart = { items: [], subtotal: 0, tax: 0, shipping: 0, total: 0 };
        updateCartUI();
    }
}

async function addToCart(vehicleId, quantity = 1) {
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE}/cart/add?vehicleId=${vehicleId}&quantity=${quantity}`, {
            method: 'POST'
        });
        cart = await response.json();
        updateCartUI();
        showSuccess('Item added to cart!');
    } catch (error) {
        console.error('Error adding to cart:', error);
        showError('Failed to add item to cart');
    } finally {
        showLoading(false);
    }
}

async function removeFromCart(vehicleId) {
    try {
        const response = await fetch(`${API_BASE}/cart/remove/${vehicleId}`, {
            method: 'DELETE'
        });
        cart = await response.json();
        updateCartUI();
        displayCart();
    } catch (error) {
        console.error('Error removing from cart:', error);
        showError('Failed to remove item from cart');
    }
}

async function updateCartQuantity(vehicleId, quantity) {
    try {
        const response = await fetch(`${API_BASE}/cart/update?vehicleId=${vehicleId}&quantity=${quantity}`, {
            method: 'PUT'
        });
        cart = await response.json();
        updateCartUI();
        displayCart();
    } catch (error) {
        console.error('Error updating cart:', error);
        showError('Failed to update cart');
    }
}

async function clearCart() {
    if (!confirm('Are you sure you want to clear your cart?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/cart/clear`, {
            method: 'DELETE'
        });
        cart = await response.json();
        updateCartUI();
        displayCart();
    } catch (error) {
        console.error('Error clearing cart:', error);
        showError('Failed to clear cart');
    }
}

function updateCartUI() {
    const cartCount = document.getElementById('cart-count');
    const totalItems = cart.items.reduce((sum, item) => sum + item.quantity, 0);
    cartCount.textContent = totalItems;
}

function displayCart() {
    const cartItems = document.getElementById('cart-items');
    
    if (cart.items.length === 0) {
        cartItems.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-shopping-cart"></i>
                <h3>Your cart is empty</h3>
                <p>Add some vehicles to get started!</p>
            </div>
        `;
    } else {
        cartItems.innerHTML = cart.items.map(item => `
            <div class="cart-item">
                <img src="/image/${getVehicleImage(item.vehicleId)}" alt="${item.vehicleName}" 
                     class="cart-item-image" onerror="this.src='/image/placeholder.jpg'">
                <div class="cart-item-info">
                    <div class="cart-item-name">${item.vehicleName}</div>
                    <div class="cart-item-price">$${item.price.toLocaleString()}</div>
                </div>
                <div class="quantity-controls">
                    <button class="quantity-btn" onclick="updateCartQuantity(${item.vehicleId}, ${item.quantity - 1})">-</button>
                    <input type="number" class="quantity-input" value="${item.quantity}" 
                           onchange="updateCartQuantity(${item.vehicleId}, this.value)" min="1">
                    <button class="quantity-btn" onclick="updateCartQuantity(${item.vehicleId}, ${item.quantity + 1})">+</button>
                </div>
                <button class="remove-btn" onclick="removeFromCart(${item.vehicleId})">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        `).join('');
    }
    
    // Update totals
    document.getElementById('cart-subtotal').textContent = `$${cart.subtotal.toLocaleString()}`;
    document.getElementById('cart-tax').textContent = `$${cart.tax.toLocaleString()}`;
    document.getElementById('cart-shipping').textContent = `$${cart.shipping.toLocaleString()}`;
    document.getElementById('cart-total').textContent = `$${cart.total.toLocaleString()}`;
}

function getVehicleImage(vehicleId) {
    // This is a simple approach - in a real app, you'd store the image with the cart item
    // or fetch it from the API
    return 'placeholder.jpg';
}

// Navigation functions
function showVehicles() {
    document.getElementById('vehicle-grid').style.display = 'grid';
    document.getElementById('pagination').style.display = 'flex';
    document.getElementById('cart-section').style.display = 'none';
    document.getElementById('checkout-section').style.display = 'none';
    document.querySelector('.search-section').style.display = 'block';
}

function showCart() {
    document.getElementById('vehicle-grid').style.display = 'none';
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('cart-section').style.display = 'block';
    document.getElementById('checkout-section').style.display = 'none';
    document.querySelector('.search-section').style.display = 'none';
    displayCart();
}

function showCheckout() {
    if (cart.items.length === 0) {
        showError('Your cart is empty!');
        return;
    }
    
    document.getElementById('vehicle-grid').style.display = 'none';
    document.getElementById('pagination').style.display = 'none';
    document.getElementById('cart-section').style.display = 'none';
    document.getElementById('checkout-section').style.display = 'block';
    document.querySelector('.search-section').style.display = 'none';
}

// Order functions
async function submitOrder() {
    const orderData = {
        customerName: document.getElementById('customer-name').value,
        customerEmail: document.getElementById('customer-email').value,
        customerPhone: document.getElementById('customer-phone').value,
        shippingAddress: document.getElementById('shipping-address').value,
        paymentMethod: document.getElementById('payment-method').value,
        cardNumber: document.getElementById('card-number').value
    };
    
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        });
        
        if (response.ok) {
            const order = await response.json();
            showOrderConfirmation(order);
            loadCart(); // Refresh cart (should be empty now)
        } else {
            throw new Error('Order submission failed');
        }
    } catch (error) {
        console.error('Error submitting order:', error);
        showError('Failed to submit order. Please try again.');
    } finally {
        showLoading(false);
    }
}

function showOrderConfirmation(order) {
    showModal('Order Confirmation', `
        <div class="message success">
            <h3>Order placed successfully!</h3>
            <p><strong>Order ID:</strong> ${order.id}</p>
            <p><strong>Total:</strong> $${order.total.toLocaleString()}</p>
            <p>A confirmation email has been sent to ${order.customerEmail}</p>
        </div>
        <div style="text-align: center; margin-top: 2rem;">
            <button onclick="closeModal(); showVehicles();" class="btn primary">Continue Shopping</button>
        </div>
    `);
}

// Utility functions
function showLoading(show) {
    document.getElementById('loading').style.display = show ? 'block' : 'none';
}

function showError(message) {
    showModal('Error', `
        <div class="message error">
            <strong>Error:</strong> ${message}
        </div>
    `);
}

function showSuccess(message) {
    const existingMessage = document.querySelector('.message.success');
    if (existingMessage) {
        existingMessage.remove();
    }
    
    const messageDiv = document.createElement('div');
    messageDiv.className = 'message success';
    messageDiv.textContent = message;
    messageDiv.style.position = 'fixed';
    messageDiv.style.top = '100px';
    messageDiv.style.right = '20px';
    messageDiv.style.zIndex = '10000';
    messageDiv.style.minWidth = '300px';
    
    document.body.appendChild(messageDiv);
    
    setTimeout(() => {
        messageDiv.remove();
    }, 3000);
}

function showModal(title, content) {
    document.getElementById('modal-title').textContent = title;
    document.getElementById('modal-body').innerHTML = content;
    document.getElementById('modal-overlay').style.display = 'flex';
}

function closeModal() {
    document.getElementById('modal-overlay').style.display = 'none';
}

// Close modal when clicking outside
document.getElementById('modal-overlay').addEventListener('click', function(e) {
    if (e.target === this) {
        closeModal();
    }
});

// Handle escape key
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeModal();
    }
});
