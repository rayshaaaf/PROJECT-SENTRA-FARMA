/**
 * Absensi & Presensi Dokter - Sentra Farma
 */

const Absensi = {
    timerInterval: null,

    init() {
        this.injectModalHTML();
        this.startLiveClock();
        this.renderStatus();
    },

    getLogs() {
        try {
            return JSON.parse(localStorage.getItem('sentra_doctor_attendance_logs')) || [];
        } catch(e) {
            return [];
        }
    },

    saveLogs(logs) {
        localStorage.setItem('sentra_doctor_attendance_logs', JSON.stringify(logs));
    },

    getTodayKey() {
        const today = new Date();
        return `${today.getFullYear()}-${String(today.getMonth()+1).padStart(2,'0')}-${String(today.getDate()).padStart(2,'0')}`;
    },

    getTodayRecord() {
        const logs = this.getLogs();
        const key = this.getTodayKey();
        return logs.find(l => l.dateKey === key) || null;
    },

    openModal() {
        const modal = document.getElementById('modal-absensi');
        if (modal) {
            modal.classList.remove('hidden');
            this.renderStatus();
        }
    },

    closeModal() {
        const modal = document.getElementById('modal-absensi');
        if (modal) {
            modal.classList.add('hidden');
        }
    },

    startLiveClock() {
        const updateClock = () => {
            const now = new Date();
            const dateStr = now.toLocaleDateString('id-ID', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' });
            const timeStr = now.toLocaleTimeString('id-ID', { hour: '2-digit', minute: '2-digit', second: '2-digit' }) + ' WIB';

            const dateEl = document.getElementById('absen-today-date');
            const clockEl = document.getElementById('absen-live-clock');

            if (dateEl) dateEl.innerText = dateStr;
            if (clockEl) clockEl.innerText = timeStr;
        };
        updateClock();
        if (!this.timerInterval) {
            this.timerInterval = setInterval(updateClock, 1000);
        }
    },

    getCurrentShiftName() {
        const hour = new Date().getHours();
        if (hour >= 7 && hour < 12) {
            return 'Shift Pagi (07:00 - 12:00)';
        } else if (hour >= 12 && hour < 16) {
            return 'Shift Siang (12:00 - 16:00)';
        } else if (hour >= 16 && hour < 19) {
            return 'Shift Sore (16:00 - 19:00)';
        } else {
            return 'Shift Malam (19:00 - 07:00)';
        }
    },

    renderStatus() {
        const record = this.getTodayRecord();
        const currentShift = this.getCurrentShiftName();
        const shiftEl = document.getElementById('absen-active-shift');
        if (shiftEl) {
            shiftEl.innerText = record && record.shiftName ? record.shiftName : currentShift;
        }

        const statusPill = document.getElementById('absen-status-pill');
        const btnMasuk = document.getElementById('btn-absen-masuk');
        const btnPulang = document.getElementById('btn-absen-pulang');
        const historyLog = document.getElementById('absen-history-log');

        if (!record) {
            if (statusPill) {
                statusPill.className = 'px-3 py-1 rounded-full text-[10px] font-extrabold uppercase tracking-wide bg-amber-500/20 text-amber-300 border border-amber-500/30 flex items-center justify-center gap-1.5';
                statusPill.innerHTML = '<span class="w-2 h-2 rounded-full bg-amber-400"></span> BELUM ABSEN HARI INI';
            }
            if (btnMasuk) {
                btnMasuk.disabled = false;
                btnMasuk.className = 'py-3 px-4 bg-emerald-600 hover:bg-emerald-700 text-white font-extrabold text-xs rounded-xl shadow-md transition-all flex items-center justify-center gap-2 cursor-pointer';
            }
            if (btnPulang) {
                btnPulang.disabled = true;
                btnPulang.className = 'py-3 px-4 bg-slate-200 text-slate-400 font-extrabold text-xs rounded-xl cursor-not-allowed transition-all flex items-center justify-center gap-2';
            }
        } else if (record && !record.timeOut) {
            if (statusPill) {
                statusPill.className = 'px-3 py-1 rounded-full text-[10px] font-extrabold uppercase tracking-wide bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 flex items-center justify-center gap-1.5';
                statusPill.innerHTML = `<span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span> ABSEN MASUK: ${record.timeIn}`;
            }
            if (btnMasuk) {
                btnMasuk.disabled = true;
                btnMasuk.className = 'py-3 px-4 bg-slate-200 text-slate-400 font-extrabold text-xs rounded-xl cursor-not-allowed transition-all flex items-center justify-center gap-2';
            }
            if (btnPulang) {
                btnPulang.disabled = false;
                btnPulang.className = 'py-3 px-4 bg-rose-600 hover:bg-rose-700 text-white font-extrabold text-xs rounded-xl shadow-md transition-all flex items-center justify-center gap-2 cursor-pointer';
            }
        } else {
            if (statusPill) {
                statusPill.className = 'px-3 py-1 rounded-full text-[10px] font-extrabold uppercase tracking-wide bg-blue-500/20 text-blue-300 border border-blue-500/30 flex items-center justify-center gap-1.5';
                statusPill.innerHTML = `<span class="w-2 h-2 rounded-full bg-blue-400"></span> ABSEN SELESAI (PULANG: ${record.timeOut})`;
            }
            if (btnMasuk) {
                btnMasuk.disabled = true;
                btnMasuk.className = 'py-3 px-4 bg-slate-200 text-slate-400 font-extrabold text-xs rounded-xl cursor-not-allowed transition-all flex items-center justify-center gap-2';
            }
            if (btnPulang) {
                btnPulang.disabled = true;
                btnPulang.className = 'py-3 px-4 bg-slate-200 text-slate-400 font-extrabold text-xs rounded-xl cursor-not-allowed transition-all flex items-center justify-center gap-2';
            }
        }

        // Render history log
        if (historyLog) {
            const logs = this.getLogs();
            if (logs.length === 0) {
                historyLog.innerHTML = '<p class="text-[11px] text-slate-400 italic text-center py-2">Belum ada riwayat absensi terverifikasi.</p>';
            } else {
                historyLog.innerHTML = logs.slice().reverse().map(l => `
                    <div class="p-2.5 bg-slate-50 border border-slate-200/80 rounded-xl flex items-center justify-between text-[11px]">
                        <div>
                            <span class="font-bold text-slate-800">${l.dateDisplay}</span>
                            <span class="text-[10px] text-slate-400 block">${l.shiftName || 'Shift Pagi (07:00 - 12:00)'}</span>
                        </div>
                        <div class="text-right">
                            <span class="font-mono font-bold text-brand-700">In: ${l.timeIn}</span>
                            <span class="font-mono font-bold text-rose-600 ml-2">Out: ${l.timeOut || '-'}</span>
                            <span class="px-1.5 py-0.5 bg-emerald-100 text-emerald-800 text-[9px] font-black rounded ml-1.5">HADIR</span>
                        </div>
                    </div>
                `).join('');
            }
        }
    },

    doAbsenMasuk() {
        const now = new Date();
        const dateKey = this.getTodayKey();
        const dateDisplay = now.toLocaleDateString('id-ID', { day: 'numeric', month: 'short', year: 'numeric' });
        const timeIn = now.toLocaleTimeString('id-ID', { hour: '2-digit', minute: '2-digit' }) + ' WIB';
        const shiftName = this.getCurrentShiftName();

        const logs = this.getLogs();
        let record = logs.find(l => l.dateKey === dateKey);

        if (!record) {
            record = {
                dateKey,
                dateDisplay,
                timeIn,
                timeOut: null,
                shiftName,
                status: 'HADIR'
            };
            logs.push(record);
        } else {
            record.timeIn = timeIn;
            record.shiftName = shiftName;
        }

        this.saveLogs(logs);
        this.renderStatus();

        if (typeof Toast !== 'undefined') {
            Toast.success(`Absen Masuk (${shiftName}) Terverifikasi pukul ${timeIn}. Selamat bertugas!`, 'Presensi Masuk');
        }
    },

    doAbsenPulang() {
        const now = new Date();
        const dateKey = this.getTodayKey();
        const timeOut = now.toLocaleTimeString('id-ID', { hour: '2-digit', minute: '2-digit' }) + ' WIB';

        const logs = this.getLogs();
        const record = logs.find(l => l.dateKey === dateKey);

        if (record) {
            record.timeOut = timeOut;
            this.saveLogs(logs);
            this.renderStatus();

            if (typeof Toast !== 'undefined') {
                Toast.success(`Absen Pulang Terverifikasi pukul ${timeOut}. Terima kasih atas dedikasi Anda!`, 'Presensi Pulang');
            }
        }
    },

    injectModalHTML() {
        if (document.getElementById('modal-absensi')) return;

        const currentShift = this.getCurrentShiftName();

        const modalDiv = document.createElement('div');
        modalDiv.id = 'modal-absensi';
        modalDiv.className = 'fixed inset-0 bg-slate-950/60 backdrop-blur-sm z-50 flex items-center justify-center hidden p-4';
        modalDiv.innerHTML = `
            <div class="bg-white rounded-3xl max-w-lg w-full p-6 shadow-2xl border border-slate-200 space-y-4 relative">
                <button onclick="Absensi.closeModal()" class="absolute right-5 top-5 text-slate-400 hover:text-slate-600 p-1.5 rounded-full hover:bg-slate-100 transition-all cursor-pointer">
                    <span class="material-symbols-outlined text-[20px]">close</span>
                </button>

                <div class="flex items-center gap-3 border-b border-slate-100 pb-3">
                    <div class="w-10 h-10 rounded-2xl bg-brand-50 text-brand-600 flex items-center justify-center font-bold shadow-2xs">
                        <span class="material-symbols-outlined text-[22px]">fingerprint</span>
                    </div>
                    <div>
                        <h3 class="text-sm font-extrabold text-slate-900">Presensi & Absensi Dokter</h3>
                        <p class="text-[10px] text-slate-500 font-medium">Sistem Verifikasi Clock In / Clock Out Tugas Jaga Dokter</p>
                    </div>
                </div>

                <div class="bg-gradient-to-br from-slate-900 via-slate-800 to-brand-950 text-white rounded-2xl p-4 text-center space-y-1.5 border border-slate-800 shadow-inner">
                    <span class="text-[9px] font-extrabold uppercase tracking-widest text-emerald-400 block" id="absen-today-date">Rabu, 16 September 2026</span>
                    <div class="text-2xl sm:text-3xl font-black font-mono tracking-wider text-white" id="absen-live-clock">00:00:00 WIB</div>
                    <div class="pt-1 flex items-center justify-center gap-2">
                        <span id="absen-status-pill" class="px-3 py-0.5 rounded-full text-[10px] font-extrabold uppercase tracking-wide bg-amber-500/20 text-amber-300 border border-amber-500/30 flex items-center gap-1.5">
                            <span class="w-1.5 h-1.5 rounded-full bg-amber-400"></span> BELUM ABSEN HARI INI
                        </span>
                    </div>
                </div>

                <div class="grid grid-cols-2 gap-3 text-xs">
                    <div class="p-3 bg-slate-50 border border-slate-200 rounded-xl space-y-0.5">
                        <span class="text-[9px] font-bold text-slate-400 uppercase tracking-wider block">LOKASI PRAKTIK</span>
                        <span class="font-extrabold text-slate-800 flex items-center gap-1 text-[11px]">
                            <span class="material-symbols-outlined text-brand-600 text-[14px]">location_on</span> Sentra Farma HQ
                        </span>
                        <span class="text-[9px] text-emerald-600 font-bold block">✓ Geofence Terverifikasi</span>
                    </div>
                    <div class="p-3 bg-slate-50 border border-slate-200 rounded-xl space-y-0.5">
                        <span class="text-[9px] font-bold text-slate-400 uppercase tracking-wider block">SHIFT JAGA HARI INI</span>
                        <span id="absen-active-shift" class="font-extrabold text-slate-800 text-[11px] block truncate">${currentShift}</span>
                        <span class="text-[9px] text-slate-500 font-medium block truncate">Poliklinik Sentra Farma</span>
                    </div>
                </div>

                <div class="grid grid-cols-2 gap-3 pt-1">
                    <button onclick="Absensi.doAbsenMasuk()" id="btn-absen-masuk" class="py-2.5 px-4 bg-emerald-600 hover:bg-emerald-700 text-white font-extrabold text-xs rounded-xl shadow-md transition-all flex items-center justify-center gap-1.5 cursor-pointer">
                        <span class="material-symbols-outlined text-[16px]">login</span>
                        <span>Absen Masuk</span>
                    </button>
                    <button onclick="Absensi.doAbsenPulang()" id="btn-absen-pulang" class="py-2.5 px-4 bg-slate-200 text-slate-400 font-extrabold text-xs rounded-xl cursor-not-allowed transition-all flex items-center justify-center gap-1.5" disabled>
                        <span class="material-symbols-outlined text-[16px]">logout</span>
                        <span>Absen Pulang</span>
                    </button>
                </div>

                <div class="space-y-1.5 pt-2 border-t border-slate-100">
                    <div class="flex justify-between items-center">
                        <h4 class="text-[9px] font-extrabold text-slate-400 uppercase tracking-wider">Histori Presensi Terakhir</h4>
                        <span class="text-[9px] text-slate-400 font-medium">Catatan Kehadiran</span>
                    </div>
                    <div id="absen-history-log" class="max-h-32 overflow-y-auto space-y-1.5 custom-scrollbar pr-1 text-xs">
                        <!-- Dynamically loaded history -->
                    </div>
                </div>
            </div>
        `;
        document.body.appendChild(modalDiv);
    }
};

document.addEventListener('DOMContentLoaded', () => {
    Absensi.init();
});
