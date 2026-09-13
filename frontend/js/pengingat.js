/**
 * Sentra Farma - Fitur Pengingat Minum Obat & Jadwal Konsultasi (Pill Reminder Module)
 * Persistent via localStorage with real-time notification alarm & adherence tracker.
 */

const STORAGE_KEY = 'sentra_farma_pill_reminders';
const TAKEN_LOG_KEY = 'sentra_farma_pill_taken_logs';
let lastTriggeredMinute = '';

const DEFAULT_REMINDERS = [];

// WEB AUDIO API REAL ALARM CHIME SOUND GENERATOR
function playAlarmChimeSound() {
    try {
        const AudioCtx = window.AudioContext || window.webkitAudioContext;
        if (!AudioCtx) return;
        const ctx = new AudioCtx();
        
        const now = ctx.currentTime;
        // 3 pleasant harmonic chime notes: C5 (523.25Hz), E5 (659.25Hz), G5 (783.99Hz)
        const notes = [523.25, 659.25, 783.99, 1046.50]; 
        notes.forEach((freq, idx) => {
            const osc = ctx.createOscillator();
            const gain = ctx.createGain();
            osc.type = 'sine';
            osc.frequency.setValueAtTime(freq, now + idx * 0.12);
            
            gain.gain.setValueAtTime(0, now + idx * 0.12);
            gain.gain.linearRampToValueAtTime(0.3, now + idx * 0.12 + 0.03);
            gain.gain.exponentialRampToValueAtTime(0.001, now + idx * 0.12 + 0.35);
            
            osc.connect(gain);
            gain.connect(ctx.destination);
            
            osc.start(now + idx * 0.12);
            osc.stop(now + idx * 0.12 + 0.4);
        });
    } catch (e) {
        console.warn('Audio chime notice:', e);
    }
}

// Normalize user input time (e.g., "2" -> ["02:00", "14:00"], "2.00" -> ["02:00"], "14:00" -> ["14:00"])
function normalizeTimeInput(raw) {
    const tokens = raw.split(',').map(s => s.trim()).filter(Boolean);
    let result = [];

    tokens.forEach(t => {
        let clean = t.toLowerCase().replace('.', ':');
        if (!clean.includes(':')) {
            let num = parseInt(clean);
            if (!isNaN(num)) {
                if (num >= 1 && num <= 12) {
                    let am = String(num).padStart(2, '0') + ':00';
                    let pm = String(num + 12 === 24 ? 12 : num + 12).padStart(2, '0') + ':00';
                    result.push(am, pm);
                } else if (num >= 0 && num <= 23) {
                    result.push(String(num).padStart(2, '0') + ':00');
                }
            } else {
                result.push(clean);
            }
        } else {
            let parts = clean.split(':');
            let h = parseInt(parts[0]);
            let m = parseInt(parts[1]) || 0;
            if (!isNaN(h)) {
                result.push(`${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`);
            } else {
                result.push(clean);
            }
        }
    });

    return Array.from(new Set(result));
}

// Initialize default data if empty
function initPillStorage() {
    if (!localStorage.getItem(STORAGE_KEY)) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(DEFAULT_REMINDERS));
    }
}

function getPillReminders() {
    initPillStorage();
    try {
        return JSON.parse(localStorage.getItem(STORAGE_KEY)) || DEFAULT_REMINDERS;
    } catch (e) {
        return DEFAULT_REMINDERS;
    }
}

function getTodayTakenLogs() {
    const today = new Date().toISOString().split('T')[0];
    try {
        const logs = JSON.parse(localStorage.getItem(TAKEN_LOG_KEY)) || {};
        return logs[today] || [];
    } catch (e) {
        return [];
    }
}

function togglePillTaken(reminderId, timeSlot) {
    const today = new Date().toISOString().split('T')[0];
    let logs = {};
    try {
        logs = JSON.parse(localStorage.getItem(TAKEN_LOG_KEY)) || {};
    } catch (e) { logs = {}; }

    if (!logs[today]) logs[today] = [];

    const key = `${reminderId}_${timeSlot}`;
    const idx = logs[today].indexOf(key);
    if (idx > -1) {
        logs[today].splice(idx, 1);
    } else {
        logs[today].push(key);
        if (typeof Toast !== 'undefined') {
            Toast.success(`Berhasil mencatat minum obat jam ${timeSlot}! Tetap sehat! 💊`, 'Tercatat!');
        }
    }

    localStorage.setItem(TAKEN_LOG_KEY, JSON.stringify(logs));
    renderPillWidgets();
    renderPillListModalContent();
}

