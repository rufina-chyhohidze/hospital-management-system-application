document.addEventListener("DOMContentLoaded", function () {
    const addPatientForm = document.getElementById("addPatientForm");
    const patientsTableBody = document.getElementById("patientsTableBody");

    addPatientForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const patientData = {
            firstName: document.getElementById("firstName").value.trim(),
            lastName: document.getElementById("lastName").value.trim(),
            age: parseInt(document.getElementById("age").value),
            gender: document.getElementById("gender").value,
            admissionDate: document.getElementById("admissionDate").value,
            billingAmount: parseFloat(document.getElementById("billingAmount").value)
        };

        const response = await fetch("/api/patients", {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(patientData)
        });

        if (response.status === 201) {
            const newPatient = await response.json();
            addPatientToTable(newPatient);
            addPatientForm.reset();
        } else {
            alert("Failed to add patient.");
        }
    });

    function addPatientToTable(patient) {
        const newRow = document.createElement("tr");
        newRow.setAttribute("data-patient-id", patient.patientId);

        newRow.innerHTML = `
            <td>${patient.firstName}</td>
            <td>${patient.lastName}</td>
            <td>${patient.age}</td>
            <td>${patient.gender}</td>
            <td>${patient.admissionDate}</td>
            <td>${patient.billingAmount}</td>
            <td>
                <a href="/patients/${patient.patientId}" class="btn btn-info btn-sm">
                    View Details
                </a>
           
                <button type="button" class="btn btn-danger btn-sm delete-patient-btn">
                    <i class="bi bi-trash"></i> Delete
                </button>
                
                <button type="button" class="btn btn-warning btn-sm edit-patient-btn">
                    <i class="bi bi-pencil"></i> Edit
                </button>
            </td>
        `;

        patientsTableBody.prepend(newRow);

        attachEventListeners(newRow, patient.patientId);
    }

    function attachEventListeners(tableRow, patientId) {
        const editButton = tableRow.querySelector(".edit-patient-btn");
        const deleteButton = tableRow.querySelector(".delete-patient-btn");

        editButton.addEventListener("click", () => enableEditing(tableRow, patientId));
        deleteButton.addEventListener("click", () => deletePatient(patientId, tableRow));
    }

    function enableEditing(tableRow, patientId) {
        const admissionDateCell = tableRow.children[4];
        const billingAmountCell = tableRow.children[5];

        const admissionDateInput = document.createElement("input");
        admissionDateInput.type = "date";
        admissionDateInput.value = admissionDateCell.textContent.trim();
        admissionDateCell.innerHTML = "";
        admissionDateCell.appendChild(admissionDateInput);

        const billingAmountInput = document.createElement("input");
        billingAmountInput.type = "number";
        billingAmountInput.value = billingAmountCell.textContent.trim();
        billingAmountCell.innerHTML = "";
        billingAmountCell.appendChild(billingAmountInput);

        const editButton = tableRow.querySelector(".edit-patient-btn");
        editButton.classList.remove("btn-warning");
        editButton.classList.add("btn-success");
        editButton.innerHTML = `<i class="bi bi-check-lg"></i> Save`;

        editButton.removeEventListener("click", () => enableEditing(tableRow, patientId));
        editButton.addEventListener("click", () =>
            updatePatient(patientId, admissionDateInput.value, billingAmountInput.value, tableRow)
        );
    }

    async function updatePatient(patientId, admissionDate, billingAmount, tableRow) {
        const response = await fetch(`/api/patients/${patientId}`, {
            method: "PATCH",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                admissionDate: admissionDate,
                billingAmount: parseFloat(billingAmount)
            })
        });

        if (response.status === 204) {
            tableRow.children[4].textContent = admissionDate;
            tableRow.children[5].textContent = billingAmount;

            const editButton = tableRow.querySelector(".edit-patient-btn");
            editButton.classList.remove("btn-success");
            editButton.classList.add("btn-warning");
            editButton.innerHTML = `<i class="bi bi-pencil"></i> Edit`;

            editButton.removeEventListener("click", () => updatePatient(patientId, admissionDate, billingAmount, tableRow));
            editButton.addEventListener("click", () => enableEditing(tableRow, patientId));
        } else {
            alert("Failed to update patient. Please try again.");
        }
    }

    async function deletePatient(patientId, tableRow) {
        try {
            const response = await fetch(`/api/patients/${patientId}`, { method: "DELETE" });

            if (response.status === 204) {
                tableRow.remove();
            } else if (response.status === 404) {
                alert("Error: Patient not found.");
            } else {
                alert("Error deleting patient. Please try again.");
            }
        } catch (error) {
            console.error("Error deleting patient:", error);
            alert("Unexpected error occurred.");
        }
    }
});
