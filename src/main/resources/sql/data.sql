-- Insert data into hospital table
INSERT INTO hospital ( established_date, hospital_address, hospital_name) VALUES
                                                                                 ('1985-06-15', '123 Main St, Cityville', 'Cityville General Hospital'),
                                                                                 ( '1995-09-23', '456 Oak St, Townsville', 'Townsville Medical Center'),
                                                                                 ( '2005-03-10', '789 Pine St, Villagetown', 'Villagetown Regional Hospital'),
                                                                                 ('2010-07-20', '101 Maple Ave, Metropolis', 'Metropolis Health Institute'),
                                                                                 ( '1980-11-05', '202 Elm Rd, Riverside', 'Riverside Community Hospital'),
                                                                                 ( '1992-04-18', '303 Birch Ln, Lakeview', 'Lakeview Medical Center'),
                                                                                 ( '1975-09-30', '404 Cedar Dr, Hilltop', 'Hilltop General Hospital'),
                                                                                 ( '2000-12-12', '505 Walnut St, Seaside', 'Seaside Regional Medical Center'),
                                                                                 ( '2015-08-25', '606 Spruce Ave, Downtown', 'Downtown Healthcare Facility'),
                                                                                 ( '1998-06-05', '707 Chestnut Blvd, Uptown', 'Uptown Medical Research Center');

-- Insert hospital departments
INSERT INTO hospital_departments (hospital_id, departments) VALUES
                                                                (1, 'CARDIOLOGY'), (1, 'NEUROLOGY'),
                                                                (2, 'SURGERY'), (2, 'ORTHOPEDICS'),
                                                                (3, 'PEDIATRICS'), (3, 'DENTISTRY'),
                                                                (4, 'RADIOLOGY'), (4, 'NEUROLOGY'),
                                                                (5, 'ORTHOPEDICS'), (5, 'CARDIOLOGY'),
                                                                (6, 'SURGERY'), (6, 'PEDIATRICS'),
                                                                (7, 'DENTISTRY'), (7, 'RADIOLOGY'),
                                                                (8, 'NEUROLOGY'), (8, 'CARDIOLOGY'),
                                                                (9, 'SURGERY'), (9, 'ORTHOPEDICS'),
                                                                (10, 'PEDIATRICS'), (10, 'DENTISTRY');

-- Insert admin user (creator for patients)
INSERT INTO users (id, username, password, user_role)
VALUES (1, 'admin', '$2a$12$ucFz4s/XXdApkjzjQK4T7.naea.W7PEZ/ey/GZIzMNgglH5fSq3C2', 'ADMIN'),
       (2,'doctor','$2a$12$jgW/Wg7IgD8anTcwMvxXdO8Nxlyzi/nhviq.yOe42CT4D/S/PEoai','DOCTOR');
-- 🔐 admin123
    --doctor123

-- Insert doctors
INSERT INTO doctors (hire_date, license_number, salary, hospital_id, department, first_name, gender, last_name) VALUES
                                                                                                                    ('2010-05-20', 1001, 120000.00, 1, 'CARDIOLOGY', 'Alice', 'FEMALE', 'Johnson'),
                                                                                                                    ('2012-08-15', 1002, 110000.00, 1, 'NEUROLOGY', 'Bob', 'MALE', 'Smith'),
                                                                                                                    ('2015-04-12', 1003, 130000.00, 2, 'SURGERY', 'Charlie', 'MALE', 'Brown'),
                                                                                                                    ('2018-11-03', 1004, 125000.00, 2, 'ORTHOPEDICS', 'Diana', 'FEMALE', 'Martinez'),
                                                                                                                    ('2020-06-22', 1005, 105000.00, 3, 'PEDIATRICS', 'Eva', 'FEMALE', 'Garcia'),
                                                                                                                    ('2021-09-10', 1006, 95000.00, 3, 'DENTISTRY', 'Frank', 'MALE', 'Davis');

-- Insert patients with creator_id = 1 (admin)
INSERT INTO patients (admission_date, age, billing_amount, hospital_id, first_name, gender, last_name, patient_id, creator_id)
VALUES
    ('2023-07-14', 45, 5000.00, 1, 'George', 'MALE', 'Anderson', 'P001', 1),
    ('2023-06-30', 30, 3000.00, 1, 'Hannah', 'FEMALE', 'Baker', 'P002', 1),
    ('2023-05-21', 60, 7000.00, 2, 'Ian', 'MALE', 'Clark', 'P003', 1),
    ('2023-09-10', 25, 2500.00, 2, 'Julia', 'FEMALE', 'Diaz', 'P004', 1),
    ('2023-08-05', 10, 1500.00, 3, 'Kevin', 'MALE', 'Evans', 'P005', 1),
    ('2023-07-25', 35, 4000.00, 3, 'Laura', 'FEMALE', 'Foster', 'P006', 1);

-- Insert medical records
INSERT INTO medical_record (doctor_license_number, treatment_date, diagnosis, patient_patient_id, treatment) VALUES
                                                                                                                 (1001, '2023-07-15', 'Hypertension', 'P001', 'Prescribed blood pressure medication and advised dietary changes'),
                                                                                                                 (1001, '2023-07-01', 'Migraine', 'P002', 'Recommended rest, hydration, and prescribed pain relievers'),
                                                                                                                 (1003, '2023-05-22', 'Fractured Arm', 'P003', 'Applied a cast and scheduled follow-up for healing assessment'),
                                                                                                                 (1004, '2023-09-11', 'Knee Injury', 'P004', 'Prescribed physical therapy and anti-inflammatory medication'),
                                                                                                                 (1005, '2023-08-06', 'Common Cold', 'P005', 'Recommended rest, fluids, and over-the-counter cold medicine'),
                                                                                                                 (1006, '2023-07-26', 'Tooth Decay', 'P006', 'Performed cavity filling and provided dental hygiene instructions'),
                                                                                                                 (1001, '2024-12-12', 'Migraine', 'P001', 'Recommended to spend less time behind computer!'),
                                                                                                                 (1005, '2024-12-10', 'Fever', 'P005', 'Prescribed painkillers and suggested some extra recommendations.');
