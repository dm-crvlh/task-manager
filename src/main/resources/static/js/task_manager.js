function deleteTask(deleteIcon) {
        const taskId = deleteIcon.getAttribute("data-task-id");

        const xhr = new XMLHttpRequest();
        xhr.open("POST", `/deleteTask/${taskId}`, true);
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

        xhr.send();
    }


function updateTaskStatus(selectElement) {
    const taskId = selectElement.getAttribute("data-task-id");
    const newStatus = selectElement.value;

    const xhr = new XMLHttpRequest();
    xhr.open("POST", `/updateTaskStatus/${taskId}`, true);
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

    xhr.send(`status=${newStatus}`);
}

function showEditForm(editButton) {
    const taskId = editButton.getAttribute("data-task-id");
    const taskName = editButton.getAttribute("data-task-name");
    const taskAssignedUser = editButton.getAttribute("data-task-assignedEmployee");

    const editForm = document.getElementById("editForm");


    editForm.style.display = "block";
    const form = editForm.querySelector("form");
    form.action = `/editTask/${taskId}`;
    form.querySelector("[name=taskId]").value = taskId;
    form.querySelector("[name=newTaskName]").value = taskName; // Définir le nom de la tâche dans l'input

    const employeeSelect = form.querySelector("#employee-select");
    const employees = document.querySelectorAll("#employee-select option");
    if (taskAssignedUser !== null) {

        // Si taskAssignedUser n'est pas nul, recherchez et sélectionnez l'option correspondante
        for (const userOption of employees) {
            if (userOption.value === taskAssignedUser) {
                userOption.selected = true;
                break;
            }
        }
    } else {
        // Si taskAssignedUser est null, définissez la valeur de l'élément <select> sur une chaîne vide, ce qui sélectionnera l'option par défaut "Select an Employee".
        employeeSelect.value = "";
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const plusLogo = document.getElementById("plus_logo");
    const taskForm = document.getElementById("taskForm");
    const editForm = document.getElementById("editForm");
    const cancelEditButton = document.getElementById("cancelEditButton");

    cancelEditButton.addEventListener("click", function() {
        editForm.style.display = "none";
    });


    plusLogo.addEventListener("click", function() {
        taskForm.style.display = "block";
    });
});