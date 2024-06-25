BUDGET TRACKER / PLANNER

Description
-----------
##############################





Installation
------------

BACKEND:

Requirements:
- JDK 21 -- to check your current java version open your computer's terminal and type in "java --version"
---> download: https://www.oracle.com/de/java/technologies/downloads/#java21

- H2 Database Engine v2.2.224
---> download: https://h2database.com/html/main.html

- You'll need an IDE. For example: VS Code, IntelliJ IDE, Webstorm...

- Type "npm install" in your console.



The following is required if you want to look at the database the application will be using.
After finishing the download, go to localhost:8080/h2-console. You can find the corresponding paths, the username and the password in the backend's src -> main -> resources -> application.properties, it should look like this:
Driver class: org.h2.Driver
JDBC URL: jdbc:h2:file:/data/budgettracker
User Name: sa
Password: password



---> make sure you restart your computer after installing!





Running the Application
-----------------------

To run the Backend:
- run the "BudgetTrackerV1Application" java file 

To run the Frontend:
- Type "ng serve --open" into the console. If everything has been installed correctly, your browser should open the Application at "localhost:4200"





Authors
-------
Noah-Josua Hartmann, Michelle Koops, Avery Lönker, Laura Siekierksi

