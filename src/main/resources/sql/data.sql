-- Insert hospitals
INSERT INTO hospital (hospital_name, hospital_address, established_date) VALUES
                                                                             ('City General Hospital', '123 Main St, Cityville', '1985-06-15'),
                                                                             ('Sunrise Medical Center', '456 Elm St, Townsville', '1995-09-10');

-- Insert hospital departments
INSERT INTO hospital_departments (hospital_id, departments) VALUES
                                                                (1, 'NEUROLOGY'),
                                                                (1, 'CARDIOLOGY'),
                                                                (2, 'PEDIATRICS'),
                                                                (2, 'SURGERY');

-- Insert doctors
INSERT INTO doctors (license_number, first_name, last_name, department, gender, salary, hire_date, hospital_id) VALUES
                                                                                                                    (1001, 'John', 'Doe', 'NEUROLOGY', 'MALE', 120000.00, '2010-05-20', 1),
                                                                                                                    (1002, 'Alice', 'Smith', 'CARDIOLOGY', 'FEMALE', 135000.00, '2012-08-15', 1),
                                                                                                                    (1003, 'Robert', 'Brown', 'PEDIATRICS', 'MALE', 110000.00, '2015-03-10', 2),
                                                                                                                    (1004, 'Emily', 'Johnson', 'SURGERY', 'FEMALE', 145000.00, '2008-11-30', 2);

-- Insert patients
INSERT INTO patients (patient_id, first_name, last_name, age, gender, billing_amount, admission_date, hospital_id) VALUES
                                                                                                                       ('P001', 'Michael', 'Wilson', 45, 'MALE', 5000.00, '2024-01-15', 1),
                                                                                                                       ('P002', 'Sophia', 'Davis', 30, 'FEMALE', 3200.00, '2024-02-05', 1),
                                                                                                                       ('P003', 'William', 'Martinez', 60, 'MALE', 7800.00, '2024-03-12', 2),
                                                                                                                       ('P004', 'Olivia', 'Garcia', 25, 'FEMALE', 2500.00, '2024-04-20', 2);

-- Associate doctors with patients
INSERT INTO doctor_patient (license_number, patient_id) VALUES
                                                            (1001, 'P001'),
                                                            (1002, 'P002'),
                                                            (1003, 'P003'),
                                                            (1004, 'P004'),
                                                            (1001, 'P002'),
                                                            (1003, 'P004');