function savePillReminder(reminderData) {
    const reminders = getPillReminders();
    if (reminderData.id) {
        const index = reminders.findIndex(r => r.id === reminderData.id);
        if (index > -1) reminders[index] = { ...reminders[index], ...reminderData };
    } else {
        reminderData.id = 'rem-' + Date.now();
        reminderData.aktif = true;
        reminders.push(reminderData);
    }
    localStorage.setItem(STORAGE_KEY, JSON.stringify(reminders));
    if (typeof Toast !== 'undefined') {
        Toast.success('Jadwal pengingat obat berhasil disimpan!', 'Berhasil');
    }
    renderPillWidgets();
    renderPillListModalContent();
}

function deletePillReminder(id) {
    let reminders = getPillReminders();
    reminders = reminders.filter(r => r.id !== id);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(reminders));
    if (typeof Toast !== 'undefined') {
        Toast.info('Pengingat obat telah dihapus.');
    }
    renderPillWidgets();
    renderPillListModalContent();
}

function toggleReminderStatus(id) {
    let reminders = getPillReminders();
    const item = reminders.find(r => r.id === id);
    if (item) {
        item.aktif = !item.aktif;
        localStorage.setItem(STORAGE_KEY, JSON.stringify(reminders));
        renderPillWidgets();
        renderPillListModalContent();
        if (typeof Toast !== 'undefined') {
            Toast.info(item.aktif ? `Pengingat ${item.namaObat} diaktifkan.` : `Pengingat ${item.namaObat} dinonaktifkan.`);
        }
    }
}

// Render Dashboard Pills Widget
function renderPillWidgets() {
    const container = document.getElementById('pill-reminder-container');
    if (!container) return;

    const reminders = getPillReminders();
    const takenLogs = getTodayTakenLogs();

    let totalSlots = 0;
    let takenSlots = 0;
    let itemsHTML = '';

    reminders.forEach(rem => {
        if (!rem.aktif) return;

        rem.waktu.forEach(time => {
            totalSlots++;
            const isTaken = takenLogs.includes(`${rem.id}_${time}`);
            if (isTaken) takenSlots++;

            itemsHTML += `
                <div class="p-3.5 rounded-2xl border ${isTaken ? 'bg-emerald-50/60 border-emerald-200' : 'bg-white border-slate-200/80'} shadow-2xs hover:shadow-md transition-all flex items-center justify-between gap-3 group">
                    <div class="flex items-center gap-3 min-w-0">
                        <div class="w-10 h-10 rounded-xl ${isTaken ? 'bg-emerald-500 text-white' : 'bg-brand-50 text-brand-600'} flex items-center justify-center font-bold shrink-0 transition-colors">
                            <span class="material-symbols-outlined text-[20px]">${isTaken ? 'check_circle' : 'pill'}</span>
                        </div>
                        <div class="min-w-0">
                            <div class="flex items-center gap-2 flex-wrap">
                                <span class="px-2 py-0.5 ${isTaken ? 'bg-emerald-100 text-emerald-800' : 'bg-brand-100 text-brand-800'} text-[10px] font-black rounded-md uppercase font-mono">${time} WIB</span>
                                <span class="text-[10px] text-slate-400 font-medium truncate">${rem.kategori || 'Obat'}</span>
                            </div>
                            <h4 class="text-xs font-bold text-slate-900 truncate mt-0.5 ${isTaken ? 'line-through text-slate-400' : ''}">${rem.namaObat}</h4>
                            <p class="text-[10px] text-slate-500 font-medium truncate">${rem.dosis} • ${rem.catatan || 'Tanpa catatan'}</p>
                        </div>
                    </div>

                    <div class="flex items-center gap-2 shrink-0">
                        <button onclick="togglePillTaken('${rem.id}', '${time}')"
                            class="px-3 py-1.5 rounded-xl font-extrabold text-[11px] transition-all flex items-center gap-1 shadow-2xs ${isTaken ? 'bg-emerald-600 hover:bg-emerald-700 text-white' : 'bg-brand-50 hover:bg-brand-100 text-brand-700 border border-brand-200'}">
                            <span class="material-symbols-outlined text-[15px]">${isTaken ? 'done_all' : 'task_alt'}</span>
                            <span>${isTaken ? 'Sudah Minum' : 'Minum Sekarang'}</span>
                        </button>
                        <button onclick="deletePillReminder('${rem.id}')" title="Hapus Pengingat" class="p-1.5 text-slate-300 hover:text-rose-500 hover:bg-rose-50 rounded-lg transition-colors opacity-0 group-hover:opacity-100">
                            <span class="material-symbols-outlined text-[16px]">delete</span>
                        </button>
                    </div>
                </div>
            `;
        });
    });

    const percent = totalSlots > 0 ? Math.round((takenSlots / totalSlots) * 100) : 0;

    const progressText = document.getElementById('pill-adherence-pct');
    if (progressText) progressText.innerText = `${percent}%`;

    const progressBar = document.getElementById('pill-adherence-bar');
    if (progressBar) progressBar.style.width = `${percent}%`;

    const summaryText = document.getElementById('pill-summary-count');
    if (summaryText) summaryText.innerText = `${takenSlots} dari ${totalSlots} Dosis Diminum Hari Ini`;

    if (totalSlots === 0) {
        itemsHTML = `
            <div class="p-6 text-center text-slate-400 space-y-2 col-span-full">
                <span class="material-symbols-outlined text-4xl text-slate-300">medication</span>
                <p class="text-xs font-medium">Belum ada pengingat obat diset. Klik "+ Tambah Pengingat" untuk membuat jadwal baru!</p>
            </div>
        `;
    }

    container.innerHTML = itemsHTML;
}

