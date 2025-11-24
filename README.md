# MedConnect Hospital Management System

A Java-based desktop application built using **Swing**, **MySQL**, and **JDBC**. It manages hospital operations such as Patients, Doctors, Rooms, Ambulance, Medicine, Appointments, and Billing.

---

## 🔐 Admin Login Credentials

```
Username: admin
Password: admin123
```

---

## 🚀 Features

* Patient Management
* Doctor Management
* Medicine & Pharmacy
* Room Booking System
* Ambulance Booking
* Appointment Scheduling
* Billing & Invoice Generation
* GST Auto Calculation
* MySQL Database Integration
* Clean MVC Structure (Model, DAO, UI Pages)
* CRUD Operations for All Modules

---

## 📁 Project Structure

```
MedConnect/
│
├── src/medconnect/
│     ├── Dashboard.java
│     ├── LoginPage.java
│     ├── InvoicePage.java
│     ├── PatientPage.java
│     ├── DoctorPage.java
│     ├── MedicinePage.java
│     ├── RoomPage.java
│     ├── AmbulancePage.java
│     └── ...
│
├── src/medconnect/model/
│     ├── Patient.java
│     ├── Doctor.java
│     ├── Invoice.java
│     ├── Room.java
│     └── ...
│
├── src/medconnect/dao/
│     ├── DBConnection.java
│     ├── PatientDAO.java
│     ├── DoctorDAO.java
│     ├── InvoiceDAO.java
│     ├── RoomDAO.java
│     └── ...
│
└── README.md
```

---

## 🛢️ MySQL Database Setup

Create a database:

```sql
CREATE DATABASE medconnect;
USE medconnect;
```

Example billing table:

```sql
CREATE TABLE billing (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    appointment_id INT,
    amount DOUBLE,
    bill_date DATE,
    payment_status VARCHAR(20)
);
```

---

## 🛠️ How to Run

1. Install **IntelliJ IDEA** or **NetBeans**
2. Install **MySQL 8.0**
3. Add `mysql-connector-j.jar` to project libraries
4. Update DB credentials in:

```
src/medconnect/dao/DBConnection.java
```

5. Run:

```
LoginPage.java
```

---

## 📜 Invoice Generation (Billing)

* Auto GST (18%) Calculation
* Auto Total Calculation
* Auto Bill ID: `INV-XXXX`
* Stores invoice history in MySQL

---

## 📸 Screenshots

(Add your screenshots here)

---

## 👨‍💻 Developer

**Shikher Singh | Techz-Shikher**

