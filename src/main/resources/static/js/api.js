// API Base URL
const API_BASE_URL = '/api/v1';

// Helper function to check if response is ok and parse JSON
async function handleResponse(response) {
    if (!response.ok) {
        const errorText = await response.text();
        let errorMessage = `HTTP ${response.status}: ${response.statusText}`;
        try {
            const errorJson = JSON.parse(errorText);
            errorMessage = errorJson.message || errorJson.error || errorMessage;
        } catch (e) {
            errorMessage = errorText || errorMessage;
        }
        throw new Error(errorMessage);
    }
    return response.json();
}

// API Service Class
class HotelAPI {
    // Hotels
    static async getHotels(page = 1, size = 10) {
        const response = await fetch(`${API_BASE_URL}/hotel?page=${page}&size=${size}`);
        return handleResponse(response);
    }

    static async getHotelById(id) {
        const response = await fetch(`${API_BASE_URL}/hotel/${id}`);
        return handleResponse(response);
    }

    static async createHotel(data) {
        const response = await fetch(`${API_BASE_URL}/hotel`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async updateHotel(id, data) {
        const response = await fetch(`${API_BASE_URL}/hotel/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async deleteHotel(id) {
        const response = await fetch(`${API_BASE_URL}/hotel/${id}`, {
            method: 'DELETE'
        });
        return handleResponse(response);
    }

    // Rooms
    static async getRooms(page = 1, size = 10) {
        const response = await fetch(`${API_BASE_URL}/room?page=${page}&size=${size}`);
        return handleResponse(response);
    }

    static async getRoomById(id) {
        const response = await fetch(`${API_BASE_URL}/room/${id}`);
        return handleResponse(response);
    }

    static async createRoom(data) {
        const response = await fetch(`${API_BASE_URL}/room`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async updateRoom(id, data) {
        const response = await fetch(`${API_BASE_URL}/room/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async deleteRoom(id) {
        const response = await fetch(`${API_BASE_URL}/room/${id}`, {
            method: 'DELETE'
        });
        return handleResponse(response);
    }

    // Room Types
    static async getRoomTypes(page = 1, size = 10) {
        const response = await fetch(`${API_BASE_URL}/room-type?page=${page}&size=${size}`);
        return handleResponse(response);
    }

    static async getRoomTypeById(id) {
        const response = await fetch(`${API_BASE_URL}/room-type/${id}`);
        return handleResponse(response);
    }

    static async createRoomType(data) {
        const response = await fetch(`${API_BASE_URL}/room-type`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async updateRoomType(id, data) {
        const response = await fetch(`${API_BASE_URL}/room-type/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async deleteRoomType(id) {
        const response = await fetch(`${API_BASE_URL}/room-type/${id}`, {
            method: 'DELETE'
        });
        return handleResponse(response);
    }

    // Bookings
    static async getBookings(page = 1, size = 10) {
        const response = await fetch(`${API_BASE_URL}/booking?page=${page}&size=${size}`);
        return handleResponse(response);
    }

    static async getBookingById(id) {
        const response = await fetch(`${API_BASE_URL}/booking/${id}`);
        return handleResponse(response);
    }

    static async createBooking(data) {
        const response = await fetch(`${API_BASE_URL}/booking`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async updateBooking(id, data) {
        const response = await fetch(`${API_BASE_URL}/booking/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async deleteBooking(id) {
        const response = await fetch(`${API_BASE_URL}/booking/${id}`, {
            method: 'DELETE'
        });
        return handleResponse(response);
    }

    // Bills
    static async getBills(page = 1, size = 10) {
        const response = await fetch(`${API_BASE_URL}/bill?page=${page}&size=${size}`);
        return handleResponse(response);
    }

    static async getBillById(id) {
        const response = await fetch(`${API_BASE_URL}/bill/${id}`);
        return handleResponse(response);
    }

    static async createBill(data) {
        const response = await fetch(`${API_BASE_URL}/bill`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async updateBill(id, data) {
        const response = await fetch(`${API_BASE_URL}/bill/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async deleteBill(id) {
        const response = await fetch(`${API_BASE_URL}/bill/${id}`, {
            method: 'DELETE'
        });
        return handleResponse(response);
    }

    // Guests
    static async getGuests() {
        const response = await fetch(`${API_BASE_URL}/guest`);
        return handleResponse(response);
    }

    static async getGuestById(id) {
        const response = await fetch(`${API_BASE_URL}/guest/${id}`);
        return handleResponse(response);
    }

    // Employees
    static async getEmployees() {
        const response = await fetch(`${API_BASE_URL}/employee`);
        return handleResponse(response);
    }

    // Roles
    static async getRoles(page = 1, size = 10) {
        const response = await fetch(`${API_BASE_URL}/role?page=${page}&size=${size}`);
        return handleResponse(response);
    }

    static async getRoleById(id) {
        const response = await fetch(`${API_BASE_URL}/role/${id}`);
        return handleResponse(response);
    }

    static async createRole(data) {
        const response = await fetch(`${API_BASE_URL}/role`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async updateRole(id, data) {
        const response = await fetch(`${API_BASE_URL}/role/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return handleResponse(response);
    }

    static async deleteRole(id) {
        const response = await fetch(`${API_BASE_URL}/role/${id}`, {
            method: 'DELETE'
        });
        return handleResponse(response);
    }
}

// Enhanced API wrapper with loading, error handling, and response validation
const originalAPI = { ...HotelAPI };

// Wrap all API methods with optional loading spinner and error handling
Object.keys(HotelAPI).forEach(method => {
    if (typeof HotelAPI[method] === 'function') {
        const originalMethod = HotelAPI[method];
        HotelAPI[method] = async function(...args) {
            try {
                // Only show loading for longer operations (not for quick GET requests)
                const showLoading = method.toLowerCase().includes('create') || 
                                  method.toLowerCase().includes('update') || 
                                  method.toLowerCase().includes('delete') ||
                                  method.toLowerCase().includes('save');
                
                if (showLoading && typeof LoadingSpinner !== 'undefined') {
                    LoadingSpinner.show();
                }
                
                const result = await originalMethod.apply(this, args);
                
                if (showLoading && typeof LoadingSpinner !== 'undefined') {
                    LoadingSpinner.hide();
                }
                
                return result;
            } catch (error) {
                if (typeof LoadingSpinner !== 'undefined') {
                    LoadingSpinner.hide();
                }
                if (typeof Toast !== 'undefined') {
                    Toast.show(error.message || 'An error occurred', 'error');
                }
                throw error;
            }
        };
    }
});
