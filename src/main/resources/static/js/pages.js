// Pages for all entities

// Rooms Management
async function loadRooms(page = 1) {
    const content = document.getElementById('main-content');
    // Show loading for initial page load
    if (page === 1 && typeof LoadingSpinner !== 'undefined') {
        LoadingSpinner.show();
    }
    try {
        const [roomsRes, hotelsRes, roomTypesRes] = await Promise.all([
            HotelAPI.getRooms(page, 10),
            HotelAPI.getHotels(1, 100),
            HotelAPI.getRoomTypes(1, 100)
        ]);
        
        // Handle ApiResponse<PayloadResponse<RoomDTO>> structure
        const rooms = ResponseHandler.extractItems(roomsRes);
        const pagination = ResponseHandler.extractPagination(roomsRes) || {};
        const hotels = ResponseHandler.extractItems(hotelsRes);
        const roomTypes = ResponseHandler.extractItems(roomTypesRes);

        content.innerHTML = `
            <div class="p-6">
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-3xl font-bold">Rooms</h1>
                    <button onclick="openRoomModal()" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Add Room
                    </button>
                </div>
                
                <div class="bg-white rounded-lg shadow overflow-hidden">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Room No</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Hotel</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Room Type</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Occupancy</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            ${rooms.map(room => `
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">${room.roomNo || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${room.hotelName || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${room.roomType || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${room.occupancy || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        <button onclick="editRoom('${room.roomId}')" class="text-blue-600 hover:text-blue-900">Edit</button>
                                        <button onclick="deleteRoom('${room.roomId}')" class="text-red-600 hover:text-red-900">Delete</button>
                                    </td>
                                </tr>
                            `).join('') || '<tr><td colspan="5" class="px-6 py-4 text-center text-gray-500">No rooms found</td></tr>'}
                        </tbody>
                    </table>
                </div>
                
                ${renderPagination(pagination, page, 'loadRooms')}
            </div>
        `;
        
        window.hotelsData = hotels;
        window.roomTypesData = roomTypes;
        
        // Hide spinner after content is rendered
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    } catch (error) {
        content.innerHTML = `<div class="p-6"><p class="text-red-500">Error loading rooms: ${error.message}</p></div>`;
        // Hide spinner on error
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    }
}

