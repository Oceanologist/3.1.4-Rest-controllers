document.addEventListener('DOMContentLoaded', function() {
    // Инициализация всех обработчиков
    initEditUserModal();
    initChangePasswordModal();
    initPasswordValidation();
    initCreateUserForm();
    initEditUserForm();
    initChangePasswordForm();
    initDeleteButtons();
});

// 1. Обработчик модального окна редактирования пользователя
function initEditUserModal() {
    const editModal = document.getElementById('editUserModal');
    if (!editModal) return;

    editModal.addEventListener('show.bs.modal', function(event) {
        const button = event.relatedTarget;
        const userId = button.getAttribute('data-user-id');
        const username = button.getAttribute('data-user-username');
        const lastName = button.getAttribute('data-user-lastname');
        const age = button.getAttribute('data-user-age');
        const email = button.getAttribute('data-user-email');
        const rolesStr = button.getAttribute('data-user-roles');

        // Заполнение формы
        setValue('editUserId', userId);
        setValue('displayUserId', userId);
        setValue('editUsername', username);
        setValue('editLastName', lastName);
        setValue('editAge', age);
        setValue('editEmail', email);

        // Сброс и установка ролей
        setChecked('editRoleAdmin', false);
        setChecked('editRoleUser', false);

        try {
            const roles = JSON.parse(rolesStr.replace(/'/g, '"'));
            roles.forEach(role => {
                if (role === 'ROLE_ADMIN') setChecked('editRoleAdmin', true);
                if (role === 'ROLE_USER') setChecked('editRoleUser', true);
            });
        } catch (e) {
            console.error('Error parsing roles:', e);
        }
    });
}

// 2. Обработчик модального окна смены пароля
function initChangePasswordModal() {
    const passwordModal = document.getElementById('changePasswordModal');
    if (!passwordModal) return;

    passwordModal.addEventListener('show.bs.modal', function(event) {
        const button = event.relatedTarget;
        const userId = button.getAttribute('data-user-id');

        setValue('passwordUserId', userId);
        setDisplay('passwordError', 'none');
        setValue('newPassword', '');
        setValue('confirmNewPassword', '');
    });
}

// 3. Валидация паролей в реальном времени
function initPasswordValidation() {
    const confirmPassword = document.getElementById('confirmNewPassword');
    if (!confirmPassword) return;

    confirmPassword.addEventListener('input', function() {
        const newPassword = document.getElementById('newPassword').value;
        const showError = this.value !== newPassword;
        setDisplay('passwordError', showError ? 'block' : 'none');
    });
}

// 4. Обработчик формы создания пользователя
function initCreateUserForm() {
    const form = document.getElementById('createUserForm');
    if (!form) return;

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = {
            username: getValue('createUsername'),
            lastName: getValue('createLastName'),
            age: getValue('createAge'),
            email: getValue('createEmail'),
            password: getValue('createPassword'),
            roles: [getCheckedRole("input[name='createRoles']:checked")]
        };

        sendRequest('/api/admin/new', 'POST', formData)
            .then(() => window.location.reload())
            .catch(showError);
    });
}

// 5. Обработчик формы редактирования пользователя
function initEditUserForm() {
    const form = document.getElementById('editUserForm');
    if (!form) return;

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const userData = {
            id: getValue('editUserId'),
            username: getValue('editUsername'),
            lastName: getValue('editLastName'),
            age: getValue('editAge'),
            email: getValue('editEmail'),
            roles: []
        };

        if (isChecked('editRoleAdmin')) userData.roles.push('ROLE_ADMIN');
        if (isChecked('editRoleUser')) userData.roles.push('ROLE_USER');

        sendRequest('/api/admin/edit', 'PUT', userData)
            .then(() => window.location.reload())
            .catch(showError);
    });
}

// 6. Обработчик формы смены пароля
function initChangePasswordForm() {
    const form = document.getElementById('changePasswordForm');
    if (!form) return;

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const newPassword = getValue('newPassword');
        const confirmPassword = getValue('confirmNewPassword');

        if (newPassword !== confirmPassword) {
            setDisplay('passwordError', 'block');
            return;
        }

        const passwordData = {
            userId: getValue('passwordUserId'),
            newPassword: newPassword
        };

        sendRequest('/api/admin/change-password', 'POST', passwordData)
            .then(() => {
                alert('Password changed successfully!');
                const modalEl = document.getElementById('changePasswordModal');
                const modal = bootstrap.Modal.getInstance(modalEl);
                if (modal) modal.hide();
            })
            .catch(showError);
    });
}

// 7. Обработчики кнопок удаления
function initDeleteButtons() {
    document.querySelectorAll('.delete-user-btn').forEach(button => {
        button.addEventListener('click', function() {
            const userId = this.getAttribute('data-user-id');
            if (!confirm('Are you sure you want to delete this user?')) return;

            sendRequest(`/api/admin/delete/${userId}`, 'DELETE')
                .then(() => window.location.reload())
                .catch(showError);
        });
    });
}

// ===== ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ =====

function sendRequest(url, method, data = null) {
    return fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: data ? JSON.stringify(data) : null
    }).then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text) });
        }
        return response.text();
    });
}

function showError(error) {
    console.error('Error:', error);
    alert('Error: ' + (error.message || error));
}

function getValue(id) {
    const el = document.getElementById(id);
    return el ? el.value : null;
}

function setValue(id, value) {
    const el = document.getElementById(id);
    if (el) el.value = value;
}

function isChecked(id) {
    const el = document.getElementById(id);
    return el ? el.checked : false;
}

function setChecked(id, checked) {
    const el = document.getElementById(id);
    if (el) el.checked = checked;
}

function setDisplay(id, display) {
    const el = document.getElementById(id);
    if (el) el.style.display = display;
}

function getCheckedRole(selector) {
    const radio = document.querySelector(selector);
    return radio ? radio.value : null;
}