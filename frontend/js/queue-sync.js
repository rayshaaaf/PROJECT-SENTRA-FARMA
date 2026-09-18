/**
 * Sentra Farma - Centralized Real-time Queue Sync Engine
 * Handles consistent queue numbers (U-01, U-02, A-01, G-01, K-01), loudspeaker voice calls,
 * and live synchronization across Monitor TV, User Dashboard Sidebar, and Resepsionis.
 */

const QueueSync = (function () {
    const STORAGE_LIST_KEY = 'sf_shared_antrian_list';
    const STORAGE_ACTIVE_KEY = 'sf_active_calling_queue';
    const STORAGE_DATE_KEY = 'sf_queue_last_date';
    const STORAGE_RESET_TIME_KEY = 'sf_queue_reset_time';

    function filterValidQueues(list) {
        if (!Array.isArray(list)) return [];
        const resetTimeStr = localStorage.getItem(STORAGE_RESET_TIME_KEY);
        const todayMidnight = new Date();
        todayMidnight.setHours(0, 0, 0, 0);
        const resetTime = resetTimeStr ? parseInt(resetTimeStr, 10) : todayMidnight.getTime();

        return list.filter(q => {
            if (!q) return false;

            // 1. Any item created locally has q.id = Date.now() (13 digits >= 100000000000)
            if (typeof q.id === 'number' && q.id >= 100000000000) {
                return q.id >= resetTime;
            }

            // 2. Items with string timestamp IDs
            if (typeof q.id === 'string' && /^\d{13}$/.test(q.id)) {
                return parseInt(q.id, 10) >= resetTime;
            }

            // 3. Database items with createdAt
            if (q.createdAt) {
                const createdMs = new Date(q.createdAt).getTime();
                if (!isNaN(createdMs)) return createdMs >= resetTime;
            }

            // 4. Database / offline items created in active session without timestamp ID or createdAt
            return true;
        });
    }

    function init() {
        const todayStr = new Date().toISOString().split('T')[0];
        const lastDate = localStorage.getItem(STORAGE_DATE_KEY);

        if (!localStorage.getItem(STORAGE_RESET_TIME_KEY)) {
            const todayMidnight = new Date();
            todayMidnight.setHours(0, 0, 0, 0);
            localStorage.setItem(STORAGE_RESET_TIME_KEY, todayMidnight.getTime().toString());
        }

        if (!lastDate || lastDate !== todayStr) {
            // Automatic reset on new morning / new day!
            resetAllQueues();
            localStorage.setItem(STORAGE_DATE_KEY, todayStr);
        } else if (!localStorage.getItem(STORAGE_LIST_KEY)) {
            localStorage.setItem(STORAGE_LIST_KEY, JSON.stringify([]));
            localStorage.removeItem(STORAGE_ACTIVE_KEY);
        }
    }

    init();

    function resetAllQueues() {
        const nowMs = Date.now();
        localStorage.setItem(STORAGE_RESET_TIME_KEY, nowMs.toString());
        localStorage.setItem(STORAGE_LIST_KEY, JSON.stringify([]));
        localStorage.removeItem(STORAGE_ACTIVE_KEY);
        localStorage.removeItem('sf_patient_tickets');

        if (typeof API !== 'undefined' && API.post) {
            API.post('/clinic/antrian/reset', {}).catch(() => { });
        }

        window.dispatchEvent(new Event('storage'));
        window.dispatchEvent(new CustomEvent('sf_queue_updated'));
        if (typeof Toast !== 'undefined') {
            Toast.success('Seluruh nomor antrian hari ini berhasil di-reset ke 0!', 'Reset Antrian Sukses');
        }
    }

    function getPoliPrefix(poliName) {
        const p = (poliName || '').toLowerCase();
        if (p.includes('gigi')) return 'G';
        if (p.includes('anak') || p.includes('pediatri')) return 'A';
        if (p.includes('kia') || p.includes('kebidanan')) return 'K';
        if (p.includes('ugd') || p.includes('darurat') || p.includes('emergency')) return 'E';
        return 'U'; // Default Poli Umum
    }

    function generateNextQueueNumber(poliName) {
        const list = getAllQueues();
        const prefix = getPoliPrefix(poliName);
        const matching = list.filter(q => (q.nomorAntrian || '').startsWith(`${prefix}-`));
        const nextSeq = matching.length + 1;
        return `${prefix}-${String(nextSeq).padStart(2, '0')}`;
    }

    function getAllQueues() {
        try {
            const raw = localStorage.getItem(STORAGE_LIST_KEY);
            const list = raw ? JSON.parse(raw) : [];
            return filterValidQueues(list);
        } catch (e) {
            return [];
        }
    }

    function saveAllQueues(list) {
        localStorage.setItem(STORAGE_LIST_KEY, JSON.stringify(list));
        window.dispatchEvent(new Event('storage'));
        window.dispatchEvent(new CustomEvent('sf_queue_updated'));
    }

    function getActiveCalling() {
        try {
            const raw = localStorage.getItem(STORAGE_ACTIVE_KEY);
            if (raw) return JSON.parse(raw);
        } catch (e) { }

        const list = getAllQueues();
        return list.find(q => q.status === 'DIPANGGIL') || null;
    }

    function setActiveCalling(queueItem) {
        localStorage.setItem(STORAGE_ACTIVE_KEY, JSON.stringify(queueItem));
        window.dispatchEvent(new Event('storage'));
        window.dispatchEvent(new CustomEvent('sf_queue_updated'));
    }

    function speakLoudspeaker(nomorAntrian, poliNama, pasienNama) {
        const textToSpeak = `Nomor antrian ${nomorAntrian}, ${pasienNama || 'pasien'}, silakan menuju ${poliNama || 'Poli Klinik'}.`;
        if ('speechSynthesis' in window) {
            window.speechSynthesis.cancel(); // Stop current speech if any
            const speech = new SpeechSynthesisUtterance(textToSpeak);
            speech.lang = 'id-ID';
            speech.rate = 0.88;
            speech.pitch = 1.0;
            window.speechSynthesis.speak(speech);
        }
        return textToSpeak;
    }

    function callQueue(idOrNumber, fallbackItem = null) {
        const list = getAllQueues();
        let item = list.find(q => q.id == idOrNumber || String(q.id) === String(idOrNumber) || q.nomorAntrian === idOrNumber || String(q.nomorAntrian) === String(idOrNumber));

        if (!item && fallbackItem) {
            item = { ...fallbackItem };
            list.push(item);
        }

        if (!item) return null;

        item.status = 'DIPANGGIL';
        saveAllQueues(list);
        setActiveCalling(item);

        // Also sync status into sf_patient_tickets
        try {
            const rawPts = localStorage.getItem('sf_patient_tickets');
            if (rawPts) {
                const pts = JSON.parse(rawPts);
                if (Array.isArray(pts)) {
                    pts.forEach(p => {
                        if (p.nomorAntrian === item.nomorAntrian || p.id == item.id || String(p.id) === String(item.id)) {
                            p.status = 'DIPANGGIL';
                        }
                    });
                    localStorage.setItem('sf_patient_tickets', JSON.stringify(pts));
                }
            }
        } catch(e) {}

        const textSpoken = speakLoudspeaker(item.nomorAntrian, item.poliklinik?.namaPoli || item.namaPoli, item.pasien?.namaLengkap || item.namaPasien);

        if (typeof Toast !== 'undefined') {
            Toast.info(`Memanggil Panggilan Voice: "${item.nomorAntrian} - ${item.pasien?.namaLengkap || item.namaPasien}"`, 'Pengeras Suara Loudspeaker');
        }

        return item;
    }

    function callNext() {
        const list = getAllQueues();
        const waiting = list.find(q => q.status === 'MENUNGGU');

        if (!waiting) {
            if (typeof Toast !== 'undefined') {
                Toast.info('Tidak ada antrian pasien status MENUNGGU lagi saat ini.', 'Antrian Habis');
            }
            return null;
        }

        return callQueue(waiting.id);
    }

    function callRepeat() {
        const active = getActiveCalling();
        if (!active) {
            if (typeof Toast !== 'undefined') {
                Toast.warning('Belum ada antrian yang dipanggil saat ini.', 'Panggilan Ulang Gagal');
            }
            return null;
        }

        const textSpoken = speakLoudspeaker(active.nomorAntrian, active.poliklinik?.namaPoli || active.namaPoli, active.pasien?.namaLengkap || active.namaPasien);

        if (typeof Toast !== 'undefined') {
            Toast.info(`Panggilan Ulang Voice: "${active.nomorAntrian} - ${active.pasien?.namaLengkap || active.namaPasien}"`, 'Pengeras Suara');
        }

        return active;
    }

    function completeActive() {
        const active = getActiveCalling();
        if (!active) return null;

        const list = getAllQueues();
        const item = list.find(q => q.id == active.id || q.nomorAntrian === active.nomorAntrian);
        if (item) {
            item.status = 'SELESAI';
            saveAllQueues(list);
        }

        localStorage.removeItem(STORAGE_ACTIVE_KEY);
        window.dispatchEvent(new Event('storage'));
        window.dispatchEvent(new CustomEvent('sf_queue_updated'));

        if (typeof Toast !== 'undefined') {
            Toast.success(`Antrian ${active.nomorAntrian} telah diselesaikan.`, 'Antrian Selesai');
        }

        return active;
    }

    function addQueue(data) {
        const list = getAllQueues();
        const poliName = data.namaPoli || data.poliklinik?.namaPoli || 'Poli Umum';
        const numAntrian = data.nomorAntrian || generateNextQueueNumber(poliName);

        const newQueue = {
            id: Date.now(),
            nomorAntrian: numAntrian,
            pasien: data.pasien || { namaLengkap: data.namaPasien || 'Pasien Walk-in' },
            namaPasien: data.namaPasien || data.pasien?.namaLengkap || 'Pasien Walk-in',
            poliklinik: data.poliklinik || { namaPoli: poliName },
            namaPoli: poliName,
            dokter: data.dokter || { namaDokter: data.namaDokter || 'Dokter Jaga' },
            namaDokter: data.namaDokter || data.dokter?.namaDokter || 'Dokter Jaga',
            tipe: data.tipe || 'ONLINE',
            status: 'MENUNGGU',
            tanggal: data.tanggal || new Date().toISOString().split('T')[0]
        };

        list.push(newQueue);
        saveAllQueues(list);
        return newQueue;
    }

    return {
        getAllQueues,
        getActiveCalling,
        generateNextQueueNumber,
        callQueue,
        callNext,
        callRepeat,
        completeActive,
        addQueue,
        resetAllQueues,
        filterValidQueues
    };
})();
