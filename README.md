# Inventory Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-4B8BBE?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

</div>

<div align="center">
  <h3>A comprehensive JavaFX-based inventory management solution for modern businesses</h3>
  <p>Complete control over product inventory • Sales tracking • Financial analytics • Payroll management • User administration</p>
</div>




## Overview

The Inventory Management System is a desktop application built with JavaFX that streamlines business operations by integrating inventory control, sales processing, financial reporting, and employee management into a single platform. The system features an intuitive interface with real-time data visualization and robust database management.

**Key Highlights:**
- Full-featured inventory tracking with cost of goods sold (COGS) monitoring
- Point-of-sale system with multiple payment methods
- Comprehensive financial analytics with interactive charts
- Payroll calculator with percentage-based and fixed-amount options
- Multi-user support with role-based access
- Modern, responsive UI with clean design

---

## Core Features

### Authentication & Security
Modern login interface with user registration, password protection, and role-based access control. New users can create accounts with position and hourly wage information stored securely in the database.

<p align="center">
 <img width="701" height="803" alt="Screenshot 2026-02-05 at 14 58 15" src="https://github.com/user-attachments/assets/e2f669e5-0db7-4347-9fc2-eff1ccb1f973" />
</p>


### Product Management
Add, update, and delete products with full inventory tracking. Each product includes pricing, cost of goods sold, quantity monitoring, and station assignment. The system displays real-time stock status and supports bulk operations.

<p align="center">
  <img width="800" alt="Screenshot 2026-02-05 at 15 00 36" src="https://github.com/user-attachments/assets/331fe49b-4651-48a8-bf97-7450f6fe8a5b" />

</p>

<p align="center">
  <img width="800" alt="Screenshot 2026-02-05 at 15 00 24" src="https://github.com/user-attachments/assets/1b446044-d4fd-4093-86ed-39c95151a6c9" />

</p>

### Sales Management
Streamlined point-of-sale interface with product selection, automatic tax calculation, and support for cash, credit card, and debit card payments. The system generates unique invoice numbers and maintains complete sales history.

<p align="center">
<img width="372" height="884" alt="Screenshot 2026-02-05 at 14 59 32" src="https://github.com/user-attachments/assets/915e9ee0-7600-4f25-9a05-77eddc3f5117" />

</p>

Real-time sales dashboard with revenue tracking, trend analysis over 7 days, and payment method breakdowns. Sales data is presented through interactive charts showing daily trends and payment distribution.

<p align="center">
<img width="800"  alt="Screenshot 2026-02-05 at 14 58 39" src="https://github.com/user-attachments/assets/1a3f907d-cbb7-4b22-a876-1f76901ccf23" />

</p>

<p align="center">
<img width="800" alt="Screenshot 2026-02-05 at 15 00 11" src="https://github.com/user-attachments/assets/87506921-50fe-47ce-a5c2-5c2f88de382b" />

</p>

### Financial Analytics
Comprehensive financial overview with key performance indicators including total revenue, profit, profit margin, COGS, and operating expenses. The finance module provides multiple visualization options for business intelligence.

<p align="center">
<img width="800" alt="Screenshot 2026-02-05 at 15 00 57" src="https://github.com/user-attachments/assets/aa8f1831-43e6-4801-9fd4-c4f91b8378d2" />

</p>

Advanced charting capabilities include:
- 30-day revenue trend analysis with profit tracking
- Revenue breakdown showing COGS, operating expenses, and profit distribution
- Weekly revenue vs profit comparison with expense tracking
- Top-selling products with color-coded rankings (gold, silver, bronze for top 3)

<p align="center">
<img width="800" alt="Screenshot 2026-02-05 at 15 01 11" src="https://github.com/user-attachments/assets/5c1c0b1e-d376-4148-8e8b-a68d4c62fc8e" />

</p>

### Payroll Management
Intelligent payroll calculator supporting both percentage-based and fixed-amount payment methods. The system calculates payroll costs based on revenue and displays comprehensive financial breakdowns with interactive pie charts showing COGS, payroll, and net profit distribution.

<p align="center">
<img width="800" alt="Screenshot 2026-02-05 at 15 01 23" src="https://github.com/user-attachments/assets/f077c513-0cbf-49e6-b74e-a6c1466d4fcd" />

</p>

### User Administration
Complete user management system allowing administrators to add employees with position assignments and hourly wage rates. The interface displays all users with their credentials, roles, and compensation information in an organized table format.

