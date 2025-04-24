import { getCsrf } from './util/csrf.js';
const { token, header } = getCsrf();
const addPatientForm = document.getElementById("addPatientForm");
const patientsTableBody = document.getElementById("patientsTableBody");

const currentUserId = document.querySelector("meta[name='current-user-id']")?.content;

document.querySelectorAll("tr[data-patient-id]").forEach(row => {
    const creatorId = row.getAttribute("data-creator-id");

    if (creatorId === currentUserId) {
        attachEventListeners(row, row.getAttribute("data-patient-id"));
    } else {
        const deleteBtn = row.querySelector(".delete-patient-btn");
        const editBtn = row.querySelector(".edit-patient-btn");

        if (deleteBtn) {
            deleteBtn.disabled = true;
            deleteBtn.style.opacity = "0.5";
            deleteBtn.style.pointerEvents = "none";
        }

        if (editBtn) {
            editBtn.disabled = true;
            editBtn.style.opacity = "0.5";
            editBtn.style.pointerEvents = "none";
        }
    }
});

if (addPatientForm) {
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
                "Content-Type": "application/json",
                [header]: token
            },
            body: JSON.stringify(patientData)
        });

        if (response.status === 201) {
            const newPatient = await response.json();
            addPatientToTable(newPatient);
            addPatientForm.reset();
        } else {
            // TODO HANDLE ALL POSSIBLE ERROR CODES IN ALL FETCH REQUESTS
            alert("Failed to add patient.");
        }
    });
}

function addPatientToTable(patient) {
    const currentUserId = document.querySelector("meta[name='current-user-id']")?.content;
    const isOwner = String(patient.creatorId) === String(currentUserId);

    const newRow = document.createElement("tr");
    newRow.setAttribute("data-patient-id", patient.patientId);
    newRow.setAttribute("data-creator-id", patient.creatorId);

    newRow.innerHTML = `
        <td>${patient.firstName}</td>
        <td>${patient.lastName}</td>
        <td>${patient.age}</td>
        <td>${patient.gender}</td>
        <td>${patient.admissionDate}</td>
        <td>${patient.billingAmount}</td>
        <td>
            <a href="/patients/${patient.patientId}" class="btn btn-info btn-sm">View Details</a>
            ${isOwner ? `
                <button type="button" class="btn btn-danger btn-sm delete-patient-btn">
                    <i class="bi bi-trash"></i> Delete
                </button>
                <button type="button" class="btn btn-warning btn-sm edit-patient-btn">
                    <i class="bi bi-pencil"></i> Edit
                </button>
            ` : `
                <button type="button" class="btn btn-danger btn-sm" disabled style="opacity:0.5;">
                    <i class="bi bi-trash"></i> Delete
                </button>
                <button type="button" class="btn btn-warning btn-sm" disabled style="opacity:0.5;">
                    <i class="bi bi-pencil"></i> Edit
                </button>
            `}
        </td>
    `;

    patientsTableBody.prepend(newRow);
    if (isOwner) {
        attachEventListeners(newRow, patient.patientId);
    }
}


function attachEventListeners(tableRow, patientId) {
    const deleteButton = tableRow.querySelector(".delete-patient-btn");
    deleteButton?.addEventListener("click", async () => {
        const response = await fetch(`/api/patients/${patientId}`, {
            method: "DELETE",
            headers: {
                [header]: token
            }
        });

        if (response.status === 204) {
            tableRow.remove();
        } else if (response.status === 404) {
            alert("Patient not found.");
        } else {
            alert("Error deleting patient.");
        }
    });
}

