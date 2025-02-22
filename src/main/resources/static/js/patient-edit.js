document.addEventListener("DOMContentLoaded", function () {
    const patientsTableBody = document.getElementById("patientsTableBody");

    patientsTableBody.addEventListener("click", function (event) {
        const button = event.target.closest(".edit-patient-btn");
        if (button) {
            const tableRow = button.closest("tr");
            enableEditing(tableRow);
        }
    });

    function enableEditing(tableRow) {
        const patientId = tableRow.getAttribute("data-patient-id");
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

        // Add event listener to save changes
        editButton.removeEventListener("click", () => enableEditing(tableRow));
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

            editButton.removeEventListener("click", updatePatient);
            editButton.addEventListener("click", () => enableEditing(tableRow));
        } else {
            alert("Failed to update patient. Please try again.");
        }
    }
});
