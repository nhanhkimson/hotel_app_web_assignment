// Utility Functions and UI Components

// Enhanced Toast Notification System (Flowbite Design)
class Toast {
    static show(message, type = 'success', duration = 3000) {
        // Generate unique ID for toast
        const toastId = `toast-${type}-${Date.now()}`;
        
        const toast = document.createElement('div');
        toast.id = toastId;
        toast.setAttribute('role', 'alert');
        
        // Type configurations
        const typeConfig = {
            success: {
                iconBg: 'bg-green-100',
                iconText: 'text-green-600',
                iconSvg: '<path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 11.917 9.724 16.5 19 7.5"/>',
                iconLabel: 'Check icon'
            },
            error: {
                iconBg: 'bg-red-100',
                iconText: 'text-red-600',
                iconSvg: '<path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18 17.94 6M18 18 6.06 6"/>',
                iconLabel: 'Error icon'
            },
            warning: {
                iconBg: 'bg-yellow-100',
                iconText: 'text-yellow-600',
                iconSvg: '<path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 13V8m0 8h.01M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>',
                iconLabel: 'Warning icon'
            },
            info: {
                iconBg: 'bg-blue-100',
                iconText: 'text-blue-600',
                iconSvg: '<path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0z"/>',
                iconLabel: 'Info icon'
            }
        };
        
        const config = typeConfig[type] || typeConfig.success;
        
        // Flowbite-style toast classes
        toast.className = 'flex items-center w-full max-w-sm p-4 text-gray-700 bg-white rounded-lg shadow-sm border border-gray-200 fixed top-4 right-4 z-50 transform transition-all duration-300 translate-x-full opacity-0';
        
        toast.innerHTML = `
            <div class="inline-flex items-center justify-center shrink-0 w-7 h-7 ${config.iconText} ${config.iconBg} rounded">
                <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                    ${config.iconSvg}
                </svg>
                <span class="sr-only">${config.iconLabel}</span>
            </div>
            <div class="ms-3 text-sm font-normal">${message}</div>
            <button type="button" 
                    class="ms-auto flex items-center justify-center text-gray-700 hover:text-gray-900 bg-transparent border border-transparent hover:bg-gray-100 focus:ring-4 focus:ring-gray-200 font-medium rounded text-sm h-8 w-8 focus:outline-none" 
                    data-dismiss-target="#${toastId}" 
                    aria-label="Close">
                <span class="sr-only">Close</span>
                <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18 17.94 6M18 18 6.06 6"/>
                </svg>
            </button>
        `;
        
        // Ensure toast container exists
        let toastContainer = document.getElementById('toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.id = 'toast-container';
            toastContainer.className = 'fixed top-4 right-4 z-50 space-y-4';
            document.body.appendChild(toastContainer);
        }
        
        toastContainer.appendChild(toast);
        
        // Animate in
        setTimeout(() => {
            toast.classList.remove('translate-x-full', 'opacity-0');
            toast.classList.add('translate-x-0', 'opacity-100');
        }, 10);
        
        // Auto remove after duration
        if (duration > 0) {
            setTimeout(() => {
                this.dismiss(toastId);
            }, duration);
        }
        
        // Handle close button click
        const closeButton = toast.querySelector(`[data-dismiss-target="#${toastId}"]`);
        if (closeButton) {
            closeButton.addEventListener('click', () => {
                this.dismiss(toastId);
            });
        }
    }
    
    static dismiss(toastId) {
        const toast = document.getElementById(toastId);
        if (toast) {
            toast.classList.add('translate-x-full', 'opacity-0');
            setTimeout(() => {
                toast.remove();
                // Remove container if empty
                const container = document.getElementById('toast-container');
                if (container && container.children.length === 0) {
                    container.remove();
                }
            }, 300);
        }
    }
}

// Enhanced Loading Spinner Component
class LoadingSpinner {
    static _hideTimeout = null;
    
