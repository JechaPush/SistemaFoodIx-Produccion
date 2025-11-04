/**
 * Módulo de verificación de correo electrónico
 * Controla el flujo de verificación con animación de hamburguesa
 */

class VerificationManager {
    constructor() {
        this.codeModal = null;
        this.init();
    }

    init() {
        this.bindEvents();
        this.initializeModal();
    }

    initializeModal() {
        const modalElement = document.getElementById('codeModal');
        if (modalElement) {
            this.codeModal = new bootstrap.Modal(modalElement, {
                backdrop: 'static',
                keyboard: false
            });
        }
    }

    bindEvents() {
        // Formulario de email
        const emailForm = document.getElementById('emailForm');
        if (emailForm) {
            emailForm.addEventListener('submit', (e) => this.handleEmailSubmit(e));
        }

        // Formulario de verificación
        const verificationForm = document.getElementById('verificationForm');
        if (verificationForm) {
            verificationForm.addEventListener('submit', (e) => this.handleVerificationSubmit(e));
        }
    }

    handleEmailSubmit(e) {
        e.preventDefault();
        const email = document.getElementById('email').value;
        
        if (this.validateEmail(email)) {
            this.showCodeSentModal();
            this.sendVerificationCode(email);
        } else {
            this.showError('Por favor ingresa un correo electrónico válido');
        }
    }

    handleVerificationSubmit(e) {
        e.preventDefault();
        const code = document.getElementById('code').value;
        
        if (this.validateCode(code)) {
            this.verifyCode(code);
        } else {
            this.showError('Por favor ingresa un código de 6 dígitos válido');
        }
    }

    validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    validateCode(code) {
        const codeRegex = /^[0-9]{6}$/;
        return codeRegex.test(code);
    }

    showCodeSentModal() {
        if (this.codeModal) {
            // Reiniciar la animación
            const hamburger = document.getElementById('animatedHamburger');
            if (hamburger) {
                hamburger.style.animation = 'none';
                setTimeout(() => {
                    hamburger.style.animation = '';
                }, 10);
            }
            
            this.codeModal.show();
        }
    }

    hideCodeSentModal() {
        if (this.codeModal) {
            this.codeModal.hide();
        }
    }

    async sendVerificationCode(email) {
        try {
            const response = await fetch('/auth/send-code', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `email=${encodeURIComponent(email)}`
            });

            const data = await response.json();

            if (data.success) {
                setTimeout(() => {
                    this.hideCodeSentModal();
                    this.showVerificationForm();
                }, 3000);
            } else {
                this.hideCodeSentModal();
                this.showError(data.error || 'Error al enviar el código');
            }
        } catch (error) {
            this.hideCodeSentModal();
            this.showError('Error de conexión');
        }
    }

    async verifyCode(code) {
        try {
            const response = await fetch('/auth/verify-code', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `code=${encodeURIComponent(code)}`
            });

            const data = await response.json();

            if (data.success) {
                if (data.redirectUrl) {
                    const email = document.getElementById('email').value;
                    window.location.href = data.redirectUrl + '?email=' + encodeURIComponent(email);
                }
            } else {
                this.showError(data.error || 'Código inválido');
            }
        } catch (error) {
            this.showError('Error de conexión');
        }
    }

    showVerificationForm() {
        document.getElementById('emailForm').style.display = 'none';
        document.getElementById('verificationForm').style.display = 'block';
    }

    showEmailForm() {
        document.getElementById('emailForm').style.display = 'block';
        document.getElementById('verificationForm').style.display = 'none';
    }

    showError(message) {
        const alertDiv = document.createElement('div');
        alertDiv.className = 'alert alert-danger alert-dismissible fade show';
        alertDiv.innerHTML = `
            <span>${message}</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        const cardBody = document.querySelector('.card-body');
        if (cardBody) {
            cardBody.insertBefore(alertDiv, cardBody.querySelector('form'));
        }
    }
}

// Funciones globales para compatibilidad con HTML
function showVerificationForm() {
    window.verificationManager.showVerificationForm();
}

function showEmailForm() {
    window.verificationManager.showEmailForm();
}

// Inicialización cuando el DOM está listo
document.addEventListener('DOMContentLoaded', function() {
    window.verificationManager = new VerificationManager();
});