// -------------------------------------------------------------
// DEDICATED MODAL: VIEW & MANAGE ALL REMINDERS (Daftar Pengingat Obat)
// -------------------------------------------------------------
function openPillListModal() {
    let modal = document.getElementById('pill-list-modal-dialog');
    if (!modal) {
        createPillListModalDOM();
        modal = document.getElementById('pill-list-modal-dialog');
    }
    renderPillListModalContent();
    modal.classList.remove('hidden');
    modal.classList.add('flex');
}

function closePillListModal() {
    const modal = document.getElementById('pill-list-modal-dialog');
    if (modal) {
        modal.classList.add('hidden');
        modal.classList.remove('flex');
    }
}

function createPillListModalDOM() {
    const modalHTML = `
    <div id="pill-list-modal-dialog" class="fixed inset-0 z-50 bg-slate-900/60 backdrop-blur-sm hidden items-center justify-center p-4">
        <div class="bg-white w-full max-w-2xl rounded-3xl shadow-2xl border border-slate-100 p-6 space-y-5 animate-in fade-in zoom-in duration-200">
            <!-- Header -->
            <div class="flex items-center justify-between border-b border-slate-100 pb-4">
                <div class="flex items-center gap-3">
                    <div class="w-11 h-11 rounded-2xl bg-gradient-to-tr from-brand-700 to-emerald-500 text-white flex items-center justify-center font-bold shadow-md">
                        <span class="material-symbols-outlined text-[24px]">notifications_active</span>
                    </div>
                    <div>
                        <h3 class="text-base font-extrabold text-slate-900 flex items-center gap-2">
                            <span>Daftar Pengingat Minum Obat Saya</span>
                            <span id="pill-modal-count-badge" class="px-2 py-0.5 bg-brand-100 text-brand-800 text-[10px] font-black rounded-full font-mono">0 Pengingat</span>
                        </h3>
                        <p class="text-xs text-slate-500 font-medium">Jadwal konsumsi obat harian & alarm otomatis pasien Sentra Farma</p>
                    </div>
                </div>
                <div class="flex items-center gap-2">
                    <button onclick="openPillModal()" class="px-3 py-1.5 bg-brand-600 hover:bg-brand-700 text-white text-xs font-extrabold rounded-xl shadow-xs transition-all flex items-center gap-1">
                        <span class="material-symbols-outlined text-[16px]">add_alarm</span>
                        <span>+ Tambah Baru</span>
                    </button>
                    <button onclick="closePillListModal()" class="w-8 h-8 rounded-full hover:bg-slate-100 text-slate-400 hover:text-slate-700 flex items-center justify-center transition-colors">
                        <span class="material-symbols-outlined text-[20px]">close</span>
                    </button>
                </div>
            </div>

            <!-- Test Alarm & Quick Tips Bar -->
            <div class="p-3.5 bg-emerald-50/80 border border-emerald-200/80 rounded-2xl flex items-center justify-between gap-3 text-xs">
                <div class="flex items-center gap-2.5 text-emerald-950 font-semibold">
                    <div class="w-7 h-7 rounded-xl bg-emerald-600 text-white flex items-center justify-center font-bold shrink-0">
                        <span class="material-symbols-outlined text-[16px]">volume_up</span>
                    </div>
                    <span>Klik <strong>Tes Alarm</strong> untuk uji coba suara bel & tampilan tunggal alarm obat.</span>
                </div>
                <button onclick="triggerTestAlarm('02:00')" class="px-3.5 py-1.5 bg-emerald-600 hover:bg-emerald-700 text-white font-extrabold text-[11px] rounded-xl transition-all flex items-center gap-1 shrink-0 shadow-sm">
                    <span class="material-symbols-outlined text-[14px]">notifications_active</span>
                    <span>Tes Alarm (02:00)</span>
                </button>
            </div>

            <!-- List Container -->
            <div id="pill-modal-items-list" class="space-y-3 max-h-[380px] overflow-y-auto pr-1 custom-scrollbar">
                <!-- Rendered dynamically -->
            </div>

            <div class="pt-3 border-t border-slate-100 flex items-center justify-between">
                <span class="text-[11px] text-slate-400 font-medium">Pengingat aktif akan otomatis memicu Alarm Suara di jam yang ditentukan.</span>
                <button onclick="closePillListModal()" class="px-5 py-2 bg-slate-900 hover:bg-slate-800 text-white font-extrabold text-xs rounded-xl shadow-xs transition-colors">
                    Tutup
                </button>
            </div>
        </div>
    </div>
    `;
    document.body.insertAdjacentHTML('beforeend', modalHTML);
}

