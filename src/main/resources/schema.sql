CREATE TABLE USERS (
  name VARCHAR(50) NOT NULL,
  login VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,
  role VARCHAR(255),
  PRIMARY KEY (login)
  );