async function openRoomModal(roomId = null) {
    let room = null;
    if (roomId) {
        const response = await HotelAPI.getRoomById(roomId);
        room = ResponseHandler.extractItem(response);
    }

    const hotels = window.hotelsData || [];
    const roomTypes = window.roomTypesData || [];

    const modal = document.createElement('div');
    modal.className = 'fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center';
    modal.innerHTML = `
        <div class="bg-white rounded-lg p-6 max-w-2xl w-full mx-4">
            <h3 class="text-xl font-bold mb-4">${roomId ? 'Edit' : 'Add'} Room</h3>
            <form id="roomForm" onsubmit="saveRoom(event, '${roomId || ''}')">
                <div class="space-y-4">
                    <div>
                        <label class="block text-sm font-medium mb-1">Room Number *</label>
                        <input type="text" name="roomNo" value="${room?.roomNo || ''}" required
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Hotel *</label>
                        <select name="hotelCode" required class="w-full px-3 py-2 border rounded">
                            <option value="">Select Hotel</option>
                            ${hotels.map(h => `<option value="${h.hotelCode}" ${room?.hotel?.hotelCode === h.hotelCode ? 'selected' : ''}>${h.hotelName}</option>`).join('')}
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Room Type *</label>
                        <select name="roomTypeId" required class="w-full px-3 py-2 border rounded">
                            <option value="">Select Room Type</option>
                            ${roomTypes.map(rt => `<option value="${rt.roomTypeId}" ${room?.roomType?.roomTypeId === rt.roomTypeId ? 'selected' : ''}>${rt.roomType} - $${rt.roomPrice}</option>`).join('')}
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Occupancy</label>
                        <input type="text" name="occupancy" value="${room?.occupancy || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                </div>
                <div class="flex justify-end space-x-2 mt-6">
                    <button type="button" onclick="this.closest('.fixed').remove()" 
                        class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300">Cancel</button>
                    <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Save</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(modal);
}

async function saveRoom(event, roomId) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const data = {
        roomNo: formData.get('roomNo'),
        hotelCode: formData.get('hotelCode'),
        roomTypeId: formData.get('roomTypeId'),
        occupancy: formData.get('occupancy')
    };

    try {
        if (roomId) {
            await HotelAPI.updateRoom(roomId, data);
            if (typeof Toast !== 'undefined') Toast.show('Room updated successfully', 'success');
        } else {
            await HotelAPI.createRoom(data);
            if (typeof Toast !== 'undefined') Toast.show('Room created successfully', 'success');
        }
        document.querySelector('.fixed').remove();
        loadRooms();
    } catch (error) {
            if (typeof Toast !== 'undefined') Toast.show('Error saving room: ' + (error.message || 'Unknown error'), 'error');
    }
}

async function editRoom(id) {
    await openRoomModal(id);
}

async function deleteRoom(id) {
    if (typeof Modal !== 'undefined') {
        Modal.create('Delete Room', '<p class="text-gray-700">Are you sure you want to delete this room? This action cannot be undone.</p>', async () => {
            try {
                await HotelAPI.deleteRoom(id);
                if (typeof Toast !== 'undefined') Toast.show('Room deleted successfully', 'success');
                loadRooms();
            } catch (error) {
                if (typeof Toast !== 'undefined') Toast.show('Error deleting room: ' + (error.message || 'Unknown error'), 'error');
            }
        }, 'Delete', 'Cancel');
    } else {
        if (confirm('Are you sure you want to delete this room?')) {
            try {
                await HotelAPI.deleteRoom(id);
                if (typeof Toast !== 'undefined') Toast.show('Room deleted successfully', 'success');
                loadRooms();
            } catch (error) {
                if (typeof Toast !== 'undefined') Toast.show('Error deleting room: ' + (error.message || 'Unknown error'), 'error');
            }
        }
    }
}

// Room Types Management
async function loadRoomTypes(page = 1) {
    const content = document.getElementById('main-content');
    if (page === 1 && typeof LoadingSpinner !== 'undefined') {
        LoadingSpinner.show();
    }
    try {
        const response = await HotelAPI.getRoomTypes(page, 10);
        // Handle ApiResponse<PayloadResponse<RoomTypeDto>> structure
        const roomTypes = ResponseHandler.extractItems(response);
        const pagination = ResponseHandler.extractPagination(response) || {};

        content.innerHTML = `
            <div class="p-6">
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-3xl font-bold">Room Types</h1>
                    <button onclick="openRoomTypeModal()" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Add Room Type
                    </button>
                </div>
                
                <div class="bg-white rounded-lg shadow overflow-hidden">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Price</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Default Price</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            ${roomTypes.map(rt => `
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">${rt.roomType || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">$${rt.roomPrice || 0}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">$${rt.defaultRoomPrice || 0}</td>
                                    <td class="px-6 py-4 text-sm text-gray-500">${rt.roomDesc || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        <button onclick="editRoomType('${rt.roomTypeId}')" class="text-blue-600 hover:text-blue-900">Edit</button>
                                        <button onclick="deleteRoomType('${rt.roomTypeId}')" class="text-red-600 hover:text-red-900">Delete</button>
                                    </td>
                                </tr>
                            `).join('') || '<tr><td colspan="5" class="px-6 py-4 text-center text-gray-500">No room types found</td></tr>'}
                        </tbody>
                    </table>
                </div>
                
                ${renderPagination(pagination, page, 'loadRoomTypes')}
            </div>
        `;
        
        // Hide spinner after content is rendered
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    } catch (error) {
        content.innerHTML = `<div class="p-6"><p class="text-red-500">Error loading room types: ${error.message}</p></div>`;
        // Hide spinner on error
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    }
}

async function openRoomTypeModal(roomTypeId = null) {
    let roomType = null;
    if (roomTypeId) {
        const response = await HotelAPI.getRoomTypeById(roomTypeId);
        roomType = ResponseHandler.extractItem(response);
    }

    const modal = document.createElement('div');
    modal.className = 'fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center';
    modal.innerHTML = `
        <div class="bg-white rounded-lg p-6 max-w-2xl w-full mx-4">
            <h3 class="text-xl font-bold mb-4">${roomTypeId ? 'Edit' : 'Add'} Room Type</h3>
            <form id="roomTypeForm" onsubmit="saveRoomType(event, '${roomTypeId || ''}')">
                <div class="space-y-4">
                    <div>
                        <label class="block text-sm font-medium mb-1">Room Type *</label>
                        <input type="text" name="roomType" value="${roomType?.roomType || ''}" required
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-medium mb-1">Room Price</label>
                            <input type="number" step="0.01" name="roomPrice" value="${roomType?.roomPrice || ''}"
                                class="w-full px-3 py-2 border rounded">
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Default Price</label>
                            <input type="number" step="0.01" name="defaultRoomPrice" value="${roomType?.defaultRoomPrice || ''}"
                                class="w-full px-3 py-2 border rounded">
                        </div>
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Room Image URL</label>
                        <input type="text" name="roomImg" value="${roomType?.roomImg || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Description</label>
                        <textarea name="roomDesc" rows="3" class="w-full px-3 py-2 border rounded">${roomType?.roomDesc || ''}</textarea>
                    </div>
                </div>
                <div class="flex justify-end space-x-2 mt-6">
                    <button type="button" onclick="this.closest('.fixed').remove()" 
                        class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300">Cancel</button>
                    <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Save</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(modal);
}

