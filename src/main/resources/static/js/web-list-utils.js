// Utility functions for web list pages with search, sort, and pagination

// Sort table by clicking column header
function sortTable(sortBy, currentSortBy, currentSortDir, baseUrl) {
    const search = document.getElementById('search-input')?.value || 
                   document.querySelector('[name="search"]')?.value || '';
    const newSortDir = (currentSortBy === sortBy && currentSortDir === 'asc') ? 'desc' : 'asc';
    const params = new URLSearchParams({
        page: '1',
        sortBy: sortBy,
        sortDir: newSortDir
    });
    if (search) params.append('search', search);
    window.location.href = `${baseUrl}?${params.toString()}`;
}

// Handle sort dropdown changes
function setupSortDropdowns(baseUrl) {
    const sortBySelect = document.getElementById('sort-by');
    const sortDirSelect = document.getElementById('sort-dir');
    
    if (sortBySelect) {
        sortBySelect.addEventListener('change', function() {
            const sortBy = this.value;
            const sortDir = sortDirSelect?.value || 'asc';
            const search = document.getElementById('search-input')?.value || 
                          document.querySelector('[name="search"]')?.value || '';
            const params = new URLSearchParams({
                page: '1',
                sortBy: sortBy,
                sortDir: sortDir
            });
            if (search) params.append('search', search);
            window.location.href = `${baseUrl}?${params.toString()}`;
        });
    }
    
    if (sortDirSelect) {
        sortDirSelect.addEventListener('change', function() {
            const sortDir = this.value;
            const sortBy = sortBySelect?.value || 'name';
            const search = document.getElementById('search-input')?.value || 
                          document.querySelector('[name="search"]')?.value || '';
            const params = new URLSearchParams({
                page: '1',
                sortBy: sortBy,
                sortDir: sortDir
            });
            if (search) params.append('search', search);
            window.location.href = `${baseUrl}?${params.toString()}`;
        });
    }
}

// Client-side search with debounce
function setupClientSearch(tableId) {
    const searchInput = document.getElementById('search-input') || 
                       document.querySelector('[name="search"]') ||
                       document.getElementById('hotel-search');
    if (!searchInput) return;
    
    // Clear search input if there's no search parameter in URL
    const urlParams = new URLSearchParams(window.location.search);
    if (!urlParams.has('search') || urlParams.get('search') === '') {
        searchInput.value = '';
        // Show all rows when there's no search
        const table = document.getElementById(tableId);
        if (table) {
            const rows = table.querySelectorAll('tbody tr:not([data-no-results])');
            rows.forEach(row => {
                row.style.display = '';
            });
            // Remove any "no results" message
            const noResultsRow = table.querySelector('tbody tr[data-no-results]');
            if (noResultsRow) {
                noResultsRow.remove();
            }
        }
    }
    
    let searchTimeout;
    searchInput.addEventListener('input', function(e) {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            const searchTerm = e.target.value.toLowerCase().trim();
            const table = document.getElementById(tableId);
            if (!table) return;
            
            // If search is empty, show all rows
            if (!searchTerm) {
                const rows = table.querySelectorAll('tbody tr:not([data-no-results])');
                rows.forEach(row => {
                    row.style.display = '';
                });
                const noResultsRow = table.querySelector('tbody tr[data-no-results]');
                if (noResultsRow) {
                    noResultsRow.remove();
                }
                return;
            }
            
            const rows = table.querySelectorAll('tbody tr:not([data-no-results])');
            let visibleCount = 0;
            
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                const shouldShow = text.includes(searchTerm);
                row.style.display = shouldShow ? '' : 'none';
                if (shouldShow) visibleCount++;
            });
            
            // Show/hide "no results" message
            const tbody = table.querySelector('tbody');
            let noResultsRow = tbody.querySelector('tr[data-no-results]');
            
            if (visibleCount === 0 && searchTerm && !noResultsRow) {
                noResultsRow = document.createElement('tr');
                noResultsRow.setAttribute('data-no-results', 'true');
                const colCount = table.querySelectorAll('thead th').length;
                noResultsRow.innerHTML = `<td colspan="${colCount}" class="px-6 py-8 text-center text-gray-500">No results match your search</td>`;
                tbody.appendChild(noResultsRow);
            } else if (noResultsRow && (visibleCount > 0 || !searchTerm)) {
                noResultsRow.remove();
            }
        }, 300); // Debounce for 300ms
    });
}

// Initialize all utilities
function initWebListUtils(baseUrl, tableId) {
    setupSortDropdowns(baseUrl);
    setupClientSearch(tableId);
}

