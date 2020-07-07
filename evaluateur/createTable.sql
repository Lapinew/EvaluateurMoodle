CREATE TABLE test (
    numero int NOT NULL AUTO_INCREMENT,
    nom varchar(25),
    age int,
    PRIMARY KEY (numero)
);

INSERT INTO test (nom, age) VALUES ('leo', 23);
INSERT INTO test (nom, age) VALUES ('eva', 20);