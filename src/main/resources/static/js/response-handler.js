// Response Handler Utility
// Handles different API response structures consistently

class ResponseHandler {
    /**
     * Extract data from ApiResponse structure
     * Handles both ApiResponse<List<T>> and ApiResponse<PayloadResponse<T>>
     */
    static extractPayload(response) {
        if (!response) return null;
        
        // If response is already the data (no wrapper)
        if (Array.isArray(response)) return response;
        if (response.items) return response;
        
        // If response has payload property
        if (response.payload) {
            // Check if payload is PayloadResponse (has items and pagination)
            if (response.payload.items !== undefined) {
                return response.payload;
            }
            // Otherwise payload is the data directly
            return response.payload;
        }
        
        // Fallback: return response itself
        return response;
    }
    
    /**
     * Extract items from response (for paginated responses)
     */
    static extractItems(response) {
        const payload = this.extractPayload(response);
        if (!payload) return [];
        
        // If payload has items (PayloadResponse structure)
        if (payload.items && Array.isArray(payload.items)) {
            return payload.items;
        }
        
        // If payload is directly an array
        if (Array.isArray(payload)) {
            return payload;
        }
        
        return [];
    }
    
    /**
     * Extract pagination from response
     */
    static extractPagination(response) {
        const payload = this.extractPayload(response);
        if (!payload) return null;
        
        // If payload has pagination (PayloadResponse structure)
        if (payload.pagination) {
            return payload.pagination;
        }
        
        return null;
    }
    
    /**
     * Extract single item from response (for getById endpoints)
     */
    static extractItem(response) {
        const payload = this.extractPayload(response);
        if (!payload) return null;
        
        // If payload is an object (not array), return it
        if (payload && typeof payload === 'object' && !Array.isArray(payload)) {
            // Check if it's PayloadResponse structure
            if (payload.items) {
                return payload.items[0] || null;
            }
            return payload;
        }
        
        return null;
    }
}

// Export to global scope
window.ResponseHandler = ResponseHandler;





