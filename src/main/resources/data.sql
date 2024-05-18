DROP TABLE IF EXISTS income;

CREATE TABLE IF NOT EXISTS income
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    date_planned VARCHAR(255) NOT NULL,
    category    VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount      DECIMAL(10, 2) /* TODO muss frontend eingabe möglichkeit matchen */
);

INSERT INTO income (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Freizeit', 'Eis essen 1', 2.50);

INSERT INTO income (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Freizeit', 'Eis essen 2', 2.50);

INSERT INTO income (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Freizeit', 'Eis essen 3', 2.50);

INSERT INTO income (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Freizeit', 'Eis essen 4', 2.50);

INSERT INTO income (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Freizeit', 'Eis essen 5', 2.50);

/* EXPENSE */

DROP TABLE IF EXISTS expense;

CREATE TABLE IF NOT EXISTS expense
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    date_planned VARCHAR(255) NOT NULL,
    category    VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount      DECIMAL(10, 2) /* TODO muss frontend eingabe möglichkeit matchen */
);

INSERT INTO expense (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Lernen', 'Buch 1', 2.50);

INSERT INTO expense (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Lernen', 'Buch 2', 2.50);

INSERT INTO expense (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Lernen', 'Buch 3', 2.50);

INSERT INTO expense (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Lernen', 'Buch 4', 2.50);

INSERT INTO expense (date_planned, category, description, amount)
VALUES ('11.05.2024', 'Lernen', 'Buch 5', 2.50);
