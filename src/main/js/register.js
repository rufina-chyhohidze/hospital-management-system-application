import '../scss/register.scss'
import Joi from 'joi'
;(function() {
    'use strict'

    const form = document.querySelector('.needs-validation')
    if (!form) {
        return
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault()
        event.stopPropagation()

        const username = form.querySelector('[name="username"]').value
        const password = form.querySelector('[name="password"]').value

        const schema = Joi.object({
            username: Joi.string().min(3).required().messages({
                'string.empty': 'Username is required.',
                'string.min': 'Username must be at least 3 characters.'
            }),
            password: Joi.string().min(6).required().messages({
                'string.empty': 'Password is required.',
                'string.min': 'Password must be at least 6 characters.'
            })
        })

        const { error } = schema.validate({ username, password })

        if (error) {
            alert(error.details[0].message)
            form.classList.remove('was-validated')
        } else {
            form.classList.add('was-validated')
            form.submit()
        }
    }, false)
})()
