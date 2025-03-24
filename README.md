# 📙 Question Paper Management System

A simple yet powerful application built using **Java Swing** and **MySQL (JDBC Connection)**. This project provides a user-friendly interface for managing question papers efficiently.

---

## 🌟 Features

- 🏠 **Dashboard**: Overview of the system and quick navigation to core functionalities.
- 🔐 **Login System**: Secure login for authorized users.
- 🖍️ **Question Paper Insertion**: Add new question papers to the system with ease.
- 🔍 **Search and Filter**: Locate question papers using advanced search and filter options.
- 🗃️ **Database Management**: Efficient storage and retrieval of data using MySQL.
- 🎨 **User-Friendly UI**: Built using Java Swing for an intuitive interface.

---

## 📁 Project Structure

```plaintext
src/qpm1/
├── 🗍 DashboardPage.java          # Dashboard interface and functionalities
├── 🗍 DatabaseManager.java        # Handles database connections and operations
├── 🗍 FilterAndOpenPage.java      # Search and filter questions
├── 🗍 InsertPaperPage.java        # Page to add question papers
├── 🗍 LoginPage.java              # User login interface
├── 🗍 QuestionPaperActions.java   # Actions related to question paper management
```

---

## 🛠️ Prerequisites

Before running the application, ensure you have the following:

- ✅ **Java Development Kit (JDK)** (version 8 or later)
- ✅ **MySQL Server**
- ✅ **NetBeans IDE** or any IDE that supports Java Swing

---

## 🚀 Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/username/Question_Paper_Management_System.git
   ```
2. Set up the MySQL database:
   - Create a database (e.g., `question_paper_db`).
   - Import the provided SQL script to initialize the database schema.
3. Update database configurations in `DatabaseManager.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/question_paper_db";
   private static final String DB_USER = "your_username";
   private static final String DB_PASSWORD = "your_password";
   ```
4. Run the project in your IDE.

---

## 🤝 Contributing

Contributions are welcome! Feel free to submit a pull request or open an issue for any suggestions or improvements.

---