function renderPillListModalContent() {
    const container = document.getElementById('pill-modal-items-list');
    const badgeCount = document.getElementById('pill-modal-count-badge');
    if (!container) return;

    const reminders = getPillReminders();
    const takenLogs = getTodayTakenLogs();

    if (badgeCount) badgeCount.innerText = `${reminders.length} Pengingat`;

    if (reminders.length === 0) {
        container.innerHTML = `
            <div class="p-8 text-center text-slate-400 space-y-2">
                <span class="material-symbols-outlined text-4xl text-slate-300">alarm_off</span>
                <h4 class="text-xs font-bold text-slate-700">Belum Ada Pengingat Obat</h4>
                <p class="text-xs text-slate-500 font-medium">Klik "+ Tambah Baru" untuk membuat jadwal pengingat jam minum obat Anda.</p>
            </div>
        `;
        return;
    }

    let listHTML = '';
    reminders.forEach(rem => {
        const timeBadges = rem.waktu.map(t => {
            const isTaken = takenLogs.includes(`${rem.id}_${t}`);
            return `
                <button onclick="togglePillTaken('${rem.id}', '${t}')" title="Klik untuk tandai sudah minum"
                    class="px-2.5 py-1 rounded-lg text-[10px] font-black font-mono flex items-center gap-1 transition-all ${isTaken ? 'bg-emerald-600 text-white shadow-2xs' : 'bg-slate-100 hover:bg-brand-50 text-slate-800 border border-slate-200'}">
                    <span class="material-symbols-outlined text-[13px]">${isTaken ? 'check_circle' : 'schedule'}</span>
                    <span>${t} WIB</span>
                    ${isTaken ? '<span class="text-[9px] font-semibold">(Sudah)</span>' : ''}
                </button>
            `;
        }).join('');

        listHTML += `
            <div class="p-4 rounded-2xl border ${rem.aktif ? 'bg-white border-slate-200/90 shadow-2xs' : 'bg-slate-50/80 border-slate-200/60 opacity-60'} hover:border-brand-300 transition-all flex flex-col sm:flex-row sm:items-center justify-between gap-4 group">
                <div class="space-y-2 min-w-0 flex-1">
                    <div class="flex items-center gap-2 flex-wrap">
                        <span class="px-2 py-0.5 bg-brand-100 text-brand-800 text-[10px] font-bold rounded-md uppercase">${rem.kategori || 'Obat'}</span>
                        <h4 class="text-sm font-extrabold text-slate-900 tracking-tight">${rem.namaObat}</h4>
                    </div>
                    <p class="text-xs text-slate-600 font-medium">${rem.dosis} ${rem.catatan ? '• <span class="italic text-slate-400">' + rem.catatan + '</span>' : ''}</p>
                    
                    <!-- Times list -->
                    <div class="flex items-center gap-2 flex-wrap pt-1">
                        <span class="text-[10px] text-slate-400 font-bold uppercase tracking-wider">Jadwal Jam:</span>
                        ${timeBadges}
                    </div>
                </div>

                <!-- Controls -->
                <div class="flex items-center gap-2 shrink-0 border-t sm:border-t-0 pt-2 sm:pt-0 border-slate-100">
                    <button onclick="toggleReminderStatus('${rem.id}')" title="${rem.aktif ? 'Matikan Pengingat' : 'Aktifkan Pengingat'}"
                        class="px-3 py-1.5 rounded-xl font-bold text-xs transition-colors flex items-center gap-1 ${rem.aktif ? 'bg-emerald-100 text-emerald-800 hover:bg-emerald-200' : 'bg-slate-200 text-slate-600 hover:bg-slate-300'}">
                        <span class="material-symbols-outlined text-[16px]">${rem.aktif ? 'toggle_on' : 'toggle_off'}</span>
                        <span>${rem.aktif ? 'Aktif' : 'Nonaktif'}</span>
                    </button>
                    <button onclick="deletePillReminder('${rem.id}')" title="Hapus Pengingat" 
                        class="p-2 rounded-xl text-rose-400 hover:text-rose-600 hover:bg-rose-50 transition-colors">
                        <span class="material-symbols-outlined text-[18px]">delete</span>
                    </button>
                </div>
            </div>
        `;
    });

    container.innerHTML = listHTML;
}

