document.addEventListener("DOMContentLoaded", function () {
    const deleteButtons = document.querySelectorAll(".delete-hospital-btn");

    deleteButtons.forEach(button => {
        button.addEventListener("click", async function () {
            const tableRow = button.closest("tr");
            const hospitalId = tableRow.getAttribute("data-hospital-id");

            if (!confirm("Are you sure you want to delete this hospital?")) {
                return;
            }

            try {
                const response = await fetch(`/api/hospitals/${hospitalId}`, {
                    method: "DELETE"
                });

                if (response.status === 204) {
                    tableRow.remove();
                } else if (response.status === 404) {
                    alert("Error: Hospital not found.");
                } else {
                    alert("Error deleting hospital. Please try again.");
                }
            } catch (error) {
                console.error("Error deleting hospital:", error);
                alert("Unexpected error occurred.");
            }
        });
    });
});
