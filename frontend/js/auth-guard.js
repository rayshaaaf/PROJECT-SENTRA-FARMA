// Sentra Farma - Auth Guard & Role-Based Access Control
(function () {
    const publicPages = [
        'login.html',
        'register.html',
        'forgot-password.html',
        'reset-password.html',
        '401.html',
        '403.html',
        '404.html',
        '500.html'
    ];

    const currentPath = window.location.pathname.toLowerCase();
    const fileName = currentPath.split('/').pop() || '';
    const isPublic = publicPages.includes(fileName);

    const token = localStorage.getItem('jwt_token');
    const userStr = localStorage.getItem('user_data');
    let user = null;
    try {
        user = userStr ? JSON.parse(userStr) : null;
    } catch (e) {
        user = null;
    }

    const isValidSession = Boolean(token && user && token !== 'null' && token !== 'undefined');

    // 1. IF NOT LOGGED IN & TRYING TO ACCESS PROTECTED PAGE -> REDIRECT TO LOGIN INSTANTLY
    if (!isPublic && !isValidSession) {
        window.location.replace('/frontend/html/auth/login.html');
        return;
    }

    // 2. IF LOGGED IN & VISITING LOGIN PAGE -> REDIRECT TO DASHBOARD
    if (isValidSession && currentPath.endsWith('/login.html')) {
        redirectRoleDashboard(user.role);
        return;
    }

    // 3. ROLE-BASED ACCESS CONTROL (RBAC)
    if (isValidSession && user && user.role) {
        const role = user.role.toUpperCase();
        if (currentPath.includes('/admin/') && role !== 'ADMIN') {
            redirectRoleDashboard(role);
        } else if (currentPath.includes('/dokter/') && role !== 'DOKTER' && role !== 'ADMIN') {
            redirectRoleDashboard(role);
        } else if (currentPath.includes('/apoteker/') && role !== 'APOTEKER' && role !== 'ADMIN') {
            redirectRoleDashboard(role);
        } else if (currentPath.includes('/resepsionis/') && role !== 'RESEPSIONIS' && role !== 'ADMIN') {
            redirectRoleDashboard(role);
        } else if (currentPath.includes('/user/') && role !== 'PASIEN' && role !== 'ADMIN') {
            // Non-pasien role accessing user portal
            // Let admin access or redirect to staff dashboard
            if (role === 'DOKTER' || role === 'APOTEKER' || role === 'RESEPSIONIS') {
                redirectRoleDashboard(role);
            }
        }
    }

    function redirectRoleDashboard(role) {
        switch (role) {
            case 'DOKTER':
                window.location.replace('/frontend/html/dokter/dashboard.html');
                break;
            case 'APOTEKER':
                window.location.replace('/frontend/html/apoteker/dashboard.html');
                break;
            case 'RESEPSIONIS':
                window.location.replace('/frontend/html/resepsionis/dashboard.html');
                break;
            case 'ADMIN':
                window.location.replace('/frontend/html/admin/dashboard.html');
                break;
            case 'PASIEN':
            default:
                window.location.replace('/frontend/html/user/dashboard.html');
                break;
        }
    }

    window.logout = function () {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_data');
        window.location.replace('/frontend/html/auth/login.html');
    };
})();