// SINGLE UNIFIED ALARM BANNER (No duplicate Toast calls)
function triggerTestAlarm(timeLabel = '02:00') {
    const reminders = getPillReminders();
    const rem = reminders[0] || { namaObat: 'Obat Pasien', dosis: 'Sesuai Dosis Resep' };

    // Play real Web Audio API chime sound
    playAlarmChimeSound();

    // Show single unified Sentra Farma alarm popup banner
    showAlarmPopupBanner(rem, timeLabel);
}

function showAlarmPopupBanner(rem, timeStr) {
    let alertBanner = document.getElementById('pill-alarm-popup-banner');
    
    // Play chime sound
    playAlarmChimeSound();

    if (!alertBanner) {
        const html = `
        <div id="pill-alarm-popup-banner" class="fixed top-6 right-6 z-50 max-w-md w-full bg-gradient-to-br from-brand-900 via-brand-800 to-emerald-900 text-white p-5 rounded-3xl shadow-2xl border border-emerald-500/50 space-y-3.5 animate-in slide-in-from-top-5 duration-300">
            <div class="flex items-start justify-between gap-3">
                <div class="flex items-center gap-3">
                    <div class="w-11 h-11 rounded-2xl bg-gradient-to-tr from-emerald-400 to-teal-300 text-slate-900 flex items-center justify-center font-black text-xl shadow-lg animate-bounce shrink-0">
                        <span class="material-symbols-outlined text-[24px]">notifications_active</span>
                    </div>
                    <div>
                        <div class="flex items-center gap-2">
                            <span class="px-2.5 py-0.5 bg-white/20 text-emerald-200 text-[10px] font-black rounded-md uppercase font-mono tracking-wider">JAM ${timeStr} WIB</span>
                            <span class="w-2 h-2 rounded-full bg-emerald-400 animate-ping"></span>
                        </div>
                        <h3 class="text-sm font-extrabold text-white mt-1 tracking-tight">WAKTU MINUM OBAT PASIEN</h3>
                    </div>
                </div>
                <button onclick="document.getElementById('pill-alarm-popup-banner').remove()" class="text-white/60 hover:text-white transition-colors">
                    <span class="material-symbols-outlined text-[20px]">close</span>
                </button>
            </div>

            <div class="bg-white/10 backdrop-blur-md p-3.5 rounded-2xl border border-white/15 space-y-1">
                <div class="flex items-center justify-between text-[10px] text-emerald-300 font-bold uppercase tracking-wider">
                    <span>${rem.kategori || 'Obat Resep'}</span>
                    <span>Sentra Farma Care</span>
                </div>
                <h4 id="alarm-banner-title" class="text-sm font-extrabold text-white">${rem.namaObat}</h4>
                <p id="alarm-banner-dosis" class="text-xs text-brand-100 font-medium">${rem.dosis} ${rem.catatan ? '• ' + rem.catatan : ''}</p>
            </div>

            <div class="flex items-center gap-2 pt-1">
                <button onclick="togglePillTaken('${rem.id || 'rem-1'}', '${timeStr}'); document.getElementById('pill-alarm-popup-banner').remove();"
                    class="flex-1 py-2.5 bg-emerald-400 hover:bg-emerald-300 text-slate-950 font-black text-xs rounded-xl shadow-lg transition-all flex items-center justify-center gap-1.5 cursor-pointer">
                    <span class="material-symbols-outlined text-[17px]">check_circle</span>
                    <span>Tandai Sudah Minum</span>
                </button>
                <button onclick="document.getElementById('pill-alarm-popup-banner').remove()"
                    class="px-4 py-2.5 bg-white/10 hover:bg-white/20 text-white font-bold text-xs rounded-xl transition-all">
                    Nanti
                </button>
            </div>
        </div>
        `;
        document.body.insertAdjacentHTML('beforeend', html);
    } else {
        document.getElementById('alarm-banner-title').innerText = rem.namaObat;
        document.getElementById('alarm-banner-dosis').innerText = `${rem.dosis} (${timeStr} WIB)`;
    }
}

