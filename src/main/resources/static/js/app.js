// Main Application JavaScript
let currentPage = 'dashboard';
let currentData = {};

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    loadPage('dashboard');
    setupNavigation();
});

// Navigation setup
function setupNavigation() {
    document.querySelectorAll('[data-page]').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const page = e.target.getAttribute('data-page') || e.target.closest('[data-page]').getAttribute('data-page');
            loadPage(page);
        });
    });
}

// Load page content with smooth transitions
async function loadPage(page) {
    currentPage = page;
    updateActiveNav();
    
    const content = document.getElementById('main-content');
    if (!content) return;

    // Add loading state
    content.classList.add('opacity-0', 'transition-opacity', 'duration-300');
    
    // Don't show spinner here - individual load functions handle their own spinners
    // This prevents duplicate spinners and ensures proper cleanup
    try {
        switch(page) {
            case 'dashboard':
                await loadDashboard();
                break;
            case 'hotels':
                await loadHotels();
                break;
            case 'rooms':
                await loadRooms();
                break;
            case 'room-types':
                await loadRoomTypes();
                break;
            case 'bookings':
                await loadBookings();
                break;
            case 'bills':
                await loadBills();
                break;
            case 'guests':
                await loadGuests();
                break;
            case 'employees':
                await loadEmployees();
                break;
            case 'roles':
                await loadRoles();
                break;
            default:
                content.innerHTML = '<div class="p-6"><h2 class="text-2xl font-bold">Page not found</h2></div>';
        }
        
        // Fade in content
        setTimeout(() => {
            content.classList.remove('opacity-0');
            content.classList.add('opacity-100');
        }, 50);
    } catch (error) {
        content.innerHTML = `
            <div class="p-6">
                <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                    <h2 class="text-xl font-bold text-red-800 mb-2">Error Loading Page</h2>
                    <p class="text-red-600">${error.message || 'An unexpected error occurred'}</p>
                </div>
            </div>
        `;
        if (typeof Toast !== 'undefined') {
            Toast.show('Failed to load page', 'error');
        }
        // Force hide spinner on error to prevent it from staying forever
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.forceHide();
        }
    }
}

function updateActiveNav() {
    document.querySelectorAll('[data-page]').forEach(link => {
        const page = link.getAttribute('data-page');
        if (page === currentPage) {
            link.classList.add('bg-blue-600', 'text-white', 'shadow-md');
            link.classList.remove('text-gray-300', 'hover:bg-gray-700');
        } else {
            link.classList.remove('bg-blue-600', 'text-white', 'shadow-md');
            link.classList.add('text-gray-300', 'hover:bg-gray-700', 'hover:text-white');
        }
    });
}