async function saveRoomType(event, roomTypeId) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const data = {
        roomType: formData.get('roomType'),
        roomPrice: formData.get('roomPrice') ? parseFloat(formData.get('roomPrice')) : null,
        defaultRoomPrice: formData.get('defaultRoomPrice') ? parseFloat(formData.get('defaultRoomPrice')) : null,
        roomImg: formData.get('roomImg'),
        roomDesc: formData.get('roomDesc')
    };

    try {
        if (roomTypeId) {
            await HotelAPI.updateRoomType(roomTypeId, data);
            if (typeof Toast !== 'undefined') Toast.show('Room type updated successfully', 'success');
        } else {
            await HotelAPI.createRoomType(data);
            if (typeof Toast !== 'undefined') Toast.show('Room type created successfully', 'success');
        }
        document.querySelector('.fixed').remove();
        loadRoomTypes();
    } catch (error) {
        if (typeof Toast !== 'undefined') Toast.show('Error saving room type: ' + (error.message || 'Unknown error'), 'error');
    }
}

async function editRoomType(id) {
    await openRoomTypeModal(id);
}

async function deleteRoomType(id) {
    if (confirm('Are you sure you want to delete this room type?')) {
        try {
            await HotelAPI.deleteRoomType(id);
            if (typeof Toast !== 'undefined') Toast.show('Room type deleted successfully', 'success');
            loadRoomTypes();
        } catch (error) {
            if (typeof Toast !== 'undefined') Toast.show('Error deleting room type: ' + (error.message || 'Unknown error'), 'error');
        }
    }
}

// Bookings Management
async function loadBookings(page = 1) {
    const content = document.getElementById('main-content');
    if (page === 1 && typeof LoadingSpinner !== 'undefined') {
        LoadingSpinner.show();
    }
    try {
        const response = await HotelAPI.getBookings(page, 10);
        // Handle ApiResponse<PayloadResponse<BookingDTO>> structure
        const bookings = ResponseHandler.extractItems(response);
        const pagination = ResponseHandler.extractPagination(response) || {};

        content.innerHTML = `
            <div class="p-6">
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-3xl font-bold">Bookings</h1>
                    <button onclick="openBookingModal()" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Add Booking
                    </button>
                </div>
                
                <div class="bg-white rounded-lg shadow overflow-hidden">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Booking ID</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Hotel</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Guest</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Arrival</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Departure</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            ${bookings.map(booking => `
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">${booking.bookingId?.substring(0, 8) || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.hotel?.hotelName || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.guest?.firstName || ''} ${booking.guest?.lastName || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${formatDate(booking.arrivalDate)}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${formatDate(booking.departureDate)}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        <button onclick="editBooking('${booking.bookingId}')" class="text-blue-600 hover:text-blue-900">Edit</button>
                                        <button onclick="deleteBooking('${booking.bookingId}')" class="text-red-600 hover:text-red-900">Delete</button>
                                    </td>
                                </tr>
                            `).join('') || '<tr><td colspan="6" class="px-6 py-4 text-center text-gray-500">No bookings found</td></tr>'}
                        </tbody>
                    </table>
                </div>
                
                ${renderPagination(pagination, page, 'loadBookings')}
            </div>
        `;
        
        // Hide spinner after content is rendered
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    } catch (error) {
        content.innerHTML = `<div class="p-6"><p class="text-red-500">Error loading bookings: ${error.message}</p></div>`;
        // Hide spinner on error
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    }
}