// Modal handling for Adding New Reminder
function openPillModal() {
    let modal = document.getElementById('pill-modal-dialog');
    if (!modal) {
        createPillModalDOM();
        modal = document.getElementById('pill-modal-dialog');
    }
    modal.classList.remove('hidden');
    modal.classList.add('flex');
}

function closePillModal() {
    const modal = document.getElementById('pill-modal-dialog');
    if (modal) {
        modal.classList.add('hidden');
        modal.classList.remove('flex');
    }
}

function createPillModalDOM() {
    const modalHTML = `
    <div id="pill-modal-dialog" class="fixed inset-0 z-50 bg-slate-900/60 backdrop-blur-sm hidden items-center justify-center p-4">
        <div class="bg-white w-full max-w-lg rounded-3xl shadow-2xl border border-slate-100 p-6 space-y-5 animate-in fade-in zoom-in duration-200">
            <div class="flex items-center justify-between border-b border-slate-100 pb-4">
                <div class="flex items-center gap-3">
                    <div class="w-10 h-10 rounded-2xl bg-brand-50 text-brand-600 flex items-center justify-center font-bold shadow-inner">
                        <span class="material-symbols-outlined text-[22px]">alarm</span>
                    </div>
                    <div>
                        <h3 class="text-base font-extrabold text-slate-900">Tambah Pengingat Minum Obat</h3>
                        <p class="text-xs text-slate-500 font-medium">Atur jam & frekuensi minum obat harian Anda</p>
                    </div>
                </div>
                <button onclick="closePillModal()" class="w-8 h-8 rounded-full hover:bg-slate-100 text-slate-400 hover:text-slate-700 flex items-center justify-center transition-colors">
                    <span class="material-symbols-outlined text-[20px]">close</span>
                </button>
            </div>

            <form id="pill-form" onsubmit="handlePillSubmit(event)" class="space-y-4">
                <div>
                    <label class="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">Nama Obat / Suplemen</label>
                    <input type="text" id="pill-name" required placeholder="Contoh: Paracetamol, Vitamin D3, Amoxicillin" 
                        class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-brand-500 text-xs font-semibold text-slate-800" />
                </div>

                <div class="grid grid-cols-2 gap-3">
                    <div>
                        <label class="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">Dosis & Aturan</label>
                        <input type="text" id="pill-dosis" required placeholder="Contoh: 1 Tablet (Sesudah Makan)" 
                            class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-brand-500 text-xs font-semibold text-slate-800" />
                    </div>
                    <div>
                        <label class="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">Kategori Obat</label>
                        <select id="pill-kategori" class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-brand-500 text-xs font-semibold text-slate-800">
                            <option value="Obat Resep Dokter">Obat Resep Dokter</option>
                            <option value="Obat Bebas">Obat Bebas</option>
                            <option value="Suplemen Imunitas">Suplemen Imunitas</option>
                            <option value="Vitamin & Mineral">Vitamin & Mineral</option>
                        </select>
                    </div>
                </div>

                <div>
                    <label class="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">Waktu Minum Obat (Misal: 2, 02:00, 14:00, 20:00)</label>
                    <input type="text" id="pill-waktu" required placeholder="Contoh: 2 atau 02:00, 14:00, 20:00" 
                        class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-brand-500 text-xs font-semibold font-mono text-slate-800" />
                    <p class="text-[10px] text-slate-400 mt-1 font-medium">Bisa ketik jam 2 (otomatis set 02:00 & 14:00) atau format HH:MM lain dipisah koma.</p>
                </div>

                <div>
                    <label class="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1.5">Catatan Tambahan (Opsional)</label>
                    <input type="text" id="pill-catatan" placeholder="Contoh: Minum dengan air putih hangat setelah makan" 
                        class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-brand-500 text-xs font-semibold text-slate-800" />
                </div>

                <div class="pt-3 border-t border-slate-100 flex items-center justify-end gap-2">
                    <button type="button" onclick="closePillModal()" class="px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold text-xs rounded-xl transition-colors">
                        Batal
                    </button>
                    <button type="submit" class="px-5 py-2 bg-gradient-to-r from-brand-700 to-emerald-600 hover:from-brand-800 hover:to-emerald-700 text-white font-extrabold text-xs rounded-xl shadow-md transition-all flex items-center gap-1.5">
                        <span class="material-symbols-outlined text-[16px]">save</span>
                        <span>Simpan Pengingat</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
    `;
    document.body.insertAdjacentHTML('beforeend', modalHTML);
}