<p align="center">
<img width="800" alt="Screenshot 2026-02-05 at 15 01 33" src="https://github.com/user-attachments/assets/bba45f09-9e8f-4b16-9c4c-7bca294896d5" />

</p>

### Settings
Configurable tax rate settings that apply system-wide to all sales transactions. The settings panel provides a clean interface for adjusting business parameters that affect financial calculations throughout the application.

<p align="center">
<img width="800" alt="Screenshot 2026-02-05 at 15 01 44" src="https://github.com/user-attachments/assets/b63f1804-55d7-450d-bfd7-5dd7a2e61221" />

</p>

---

## Technology Stack

**Frontend:**
- JavaFX 21.0.6 for modern UI components
- Custom CSS styling for consistent design language
- Interactive charts (LineChart, PieChart, BarChart) for data visualization

**Backend:**
- Java with JDBC for database connectivity
- SQLite/MySQL for data persistence
- BigDecimal for precise financial calculations

**Architecture:**
- MVC design pattern for code organization
- Modular structure with separate packages for database, GUI, and business objects
- Event-driven architecture for responsive user interactions

---

## Database Schema

The system uses a relational database with the following core tables:

**Users Table:**
- User credentials and authentication
- Position and hourly wage information
- Role-based access control data

**Products Table:**
- Product information (name, price, COGS, quantity)
- Station assignments for organization
- Stock status tracking

**Sales Table:**
- Transaction records with invoice numbers
- Revenue and profit calculations
- Payment method tracking
- Timestamp for trend analysis

**SaleItems Table:**
- Individual line items for each sale
- Product-sale relationships
- Quantity and pricing details

**Settings Table:**
- System-wide configuration (tax rates)
- Business parameter storage

<p align="center">
<img width="484" height="817" alt="Screenshot 2026-02-05 at 15 15 56" src="https://github.com/user-attachments/assets/78e7fb12-302e-4aae-94a5-6629abecce7d" />

</p>

---

## Installation & Setup

**Prerequisites:**
- Java Development Kit (JDK) 17 or higher
- JavaFX SDK 21.0.6
- Maven for dependency management
- Database server (SQLite for development, MySQL for production)

**Build Instructions:**

Clone the repository and navigate to the project directory:
```bash
git clone https://github.com/yourusername/inventory-management-system.git
cd inventory-management-system
```

Build the project using Maven:
```bash
mvn clean install
```

Run the application:
```bash
mvn javafx:run
```

**Database Configuration:**

The application will automatically create the necessary database tables on first run. Update the database connection settings in `DatabaseConfig.java` if using a different database server.

---

## Usage Guide

**First Time Setup:**

1. Launch the application and create an admin account using the registration interface
2. Configure the tax rate in Settings based on your business location
3. Add products to the inventory with pricing and COGS information
4. Add employee users with their position and wage details

**Daily Operations:**

1. Process sales through the Sales Management interface
2. Monitor inventory levels in the Products section
3. Review financial performance in the Finance dashboard
4. Calculate payroll costs using the Payroll module
5. Manage user accounts and permissions as needed

---

## Project Structure

```
src/main/java/com/example/javafx_inventoryms/
├── db/
│   ├── DatabaseConfig.java
│   └── DatabaseOperations.java
├── gui/
│   ├── Finance/
│   │   ├── Finance.java
│   │   └── Payroll.java
│   ├── Products/
│   │   ├── UpdateDeleteProducts.java
│   │   └── ViewAddProducts.java
│   ├── Sales/
│   │   ├── SalesPage.java
│   │   └── SpecialSale.java
│   ├── Users/
│   │   └── UsersPage.java
│   ├── Home.java
│   ├── LoginPage.java
│   └── Settings.java
├── objects/
│   ├── Product.java
│   ├── Sale.java
│   ├── SaleItem.java
│   └── User.java
└── Main.java

src/main/resources/com/example/javafx_inventoryms/styles/
├── login.css
├── menu.css
└── products.css
```

---

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/enhancement`)
3. Commit your changes with clear messages
4. Push to your branch (`git push origin feature/enhancement`)
5. Open a Pull Request with a detailed description

Please ensure all code follows the existing style conventions and includes appropriate documentation.

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## Acknowledgments

This project was developed as a comprehensive solution for small business inventory and sales management. Special thanks to the JavaFX community for excellent documentation and resources.

---

## Contact & Support

For questions, issues, or feature requests, please open an issue on GitHub or contact the development team.

**Project Link:** [https://github.com/yourusername/inventory-management-system](https://github.com/yourusername/inventory-management-system)
