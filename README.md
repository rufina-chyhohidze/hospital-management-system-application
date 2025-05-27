# Programming 5 - Hospital Management System

## Project Overview
This is a **Spring Boot** application that manages **hospitals, doctors, patients, and medical records**. The application allows **CRUD operations** for all entities and includes **RESTful APIs** with dynamic UI updates using **JavaScript & Fetch API**.

---

##  Course Information
- **Course Name**: Programming 5
- **Student Name**: Chyhohidze Rufina
- **KdG Email**: rufina.chyhohidze@student.kdg.be
- **Academic Year**: 2024-2025
- **Group**: ACS202

---

Build Instructions

### 1. Install NPM Dependencies
Run this once to install frontend packages:

```bash
npm install
````
```bash
./gradlew npm_run_build
```
- and start the application
```bash
./gradlew bootRun
```


##  Main Entities
- Hospital (One-to-Many with Doctors)
- Doctor (Many-to-One with Hospital)
- Patient
- MedicalRecord (Many-to-One with Doctor and Patient)

---

##  Seeded Users

| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin123 | ADMIN |
| user     | user123  | USER  |
| admin2   | admin123 | ADMIN |
    

> These users can be used to log in during testing.

---

##  Authentication Access

-  [Public Page (Hospitals)](http://localhost:8080/hospitals) – accessible by anyone
-  [Patients Page](http://localhost:8080/patients) – requires login (ADMIN or USER)

---

##  Roles & Permissions

###  Unauthenticated Users:
- Can **view hospitals**.
- Cannot **view, edit, delete, or create** patients, doctors, or medical records.

### ️ USER (user):
- Can **view all patients**.
- Can **view hospitals**.
- Can **view doctors**.


###  ADMIN (admin):
- Can **view, add, edit, delete patients he created patients**.
- Can **access additional admin-only functionality**.
- Can **delete doctors**
---

##  Hidden Information for Unauthenticated Users

- Pages like [Patients](http://localhost:8080/patients) are not accessible.
- All `Edit` and `Delete` buttons are hidden unless the user is authenticated.
- Delete button is hidden for unauthorized users
---

##  User Relations

- A `User` is associated with the `Patient` entity as the **creator**.
- Only the **creator** of a patient (or an ADMIN) can **edit/delete** the patient.
- This logic is enforced both on **backend API level** and on the **frontend UI**.

---

## How to Build & Run
### Clone the Repository
```sh
https://gitlab.com/kdg-ti/programming-5/projects-24-25/acs202/rufina.chyhohidze/spring-backend.git
```

### Project Setup & Build Instructions
- Requires PostgreSQL database (configured via `docker-compose.yml`)
- Check database credentials before running



##  API Examples

### Week 2 - API Requests & Responses

####  Get all patients
```
GET http://localhost:8080/api/patients
```
- Response: 200 OK

####  Delete patient
```
DELETE http://localhost:8080/api/patients/P001
```
- 204 No Content if deleted
- 404 Not Found if patient not found

####  Search hospital
```
GET http://localhost:8080/api/hospitals?search=uptown
```
- 200 OK or 204 No Content

---

### Week 3 - Add & Update Patient

#### ➕ Add Patient
```
POST http://localhost:8080/api/patients
```
- 201 Created
- 400 Bad Request if invalid

####  Update patient billing or date
```
PATCH http://localhost:8080/api/patients/P001
```
- 204 No Content
- 404 Not Found
- 400 Validation Failed

---

##  Test API Calls

### Get all patients
```
GET http://localhost:8080/api/patients
Accept: application/json
```

### Delete a patient
```
DELETE http://localhost:8080/api/patients/P005
X-CSRF-TOKEN: <token>
Cookie: JSESSIONID=<session_id>
```

### Search hospital
```
GET http://localhost:8080/api/hospitals?search=uptown
Accept: application/json
```

### Add patient
```
POST http://localhost:8080/api/patients
Content-Type: application/json
X-CSRF-TOKEN: <token>
Cookie: JSESSIONID=<session_id>

{
  "firstName": "Test2",
  "lastName": "Patient",
  "age": 40,
  "gender": "MALE",
  "admissionDate": "2025-04-03",
  "billingAmount": 1234.56
}
```

### Update patient
```
PATCH http://localhost:8080/api/patients/P006
Content-Type: application/json
X-CSRF-TOKEN: <token>
Cookie: JSESSIONID=<session_id>

{
  "billingAmount": 5000,
  "admissionDate": "2025-03-01"
}
```

### Mocking Tests
These tests use `@MockitoBean` to isolate and mock dependencies:

- `PatientRestControllerUnitTest`
- `DoctorJpaDataServiceImplTest`

### Tests Using `verify(...)`
These tests assert that specific methods were called with expected arguments:

- `PatientRestControllerUnitTest`
- `DoctorJpaDataServiceImplTest`
- `DoctorJpaDataServiceImplUnitTest` 

## Client Project

### Week11

- Set up a separate project using npm, Webpack, Sass, and ESLint.
- Used SCSS with variables and nesting.
- Used Bootstrap and customized its colors.
- Made a single-page app with two sections: Search and Add.
- Wrote my own JavaScript to switch between sections (no Bootstrap JS).
- Added a form to update hospital details (sends PATCH request).
- Backend allows requests from this client (localhost:9000) and disables CSRF for testing.

### Bootstrap Icon Usage

- **Icon Added**: `bi-hospital-fill`
- **Where**: Home page title section
- **URL**: [http://localhost:8080/home](http://localhost:8080/home)
- **Source File**: `src/main/resources/templates/home.html`

```html
<h2 class="text-center mb-4 fw-bold">
  <i class="bi bi-hospital-fill me-2"></i>Overview of our Departments
</h2>
```

## Client-Side Form Validation

- **Form**: Register Form
- **Library**: [`joi`](https://www.npmjs.com/package/joi)
- **URL**: [http://localhost:8080/register](http://localhost:8080/register)
- **Source Files**:
    - JS validation: `src/main/js/register.js`
    - Page template: `src/main/resources/templates/register.html`

### Validation Rules:
- `username`: minimum 3 characters
- `password`: minimum 6 characters

---

## JavaScript Dependencies Added

| Package           | Purpose                          | File                                                                                | Demo URL                                                                                          |
|-------------------|----------------------------------|-------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `joi`             | Form validation                  | `src/main/js/register.js`                                                           | [http://localhost:8080/register](http://localhost:8080/register)                                  |
| `@motionone/dom`  | Animations (fade-in for results) | `src/main/js/search-hospitals.js` `src/main/js/docotrs.js``src/main/js/patients.js` | [http://localhost:8080/hospitals](http://localhost:8080/patients) [http://localhost:8080/doctors] |
| `dayjs`           | Date formatting                  | `src/main/js/search-hospitals.js`                                                   | [http://localhost:8080/hospitals](http://localhost:8080/hospitals)                                |
