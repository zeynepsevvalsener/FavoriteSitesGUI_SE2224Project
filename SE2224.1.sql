CREATE DATABASE FavoriteSitesDB;
USE FavoriteSitesDB;

CREATE TABLE userinfo (
    userid INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE visits (
    visitid INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    countryname VARCHAR(255),
    cityname VARCHAR(255),
    yearvisited INT,
    seasonvisited VARCHAR(50),
    bestfeature VARCHAR(255),
    comments VARCHAR(255),
    rating INT,
    FOREIGN KEY (username) REFERENCES userinfo(username)
);

CREATE TABLE sharedvisits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    friend_username VARCHAR(255),
    visitid INT,
    FOREIGN KEY (username) REFERENCES userinfo(username),
    FOREIGN KEY (visitid) REFERENCES visits(visitid)
);

INSERT INTO userinfo (username, password) VALUES ('root', 'root');
INSERT INTO userinfo (username, password) VALUES ('admin2', 'admin2');
