function validateForm() {
    var fileInput = document.getElementById('csvFile');
    var filePath = fileInput.value;
    var allowedExtensions = /(\.csv)$/i;
    
    // Check if file extension is .csv
    if (!allowedExtensions.exec(filePath)) {
        fileInput.value = '';
        Toastify({
            text: "Please check your CSV file.",
            duration: 3000,
            close: true,
            gravity: "top",
            position: "right",
            backgroundColor: "#f44336",
        }).showToast();
        return false;
    }
    
    // Read the file contents
    var reader = new FileReader();
    reader.onload = function(e) {
        var contents = e.target.result;
        
        // Split contents into lines
        var lines = contents.split(/\r\n|\n/);
        
        // Check if the first line (header) matches expected format
        var expectedHeader = "ID,Name,DateOfBirth,Address,Department";
        var actualHeader = lines[0].trim();
        
        if (actualHeader !== expectedHeader) {
            fileInput.value = '';
            Toastify({
                text: "Invalid CSV format. Please check your CSV file.",
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#f44336",
            }).showToast();
            return false;
        }
        
        // If all validations pass, allow form submission
        fileInput.closest('form').submit();
        
        // Optionally, show success message using Toastify or other method
        Toastify({
            text: "CSV submitted successfully!",
            duration: 3000,
            close: true,
            gravity: "top",
            position: "right",
            backgroundColor: "#4caf50",
        }).showToast();
    };
    
    // Read the file as text
    reader.readAsText(fileInput.files[0]);
    
    // Prevent form submission for now
    return false;
}