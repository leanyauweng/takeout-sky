# Takeout-sky A full-stack takeaway management system, including **Frontend (Vue 3 + Element Plus)** and **Backend (Spring Boot + MySQL)**. --- ## 📂 Project Structure takeout-sky/ ├── frontend/ # Vue 3 frontend │ ├── src/ │ ├── public/ │ └── package.json ├── backend/ # Spring Boot backend │ ├── src/ │ └── pom.xml └── README.md yaml Copy code --- ## 💻 Frontend - Built with **Vue 3**, **Element Plus**, and **Vite**. - Features: - Employee management - Menu & order management - Shopping cart - User login & profile - Install dependencies:
bash
cd frontend
npm install
Run development server:

bash
Copy code
npm run dev
Build for production:

bash
Copy code
npm run build
Preview built files:

bash
Copy code
npm run preview
🖥️ Backend
Built with Spring Boot and MySQL.

Provides RESTful APIs for managing:

Employees

Dishes

Set meals

Orders

User authentication

Configure database in application.yml:

yaml
Copy code
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/takeout_db?useSSL=false&serverTimezone=UTC
    username: root
    password: your_password
Run the backend server:

bash
Copy code
cd backend
mvn spring-boot:run
The server runs on http://localhost:8080 by default.

⚡ Notes
Make sure backend is running before starting the frontend.

API requests from frontend are configured to http://localhost:8080.

You can modify the frontend/src/api base URL if needed.

This project is for demo purposes and uses sample data.