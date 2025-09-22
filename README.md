A Gemini Based Chatbot Platform

This is a chatbot platform that allows users to create an account, and converse with a chatbot powered by Gemini.

Features:
1) JWT-based Authentication with secure login/registration, User Account Management with password encryption, session management with token validation.
2) User can create multiple chatbot projects/agents.
3) The platform uses persistent H2 database to store data.
4) The platform has a user-friendly interface for users to interact with the chatbot.

Tech-Stack:
Backend
1) Java 17+ with Spring Boot 3.x
2) Spring Security with JWT authentication
3) Spring Data JPA for database operations
4) H2 database support
5) Maven for dependency management
6) Google Gemini API for AI responses

Frontend
HTML5 & CSS3, with JavaScript

Prerequisites:
1) Java 17 or higher
2) Maven 3.6+
3) Google Gemini API Key
4) Database - H2 for development, update to MySQL/PostgreSQL for production

Steps to Run the application (Using JAR)
1) Download the JAR file - chat-0.0.1-SNAPSHOT.jar 
2) Run the JAR
3) For frontend, in a browser go to - http://localhost:8080

Steps to run the application:
1) Import project as Maven project.
2) Update the application properties with your credentials.
3) Get API keys for Gemini from google AI studio.
4) mvn clean and update project.
5) Run the main application class.
6) Open http://localhost:8080 for the frontend.