async function openBookingModal(bookingId = null) {
    let booking = null;
    if (bookingId) {
        const response = await HotelAPI.getBookingById(bookingId);
        booking = ResponseHandler.extractItem(response);
    }

    const [hotelsRes, guestsRes, roomsRes] = await Promise.all([
        HotelAPI.getHotels(1, 100),
        HotelAPI.getGuests(),
        HotelAPI.getRooms(1, 100)
    ]);
    // Handle different response structures
    const hotels = ResponseHandler.extractItems(hotelsRes);
    // Guests API returns ApiResponse<List<Guest>>, so payload is the list directly
    const guests = ResponseHandler.extractItems(guestsRes);
    const rooms = ResponseHandler.extractItems(roomsRes);

    const modal = document.createElement('div');
    modal.className = 'fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center';
    modal.innerHTML = `
        <div class="bg-white rounded-lg p-6 max-w-4xl w-full mx-4 max-h-[90vh] overflow-y-auto">
            <h3 class="text-xl font-bold mb-4">${bookingId ? 'Edit' : 'Add'} Booking</h3>
            <form id="bookingForm" onsubmit="saveBooking(event, '${bookingId || ''}')">
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium mb-1">Hotel *</label>
                        <select name="hotelCode" required class="w-full px-3 py-2 border rounded">
                            <option value="">Select Hotel</option>
                            ${hotels.map(h => `<option value="${h.hotelCode}" ${booking?.hotel?.hotelCode === h.hotelCode ? 'selected' : ''}>${h.hotelName}</option>`).join('')}
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Guest *</label>
                        <select name="guestId" required class="w-full px-3 py-2 border rounded">
                            <option value="">Select Guest</option>
                            ${guests.map(g => `<option value="${g.guestId}" ${booking?.guest?.guestId === g.guestId ? 'selected' : ''}>${g.firstName} ${g.lastName}</option>`).join('')}
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Room *</label>
                        <select name="roomId" required class="w-full px-3 py-2 border rounded">
                            <option value="">Select Room</option>
                            ${rooms.map(r => `<option value="${r.roomId}" ${booking?.room?.roomId === r.roomId ? 'selected' : ''}>${r.roomNo} - ${r.roomType?.roomType}</option>`).join('')}
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Booking Date</label>
                        <input type="date" name="bookingDate" value="${booking?.bookingDate ? booking.bookingDate.split('T')[0] : ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Arrival Date *</label>
                        <input type="date" name="arrivalDate" value="${booking?.arrivalDate ? booking.arrivalDate.split('T')[0] : ''}" required
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Departure Date *</label>
                        <input type="date" name="departureDate" value="${booking?.departureDate ? booking.departureDate.split('T')[0] : ''}" required
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Number of Adults</label>
                        <input type="number" name="numAdults" value="${booking?.numAdults || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Number of Children</label>
                        <input type="number" name="numChildren" value="${booking?.numChildren || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div class="col-span-2">
                        <label class="block text-sm font-medium mb-1">Special Requirements</label>
                        <textarea name="specialReq" rows="3" class="w-full px-3 py-2 border rounded">${booking?.specialReq || ''}</textarea>
                    </div>
                </div>
                <div class="flex justify-end space-x-2 mt-6">
                    <button type="button" onclick="this.closest('.fixed').remove()" 
                        class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300">Cancel</button>
                    <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Save</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(modal);
}

async function saveBooking(event, bookingId) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const data = {
        hotelCode: formData.get('hotelCode'),
        guestId: formData.get('guestId'),
        roomId: formData.get('roomId'),
        bookingDate: formData.get('bookingDate'),
        arrivalDate: formData.get('arrivalDate'),
        departureDate: formData.get('departureDate'),
        numAdults: formData.get('numAdults') ? parseInt(formData.get('numAdults')) : null,
        numChildren: formData.get('numChildren') ? parseInt(formData.get('numChildren')) : null,
        specialReq: formData.get('specialReq')
    };

    try {
        if (bookingId) {
            await HotelAPI.updateBooking(bookingId, data);
            if (typeof Toast !== 'undefined') Toast.show('Booking updated successfully', 'success');
        } else {
            await HotelAPI.createBooking(data);
            if (typeof Toast !== 'undefined') Toast.show('Booking created successfully', 'success');
        }
        document.querySelector('.fixed').remove();
        loadBookings();
    } catch (error) {
        if (typeof Toast !== 'undefined') Toast.show('Error saving booking: ' + (error.message || 'Unknown error'), 'error');
    }
}

async function editBooking(id) {
    await openBookingModal(id);
}

async function deleteBooking(id) {
    if (confirm('Are you sure you want to delete this booking?')) {
        try {
            await HotelAPI.deleteBooking(id);
            if (typeof Toast !== 'undefined') Toast.show('Booking deleted successfully', 'success');
            loadBookings();
        } catch (error) {
            if (typeof Toast !== 'undefined') Toast.show('Error deleting booking: ' + (error.message || 'Unknown error'), 'error');
        }
    }
}

// Bills Management
async function loadBills(page = 1) {
    const content = document.getElementById('main-content');
    if (page === 1 && typeof LoadingSpinner !== 'undefined') {
        LoadingSpinner.show();
    }
    try {
        const response = await HotelAPI.getBills(page, 10);
        // Handle ApiResponse<PayloadResponse<BillDTO>> structure
        const bills = ResponseHandler.extractItems(response);
        const pagination = ResponseHandler.extractPagination(response) || {};

        content.innerHTML = `
            <div class="p-6">
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-3xl font-bold">Bills</h1>
                    <button onclick="openBillModal()" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Add Bill
                    </button>
                </div>
                
                <div class="bg-white rounded-lg shadow overflow-hidden">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Invoice No</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Guest</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Room Charge</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Total</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Payment Date</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            ${bills.map(bill => {
                                const total = (bill.roomCharge || 0) + (bill.roomService || 0) + (bill.restaurantCharges || 0) + (bill.barCharges || 0) + (bill.miscCharges || 0);
                                return `
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">${bill.invoiceNo?.substring(0, 8) || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${bill.guest?.firstName || ''} ${bill.guest?.lastName || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">$${bill.roomCharge || 0}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">$${total.toFixed(2)}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${formatDate(bill.paymentDate)}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        <button onclick="editBill('${bill.invoiceNo}')" class="text-blue-600 hover:text-blue-900">Edit</button>
                                        <button onclick="deleteBill('${bill.invoiceNo}')" class="text-red-600 hover:text-red-900">Delete</button>
                                    </td>
                                </tr>
                            `;
                            }).join('') || '<tr><td colspan="6" class="px-6 py-4 text-center text-gray-500">No bills found</td></tr>'}
                        </tbody>
                    </table>
                </div>
                
                ${renderPagination(pagination, page, 'loadBills')}
            </div>
        `;
        
        // Hide spinner after content is rendered
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    } catch (error) {
        content.innerHTML = `<div class="p-6"><p class="text-red-500">Error loading bills: ${error.message}</p></div>`;
        // Hide spinner on error
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    }
}

async function openBillModal(billId = null) {
    let bill = null;
    if (billId) {
        const response = await HotelAPI.getBillById(billId);
        bill = ResponseHandler.extractItem(response);
    }

    const [bookingsRes, guestsRes] = await Promise.all([
        HotelAPI.getBookings(1, 100),
        HotelAPI.getGuests()
    ]);
    // Handle different response structures
    const bookings = ResponseHandler.extractItems(bookingsRes);
    // Guests API returns ApiResponse<List<Guest>>, so payload is the list directly
    const guests = ResponseHandler.extractItems(guestsRes);

    const modal = document.createElement('div');
    modal.className = 'fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center';
    modal.innerHTML = `
        <div class="bg-white rounded-lg p-6 max-w-4xl w-full mx-4 max-h-[90vh] overflow-y-auto">
            <h3 class="text-xl font-bold mb-4">${billId ? 'Edit' : 'Add'} Bill</h3>
            <form id="billForm" onsubmit="saveBill(event, '${billId || ''}')">
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium mb-1">Booking *</label>
                        <select name="bookingId" required class="w-full px-3 py-2 border rounded">
                            <option value="">Select Booking</option>
                            ${bookings.map(b => `<option value="${b.bookingId}" ${bill?.booking?.bookingId === b.bookingId ? 'selected' : ''}>${b.bookingId?.substring(0, 8)}</option>`).join('')}
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Guest *</label>
                        <select name="guestId" required class="w-full px-3 py-2 border rounded">
                            <option value="">Select Guest</option>
                            ${guests.map(g => `<option value="${g.guestId}" ${bill?.guest?.guestId === g.guestId ? 'selected' : ''}>${g.firstName} ${g.lastName}</option>`).join('')}
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Room Charge</label>
                        <input type="number" step="0.01" name="roomCharge" value="${bill?.roomCharge || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Room Service</label>
                        <input type="number" step="0.01" name="roomService" value="${bill?.roomService || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Restaurant Charges</label>
                        <input type="number" step="0.01" name="restaurantCharges" value="${bill?.restaurantCharges || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Bar Charges</label>
                        <input type="number" step="0.01" name="barCharges" value="${bill?.barCharges || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Misc Charges</label>
                        <input type="number" step="0.01" name="miscCharges" value="${bill?.miscCharges || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Late Checkout</label>
                        <input type="checkbox" name="ifLateCheckout" ${bill?.ifLateCheckout ? 'checked' : ''}
                            class="mt-2">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Payment Date</label>
                        <input type="date" name="paymentDate" value="${bill?.paymentDate ? bill.paymentDate.split('T')[0] : ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Payment Mode</label>
                        <select name="paymentMode" class="w-full px-3 py-2 border rounded">
                            <option value="">Select Payment Mode</option>
                            <option value="Cash" ${bill?.paymentMode === 'Cash' ? 'selected' : ''}>Cash</option>
                            <option value="Credit Card" ${bill?.paymentMode === 'Credit Card' ? 'selected' : ''}>Credit Card</option>
                            <option value="Debit Card" ${bill?.paymentMode === 'Debit Card' ? 'selected' : ''}>Debit Card</option>
                            <option value="Cheque" ${bill?.paymentMode === 'Cheque' ? 'selected' : ''}>Cheque</option>
                        </select>
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Credit Card No</label>
                        <input type="text" name="creditCardNo" value="${bill?.creditCardNo || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Expire Date</label>
                        <input type="date" name="expireDate" value="${bill?.expireDate ? bill.expireDate.split('T')[0] : ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Cheque No</label>
                        <input type="text" name="chequeNo" value="${bill?.chequeNo || ''}"
                            class="w-full px-3 py-2 border rounded">
                    </div>
                </div>
                <div class="flex justify-end space-x-2 mt-6">
                    <button type="button" onclick="this.closest('.fixed').remove()" 
                        class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300">Cancel</button>
                    <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Save</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(modal);
}