// Enhanced Dashboard with better visuals
async function loadDashboard() {
    const content = document.getElementById('main-content');
    // Show loading indicator for dashboard
    if (typeof LoadingSpinner !== 'undefined') {
        LoadingSpinner.show();
    }
    try {
        const [hotelsRes, roomsRes, bookingsRes, billsRes] = await Promise.all([
            HotelAPI.getHotels(1, 5),
            HotelAPI.getRooms(1, 5),
            HotelAPI.getBookings(1, 5),
            HotelAPI.getBills(1, 5)
        ]);

        // Handle ApiResponse<PayloadResponse<T>> structure
        const hotelsCount = ResponseHandler.extractPagination(hotelsRes)?.totalElements || 0;
        const roomsCount = ResponseHandler.extractPagination(roomsRes)?.totalElements || 0;
        const bookingsCount = ResponseHandler.extractPagination(bookingsRes)?.totalElements || 0;
        const billsCount = ResponseHandler.extractPagination(billsRes)?.totalElements || 0;

        content.innerHTML = `
            <div class="p-6 animate-fade-in">
                <div class="mb-6">
                    <h1 class="text-3xl font-bold text-gray-800 mb-2">Dashboard</h1>
                    <p class="text-gray-600">Welcome back! Here's an overview of your hotel management system.</p>
                </div>
                
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                    <div class="bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl shadow-lg p-6 text-white card-hover">
                        <div class="flex items-center justify-between mb-4">
                            <div class="bg-white bg-opacity-20 rounded-lg p-3">
                                <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
                                </svg>
                            </div>
                        </div>
                        <h3 class="text-blue-100 text-sm font-medium mb-1">Total Hotels</h3>
                        <p class="text-4xl font-bold">${hotelsCount}</p>
                    </div>
                    
                    <div class="bg-gradient-to-br from-green-500 to-green-600 rounded-xl shadow-lg p-6 text-white card-hover">
                        <div class="flex items-center justify-between mb-4">
                            <div class="bg-white bg-opacity-20 rounded-lg p-3">
                                <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
                                </svg>
                            </div>
                        </div>
                        <h3 class="text-green-100 text-sm font-medium mb-1">Total Rooms</h3>
                        <p class="text-4xl font-bold">${roomsCount}</p>
                    </div>
                    
                    <div class="bg-gradient-to-br from-purple-500 to-purple-600 rounded-xl shadow-lg p-6 text-white card-hover">
                        <div class="flex items-center justify-between mb-4">
                            <div class="bg-white bg-opacity-20 rounded-lg p-3">
                                <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
                                </svg>
                            </div>
                        </div>
                        <h3 class="text-purple-100 text-sm font-medium mb-1">Active Bookings</h3>
                        <p class="text-4xl font-bold">${bookingsCount}</p>
                    </div>
                    
                    <div class="bg-gradient-to-br from-orange-500 to-orange-600 rounded-xl shadow-lg p-6 text-white card-hover">
                        <div class="flex items-center justify-between mb-4">
                            <div class="bg-white bg-opacity-20 rounded-lg p-3">
                                <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                                </svg>
                            </div>
                        </div>
                        <h3 class="text-orange-100 text-sm font-medium mb-1">Total Bills</h3>
                        <p class="text-4xl font-bold">${billsCount}</p>
                    </div>
                </div>

                <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    <div class="bg-white rounded-xl shadow-lg p-6 card-hover">
                        <div class="flex items-center justify-between mb-4">
                            <h2 class="text-xl font-bold text-gray-800">Recent Bookings</h2>
                            <a href="#" data-page="bookings" class="text-blue-500 hover:text-blue-600 text-sm font-medium">View All →</a>
                        </div>
                        <div class="space-y-3 custom-scrollbar max-h-96 overflow-y-auto">
                            ${ResponseHandler.extractItems(bookingsRes).slice(0, 5).map(booking => `
                                <div class="border-b border-gray-200 pb-3 table-row-hover p-2 rounded">
                                    <div class="flex items-center justify-between">
                                        <div>
                                            <p class="font-semibold text-gray-800">Booking #${booking.bookingId?.substring(0, 8)}</p>
                                            <p class="text-sm text-gray-500">${booking.hotel?.hotelName || 'N/A'}</p>
                                        </div>
                                        <div class="text-right">
                                            <p class="text-sm font-medium text-gray-700">${formatDate(booking.arrivalDate)}</p>
                                            <p class="text-xs text-gray-500">to ${formatDate(booking.departureDate)}</p>
                                        </div>
                                    </div>
                                </div>
                            `).join('') || '<p class="text-gray-500 text-center py-4">No bookings found</p>'}
                        </div>
                    </div>
                    
                    <div class="bg-white rounded-xl shadow-lg p-6 card-hover">
                        <div class="flex items-center justify-between mb-4">
                            <h2 class="text-xl font-bold text-gray-800">Recent Bills</h2>
                            <a href="#" data-page="bills" class="text-blue-500 hover:text-blue-600 text-sm font-medium">View All →</a>
                        </div>
                        <div class="space-y-3 custom-scrollbar max-h-96 overflow-y-auto">
                            ${ResponseHandler.extractItems(billsRes).slice(0, 5).map(bill => {
                                const total = (bill.roomCharge || 0) + (bill.roomService || 0) + (bill.restaurantCharges || 0) + (bill.barCharges || 0) + (bill.miscCharges || 0);
                                return `
                                <div class="border-b border-gray-200 pb-3 table-row-hover p-2 rounded">
                                    <div class="flex items-center justify-between">
                                        <div>
                                            <p class="font-semibold text-gray-800">Invoice #${bill.invoiceNo?.substring(0, 8)}</p>
                                            <p class="text-sm text-gray-500">${bill.guest?.firstName || ''} ${bill.guest?.lastName || ''}</p>
                                        </div>
                                        <div class="text-right">
                                            <p class="text-sm font-bold text-green-600">${CurrencyFormatter.format(total)}</p>
                                            <p class="text-xs text-gray-500">${formatDate(bill.paymentDate)}</p>
                                        </div>
                                    </div>
                                </div>
                            `;
                            }).join('') || '<p class="text-gray-500 text-center py-4">No bills found</p>'}
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        // Re-attach event listeners for navigation
        setupNavigation();
        
        // Hide spinner after content is rendered
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    } catch (error) {
        content.innerHTML = `
            <div class="p-6">
                <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                    <h2 class="text-xl font-bold text-red-800 mb-2">Error Loading Dashboard</h2>
                    <p class="text-red-600">${error.message || 'An unexpected error occurred'}</p>
                </div>
            </div>
        `;
        if (typeof Toast !== 'undefined') {
            Toast.show('Failed to load dashboard', 'error');
        }
        // Hide spinner on error
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    }
}

// Enhanced Hotels Management with Search
async function loadHotels(page = 1) {
    const content = document.getElementById('main-content');
    if (page === 1 && typeof LoadingSpinner !== 'undefined') {
        LoadingSpinner.show();
    }
    try {
        const response = await HotelAPI.getHotels(page, 10);
        // Handle ApiResponse<PayloadResponse<HotelDTO>> structure
        const hotels = ResponseHandler.extractItems(response);
        const pagination = ResponseHandler.extractPagination(response) || {};

        content.innerHTML = `
            <div class="p-6 animate-fade-in">
                <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
                    <div>
                        <h1 class="text-3xl font-bold text-gray-800">Hotels</h1>
                        <p class="text-gray-600 mt-1">Manage your hotel properties</p>
                    </div>
                    <button onclick="openHotelModal()" class="btn-primary bg-gradient-to-r from-blue-500 to-indigo-600 text-white px-6 py-3 rounded-lg hover:from-blue-600 hover:to-indigo-700 font-medium shadow-lg hover:shadow-xl transition-all">
                        <span class="flex items-center space-x-2">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                            </svg>
                            <span>Add Hotel</span>
                        </span>
                    </button>
                </div>
                
                <div class="bg-white rounded-xl shadow-lg mb-4 p-4">
                    <div class="relative">
                        <input type="text" 
                               id="hotel-search" 
                               placeholder="Search hotels by name, city, or country..." 
                               class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent input-focus">
                        <svg class="absolute left-3 top-3.5 w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                        </svg>
                    </div>
                </div>
                
                <div class="bg-white rounded-xl shadow-lg overflow-hidden">
                    <div class="overflow-x-auto">
                        <table id="hotels-table" class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gradient-to-r from-gray-50 to-gray-100">
                                <tr>
                                    <th class="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Name</th>
                                    <th class="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">City</th>
                                    <th class="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Country</th>
                                    <th class="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Rooms</th>
                                    <th class="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Rating</th>
                                    <th class="px-6 py-4 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">Actions</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                ${hotels.map(hotel => `
                                    <tr class="table-row-hover">
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="flex items-center">
                                                <div class="flex-shrink-0 h-10 w-10 bg-blue-100 rounded-lg flex items-center justify-center mr-3">
                                                    <span class="text-blue-600 font-bold">${(hotel.hotelName || 'H')[0].toUpperCase()}</span>
                                                </div>
                                                <div class="text-sm font-semibold text-gray-900">${hotel.hotelName || ''}</div>
                                            </div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">${hotel.city || 'N/A'}</td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">${hotel.country || 'N/A'}</td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <span class="px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                                                ${hotel.numRooms || 0} rooms
                                            </span>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm">
                                            <div class="flex items-center text-yellow-400">
                                                ${'★'.repeat(hotel.starRating || 0)}${'☆'.repeat(5 - (hotel.starRating || 0))}
                                            </div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                            <div class="flex items-center space-x-3">
                                                <button onclick="editHotel('${hotel.hotelCode}')" class="text-blue-600 hover:text-blue-900 hover:underline transition-colors">
                                                    Edit
                                                </button>
                                                <button onclick="deleteHotel('${hotel.hotelCode}')" class="text-red-600 hover:text-red-900 hover:underline transition-colors">
                                                    Delete
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                `).join('') || '<tr><td colspan="6" class="px-6 py-8 text-center text-gray-500"><p class="text-lg">No hotels found</p></td></tr>'}
                            </tbody>
                        </table>
                    </div>
                </div>
                
                ${renderPagination(pagination, page, 'loadHotels')}
            </div>
        `;
        
        // Add search functionality
        const searchInput = document.getElementById('hotel-search');
        if (searchInput) {
            searchInput.addEventListener('input', debounce((e) => {
                SearchFilter.filterTable('hotels-table', e.target.value, [0, 1, 2]);
            }, 300));
        }
        
        // Hide spinner after content is rendered
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    } catch (error) {
        content.innerHTML = `
            <div class="p-6">
                <div class="bg-red-50 border border-red-200 rounded-lg p-4">
                    <h2 class="text-xl font-bold text-red-800 mb-2">Error Loading Hotels</h2>
                    <p class="text-red-600">${error.message || 'An unexpected error occurred'}</p>
                </div>
            </div>
        `;
        if (typeof Toast !== 'undefined') {
            Toast.show('Failed to load hotels', 'error');
        }
        // Hide spinner on error
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    }
}

async function openHotelModal(hotelId = null) {
    let hotel = null;
    if (hotelId) {
        try {
            const response = await HotelAPI.getHotelById(hotelId);
            hotel = ResponseHandler.extractItem(response);
        } catch (error) {
            if (typeof Toast !== 'undefined') {
                Toast.show('Error loading hotel data', 'error');
            }
            return;
        }
    }

    const formContent = `
        <form id="hotelForm" onsubmit="event.preventDefault(); saveHotel(event, '${hotelId || ''}')" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">
                        Hotel Name <span class="text-red-500">*</span>
                    </label>
                    <input type="text" 
                           name="hotelName" 
                           value="${hotel?.hotelName || ''}" 
                           required
                           class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent input-focus transition-all"
                           placeholder="Enter hotel name">
                </div>
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Phone Number</label>
                    <input type="tel" 
                           name="phoneNo" 
                           value="${hotel?.phoneNo || ''}"
                           class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent input-focus transition-all"
                           placeholder="+1 (555) 123-4567">
                </div>
                <div class="md:col-span-2">
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Address</label>
                    <input type="text" 
                           name="address" 
                           value="${hotel?.address || ''}"
                           class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent input-focus transition-all"
                           placeholder="Street address">
                </div>
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">City</label>
                    <input type="text" 
                           name="city" 
                           value="${hotel?.city || ''}"
                           class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent input-focus transition-all"
                           placeholder="City name">
                </div>
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Country</label>
                    <input type="text" 
                           name="country" 
                           value="${hotel?.country || ''}"
                           class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent input-focus transition-all"
                           placeholder="Country name">
                </div>
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Postcode</label>
                    <input type="text" 
                           name="postcode" 
                           value="${hotel?.postcode || ''}"
                           class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent input-focus transition-all"
                           placeholder="12345">
                </div>
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Number of Rooms</label>
                    <input type="number" 
                           name="numRooms" 
                           value="${hotel?.numRooms || ''}"
                           min="0"
                           class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent input-focus transition-all"
                           placeholder="0">
                </div>
                <div>
                    <label class="block text-sm font-semibold text-gray-700 mb-2">Star Rating</label>
                    <select name="starRating" 
                            class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent input-focus transition-all">
                        <option value="">Select rating</option>
                        ${[1, 2, 3, 4, 5].map(r => `<option value="${r}" ${hotel?.starRating === r ? 'selected' : ''}>${'★'.repeat(r)} ${r} Star${r > 1 ? 's' : ''}</option>`).join('')}
                    </select>
                </div>
            </div>
            <div class="flex justify-end space-x-3 pt-4 border-t">
                <button type="button" 
                        onclick="this.closest('.fixed').remove()" 
                        class="px-6 py-2.5 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors font-medium">
                    Cancel
                </button>
                <button type="submit" 
                        class="px-6 py-2.5 bg-gradient-to-r from-blue-500 to-indigo-600 text-white rounded-lg hover:from-blue-600 hover:to-indigo-700 transition-all font-medium shadow-lg hover:shadow-xl">
                    ${hotelId ? 'Update' : 'Create'} Hotel
                </button>
            </div>
        </form>
    `;

    if (typeof Modal !== 'undefined') {
        const modal = Modal.create(
            `${hotelId ? 'Edit' : 'Add'} Hotel`,
            formContent,
            null,
            '',
            '',
            false // Don't show footer since form has its own buttons
        );
        
        // Handle form submission
        setTimeout(() => {
            const form = document.getElementById('hotelForm');
            if (form) {
                // Remove any existing listeners
                const newForm = form.cloneNode(true);
                form.parentNode.replaceChild(newForm, form);
                
                newForm.addEventListener('submit', (e) => {
                    e.preventDefault();
                    saveHotel(e, hotelId);
                });
            }
        }, 100);
    } else {
        // Fallback to old modal
        const modal = document.createElement('div');
        modal.className = 'fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center';
        modal.innerHTML = `<div class="bg-white rounded-lg p-6 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">${formContent}</div>`;
        document.body.appendChild(modal);
    }
}

async function saveHotel(event, hotelId) {
    event.preventDefault();
    const form = event.target;
    
    // Validate form
    if (typeof FormValidator !== 'undefined') {
        const validation = FormValidator.validate(form);
        if (!validation.isValid) {
            if (typeof Toast !== 'undefined') {
                Toast.show(validation.errors[0], 'error');
            }
            return;
        }
    }
    
    const formData = new FormData(form);
    const data = Object.fromEntries(formData);
    data.numRooms = data.numRooms ? parseInt(data.numRooms) : null;
    data.starRating = data.starRating ? parseInt(data.starRating) : null;

    try {
        if (hotelId) {
            await HotelAPI.updateHotel(hotelId, data);
            if (typeof Toast !== 'undefined') {
                Toast.show('Hotel updated successfully', 'success');
            }
        } else {
            await HotelAPI.createHotel(data);
            if (typeof Toast !== 'undefined') {
                Toast.show('Hotel created successfully', 'success');
            }
        }
        document.querySelector('.fixed').remove();
        await loadHotels();
    } catch (error) {
        if (typeof Toast !== 'undefined') {
            Toast.show('Error saving hotel: ' + (error.message || 'Unknown error'), 'error');
        }
    }
}

async function editHotel(id) {
    await openHotelModal(id);
}

async function deleteHotel(id) {
    if (typeof Modal !== 'undefined') {
        Modal.create(
            'Delete Hotel',
            '<p class="text-gray-700">Are you sure you want to delete this hotel? This action cannot be undone.</p>',
            async () => {
                try {
                    await HotelAPI.deleteHotel(id);
                    if (typeof Toast !== 'undefined') {
                        Toast.show('Hotel deleted successfully', 'success');
                    }
                    await loadHotels();
                } catch (error) {
                    if (typeof Toast !== 'undefined') {
                        Toast.show('Error deleting hotel: ' + (error.message || 'Unknown error'), 'error');
                    }
                }
            },
            'Delete',
            'Cancel'
        );
    } else {
        if (confirm('Are you sure you want to delete this hotel?')) {
            try {
                await HotelAPI.deleteHotel(id);
                if (typeof Toast !== 'undefined') {
                    Toast.show('Hotel deleted successfully', 'success');
                }
                await loadHotels();
            } catch (error) {
                if (typeof Toast !== 'undefined') {
                    Toast.show('Error deleting hotel: ' + (error.message || 'Unknown error'), 'error');
                }
            }
        }
    }
}

// Pagination helper
function renderPagination(pagination, currentPage, loadFunction) {
    if (!pagination || pagination.totalPages <= 1) return '';
    
    const pages = [];
    for (let i = 1; i <= pagination.totalPages; i++) {
        pages.push(i);
    }

    return `
        <div class="mt-4 flex justify-center space-x-2">
            ${currentPage > 1 ? `<button onclick="${loadFunction}(${currentPage - 1})" class="px-4 py-2 border rounded">Previous</button>` : ''}
            ${pages.map(page => `
                <button onclick="${loadFunction}(${page})" 
                    class="px-4 py-2 border rounded ${page === currentPage ? 'bg-blue-500 text-white' : ''}">
                    ${page}
                </button>
            `).join('')}
            ${currentPage < pagination.totalPages ? `<button onclick="${loadFunction}(${currentPage + 1})" class="px-4 py-2 border rounded">Next</button>` : ''}
        </div>
    `;
}

// Export functions to global scope
window.loadPage = loadPage;
window.loadHotels = loadHotels;
window.openHotelModal = openHotelModal;
window.saveHotel = saveHotel;
window.editHotel = editHotel;
window.deleteHotel = deleteHotel;
window.renderPagination = renderPagination;

