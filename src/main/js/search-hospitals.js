import dayjs from 'dayjs'
import { animate, stagger } from 'motion'

const searchTermInput = document.getElementById('searchTerm')
const tableBody = document.querySelector('tbody')

function debounce(fn, delay = 300) {
    let timeout
    return (...args) => {
        clearTimeout(timeout)
        timeout = setTimeout(() => fn(...args), delay)
    }
}

function highlightMatch(text, term) {
    const escapedTerm = term.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
    return text.replace(
        new RegExp(`(${escapedTerm})`, 'gi'),
        '<mark style="background-color: greenyellow; color: black;">$1</mark>'
    )
}

async function searchHospitals() {
    const term = searchTermInput.value.trim()

    if (term.length === 0) {
        tableBody.innerHTML = ''
        return
    }

    const response = await fetch(`/api/hospitals?search=${encodeURIComponent(term)}`)

    if (response.ok) {
        const hospitals = await response.json()
        tableBody.innerHTML = ''

        for (const hospital of hospitals) {
            const formattedDate = dayjs(hospital.establishedDate).format('MMMM D, YYYY')

            const row = document.createElement('tr')
            row.innerHTML = `
        <td>${highlightMatch(hospital.hospitalName, term)}</td>
        <td>${highlightMatch(hospital.hospitalAddress, term)}</td>
        <td>${formattedDate}</td>
        <td>${hospital.departments.join(', ')}</td>
      `
            tableBody.appendChild(row)
        }

        animate(tableBody.querySelectorAll('tr'), {
            opacity: [ 0, 1 ],
            y: [ -10, 0 ]
        }, {
            duration: 0.4,
            delay: stagger(0.05)
        })
    } else {
        tableBody.innerHTML = `<tr><td colspan="4">No hospitals found</td></tr>`
    }
}

// Use debounce on input
searchTermInput.addEventListener('input', debounce(searchHospitals, 300))
