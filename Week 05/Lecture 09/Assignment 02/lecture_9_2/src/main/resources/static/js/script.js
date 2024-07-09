document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const submitButton = document.querySelector('button[type="submit"]');
    const inputs = form.querySelectorAll('input, select, textarea');

    function checkFormCompletion() {
        let isComplete = true;
        inputs.forEach(input => {
            if (!input.value.trim()) {
                isComplete = false;
            }
        });

        if (isComplete) {
            submitButton.disabled = false;
        } else {
            submitButton.disabled = true;
        }
    }

    inputs.forEach(input => {
        input.addEventListener('input', checkFormCompletion);
    });

    form.addEventListener('submit', function(event) {
        if (!submitButton.disabled) {
            Toastify({
                text: "Form submitted successfully!",
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#4caf50",
            }).showToast();
        } else {
            event.preventDefault();
            Toastify({
                text: "Please complete all fields.",
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#f44336",
            }).showToast();
        }
    });
});