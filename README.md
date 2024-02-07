<h1 align="center">Learning Management System</h1>

## Description

University of Los Angeles management system.  
Backend is done with microservice architecture, frontend is in Angular 9.  
There are five roles in total.  
Administrator manages university information, users, faculties and study programs.  
Teacher manages Exams, Courses and Student's grades.  
Student can see it's grades, exams, courses that he is enrolled in, chat with others.  
Content creator manages university events, research works and news.  
University staff works with university-student related information.


## Application Architecture

<img src="https://user-images.githubusercontent.com/17277467/95520216-0f9cac80-09c7-11eb-8532-76bc75f5d815.png">

## How to run

@TODO

### Technologies and frameworks

-   SpringBoot
-   Zuul Proxy
-   Eureka Service Discovery
-   Hystrix Circuit Breaker
-   MySQL
-   MongoDB
-   Angular 9 with ngrx
-   Sass
-   Python (code-generation) - [SpringLeaf](https://github.com/OMKE/SpringLeaf)


### RUN FE project
To run the project outlined in the provided package.json file, you'll need to follow these steps:

Install Node.js and npm:
Ensure that Node.js and npm (Node Package Manager) are installed on your system. You can download and install them from the official Node.js website: Node.js Downloads.

Install Angular CLI:
The project is an Angular application, so you need to have Angular CLI (Command Line Interface) installed globally on your system. You can install it via npm using the following command:

css
Copy code
npm install -g @angular/cli@latest
Install project dependencies:
Navigate to the project directory (kadem-projet) using the terminal or command prompt and run the following command to install the project dependencies:

Copy code
npm install
Run the project:
Once the dependencies are installed, you can start the development server by running the following command:

sql
Copy code
npm start
This command will build the project and start a development server. By default, the server runs on http://localhost:4200/.

Access the application:
Open a web browser and navigate to http://localhost:4200/ to view the running application.


### Design resources

[Figma project](https://www.figma.com/file/qc1DDLrPyskpR015t6c3Q1/University-of-Los-Angeles-LMS?node-id=0%3A1) <br>
[Figma prototype](https://www.figma.com/proto/qc1DDLrPyskpR015t6c3Q1/University-of-Los-Angeles-LMS?node-id=0%3A1)

### Documentation

[University of Los Angeles - API](https://documenter.getpostman.com/view/6089658/TVKFzGJv)

