DROP TABLE IF EXISTS income;

CREATE TABLE IF NOT EXISTS income
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    date_created TIMESTAMP NOT NULL,
    date_planned TIMESTAMP NOT NULL,
    category    VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount      DECIMAL(10, 2) /* TODO muss frontend eingabe möglichkeit matchen */
    );

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SALARY', 'Test 1', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'POCKET_MONEY', 'Test 1', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ALIMENT', 'Test 3', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CAPITAL_ASSETS', 'Test 4', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'RENTAL', 'Test 5', 2.50);

/* EXPENSE */

DROP TABLE IF EXISTS expense;

CREATE TABLE IF NOT EXISTS expense
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    date_created TIMESTAMP NOT NULL,
    date_planned TIMESTAMP NOT NULL,
    category    VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount      DECIMAL(10, 2) /* TODO muss frontend eingabe möglichkeit matchen */
    );

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'GROCERIES', 'Buch 1', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'DRUGSTORE', 'Buch 2', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FREE_TIME', 'Buch 3', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'RENT', 'Buch 4', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'EDUCATION', 'Buch 5', 2.50);
