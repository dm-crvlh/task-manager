document.addEventListener("DOMContentLoaded", function() {
        const plusLogo = document.getElementById("plus_logo");
        const projectForm = document.getElementById("projectForm");

        plusLogo.addEventListener("click", function() {
            projectForm.style.display = "block";
        });

        const deleteButtons = document.querySelectorAll(".delete-icon");
        deleteButtons.forEach(button => {
            button.addEventListener("click", function() {
                const projectId = button.getAttribute("data-task-id");
                deleteProject(projectId);
            });
        });

        const editButtons = document.querySelectorAll(".edit-icon");
        editButtons.forEach(button => {
            button.addEventListener("click", function() {
                const projectId = button.getAttribute("data-task-id");
                const editForm = document.querySelector(`.edit-form[data-task-id="${projectId}"]`);
                editForm.style.display = "block";
            });
        });

        const saveButtons = document.querySelectorAll(".save-button");
        saveButtons.forEach(button => {
            button.addEventListener("click", function(event) {
                event.preventDefault();
                const projectId = button.parentElement.getAttribute("data-task-id");
                const editedProjectName = button.parentElement.querySelector('input[name="editedProjectName"]').value;
                saveEditedProject(projectId, editedProjectName);
            });
        });

        const cancelButtons = document.querySelectorAll(".cancel-button");
        cancelButtons.forEach(button => {
            button.addEventListener("click", function(event) {
                event.preventDefault();
                const editForm = button.parentElement;
                editForm.style.display = "none";
            });
        });


    function deleteProject(projectId) {
        const xhr = new XMLHttpRequest();
        xhr.open("DELETE", `/deleteProject/${projectId}`, true);
        xhr.onload = function() {
            if (xhr.status === 204) {
                location.reload();
            } else {
                console.error("Error deleting project:", xhr.statusText);
            }
        };

        xhr.onerror = function() {
            console.error("Network error");
        };

        xhr.send();
    }

    function saveEditedProject(projectId, editedProjectName) {
        const xhr = new XMLHttpRequest();
        xhr.open("POST", `/updateProjectName/${projectId}`, true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    location.reload();
                } else {
                    console.error("An error occurred:", xhr.statusText);
                }
            }
        };

        xhr.send(`projectName=${editedProjectName}`);
    }
});