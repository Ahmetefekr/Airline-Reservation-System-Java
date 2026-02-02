# ✈️ Airline Reservation & Management System

> A comprehensive Java-based desktop application that simulates real-world airline operations, including flight scheduling, dynamic seat reservations, and concurrent booking management.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing/JavaFX](https://img.shields.io/badge/GUI-Swing%2FJavaFX-blue?style=for-the-badge)
![JUnit 5](https://img.shields.io/badge/Testing-JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![File I/O](https://img.shields.io/badge/Storage-.dat_Files-purple?style=for-the-badge)

## 📖 Project Overview
[cite_start]This project was developed as a **Capstone Course Project** to apply advanced **Object-Oriented Programming (OOP)** principles[cite: 360, 361]. [cite_start]The system handles multiple modules including Flight Management and Ticketing without using a traditional database, utilizing efficient **file-based persistence** with `.dat` files[cite: 404, 405].

## 👥 Contributors (The Team)
This project was collaboratively developed by:
* **[Senin Adın]** - *Backend Logic & Concurrency Management*
* **[Arkadaşının Adı]** ([@ArkadasininGithubKullaniciAdi](https://github.com/ArkadasininGithubKullaniciAdi)) - *Frontend GUI & Reporting Module*

## ⚙️ Key Features

### 1. Advanced Concurrency (Multithreading)
The system solves critical synchronization scenarios:
* [cite_start]**Simultaneous Seat Reservation:** Implements thread-safe logic to prevent "double-booking" when multiple users try to book the same seat simultaneously (simulated with 90 passengers/threads)[cite: 376, 377].
* [cite_start]**Asynchronous Reporting:** Generates heavy occupancy reports in background threads (`ReportGenerator Thread`) to keep the main GUI responsive[cite: 384, 386].

### 2. Core Modules
* [cite_start]**Flight Management:** Create/Edit flights, manage routes, and scheduling[cite: 364].
* [cite_start]**Smart Ticketing:** Dynamic pricing based on Business/Economy classes and baggage allowance[cite: 366, 369].
* [cite_start]**Visual Seat Selection:** Interactive GUI matrix representing the plane's seating capacity[cite: 379].

### 3. Quality Assurance
* [cite_start]**Unit Testing:** Contains JUnit 5 tests covering critical business logic like `PriceCalculation`, `FlightSearchEngine` and `SeatManager` exceptions[cite: 388, 390].

## 📸 Screenshots
| Login Screen | Seat Selection |
|:---:|:---:|
| <img src="screenshots/login.png" width="400" alt="Login"> | <img src="screenshots/seat.png" width="400" alt="Seat Map"> |

## 🛠️ Technical Architecture
* **Language:** Java 17+
* [cite_start]**GUI:** Swing / JavaFX[cite: 399].
* [cite_start]**Data Storage:** Custom File I/O using `.dat` files for secure data persistence[cite: 404].
* **Architecture:** Modular design with strict separation of concerns (Managers, Services, Models).

---
*Developed as part of the **BLM2012 Object Oriented Programming** course curriculum at Yildiz Technical University.*
