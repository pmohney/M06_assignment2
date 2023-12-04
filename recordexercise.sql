--Statements for creating compatible database

CREATE DATABASE IF NOT EXISTS recordexercise;

USE recordexercise;

CREATE TABLE IF NOT EXISTS Temp (
    num1 DOUBLE,
    num2 DOUBLE,
    num3 DOUBLE
);

CREATE USER 'scott'@'localhost' IDENTIFIED BY 'tiger';

GRANT ALL PRIVILEGES ON recordexercise.* TO 'scott'@'localhost';

FLUSH PRIVILEGES;
