DROP TABLE IF EXISTS income;

CREATE TABLE IF NOT EXISTS income
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    date_created DATE NOT NULL,
    date_planned DATE NOT NULL,
    category    VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount      DECIMAL(10, 2)
    );

/*
INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'SALARY', 'Test 1', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'POCKET_MONEY', 'Test 1', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'ALIMENT', 'Test 3', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'CAPITAL_ASSETS', 'Test 4', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'RENTAL', 'Test 5', 2.50);
*/

/* EXPENSE */

DROP TABLE IF EXISTS expense;

CREATE TABLE IF NOT EXISTS expense
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    date_created DATE NOT NULL,
    date_planned DATE NOT NULL,
    category    VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount      DECIMAL(10, 2)
    );

/*
INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'GROCERIES', 'Buch 1', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'DRUGSTORE', 'Buch 2', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'FREE_TIME', 'Buch 3', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'RENT', 'Buch 4', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURDATE(), CURDATE(), 'EDUCATION', 'Buch 5', 2.50);
*/
