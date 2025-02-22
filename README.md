# Programming 5 - Hospital Management System

## Project Overview
This is a **Spring Boot** application that manages **hospitals, doctors, patients, and medical records**. The application allows **CRUD operations** for all entities and includes **RESTful APIs** with dynamic UI updates using **JavaScript & Fetch API**.

### **How to Build & Run**
#### ** Clone the Repository**
```sh
https://gitlab.com/kdg-ti/programming-5/projects-24-25/acs202/rufina.chyhohidze/spring-backend.git
```
##  Course Information
- **Course Name**: Programming 5
- **Student Name**: Chyhohidze Rufina
- **KdG Email**: rufina.chyhohidze@student.kdg.be
- **Student ID**: 
- **Academic Year**: 2024-2025
- **Group**: ACS202
- 
## ** Project Setup & Build Instructions**
- PostgreSQL database (with correct schema)
- **To be able to run the application** Check the database credentials in docker-compose.yml file in root of the project

## **Main entities**
- Doctor
- Hospital One-to-Many with doctor
  - Medical Record (doctor (Many-to-One)
                   patient (Many-to-One))
- Patient

## **Week 2 - API Requests & Responses**
- ### To get all patients
```
GET http://localhost:8080/api/patients
```
- Fetching All Patients - status 200 OK
-  ### To delete a patient
```
DELETE http://localhost:8080/api/patients/P001
```
- Successful deletion - status 204
- Patient not found for deletion - status 404
- ### To search for a hospital
```
GET http://localhost:8080/api/hospitals?search=uptown
```
- Successful search - status 200
- No content - status 204 

## **Week 3 - Addition API and updating
- ### To add patient
```
POST http://localhost:8080/api/patients
```
- Successful addition - status 201
- Bad request - status 400
### To update patients billing amount or admission date
```
  PATCH http://localhost:8080/api/patients/P001
```
- Successful update - status 204
- Patient not found for update - status 404
- Bad request (not corresponds to validation) - status 400