async function saveBill(event, billId) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const data = {
        bookingId: formData.get('bookingId'),
        guestId: formData.get('guestId'),
        roomCharge: formData.get('roomCharge') ? parseFloat(formData.get('roomCharge')) : null,
        roomService: formData.get('roomService') ? parseFloat(formData.get('roomService')) : null,
        restaurantCharges: formData.get('restaurantCharges') ? parseFloat(formData.get('restaurantCharges')) : null,
        barCharges: formData.get('barCharges') ? parseFloat(formData.get('barCharges')) : null,
        miscCharges: formData.get('miscCharges') ? parseFloat(formData.get('miscCharges')) : null,
        ifLateCheckout: formData.has('ifLateCheckout'),
        paymentDate: formData.get('paymentDate'),
        paymentMode: formData.get('paymentMode'),
        creditCardNo: formData.get('creditCardNo'),
        expireDate: formData.get('expireDate'),
        chequeNo: formData.get('chequeNo')
    };

    try {
        if (billId) {
            await HotelAPI.updateBill(billId, data);
            if (typeof Toast !== 'undefined') Toast.show('Bill updated successfully', 'success');
        } else {
            await HotelAPI.createBill(data);
            if (typeof Toast !== 'undefined') Toast.show('Bill created successfully', 'success');
        }
        document.querySelector('.fixed').remove();
        loadBills();
    } catch (error) {
        if (typeof Toast !== 'undefined') Toast.show('Error saving bill: ' + (error.message || 'Unknown error'), 'error');
    }
}

