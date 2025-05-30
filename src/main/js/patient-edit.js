import { getCsrf } from './util/csrf.js'

const { token, header } = getCsrf()
const patientsTableBody = document.getElementById('patientsTableBody')

patientsTableBody.addEventListener('click', async function(event) {
    const button = event.target.closest('.edit-patient-btn')
    if (!button) {
        return
    }

    const tableRow = button.closest('tr')
    const isEditing = button.dataset.editing === 'true'

    if (!isEditing) {
        const admissionDateCell = tableRow.children[4]
        const billingAmountCell = tableRow.children[5]

        const admissionDateInput = document.createElement('input')
        admissionDateInput.type = 'date'
        admissionDateInput.classList.add('form-control')
        admissionDateInput.value = admissionDateCell.textContent.trim()
        admissionDateCell.innerHTML = ''
        admissionDateCell.appendChild(admissionDateInput)

        const billingAmountInput = document.createElement('input')
        billingAmountInput.type = 'number'
        billingAmountInput.classList.add('form-control')
        billingAmountInput.value = billingAmountCell.textContent.trim()
        billingAmountCell.innerHTML = ''
        billingAmountCell.appendChild(billingAmountInput)

        button.dataset.editing = 'true'
        button.classList.remove('btn-warning')
        button.classList.add('btn-success')
        button.innerHTML = `<i class="bi bi-check-lg"></i> Save`
    } else {
        const patientId = tableRow.getAttribute('data-patient-id')
        const newAdmissionDate = tableRow.children[4].querySelector('input').value
        const newBillingAmount = parseFloat(tableRow.children[5].querySelector('input').value)

        const response = await fetch(`/api/patients/${patientId}`, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                [header]: token
            },
            body: JSON.stringify({
                admissionDate: newAdmissionDate,
                billingAmount: newBillingAmount
            })
        })

        if (response.status === 204) {
            tableRow.children[4].textContent = newAdmissionDate
            tableRow.children[5].textContent = newBillingAmount.toFixed(2)

            button.dataset.editing = 'false'
            button.classList.remove('btn-success')
            button.classList.add('btn-warning')
            button.innerHTML = `<i class="bi bi-pencil"></i> Edit`
        } else {
            alert('Failed to update patient. Please try again.')
        }
    }
})
