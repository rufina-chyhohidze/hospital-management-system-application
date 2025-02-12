document.addEventListener("DOMContentLoaded", function () {
    const deleteButtons = document.querySelectorAll(".delete-patient-btn");

    deleteButtons.forEach(button => {
        button.addEventListener("click", async function (event) {
            event.preventDefault();

            const tableRow = event.target.closest("tr");
            const patientId = tableRow.getAttribute("data-patient-id");

            const response = await fetch(`/api/patients/${patientId}`, { method: "DELETE" });

            if (response.status === 204) {
                console.log(`Patient ${patientId} deleted successfully.`);
                tableRow.remove(); //
            } else if (response.status === 404) {
                console.warn(`Patient ${patientId} not found.`);
                alert("Error: Patient not found.");
            } else {
                console.error(` Unexpected error while deleting patient ${patientId}.`);
                alert("Error deleting patient. Please try again.");
            }
        });
    });
});
