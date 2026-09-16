/**
 * Sentra Farma - Live Chat Konsultasi Dokter Real-Time
 * Direct messaging between Patient and Doctor with API persistence and real-time polling
 */
(function() {
    let activeDoctorId = 1;
    let pollInterval = null;
    let doctorsList = [];

    document.addEventListener('DOMContentLoaded', () => {
        injectChatWidgetHTML();
        fetchDoctorsFromAPI();
        loadChatHistory();
    });

    function getUserInfo() {
        const u = window.API ? window.API.getUser() : null;
        return u || { id: 1, namaLengkap: 'Pasien Sentra Farma' };
    }

    function getSessionId() {
        const u = getUserInfo();
        return `chat_patient_${u.id}_doc_${activeDoctorId}`;
    }

    async function fetchDoctorsFromAPI() {
        if (window.API) {
            try {
                const res = await window.API.get('/clinic/dokter');
                if (res && Array.isArray(res) && res.length > 0) {
                    doctorsList = res;
                    if (!activeDoctorId || !doctorsList.find(d => d.id == activeDoctorId)) {
                        activeDoctorId = doctorsList[0].id;
                    }
                    populateDoctorSelect();
                }
            } catch(e) {}
        }
    }

    function populateDoctorSelect() {
        const sel = document.getElementById('chat-doctor-select');
        if (!sel) return;
        if (doctorsList.length === 0) {
            sel.innerHTML = '<option value="">Memuat daftar dokter...</option>';
            return;
        }
        sel.innerHTML = doctorsList.map(d => `
            <option value="${d.id}" class="text-slate-900">${d.namaDokter} (${d.spesialisasi || 'Dokter Spesialis'})</option>
        `).join('');
        sel.value = activeDoctorId;
    }

    function injectChatWidgetHTML() {
        const existing = document.getElementById('sentra-chat-widget-root');
        if (existing) existing.remove();

        const wrapper = document.createElement('div');
        wrapper.id = 'sentra-chat-widget-root';
        wrapper.innerHTML = `
            <!-- BACKDROP OVERLAY -->
            <div id="sentra-chat-backdrop" onclick="window.toggleSentraChatWindow()" class="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 hidden opacity-0 transition-opacity duration-300"></div>

            <!-- PREMIUM SLIDE-IN RIGHT PANEL -->
            <div id="sentra-chat-drawer" class="hidden fixed top-0 right-0 h-screen w-[440px] max-w-full bg-white shadow-2xl border-l border-slate-200/80 z-50 flex flex-col transition-transform duration-300 ease-in-out translate-x-full">
                
                <!-- HEADER -->
                <div class="p-4 bg-gradient-to-r from-brand-900 via-brand-800 to-emerald-950 text-white flex flex-col gap-3 shrink-0 shadow-md">
                    <div class="flex items-center justify-between">
                        <div class="flex items-center gap-3">
                            <div class="w-9 h-9 rounded-2xl bg-white/10 flex items-center justify-center font-bold text-brand-300 border border-white/10">
                                <span class="material-symbols-outlined text-[20px]">forum</span>
                            </div>
                            <div>
                                <h3 class="text-sm font-extrabold tracking-tight">Konsultasi Live Dokter Real-Time</h3>
                                <p class="text-[10px] text-emerald-300 font-extrabold flex items-center gap-1 mt-0.5">
                                    <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span> Terhubung Langsung dengan Dokter
                                </p>
                            </div>
                        </div>
                        <div class="flex items-center gap-1.5">
                            <button onclick="window.clearPatientChatHistory()" title="Bersihkan Chat" class="px-2.5 py-1 rounded-xl bg-white/10 hover:bg-rose-500/30 text-rose-200 text-[10px] font-extrabold border border-white/10 flex items-center gap-1 transition-all cursor-pointer">
                                <span class="material-symbols-outlined text-[14px]">delete</span>
                                <span>Bersihkan</span>
                            </button>
                            <button onclick="window.toggleSentraChatWindow()" class="p-2 rounded-2xl hover:bg-white/10 text-white/80 transition-colors flex items-center justify-center">
                                <span class="material-symbols-outlined text-[20px]">close</span>
                            </button>
                        </div>
                    </div>

                    <!-- Doctor Select -->
                    <div class="pt-1">
                        <label class="text-[10px] font-extrabold uppercase text-brand-200 tracking-wider block mb-1">Pilih Dokter Spesialis:</label>
                        <select id="chat-doctor-select" onchange="window.onDoctorChatChanged(this.value)" class="w-full bg-slate-900/80 border border-white/20 rounded-xl py-2 px-3 font-bold text-white text-xs focus:outline-none focus:border-emerald-400 cursor-pointer">
                            <option value="" class="text-slate-900">Memuat daftar dokter...</option>
                        </select>
                    </div>
                </div>

                <!-- CHAT MESSAGES BODY -->
                <div id="chat-messages-container" class="flex-1 p-4 overflow-y-auto space-y-4 bg-slate-50/60">
                    <!-- Dynamically populated -->
                </div>

                <!-- QUICK SUGGESTION CHIPS -->
                <div class="px-3 py-2 bg-slate-100/80 border-t border-slate-200/60 flex items-center gap-1.5 overflow-x-auto no-scrollbar shrink-0">
                    <button onclick="window.sendQuickChip('Halo dok, mau konsultasi keluhan pusing & demam')" class="shrink-0 px-2.5 py-1 bg-white hover:bg-brand-50 hover:border-brand-300 border border-slate-200 rounded-lg text-[11px] font-semibold text-slate-700 transition-colors">
                        😷 Pusing & Demam
                    </button>
                    <button onclick="window.sendQuickChip('Dok, mau tanya aturan minum obat resep saya')" class="shrink-0 px-2.5 py-1 bg-white hover:bg-brand-50 hover:border-brand-300 border border-slate-200 rounded-lg text-[11px] font-semibold text-slate-700 transition-colors">
                        💊 Aturan Minum Obat
                    </button>
                    <button onclick="window.sendQuickChip('Bagaimana alur reservasi & pembayarannya dok?')" class="shrink-0 px-2.5 py-1 bg-white hover:bg-brand-50 hover:border-brand-300 border border-slate-200 rounded-lg text-[11px] font-semibold text-slate-700 transition-colors">
                        💳 Info Pembayaran
                    </button>
                </div>

                <!-- FOOTER DIRECT INPUT FORM -->
                <form id="chat-send-form" onsubmit="window.sendDirectMessage(event)" class="p-3 bg-white border-t border-slate-200/80 flex items-center gap-2 shrink-0">
                    <input type="text" id="chat-input-message" autocomplete="off" placeholder="Tulis pesan langsung untuk dokter..." class="flex-1 bg-slate-100 border border-slate-200 rounded-xl px-4 py-2.5 text-xs text-slate-800 focus:outline-none focus:border-brand-500 focus:bg-white transition-all font-medium" />
                    <button type="submit" class="p-2.5 bg-brand-600 hover:bg-brand-700 text-white rounded-xl shadow-md transition-colors flex items-center justify-center">
                        <span class="material-symbols-outlined text-[18px]">send</span>
                    </button>
                </form>

            </div>

            <!-- CUSTOM CENTER CONFIRMATION MODAL FOR PATIENT -->
            <div id="sentra-chat-clear-modal" class="fixed inset-0 bg-slate-900/60 backdrop-blur-sm z-[999] hidden items-center justify-center p-4 transition-all duration-300 opacity-0">
                <div class="bg-white rounded-3xl max-w-xs w-full p-6 shadow-2xl border border-slate-200 text-center space-y-4 transform scale-95 transition-all duration-300">
                    <div class="w-14 h-14 rounded-2xl bg-rose-50 text-rose-500 border border-rose-100 flex items-center justify-center mx-auto shadow-2xs">
                        <span class="material-symbols-outlined text-[32px]">delete_forever</span>
                    </div>
                    <div class="space-y-1">
                        <h4 class="text-base font-extrabold text-slate-900">Bersihkan Riwayat Chat?</h4>
                        <p class="text-xs text-slate-500 font-medium leading-relaxed">
                            Seluruh riwayat obrolan konsultasi dengan Dokter akan dibersihkan.
                        </p>
                    </div>
                    <div class="flex items-center gap-3 pt-2">
                        <button onclick="window.closePatientClearModal()" class="flex-1 py-2.5 px-4 bg-slate-100 hover:bg-slate-200 text-slate-700 font-extrabold text-xs rounded-xl transition-all cursor-pointer">
                            Batal
                        </button>
                        <button onclick="window.executePatientClearHistory()" class="flex-1 py-2.5 px-4 bg-rose-600 hover:bg-rose-700 text-white font-extrabold text-xs rounded-xl shadow-md shadow-rose-600/30 transition-all cursor-pointer">
                            Ya, Bersihkan
                        </button>
                    </div>
                </div>
            </div>
        `;
        document.body.appendChild(wrapper);
    }

    window.toggleSentraChatWindow = function() {
        const drawer = document.getElementById('sentra-chat-drawer');
        const backdrop = document.getElementById('sentra-chat-backdrop');
        if (!drawer || !backdrop) return;

        const isClosed = drawer.classList.contains('translate-x-full') || drawer.classList.contains('hidden');

        if (isClosed) {
            backdrop.classList.remove('hidden');
            setTimeout(() => backdrop.classList.remove('opacity-0'), 10);
            
            drawer.classList.remove('hidden');
            void drawer.offsetHeight;
            drawer.classList.remove('translate-x-full');

            loadChatHistory();
            startPolling();
        } else {
            drawer.classList.add('translate-x-full');
            backdrop.classList.add('opacity-0');
            stopPolling();
            setTimeout(() => {
                backdrop.classList.add('hidden');
                drawer.classList.add('hidden');
            }, 300);
        }
    };

    function startPolling() {
        stopPolling();
        pollInterval = setInterval(loadChatHistory, 3000);
    }

    function stopPolling() {
        if (pollInterval) {
            clearInterval(pollInterval);
            pollInterval = null;
        }
    }

    window.onDoctorChatChanged = function(val) {
        activeDoctorId = parseInt(val);
        loadChatHistory();
    };

    async function loadChatHistory() {
        const container = document.getElementById('chat-messages-container');
        if (!container) return;

        const doc = doctorsList.find(d => d.id == activeDoctorId) || doctorsList[0];
        const sessionId = getSessionId();

        if (localStorage.getItem('cleared_' + sessionId) === 'true') {
            container.innerHTML = `
                <div class="text-center text-slate-400 py-16 font-medium space-y-2">
                    <span class="material-symbols-outlined text-4xl text-slate-300 block">cleaning_services</span>
                    <p class="text-xs font-bold text-slate-600">Riwayat percakapan telah dibersihkan.</p>
                    <p class="text-[10px] text-slate-400">Ketik pesan di bawah untuk konsultasi baru dengan Dokter ${doc.namaDokter}.</p>
                </div>
            `;
            return;
        }

        let messages = [];

        if (window.API) {
            try {
                const res = await window.API.get(`/clinic/chat/messages?sessionId=${sessionId}`);
                if (Array.isArray(res)) {
                    messages = res;
                }
            } catch(e) {}
        }

        // If no API messages yet, load local storage or show welcome message
        if (messages.length === 0) {
            const local = JSON.parse(localStorage.getItem(sessionId) || '[]');
            if (local.length > 0) {
                messages = local;
            } else {
                messages = [{
                    id: 0,
                    senderName: doc.namaDokter,
                    senderId: doc.id,
                    pesan: `Halo! Saya ${doc.namaDokter} (${doc.spesialisasi}). Silakan ketik keluhan kesehatan Anda di bawah ini, saya akan membalas pesan Anda langsung.`,
                    createdAt: new Date().toISOString()
                }];
            }
        }

        renderChatMessages(messages, doc);
    }

    function renderChatMessages(list, doc) {
        const container = document.getElementById('chat-messages-container');
        if (!container) return;

        const user = getUserInfo();

        container.innerHTML = list.map(m => {
            const isUser = (m.senderId === user.id) || (m.senderName && m.senderName.includes('Pasien'));
            const isAi = (m.senderName && m.senderName.includes('AI')) || m.tipePesan === 'AI_TRIAGE';
            const timeStr = m.createdAt ? new Date(m.createdAt).toLocaleTimeString('id-ID', { hour: '2-digit', minute: '2-digit' }) : new Date().toLocaleTimeString('id-ID', { hour: '2-digit', minute: '2-digit' });

            if (isUser) {
                return `
                    <div class="flex flex-col items-end space-y-1">
                        <div class="bg-brand-600 text-white px-4 py-2.5 rounded-2xl rounded-tr-xs text-xs font-semibold max-w-[85%] shadow-sm leading-relaxed">
                            ${escapeHtml(m.pesan)}
                        </div>
                        <span class="text-[9px] text-slate-400 font-bold px-1">${timeStr}</span>
                    </div>
                `;
            } else if (isAi) {
                return `
                    <div class="flex flex-col items-center my-2">
                        <div class="bg-gradient-to-r from-emerald-50 to-teal-50 border border-emerald-300 text-emerald-950 px-4 py-3 rounded-2xl text-xs font-medium max-w-[92%] shadow-2xs space-y-1">
                            <div class="flex items-center gap-1.5 text-[10px] font-extrabold text-emerald-700">
                                <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
                                <span>${escapeHtml(m.senderName || 'SentraAI Assistant')}</span>
                            </div>
                            <div class="whitespace-pre-line leading-relaxed text-slate-800 font-medium">${escapeHtml(m.pesan)}</div>
                        </div>
                        <span class="text-[9px] text-slate-400 font-bold mt-0.5">${timeStr}</span>
                    </div>
                `;
            } else {
                return `
                    <div class="flex flex-col items-start space-y-1">
                        <div class="flex items-center gap-1.5 text-[10px] font-extrabold text-brand-800">
                            <span class="material-symbols-outlined text-[14px]">stethoscope</span>
                            <span>${escapeHtml(m.senderName || doc.namaDokter)}</span>
                        </div>
                        <div class="bg-white border border-slate-200/80 text-slate-800 px-4 py-3 rounded-2xl rounded-tl-xs text-xs font-medium max-w-[88%] shadow-sm leading-relaxed">
                            <div class="whitespace-pre-line">${escapeHtml(m.pesan)}</div>
                        </div>
                        <span class="text-[9px] text-slate-400 font-bold px-1">${timeStr}</span>
                    </div>
                `;
            }
        }).join('');

        scrollToBottomChat();
    }

    function scrollToBottomChat() {
        const container = document.getElementById('chat-messages-container');
        if (container) {
            container.scrollTop = container.scrollHeight;
        }
    }

    window.clearPatientChatHistory = function() {
        const modal = document.getElementById('sentra-chat-clear-modal');
        if (!modal) return;
        modal.classList.remove('hidden');
        modal.classList.add('flex');
        setTimeout(() => {
            modal.classList.remove('opacity-0');
            if (modal.firstElementChild) modal.firstElementChild.classList.remove('scale-95');
        }, 10);
    };

    window.closePatientClearModal = function() {
        const modal = document.getElementById('sentra-chat-clear-modal');
        if (!modal) return;
        modal.classList.add('opacity-0');
        if (modal.firstElementChild) modal.firstElementChild.classList.add('scale-95');
        setTimeout(() => {
            modal.classList.remove('flex');
            modal.classList.add('hidden');
        }, 200);
    };

    window.executePatientClearHistory = async function() {
        window.closePatientClearModal();
        const sessionId = getSessionId();
        localStorage.setItem('cleared_' + sessionId, 'true');
        localStorage.removeItem(sessionId);

        if (window.API) {
            try {
                await window.API.delete(`/clinic/chat/messages?sessionId=${sessionId}`);
            } catch(e) {}
        }

        const container = document.getElementById('chat-messages-container');
        const doc = doctorsList.find(d => d.id == activeDoctorId) || doctorsList[0];
        if (container) {
            container.innerHTML = `
                <div class="text-center text-slate-400 py-16 font-medium space-y-2">
                    <span class="material-symbols-outlined text-4xl text-slate-300 block">cleaning_services</span>
                    <p class="text-xs font-bold text-slate-600">Riwayat percakapan telah dibersihkan.</p>
                    <p class="text-[10px] text-slate-400">Ketik pesan di bawah untuk konsultasi baru dengan Dokter ${doc ? doc.namaDokter : ''}.</p>
                </div>
            `;
        }
    };

    window.sendQuickChip = function(text) {
        const input = document.getElementById('chat-input-message');
        if (input) {
            input.value = text;
            window.sendDirectMessage(new Event('submit'));
        }
    };

    window.sendDirectMessage = async function(e) {
        if (e) e.preventDefault();
        const input = document.getElementById('chat-input-message');
        if (!input) return;

        const text = input.value.trim();
        if (!text) return;

        input.value = '';

        const user = getUserInfo();
        const doc = doctorsList.find(d => d.id == activeDoctorId) || doctorsList[0];
        const sessionId = getSessionId();
        localStorage.removeItem('cleared_' + sessionId);

        const payload = {
            sessionId: sessionId,
            senderId: user.id,
            receiverId: doc.id,
            senderName: `${user.namaLengkap || 'Pasien'} (Pasien)`,
            pesan: text,
            tipePesan: 'TEXT'
        };

        // Save to API
        if (window.API) {
            try {
                await window.API.post('/clinic/chat/messages', payload);
            } catch(err) {
                console.warn('Backend API save message failed, saving locally:', err);
                saveLocalMessage(sessionId, payload);
            }
        } else {
            saveLocalMessage(sessionId, payload);
        }

        loadChatHistory();

        // Trigger SentraAI Triage Auto-Reply after 1 second
        setTimeout(async () => {
            await triggerAiTriageReply(sessionId, doc, text);
        }, 1000);
    };

    async function triggerAiTriageReply(sessionId, doc, userText) {
        const textLower = userText.toLowerCase().trim();
        let aiPesan = "";

        // Regex Intent Recognition
        const isGreeting = /^(hai|halo|hi|hallo|pagi|siang|sore|malam|ping|p|tes|test|selamat|permisi|haloo)(\s+(hai|halo|hi|pagi|siang|sore|malam|dok|dokter))?$/i.test(textLower);
        const isMedicalCheckup = /(berobat|periksa|konsul|konsultasi|sakit|pusing|demam|batuk|pilek|mual|muntah|dada|perut|luka|gatal|lemas|diare|nyeri|sesak|gejala|kurang enak badan|badan panas)/i.test(textLower);
        const isPrescriptionOnly = /\b(resep|dosis|aturan minum|tebus|racikan|vitamin|paracetamol|salep|sirup)\b/i.test(textLower) || (/\bobat\b/i.test(textLower) && !textLower.includes('berobat'));
        const isAdminPayment = /(bayar|alur|reservasi|harga|antrian|bpjs|biaya|tarif|jadwal)/i.test(textLower);

        if (isGreeting && !isMedicalCheckup) {
            aiPesan = `Halo! Selamat datang di Konsultasi Medis Klinik Sentra Farma. 👋\n\nSilakan sebutkan keluhan/gejala kesehatan yang sedang Anda rasakan saat ini, agar dapat saya catat ringkasannya secara otomatis untuk Dokter ${doc.namaDokter}.`;
        } else if (isMedicalCheckup) {
            aiPesan = `Baik, keluhan kesehatan Anda ("${userText}") telah dicatat secara otomatis oleh AI Triage.\n\nMohon sebutkan sudah berapa hari dirasakan & apakah ada alergi obat? Ringkasan gejala awal telah diteruskan ke layar Dokter ${doc.namaDokter}.`;
        } else if (isPrescriptionOnly) {
            aiPesan = `Terima kasih! Pertanyaan terkait resep & aturan minum obat telah diteruskan ke Dokter ${doc.namaDokter} & Tim Apoteker Sentra Farma secara prioritas.`;
        } else if (isAdminPayment) {
            aiPesan = `Untuk alur pendaftaran antrian & info pembayaran klinik (mendukung QRIS, Transfer Bank, & BPJS Kesehatan), Anda dapat mengecek langsung pada menu Antrian Online atau jadwal dokter.`;
        } else {
            aiPesan = `Pesan Anda ("${userText}") telah dicatat oleh AI Assistant. Dokter ${doc.namaDokter} akan membalas segera setelah selesai memeriksa pasien di poli.`;
        }

        const aiPayload = {
            sessionId: sessionId,
            senderId: 999,
            receiverId: doc.id,
            senderName: 'SentraAI Triage Bot',
            pesan: aiPesan,
            tipePesan: 'AI_TRIAGE'
        };

        if (window.API) {
            try {
                await window.API.post('/clinic/chat/messages', aiPayload);
            } catch(err) {
                saveLocalMessage(sessionId, aiPayload);
            }
        } else {
            saveLocalMessage(sessionId, aiPayload);
        }

        loadChatHistory();
    }

    function saveLocalMessage(sessionId, payload) {
        const history = JSON.parse(localStorage.getItem(sessionId) || '[]');
        history.push({
            id: Date.now(),
            senderId: payload.senderId,
            senderName: payload.senderName,
            pesan: payload.pesan,
            createdAt: new Date().toISOString()
        });
        localStorage.setItem(sessionId, JSON.stringify(history));
    }

    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }
})();
