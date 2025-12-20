# MedConnect Hospital Management System

A Java-based desktop application built using **Swing**, **MySQL**, and **JDBC**. It manages hospital operations such as Patients, Doctors, Rooms, Ambulance, Medicine, Appointments, and Billing.

---

## 🔐 Login Credentials (Role-Based)

| Role          | Username        | Password     |
|---------------|----------------|------------|
| Admin         | admin          | admin123   |
| Doctor        | drsmith        | doc123     |
| Patient       | patient1       | patient123 |

> Each role has **specific access**:
> - **Admin:** Full access to all modules  
> - **Doctor:** View/edit appointments, patients, and EHR  
> - **Patient:** View personal appointments, prescriptions, and bills  

---

## 🚀 Features
**Desktop Swing Application:**
- Patient Management (CRUD)  
- Doctor Management  
- Electronic Health Records (EHR)  
- Medicine & Pharmacy  
- Room Booking System  
- Ambulance Booking  
- Appointment Scheduling  
- Billing & Invoice Generation (Auto GST 18%)  

**Web Servlet Modules (3 Pages):**
1. **LoginServlet** – Handles user authentication and role-based access  
2. **PatientServlet** – Manage patient records and EHR online  
3. **AppointmentServlet** – Schedule, update, and view appointments via web  

---

## 📁 Project Structure

```
MedConnect/
│
├── src/
│   ├── medconnect/
│   │   ├── Dashboard.java
│   │   ├── LoginPage.java
│   │   ├── Main.java
│   │   │
│   │   ├── PatientPage.java
│   │   ├── DoctorPage.java
│   │   ├── MedicinePage.java
│   │   ├── RoomPage.java
│   │   ├── AmbulancePage.java
│   │   ├── AppointmentPage.java
│   │   ├── InvoicePage.java
│   │   ├── EHRPage.java
│   │   │
│   │   ├── util/
│   │   │   ├── DBConnection.java
│   │   │   └── ValidationUtil.java
│   │   │
│   │   ├── model/
│   │   │   ├── Person.java
│   │   │   ├── Patient.java
│   │   │   ├── Doctor.java
│   │   │   ├── Medicine.java
│   │   │   ├── Room.java
│   │   │   ├── AmbulanceRequest.java
│   │   │   ├── Appointment.java
│   │   │   ├── Invoice.java
│   │   │   ├── EHR.java
│   │   │
│   │   ├── dao/
│   │   |    ├── PatientDAO.java
│   │   |    ├── DoctorDAO.java
│   │   |    ├── MedicineDAO.java
│   │   |    ├── RoomDAO.java
│   │   |    ├── AmbulanceDAO.java
│   │   |    ├── AppointmentDAO.java
│   │   |    ├── InvoiceDAO.java
│   │   |    └── UserDAO.java
│   │   |    ├── EHRDAO.java
│   │   ├── servlet/
│   │   │    ├── LoginServlet.java
│   │   │    ├── PatientServlet.java
│   │   │    └── AppointmentServlet.java
├── lib/
│   ├── mysql-connector-j-8.0.xx.jar
│
├── database/
│   ├── medconnect.sql
│
├── README.md
├── .gitignore



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



## 👨‍💻 Developer

**Shikher Singh | Techz-Shikher**

