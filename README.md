BUDGET TRACKER / PLANNER

Description
-----------

You can use this software to plan finances by creating income and expense entries according to what amount you spent, when you spent it and what you spent it on. 
The entries you create will be displayed as a list with all the details you added to give you a clear overview of your finances.
After creating several entries you will be able to see your total incomes and total expenses, as well as your current balance. 
By clicking on either of those buttons, you will toggle a income-only or expense-only list view. If you click on your total balance, the list will go back to displaying both kind of entries.
Additionally, you are able to delete and completely edit previously created entries.




Installation
------------

BACKEND:

Requirements:
- JDK 21 -- to check your current java version open your computer's terminal and type in "java --version"
---> download: https://www.oracle.com/de/java/technologies/downloads/#java21

- H2 Database Engine v2.2.224
---> download: https://h2database.com/html/main.html

- You'll need an IDE. For example: VS Code, IntelliJ IDE, Webstorm...

- Type "npm install" in your console to ensure that you are not missing anything.

---> make sure you restart your computer after installing!



The following is required if you want to look at the database the application will be using.
After finishing the download, go to localhost:8080/h2-console. You can find the corresponding paths, the username and the password in ../src/main/resources/application.properties, it should look like this:
Driver class: org.h2.Driver
JDBC URL: jdbc:h2:file:/data/budgettracker
User Name: sa
Password: password




Running the Application
-----------------------

- run the "BudgetTrackerV1Application" java file 





Authors
-------
Noah-Josua Hartmann, Michelle Koops, Avery Lönker, Laura Siekierksi

