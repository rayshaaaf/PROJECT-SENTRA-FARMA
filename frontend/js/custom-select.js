/**
 * Sentra Farma - Universal Custom Select Dropdown UI Enhancer
 * Automatically upgrades all native <select> HTML elements into 
 * modern, beautifully styled emerald dropdown cards with soft shadows.
 */

(function() {
    // 1. Inject custom CSS dynamically if not present
    function injectStyles() {
        if (document.getElementById('sf-custom-select-styles')) return;

        const styleTag = document.createElement('style');
        styleTag.id = 'sf-custom-select-styles';
        styleTag.textContent = `
            select {
                appearance: none !important;
                -webkit-appearance: none !important;
                -moz-appearance: none !important;
                background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%252364748B'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M19 9l-7 7-7-7'%3E%3C/path%3E%3C/svg%3E") !important;
                background-repeat: no-repeat !important;
                background-position: right 0.75rem center !important;
                background-size: 1.1em 1.1em !important;
                padding-right: 2.25rem !important;
                border-radius: 0.75rem !important;
                font-family: 'Plus Jakarta Sans', -apple-system, BlinkMacSystemFont, sans-serif !important;
                transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1) !important;
            }

            select option {
                padding: 10px 14px !important;
                font-size: 0.875rem !important;
                font-weight: 500 !important;
                color: #1e293b !important;
                background-color: #ffffff !important;
            }

            select option:hover,
            select option:focus,
            select option:checked {
                background: #ecfdf5 !important;
                color: #047857 !important;
                font-weight: 700 !important;
            }

            .sf-select-menu-container {
                position: absolute;
                z-index: 999999;
                background: rgba(255, 255, 255, 0.98);
                backdrop-filter: blur(16px);
                -webkit-backdrop-filter: blur(16px);
                border: 1px solid rgba(16, 185, 129, 0.25);
                border-radius: 1rem;
                box-shadow: 0 20px 40px -10px rgba(0, 0, 0, 0.15), 0 10px 20px -5px rgba(16, 185, 129, 0.1);
                padding: 6px;
                max-height: 250px;
                overflow-y: auto;
                opacity: 0;
                transform: translateY(-8px) scale(0.97);
                pointer-events: none;
                transition: opacity 0.2s ease, transform 0.2s cubic-bezier(0.16, 1, 0.3, 1);
            }

            .sf-select-menu-container.sf-open {
                opacity: 1;
                transform: translateY(0) scale(1);
                pointer-events: auto;
            }

            .sf-select-menu-item {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 9px 14px;
                font-size: 0.8125rem;
                font-weight: 600;
                color: #334155;
                border-radius: 0.625rem;
                cursor: pointer;
                transition: all 0.15s ease;
                margin-bottom: 2px;
            }

            .sf-select-menu-item:hover,
            .sf-select-menu-item.active {
                background-color: #ecfdf5;
                color: #047857;
                padding-left: 17px;
            }

            .sf-select-menu-item.selected {
                background-color: #dcfce7;
                color: #065f46;
                font-weight: 700;
            }

            .sf-select-menu-item .sf-check-icon {
                font-size: 16px;
                color: #10b981;
                margin-left: 8px;
            }
        `;
        document.head.appendChild(styleTag);
    }

    let activeDropdownMenu = null;

    function closeActiveMenu() {
        if (activeDropdownMenu) {
            activeDropdownMenu.classList.remove('sf-open');
            setTimeout(() => {
                if (activeDropdownMenu && activeDropdownMenu.parentNode) {
                    activeDropdownMenu.parentNode.removeChild(activeDropdownMenu);
                }
                activeDropdownMenu = null;
            }, 180);
        }
    }

    function openCustomMenu(selectEl) {
        closeActiveMenu();

        const options = Array.from(selectEl.options);
        if (options.length === 0) return;

        const rect = selectEl.getBoundingClientRect();
        const menu = document.createElement('div');
        menu.className = 'sf-select-menu-container';

        // Set position matching input
        menu.style.top = `${rect.bottom + window.scrollY + 6}px`;
        menu.style.left = `${rect.left + window.scrollX}px`;
        menu.style.width = `${Math.max(rect.width, 180)}px`;

        options.forEach((opt, idx) => {
            if (opt.disabled) return;
            const item = document.createElement('div');
            item.className = 'sf-select-menu-item';
            if (opt.value === selectEl.value || (opt.selected && !selectEl.value)) {
                item.classList.add('selected');
            }

            const labelSpan = document.createElement('span');
            labelSpan.textContent = opt.text;

            const iconSpan = document.createElement('span');
            iconSpan.className = 'material-symbols-outlined sf-check-icon';
            iconSpan.textContent = item.classList.contains('selected') ? 'check_circle' : '';

            item.appendChild(labelSpan);
            item.appendChild(iconSpan);

            item.addEventListener('click', (e) => {
                e.stopPropagation();
                selectEl.value = opt.value;

                // Dispatch native change and input events
                selectEl.dispatchEvent(new Event('change', { bubbles: true }));
                selectEl.dispatchEvent(new Event('input', { bubbles: true }));

                closeActiveMenu();
            });

            menu.appendChild(item);
        });

        document.body.appendChild(menu);
        activeDropdownMenu = menu;

        // Force reflow and show
        requestAnimationFrame(() => {
            menu.classList.add('sf-open');
        });
    }

    function enhanceSelectElement(selectEl) {
        if (selectEl.dataset.sfEnhanced === 'true') return;
        selectEl.dataset.sfEnhanced = 'true';
        selectEl.classList.add('sf-custom-select');

        // Intercept mousedown/click to show sleek custom floating dropdown
        selectEl.addEventListener('mousedown', function(e) {
            // Check if device is desktop / normal screen
            if (window.innerWidth > 480) {
                e.preventDefault();
                selectEl.focus();
                if (activeDropdownMenu && activeDropdownMenu.dataset.targetId === selectEl.id) {
                    closeActiveMenu();
                } else {
                    openCustomMenu(selectEl);
                }
            }
        });
    }

    function enhanceAllSelects() {
        injectStyles();
        document.querySelectorAll('select').forEach(enhanceSelectElement);
    }

    // Auto init on DOM ready & window scroll/resize events
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', enhanceAllSelects);
    } else {
        enhanceAllSelects();
    }

    // Observer for dynamically added selects
    const observer = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
            mutation.addedNodes.forEach((node) => {
                if (node.nodeType === 1) {
                    if (node.tagName === 'SELECT') {
                        enhanceSelectElement(node);
                    } else if (node.querySelectorAll) {
                        node.querySelectorAll('select').forEach(enhanceSelectElement);
                    }
                }
            });
        });
    });

    observer.observe(document.body || document.documentElement, {
        childList: true,
        subtree: true
    });

    // Close menu on click outside, scroll, resize
    window.addEventListener('click', (e) => {
        if (activeDropdownMenu && !activeDropdownMenu.contains(e.target) && !e.target.closest('select')) {
            closeActiveMenu();
        }
    });

    window.addEventListener('scroll', () => {
        if (activeDropdownMenu) closeActiveMenu();
    }, { passive: true });

    window.addEventListener('resize', () => {
        if (activeDropdownMenu) closeActiveMenu();
    });

    window.SFCustomSelect = {
        enhanceAll: enhanceAllSelects,
        enhance: enhanceSelectElement
    };
})();
