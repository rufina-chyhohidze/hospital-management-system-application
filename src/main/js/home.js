import '../scss/home.scss'
import { animate, inView, stagger } from 'motion'

animate('h2.fw-bold', {
    opacity: [ 0, 1 ],
    y: [ -50, 0 ]
}, {
    delay: 0.2,
    duration: 1,
    easing: 'ease-out'
})

inView('.department-img', ({ target }) => {
    animate(target, {
        opacity: [ 0, 1 ],
        scale: [ 0.9, 1 ],
        rotate: [ -2, 0 ]
    }, {
        duration: 0.6,
        easing: 'ease-out',
        delay: stagger(0.15)
    })
})
