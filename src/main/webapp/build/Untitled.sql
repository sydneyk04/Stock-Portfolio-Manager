DROP DATABASE IF EXISTS PortfolioTracker;
CREATE DATABASE PortfolioTracker;

USE PortfolioTracker;

CREATE TABLE Users (
    email VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    portfolio VARCHAR(8000)
);
