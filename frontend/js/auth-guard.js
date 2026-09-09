// Sentra Farma - Auth Guard & Role-Based Access Control
(function () {
    const publicPaths = [
        '/frontend/html/auth/login.html',
        '/frontend/html/auth/register.html',
        '/frontend/html/auth/forgot-password.html',
        '/frontend/html/auth/reset-password.html',
        '/frontend/html/errors/401.html',
        '/frontend/html/errors/403.html',
        '/frontend/html/errors/404.html',
        '/frontend/html/errors/500.html'
    ];

    const currentPath = window.location.pathname;
    const isPublic = publicPaths.some(p => currentPath.endsWith(p));

    const token = localStorage.getItem('jwt_token');
    const userStr = localStorage.getItem('user_data');
    const user = userStr ? JSON.parse(userStr) : null;

    if (!isPublic && (!token || !user)) {
        window.location.href = '/frontend/html/auth/login.html';
        return;
    }

    if (token && user && currentPath.includes('/auth/login.html')) {
        // Redirect to appropriate dashboard based on role
        redirectRoleDashboard(user.role);
    }

    function redirectRoleDashboard(role) {
        switch (role) {
            case 'DOKTER':
                window.location.href = '/frontend/html/dokter/dashboard.html';
                break;
            case 'APOTEKER':
                window.location.href = '/frontend/html/apoteker/dashboard.html';
                break;
            case 'RESEPSIONIS':
                window.location.href = '/frontend/html/resepsionis/dashboard.html';
                break;
            case 'ADMIN':
                window.location.href = '/frontend/html/admin/dashboard.html';
                break;
            case 'PASIEN':
            default:
                window.location.href = '/frontend/html/user/dashboard.html';
                break;
        }
    }

    window.logout = function () {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_data');
        window.location.href = '/frontend/html/auth/login.html';
    };
})();
