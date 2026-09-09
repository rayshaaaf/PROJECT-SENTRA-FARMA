// Sentra Farma - API Gateway Helper Client
const API_BASE_URL = 'http://localhost:8080/api/v1';

const API = {
    getToken() {
        return localStorage.getItem('jwt_token');
    },

    getUser() {
        const userStr = localStorage.getItem('user_data');
        return userStr ? JSON.parse(userStr) : null;
    },

    setSession(token, user) {
        localStorage.setItem('jwt_token', token);
        localStorage.setItem('user_data', JSON.stringify(user));
    },

    clearSession() {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_data');
    },

    async request(endpoint, options = {}) {
        const token = this.getToken();
        const headers = {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                ...options,
                headers
            });

            if (response.status === 401) {
                this.clearSession();
                if (!window.location.pathname.includes('/auth/')) {
                    window.location.href = '/frontend/html/auth/login.html';
                }
            }

            const data = await response.json().catch(() => ({}));

            if (!response.ok) {
                let errorMsg = data.message || data.error;
                if (!errorMsg || errorMsg === 'Internal Server Error') {
                    if (response.status === 400 || response.status === 401) {
                        errorMsg = 'Email atau password yang Anda masukkan tidak sesuai.';
                    } else if (response.status >= 500) {
                        errorMsg = 'Layanan sistem sedang memuat ulang. Silakan coba beberapa saat lagi.';
                    } else {
                        errorMsg = 'Terjadi kesalahan sistem, silakan periksa input Anda.';
                    }
                }
                throw new Error(errorMsg);
            }

            return data;
        } catch (err) {
            console.error('API Error:', err);
            throw err;
        }
    },

    get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    },

    post(endpoint, body) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    },

    put(endpoint, body) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    },

    patch(endpoint, queryParams = {}) {
        const query = new URLSearchParams(queryParams).toString();
        const url = query ? `${endpoint}?${query}` : endpoint;
        return this.request(url, { method: 'PATCH' });
    },

    delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
};

window.API = API;
