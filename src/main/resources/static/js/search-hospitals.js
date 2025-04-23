
// TODO: Remove unnecessary domcontentloaded wait, use defer in script tag instead
    const searchTermInput = document.getElementById('searchTerm');
    const tableBody = document.querySelector('tbody');

    async function searchHospitals() {
        if (searchTermInput.value.trim().length === 0) {
            tableBody.innerHTML = "";
            return;
        }

        const response = await fetch(`/api/hospitals?search=${searchTermInput.value}`);

        if (response.status === 200) {
            const hospitals = await response.json();
            tableBody.innerHTML = "";

            for (const hospital of hospitals) {
                tableBody.innerHTML += `
                <tr>
                    <td>${hospital.hospitalName}</td>
                    <td>${hospital.hospitalAddress}</td>
                    <td>${hospital.establishedDate}</td>
                    <td>${hospital.departments.join(", ")}</td>
                </tr>`;
            }
        } else {
            tableBody.innerHTML = "";
        }
    }
    searchTermInput.addEventListener('input', searchHospitals);