    static show(targetElement) {
        // Clear any pending hide timeout
        if (this._hideTimeout) {
            clearTimeout(this._hideTimeout);
            this._hideTimeout = null;
        }
        
        // Remove existing spinner if any
        this.forceHide();
        
        const spinner = document.createElement('div');
        spinner.id = 'loading-spinner';
        spinner.className = 'fixed inset-0 z-50 flex items-center justify-center backdrop-blur-sm';
        spinner.style.backgroundColor = 'rgba(0, 0, 0, 0.4)';
        spinner.style.opacity = '1';
        spinner.innerHTML = `
            <div class="bg-white rounded-2xl p-8 shadow-2xl transform transition-all animate-scale-in max-w-sm w-full mx-4">
                <div class="flex flex-col items-center space-y-4">
                    <div class="relative">
                        <div class="animate-spin rounded-full h-16 w-16 border-4 border-blue-100"></div>
                        <div class="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-500 absolute top-0 left-0"></div>
                    </div>
                    <p class="text-gray-700 font-semibold text-lg">Loading...</p>
                    <p class="text-gray-500 text-sm">Please wait</p>
                </div>
            </div>
        `;
        document.body.appendChild(spinner);
    }
    
    static hide() {
        const spinner = document.getElementById('loading-spinner');
        if (spinner) {
            // Clear any existing timeout
            if (this._hideTimeout) {
                clearTimeout(this._hideTimeout);
            }
            
            spinner.style.opacity = '0';
            spinner.style.transition = 'opacity 0.2s ease-out';
            
            // Use a shorter timeout and ensure it executes
            this._hideTimeout = setTimeout(() => {
                this.forceHide();
                this._hideTimeout = null;
            }, 200);
        }
    }
    
    // Force hide - removes immediately without animation
    static forceHide() {
        // Clear any pending timeout
        if (this._hideTimeout) {
            clearTimeout(this._hideTimeout);
            this._hideTimeout = null;
        }
        
        const spinner = document.getElementById('loading-spinner');
        if (spinner) {
            // Remove immediately
            if (spinner.parentNode) {
                spinner.parentNode.removeChild(spinner);
            }
        }
        
        // Re-enable body scroll if no other modals
        if (!document.querySelector('.modal-overlay')) {
            document.body.classList.remove('modal-open');
        }
    }
}

// Enhanced Modal Component with better styling
class Modal {
    static create(title, content, onConfirm = null, confirmText = 'Save', cancelText = 'Cancel', showFooter = true) {
        // Remove existing modals
        document.querySelectorAll('.modal-overlay').forEach(m => m.remove());
        
        const modal = document.createElement('div');
        modal.className = 'modal-overlay fixed inset-0 z-50 flex items-center justify-center p-4';
        modal.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
        modal.style.backdropFilter = 'blur(4px)';
        modal.style.opacity = '0';
        modal.style.transition = 'opacity 0.3s ease-in-out';
        
        modal.innerHTML = `
            <div class="modal-content bg-white rounded-2xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-hidden transform transition-all" style="transform: scale(0.95);">
                <div class="bg-gradient-to-r from-blue-500 via-blue-600 to-indigo-600 px-6 py-5 shadow-lg">
                    <div class="flex items-center justify-between">
                        <h3 class="text-2xl font-bold text-white">${title}</h3>
                        <button type="button" class="modal-close text-white hover:text-gray-200 transition-colors p-1 rounded-full hover:bg-white hover:bg-opacity-20">
                            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                            </svg>
                        </button>
                    </div>
                </div>
                <div class="p-6 overflow-y-auto max-h-[calc(90vh-140px)] custom-scrollbar bg-gray-50">
                    ${content}
                </div>
                ${showFooter ? `
                <div class="bg-white px-6 py-4 flex justify-end space-x-3 border-t border-gray-200 shadow-inner">
                    <button type="button" class="modal-cancel px-6 py-2.5 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-all font-medium shadow-sm hover:shadow">
                        ${cancelText}
                    </button>
                    ${onConfirm ? `<button type="button" class="modal-confirm px-6 py-2.5 bg-gradient-to-r from-blue-500 to-indigo-600 text-white rounded-lg hover:from-blue-600 hover:to-indigo-700 transition-all font-medium shadow-lg hover:shadow-xl">
                        ${confirmText}
                    </button>` : ''}
                </div>
                ` : ''}
            </div>
        `;
        
        // Close handlers
        const closeBtn = modal.querySelector('.modal-close');
        if (closeBtn) {
            closeBtn.addEventListener('click', () => this.close(modal));
        }
        
        const cancelBtn = modal.querySelector('.modal-cancel');
        if (cancelBtn) {
            cancelBtn.addEventListener('click', () => this.close(modal));
        }
        
        if (onConfirm) {
            const confirmBtn = modal.querySelector('.modal-confirm');
            if (confirmBtn) {
                confirmBtn.addEventListener('click', () => {
                    onConfirm();
                    this.close(modal);
                });
            }
        }
        
        // Close on backdrop click
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                this.close(modal);
            }
        });
        
        // Close on ESC key
        const escHandler = (e) => {
            if (e.key === 'Escape') {
                this.close(modal);
                document.removeEventListener('keydown', escHandler);
            }
        };
        document.addEventListener('keydown', escHandler);
        
        document.body.appendChild(modal);
        
        // Prevent body scroll
        document.body.classList.add('modal-open');
        
        // Animate in
        setTimeout(() => {
            modal.style.opacity = '1';
            const content = modal.querySelector('.modal-content');
            if (content) {
                content.style.transform = 'scale(1)';
            }
        }, 10);
        
        return modal;
    }
    
    static close(modal) {
        if (!modal) return;
        
        // Re-enable body scroll
        document.body.classList.remove('modal-open');
        
        modal.style.opacity = '0';
        const content = modal.querySelector('.modal-content');
        if (content) {
            content.style.transform = 'scale(0.95)';
        }
        
        setTimeout(() => {
            modal.remove();
        }, 300);
    }
}

