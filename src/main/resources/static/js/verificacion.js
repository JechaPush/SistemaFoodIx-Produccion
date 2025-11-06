// Función para mostrar el formulario de correo
    function showEmailForm() {
        document.getElementById('emailForm').style.display = 'block';
        document.getElementById('verificationForm').style.display = 'none';
    }

    // Función para mostrar el formulario de verificación
    function showVerificationForm() {
        document.getElementById('emailForm').style.display = 'none';
        document.getElementById('verificationForm').style.display = 'block';
    }

    // Manejar el envío del formulario de correo
    document.getElementById('emailForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Mostrar el modal con la animación
        var codeModal = new bootstrap.Modal(document.getElementById('codeModal'));
        codeModal.show();
        
        // Simular envío (en producción esto sería una llamada AJAX real)
        setTimeout(function() {
            codeModal.hide();
            showVerificationForm();
        }, 3000);
        
        // En producción, aquí enviarías el formulario realmente
        // this.submit();
    });