async function editBill(id) {
    await openBillModal(id);
}

async function deleteBill(id) {
    if (confirm('Are you sure you want to delete this bill?')) {
        try {
            await HotelAPI.deleteBill(id);
            if (typeof Toast !== 'undefined') Toast.show('Bill deleted successfully', 'success');
            loadBills();
        } catch (error) {
            if (typeof Toast !== 'undefined') Toast.show('Error deleting bill: ' + (error.message || 'Unknown error'), 'error');
        }
    }
}

// Guests Management
async function loadGuests() {
    const content = document.getElementById('main-content');
    if (typeof LoadingSpinner !== 'undefined') {
        LoadingSpinner.show();
    }
    try {
        const response = await HotelAPI.getGuests();
        // Handle ApiResponse<List<Guest>> structure: response.payload is List<Guest> directly
        const guests = ResponseHandler.extractItems(response);

        content.innerHTML = `
            <div class="p-6">
                <h1 class="text-3xl font-bold mb-6">Guests</h1>
                
                <div class="bg-white rounded-lg shadow overflow-hidden">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Phone</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">City</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Country</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            ${guests.map(guest => `
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">${guest.firstName || ''} ${guest.lastName || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${guest.email || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${guest.phoneNo || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${guest.city || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${guest.country || ''}</td>
                                </tr>
                            `).join('') || '<tr><td colspan="5" class="px-6 py-4 text-center text-gray-500">No guests found</td></tr>'}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
        
        // Hide spinner after content is rendered
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    } catch (error) {
        content.innerHTML = `<div class="p-6"><p class="text-red-500">Error loading guests: ${error.message}</p></div>`;
        // Hide spinner on error
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    }
}

// Employees Management
async function loadEmployees() {
    const content = document.getElementById('main-content');
    if (typeof LoadingSpinner !== 'undefined') {
        LoadingSpinner.show();
    }
    try {
        const response = await HotelAPI.getEmployees();
        // Handle ApiResponse<List<EmployeeDTO>> structure: response.payload is List<EmployeeDTO> directly
        const employees = ResponseHandler.extractItems(response);

        content.innerHTML = `
            <div class="p-6">
                <h1 class="text-3xl font-bold mb-6">Employees</h1>
                
                <div class="bg-white rounded-lg shadow overflow-hidden">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Phone</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Role</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Salary</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            ${employees.map(emp => `
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">${emp.firstName || ''} ${emp.lastName || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${emp.email || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${emp.phoneNo || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${emp.role?.roleTitle || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">$${emp.salary || 0}</td>
                                </tr>
                            `).join('') || '<tr><td colspan="5" class="px-6 py-4 text-center text-gray-500">No employees found</td></tr>'}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
        
        // Hide spinner after content is rendered
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    } catch (error) {
        content.innerHTML = `<div class="p-6"><p class="text-red-500">Error loading employees: ${error.message}</p></div>`;
        // Hide spinner on error
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    }
}

