// Sentra Farma - Premium Glassmorphic Toast Notification System
const Toast = {
    initContainer() {
        let container = document.getElementById('toast-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toast-container';
            container.className = 'fixed top-6 right-6 z-[9999] flex flex-col gap-3 max-w-sm w-full pointer-events-none px-4 sm:px-0';
            document.body.appendChild(container);
        }
        return container;
    },

    show(message, type = 'info', title = '') {
        const container = this.initContainer();

        const config = {
            success: { 
                bg: 'bg-gradient-to-r from-emerald-600/95 via-brand-700/95 to-teal-800/95 border-emerald-400/30 text-white shadow-emerald-900/20', 
                iconBg: 'bg-white/20 text-emerald-100',
                icon: 'check_circle', 
                title: title || 'Berhasil' 
            },
            error: { 
                bg: 'bg-gradient-to-r from-rose-600/95 via-rose-700/95 to-red-800/95 border-rose-400/30 text-white shadow-rose-900/25', 
                iconBg: 'bg-white/20 text-rose-100',
                icon: 'warning', 
                title: title || 'Gagal Login' 
            },
            warning: { 
                bg: 'bg-gradient-to-r from-amber-500/95 via-amber-600/95 to-orange-600/95 border-amber-300/30 text-white shadow-amber-900/20', 
                iconBg: 'bg-white/20 text-amber-100',
                icon: 'warning', 
                title: title || 'Peringatan' 
            },
            info: { 
                bg: 'bg-gradient-to-r from-emerald-800/95 via-brand-800/95 to-teal-900/95 border-emerald-400/40 text-white shadow-emerald-950/30', 
                iconBg: 'bg-emerald-500/30 text-emerald-100',
                icon: 'notifications', 
                title: title || 'Informasi' 
            }
        }[type] || { 
            bg: 'bg-slate-800/95 border-slate-700/40 text-white shadow-slate-900/20', 
            iconBg: 'bg-white/20 text-slate-100',
            icon: 'notifications', 
            title: title || 'Info' 
        };

        const toast = document.createElement('div');
        toast.className = `pointer-events-auto ${config.bg} backdrop-blur-xl border p-4 rounded-2xl shadow-2xl flex items-start gap-3.5 transform translate-x-8 opacity-0 transition-all duration-300 ease-out relative overflow-hidden group`;

        toast.innerHTML = `
            <div class="w-9 h-9 rounded-xl ${config.iconBg} flex items-center justify-center shrink-0 shadow-sm mt-0.5">
                <span class="material-symbols-outlined text-[20px]">${config.icon}</span>
            </div>
            <div class="flex-1 text-xs pr-2">
                <div class="font-extrabold text-sm tracking-tight mb-0.5">${config.title}</div>
                <div class="text-white/90 font-medium leading-relaxed">${message}</div>
            </div>
            <button onclick="this.parentElement.remove()" class="text-white/60 hover:text-white hover:bg-white/10 p-1 rounded-lg transition-all shrink-0">
                <span class="material-symbols-outlined text-[16px]">close</span>
            </button>
        `;

        container.appendChild(toast);

        // Animate in
        requestAnimationFrame(() => {
            toast.classList.remove('translate-x-8', 'opacity-0');
        });

        // Auto remove after 4.5s
        setTimeout(() => {
            toast.classList.add('translate-x-8', 'opacity-0');
            setTimeout(() => toast.remove(), 300);
        }, 4500);
    },

    success(message, title) { this.show(message, 'success', title); },
    error(message, title) { this.show(message, 'error', title); },
    warning(message, title) { this.show(message, 'warning', title); },
    info(message, title) { this.show(message, 'info', title); }
};

window.Toast = Toast;
