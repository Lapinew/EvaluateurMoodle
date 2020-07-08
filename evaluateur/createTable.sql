CREATE TABLE test (
    numero int NOT NULL AUTO_INCREMENT,
    nom varchar(25),
    prenom varchar(25),
    age int,
    PRIMARY KEY (numero)
);

INSERT INTO test (nom, prenom, age) VALUES ('Alain', 'Térieur', 23);
INSERT INTO test (nom, prenom, age) VALUES ('Alain', 'Verse', 48);
INSERT INTO test (nom, prenom, age) VALUES ('Édith', 'Orial', 25);
INSERT INTO test (nom, prenom, age) VALUES ('Eva', 'Lavétoncul', 40);
INSERT INTO test (nom, prenom, age) VALUES ('Jean', 'Foupahune', 17);
INSERT INTO test (nom, prenom, age) VALUES ('Mick', 'Emmaus', 34);
INSERT INTO test (nom, prenom, age) VALUES ('Pierre', 'Kiroul', 56);