// Roles Management
async function loadRoles(page = 1) {
    const content = document.getElementById('main-content');
    if (page === 1 && typeof LoadingSpinner !== 'undefined') {
        LoadingSpinner.show();
    }
    try {
        const response = await HotelAPI.getRoles(page, 10);
        // Handle ApiResponse<PayloadResponse<RoleDTO>> structure
        const roles = ResponseHandler.extractItems(response);
        const pagination = ResponseHandler.extractPagination(response) || {};

        content.innerHTML = `
            <div class="p-6">
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-3xl font-bold">Roles</h1>
                    <button onclick="openRoleModal()" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                        Add Role
                    </button>
                </div>
                
                <div class="bg-white rounded-lg shadow overflow-hidden">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Role Title</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            ${roles.map(role => `
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">${role.roleTitle || ''}</td>
                                    <td class="px-6 py-4 text-sm text-gray-500">${role.roleDesc || ''}</td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                        <button onclick="editRole('${role.roleId}')" class="text-blue-600 hover:text-blue-900">Edit</button>
                                        <button onclick="deleteRole('${role.roleId}')" class="text-red-600 hover:text-red-900">Delete</button>
                                    </td>
                                </tr>
                            `).join('') || '<tr><td colspan="3" class="px-6 py-4 text-center text-gray-500">No roles found</td></tr>'}
                        </tbody>
                    </table>
                </div>
                
                ${renderPagination(pagination, page, 'loadRoles')}
            </div>
        `;
        
        // Hide spinner after content is rendered
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    } catch (error) {
        content.innerHTML = `<div class="p-6"><p class="text-red-500">Error loading roles: ${error.message}</p></div>`;
        // Hide spinner on error
        if (typeof LoadingSpinner !== 'undefined') {
            LoadingSpinner.hide();
        }
    }
}

