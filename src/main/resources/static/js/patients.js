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
            </td>
        `;

        patientsTableBody.prepend(newRow);


        const newDeleteButton = newRow.querySelector(".delete-patient-btn");
        newDeleteButton.addEventListener("click", () => deletePatient(patient.patientId, newRow));
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