function handlePillSubmit(e) {
    e.preventDefault();
    const namaObat = document.getElementById('pill-name').value.trim();
    const dosis = document.getElementById('pill-dosis').value.trim();
    const kategori = document.getElementById('pill-kategori').value;
    const waktuRaw = document.getElementById('pill-waktu').value.trim();
    const catatan = document.getElementById('pill-catatan').value.trim();

    const waktuArr = normalizeTimeInput(waktuRaw);

    savePillReminder({
        namaObat,
        dosis,
        kategori,
        waktu: waktuArr,
        catatan
    });

    closePillModal();
    document.getElementById('pill-form').reset();
}

// Background alarm checker loop
function startPillAlarmMonitor() {
    setInterval(() => {
        const now = new Date();
        const currentH = String(now.getHours()).padStart(2, '0');
        const currentM = String(now.getMinutes()).padStart(2, '0');
        const currentTimeStr = `${currentH}:${currentM}`;

        if (lastTriggeredMinute !== currentTimeStr) {
            const reminders = getPillReminders();
            const takenLogs = getTodayTakenLogs();

            reminders.forEach(rem => {
                if (!rem.aktif) return;
                rem.waktu.forEach(time => {
                    if (time === currentTimeStr) {
                        const isTaken = takenLogs.includes(`${rem.id}_${time}`);
                        if (!isTaken) {
                            lastTriggeredMinute = currentTimeStr;
                            showAlarmPopupBanner(rem, currentTimeStr);
                        }
                    }
                });
            });
        }
    }, 2000);
}

document.addEventListener('DOMContentLoaded', () => {
    initPillStorage();
    renderPillWidgets();
    startPillAlarmMonitor();
});
