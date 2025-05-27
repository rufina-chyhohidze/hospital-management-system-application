import { animate, stagger } from 'motion'

// Animate table rows (doctor list)
const doctorRows = document.querySelectorAll('#doctorsTableBody tr')

animate(doctorRows, {
    opacity: [ 0, 1 ],
    y: [ -20, 0 ]
}, {
    duration: 0.6,
    delay: stagger(0.1),
    easing: 'ease-out'
})

// Optional: Animate title and search/add forms if present
animate('h1.page-title', {
    opacity: [ 0, 1 ],
    x: [ -40, 0 ]
}, {
    duration: 0.6,
    delay: 0.2
})

animate('#addDoctorForm', {
    opacity: [ 0, 1 ],
    scale: [ 0.95, 1 ]
}, {
    delay: 0.4,
    duration: 0.5
})