async function openRoleModal(roleId = null) {
    let role = null;
    if (roleId) {
        const response = await HotelAPI.getRoleById(roleId);
        role = ResponseHandler.extractItem(response);
    }

    const modal = document.createElement('div');
    modal.className = 'fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center';
    modal.innerHTML = `
        <div class="bg-white rounded-lg p-6 max-w-2xl w-full mx-4">
            <h3 class="text-xl font-bold mb-4">${roleId ? 'Edit' : 'Add'} Role</h3>
            <form id="roleForm" onsubmit="saveRole(event, '${roleId || ''}')">
                <div class="space-y-4">
                    <div>
                        <label class="block text-sm font-medium mb-1">Role Title *</label>
                        <input type="text" name="roleTitle" value="${role?.roleTitle || ''}" required
                            class="w-full px-3 py-2 border rounded">
                    </div>
                    <div>
                        <label class="block text-sm font-medium mb-1">Description</label>
                        <textarea name="roleDesc" rows="3" class="w-full px-3 py-2 border rounded">${role?.roleDesc || ''}</textarea>
                    </div>
                </div>
                <div class="flex justify-end space-x-2 mt-6">
                    <button type="button" onclick="this.closest('.fixed').remove()" 
                        class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300">Cancel</button>
                    <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Save</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(modal);
}

async function saveRole(event, roleId) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const data = {
        roleTitle: formData.get('roleTitle'),
        roleDesc: formData.get('roleDesc')
    };

    try {
        if (roleId) {
            await HotelAPI.updateRole(roleId, data);
            if (typeof Toast !== 'undefined') Toast.show('Role updated successfully', 'success');
        } else {
            await HotelAPI.createRole(data);
            if (typeof Toast !== 'undefined') Toast.show('Role created successfully', 'success');
        }
        document.querySelector('.fixed').remove();
        loadRoles();
    } catch (error) {
        if (typeof Toast !== 'undefined') Toast.show('Error saving role: ' + (error.message || 'Unknown error'), 'error');
    }
}

async function editRole(id) {
    await openRoleModal(id);
}

async function deleteRole(id) {
    if (confirm('Are you sure you want to delete this role?')) {
        try {
            await HotelAPI.deleteRole(id);
            if (typeof Toast !== 'undefined') Toast.show('Role deleted successfully', 'success');
            loadRoles();
        } catch (error) {
            if (typeof Toast !== 'undefined') Toast.show('Error deleting role: ' + (error.message || 'Unknown error'), 'error');
        }
    }
}

// Pagination helper (if not already defined in app.js)
if (typeof renderPagination === 'undefined') {
    window.renderPagination = function(pagination, currentPage, loadFunction) {
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
    };
}

// Export all functions to global scope
window.loadRooms = loadRooms;
window.openRoomModal = openRoomModal;
window.saveRoom = saveRoom;
window.editRoom = editRoom;
window.deleteRoom = deleteRoom;
window.loadRoomTypes = loadRoomTypes;
window.openRoomTypeModal = openRoomTypeModal;
window.saveRoomType = saveRoomType;
window.editRoomType = editRoomType;
window.deleteRoomType = deleteRoomType;
window.loadBookings = loadBookings;
window.openBookingModal = openBookingModal;
window.saveBooking = saveBooking;
window.editBooking = editBooking;
window.deleteBooking = deleteBooking;
window.loadBills = loadBills;
window.openBillModal = openBillModal;
window.saveBill = saveBill;
window.editBill = editBill;
window.deleteBill = deleteBill;
window.loadGuests = loadGuests;
window.loadEmployees = loadEmployees;
window.loadRoles = loadRoles;
window.openRoleModal = openRoleModal;
window.saveRole = saveRole;
window.editRole = editRole;
window.deleteRole = deleteRole;

