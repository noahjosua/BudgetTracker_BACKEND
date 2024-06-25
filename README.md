# Budget Tracker

Description
-----------
You can use this software to plan finances by creating income and expense entries according to what amount you spent, when you spent it and what you spent it on. 
The entries you create will be displayed as a list with all the details you added to give you a clear overview of your finances.
After creating several entries you will be able to see your total incomes and total expenses, as well as your current balance. 
By clicking on either of those buttons, you will toggle a income-only or expense-only list view. If you click on your total balance, the list will go back to displaying both kind of entries.
Additionally, you are able to delete and completely edit previously created entries.

Installation
------------

- JDK 21 -- to check your current java version open your computer's terminal and type in `java --version`
  - set the JAVA_HOME system variable to the directory where your JRE is installed, e.g.: `C:\Program Files\Java\jdk-21`
  

- H2 Database Engine v2.2.224 
  

- You'll need an IDE. For example: [VS Code](https://code.visualstudio.com/) or [IntelliJ IDE](https://www.jetbrains.com/idea/)


- Restart your Computer.


- Clone this Repository and open the Project in your IDE


Running the Application
-----------------------

- Run `BudgetTrackerApplication`. If you see a log output similar to this one `Started BudgetTrackerApplication in 5.131 seconds (process running for 6.059)` in the console everything works as expected!

- To use this application with an UI, you'll need to run the [frontend](https://github.com/noahjosua/BudgetTracker_FRONTEND) too.


Database
------------
The following is required if you want to look at the database the application will be using.
After the application has started successfully you can open your browser and go to [http://localhost:8080/h2-console](http://localhost:8080/h2-console). 

You need to configure a little bit before you can connect to the database: 
- Saved Settings: `Generic H2 (Embedded)`
- Setting Name: `Generic H2 (Embedded)`
- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:file:/data/budgettracker`
- User Name: `sa`
- Password: `password`


Documentation
------------
You can look at our interactive API Documentation while the application is running: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


Authors
-------
Noah-Josua Hartmann, Michelle Koops, Avery Lönker, Laura Siekierksi