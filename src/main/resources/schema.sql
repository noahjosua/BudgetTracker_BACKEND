DROP TABLE IF EXISTS income;

CREATE TABLE IF NOT EXISTS income
(
    id           SERIAL PRIMARY KEY,
    date_created DATE NOT NULL,
    date_planned DATE NOT NULL,
    category    VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount      DECIMAL(10, 2) /* TODO muss frontend eingabe möglichkeit matchen */
    );

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'SALARY', 'Test 1', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'POCKET_MONEY', 'Test 1', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'ALIMENT', 'Test 3', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'CAPITAL_ASSETS', 'Test 4', 2.50);

INSERT INTO income (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'RENTAL', 'Test 5', 2.50);

/* EXPENSE */

DROP TABLE IF EXISTS expense;

CREATE TABLE IF NOT EXISTS expense
(
    id           SERIAL PRIMARY KEY,
    date_created DATE NOT NULL,
    date_planned DATE NOT NULL,
    category    VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount      DECIMAL(10, 2) /* TODO muss frontend eingabe möglichkeit matchen */
    );

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'GROCERIES', 'Buch 1', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'DRUGSTORE', 'Buch 2', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'FREE_TIME', 'Buch 3', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'RENT', 'Buch 4', 2.50);

INSERT INTO expense (date_planned, date_created, category, description, amount)
VALUES (CURRENT_DATE, CURRENT_DATE, 'EDUCATION', 'Buch 5', 2.50);