// Form Validation Helper
class FormValidator {
    static validate(form) {
        const errors = [];
        const inputs = form.querySelectorAll('[required]');
        
        inputs.forEach(input => {
            if (!input.value.trim()) {
                errors.push(`${input.name || input.id} is required`);
                input.classList.add('border-red-500');
            } else {
                input.classList.remove('border-red-500');
                input.classList.add('border-green-500');
            }
        });
        
        // Email validation
        const emailInputs = form.querySelectorAll('input[type="email"]');
        emailInputs.forEach(input => {
            if (input.value && !this.isValidEmail(input.value)) {
                errors.push('Invalid email format');
                input.classList.add('border-red-500');
            }
        });
        
        return {
            isValid: errors.length === 0,
            errors
        };
    }
    
    static isValidEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }
}

// Search and Filter Helper
class SearchFilter {
    static filterTable(tableId, searchTerm, searchableColumns = []) {
        const table = document.getElementById(tableId);
        if (!table) return;
        
        const rows = table.querySelectorAll('tbody tr');
        const term = searchTerm.toLowerCase();
        
        rows.forEach(row => {
            let found = false;
            const cells = row.querySelectorAll('td');
            
            searchableColumns.forEach(colIndex => {
                if (cells[colIndex] && cells[colIndex].textContent.toLowerCase().includes(term)) {
                    found = true;
                }
            });
            
            row.style.display = found ? '' : 'none';
        });
    }
}

// Date Formatter
const DateFormatter = {
    format(dateString, format = 'short') {
        if (!dateString) return '';
        const date = new Date(dateString);
        
        if (format === 'short') {
            return date.toLocaleDateString();
        } else if (format === 'long') {
            return date.toLocaleDateString('en-US', { 
                year: 'numeric', 
                month: 'long', 
                day: 'numeric' 
            });
        } else if (format === 'datetime') {
            return date.toLocaleString();
        }
        return dateString;
    },
    
    formatTime(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleTimeString('en-US', { 
            hour: '2-digit', 
            minute: '2-digit' 
        });
    }
};

// Currency Formatter
const CurrencyFormatter = {
    format(amount) {
        if (!amount && amount !== 0) return '$0.00';
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    }
};

// Debounce function for search
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Smooth scroll to element
function smoothScrollTo(element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

// Export to global scope
window.Toast = Toast;
window.LoadingSpinner = LoadingSpinner;
window.Modal = Modal;
window.FormValidator = FormValidator;
window.SearchFilter = SearchFilter;
window.DateFormatter = DateFormatter;
window.CurrencyFormatter = CurrencyFormatter;
window.debounce = debounce;
window.smoothScrollTo = smoothScrollTo;

// Update existing formatDate function
window.formatDate = (dateString) => DateFormatter.format(dateString);
window.formatDateTime = (dateString) => DateFormatter.format(dateString, 'datetime');

