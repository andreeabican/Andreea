DROP DATABASE Grupa341C4_BadoiuSimona;
CREATE DATABASE Grupa341C4_BadoiuSimona;

USE Grupa341C4_BadoiuSimona;

CREATE TABLE utilizatori (
	idutilizator	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	CNP		        BIGINT(13) UNSIGNED NOT NULL,
	nume	    	VARCHAR(30) NOT NULL,
	prenume 		VARCHAR(30) NOT NULL,
	adresa	    	VARCHAR(100) NOT NULL,
	telefon 		INT(10),
	email	    	VARCHAR(60) NOT NULL,
	IBAN	    	VARCHAR(30) NOT NULL,
	tip		        VARCHAR(30) NOT NULL DEFAULT 'client',
	numeutilizator 	VARCHAR(30) NOT NULL,
	parola	    	VARCHAR(30) NOT NULL,
	cont	    	DECIMAL(10,2) UNSIGNED NOT NULL DEFAULT 5000
);
ALTER TABLE utilizatori ADD CONSTRAINT email_chk CHECK (email LIKE '%@%.%');
ALTER TABLE utilizatori ADD CONSTRAINT tip_chk CHECK (tip IN ('administrator', 'client', 'consilier-tehnic', 'reprezentant-vanzari'));

CREATE TABLE cerere_cont_utilizator (
    idcont      	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	CNP		        BIGINT(13) UNSIGNED NOT NULL,
	nume	    	VARCHAR(30) NOT NULL,
	prenume 		VARCHAR(30) NOT NULL,
	adresa	    	VARCHAR(100) NOT NULL,
	telefon 		INT(10),
	email	    	VARCHAR(60) NOT NULL,
	IBAN	    	VARCHAR(30) NOT NULL,
	tip		        VARCHAR(30) NOT NULL DEFAULT 'client',
	numeutilizator 	VARCHAR(30) NOT NULL,
	parola	    	VARCHAR(30) NOT NULL
);
ALTER TABLE cerere_cont_utilizator ADD CONSTRAINT email_chk CHECK (email LIKE '%@%.%');

/*CREATE TABLE produse (
	idprodus	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	denumire	VARCHAR(100) NOT NULL,
	descriere	VARCHAR(300) NOT NULL,
	pret		DECIMAL(10, 2) UNSIGNED NOT NULL
);*/

CREATE TABLE proiecte (
	idproiect	    INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	denumire	    VARCHAR(30) NOT NULL,
	descriere	    VARCHAR(100) NOT NULL,
    rating          INT(10) UNSIGNED DEFAULT 0,
    nrvoturi        INT(10) UNSIGNED DEFAULT 0
);

CREATE TABLE facturi (
	idfactura	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	nrfactura	VARCHAR(10) NOT NULL,
	data		DATE NOT NULL,
	total		DECIMAL(10, 2) NOT NULL,
	statut 		VARCHAR(30) DEFAULT 'emisa',
	data_scadenta	DATE NOT NULL,
	idutilizator	INT(10) UNSIGNED NOT NULL,
	FOREIGN KEY (idutilizator) REFERENCES utilizatori(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE randfactura (
	idrand		INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idproiect	INT(10) UNSIGNED NOT NULL,
	idfactura	INT(10) UNSIGNED NOT NULL,
	cantitate	INT(5)	UNSIGNED NOT NULL,
    FOREIGN KEY (idproiect) REFERENCES proiecte(idproiect) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (idfactura) REFERENCES facturi(idfactura) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE venit (
	idvenit		INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idprodus	INT(10) UNSIGNED NOT NULL,
	venit_total	DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (idprodus) REFERENCES proiecte(idproiect) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE dificultati_tehnice (
	iddificultate	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idproiect	INT(10) UNSIGNED NOT NULL,
	idfactura	INT(10) UNSIGNED NOT NULL,
	descriere	VARCHAR(200) NOT NULL,
    FOREIGN KEY (idproiect) REFERENCES proiecte(idproiect) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (idfactura) REFERENCES facturi(idfactura) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE mesaje (
	idmesaj		INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	subiect		VARCHAR(30) NOT NULL,
	continut	VARCHAR(300) NOT NULL,
	destinatar	VARCHAR(30) NOT NULL,
	expeditor	VARCHAR(30) NOT NULL,
	data		DATE NOT NULL,
	status		VARCHAR(10) DEFAULT 'necitit',
	idprodus	INT(10) UNSIGNED NOT NULL,
	idfactura	INT(10) UNSIGNED NOT NULL,
    FOREIGN KEY (idprodus) REFERENCES proiecte(idproiect) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (idfactura) REFERENCES facturi(idfactura) ON UPDATE CASCADE ON DELETE CASCADE
);

# asocieri echipa angajat proiect -> de aici se scot datele
# finale ale produselor comercializate

CREATE TABLE departamente (
	iddepartament	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	denumire	VARCHAR(30) NOT NULL
);

CREATE TABLE functii (
	idfunctie	INT(10)	UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	iddepartament	INT(10) UNSIGNED NOT NULL,
	denumire	VARCHAR(30),
	descriere	VARCHAR(100),
	FOREIGN KEY (iddepartament) REFERENCES departamente(iddepartament) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE angajati (
    idutilizator        INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
    CNP                 BIGINT(13) NOT NULL,
    nume                VARCHAR(30) NOT NULL,
    prenume             VARCHAR(30) NOT NULL,
    adresa              VARCHAR(100) NOT NULL,
    telefon             INT(10),
    email               VARCHAR(60) NOT NULL,
    IBAN                VARCHAR(34) NOT NULL,
    nrcontract          INT(10) NOT NULL,
    dataangajarii       DATE NOT NULL,
    tip                 VARCHAR(30) NOT NULL DEFAULT 'angajat',
    zileconcediuramase	INT(10) NOT NULL DEFAULT 15,
    numarorecontract    INT(10) NOT NULL,     
    salariunegociat     INT(10) NOT NULL,
    numeutilizator      VARCHAR(30) NOT NULL,
    parola              VARCHAR(30) NOT NULL
);
ALTER TABLE angajati ADD CONSTRAINT email_chk CHECK (email LIKE '%@%.%');
ALTER TABLE angajati ADD CONSTRAINT tip_chk CHECK (tip IN ('administrator', 'angajat', 'super-administrator'));

CREATE TABLE echipe (
	idechipa	    INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idproiect	    INT(10) UNSIGNED NOT NULL,
	idresponsabil	INT(10) UNSIGNED NOT NULL,
	FOREIGN KEY (idproiect) REFERENCES proiecte(idproiect) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (idresponsabil) REFERENCES utilizatori(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE asociereechipaangajat (
	idasociere	    INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idechipa	    INT(10) UNSIGNED NOT NULL,
	idutilizator	INT(10) UNSIGNED NOT NULL,
	datastart	    DATE NOT NULL,
	dataincheiere	DATE NOT NULL,
	FOREIGN KEY (idechipa) REFERENCES echipe(idechipa) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (idutilizator) REFERENCES utilizatori(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE asociereutilizatorfunctie (
	idasociere	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idutilizator	INT(10) UNSIGNED NOT NULL,
	idfunctie	INT(10) UNSIGNED NOT NULL,
	FOREIGN KEY (idutilizator) REFERENCES angajati(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (idfunctie) REFERENCES functii(idfunctie) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1220713379378', 'ZOTA', 'Daniel', '-', '0', 'zota.daniel@google.com', 'RO49 AAAA 1B31 0075 9384 0000', 'administrator', 'zota.daniel', '-', 0);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1160121873449', 'NEGREANU', 'Mircea', '-', '0', 'negreanu.mircea@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'negreanu.mircea', '-', 1000);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1560330668692', 'ROGOBETE', 'Mircea', '-', '0', 'rogobete.mircea@live.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'rogobete.mircea', '-', 100);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '2981007225570', 'GIUMALE', 'Petre', '-', '0', 'giumale.petre@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', 'administrator', 'giumale.petre', '-', 0);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1350615180258', 'DRAGUSIN', 'Camelia', '-', '0', 'dragusin.camelia@live.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'dragusin.camelia', '-', 2500);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1860926240323', 'IONESCU', 'Tudor', '-', '0', 'ionescu.tudor@google.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client' , 'ionescu.tudor', '-', 120);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1921202602499', 'GEORGESCU', 'Monica', '-', '0', 'georgescu.monica@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'georgescu.monica', '-', 12000);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1320810148322', 'POPA', 'Monica', '-', '0', 'popa.monica@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', 'consilier-tehnic', 'popa.monica',  '-', 100);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '2390831245242', 'CHIVU', 'Camelia', '-', '0', 'chivu.camelia@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', 'reprezentant-vanzari', 'chivu.camelia', '-', 200);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1400515716087', 'POPA', 'Cristina', '-', '0', 'popa.cristina@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', 'consilier-tehnic', 'popa.cristina', '-', 200);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '2740606757054', 'VASILESCU', 'Radu', '-', '0', 'vasilescu.radu@google.com', 'RO49 AAAA 1B31 0075 9384 0000', 'reprezentant-vanzari', 'vasilescu.radu', '-', 150);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1181216988226', 'POPESCU', 'Ioana', '-', '0', 'popescu.ioana@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'popescu.ioana', '-', 1500);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1331019521959', 'VOITAN', 'Otilia', '-', '0', 'voitan.otilia@live.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'voitan.otilia', '-', 10000);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '2740822491303', 'NEGREANU', 'Corina', '-', '0', 'negreanu.corina@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'negreanu.corina', '-', 1678.3);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1330818720833', 'RUSU', 'Teodora', '-', '0', 'rusu.teodora@live.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'rusu.teodora', '-', 1000);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1621205967255', 'NECULA', 'Cristina', '-', '0', 'necula.cristina@oracle.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'necula.cristina', '-', 670.2);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '2430329797024', 'SERBAN', 'David', '-', '0', 'serban.david@live.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'serban.david', '-', 2200);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '2910414242990', 'DUMITRESCU', 'Mircea', '-', '0', 'dumitrescu.mircea@live.com', 'RO49 AAAA 1B31 0075 9384 0000', 'administrator', 'dumitrescu.mircea', '-', 600);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '2820915291696', 'IONASCU', 'Tudor', '-', '0', 'ionascu.tudor@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'ionascu.tudor', '-', 18000);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1130423330580', 'NECULA', 'Ana Maria', '-', '0', 'necula.ana maria@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'necula.ana maria', '-', 1430.30);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1970809923368', 'SPIRIDON', 'Mihai', '-', '0', 'spiridon.mihai@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'spiridon.mihai', '-', 2222.22);

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, tip, numeutilizator, parola, cont)
 VALUES(DEFAULT, '1150928544482', 'SERBAN', 'David', '-', '0', 'serban.david@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', 'client', 'serban.david', '-', 200.2);

#populeaza tabelele departamente si functii
INSERT INTO departamente(iddepartament, denumire)
 VALUES (1, 'HR');
INSERT INTO departamente(iddepartament, denumire)
 VALUES (2, 'contabilitate');
INSERT INTO departamente(iddepartament, denumire)
 VALUES (3, 'programare');
INSERT INTO departamente(iddepartament, denumire)
 VALUES (4, 'asigurarea calitatii');
#INSERT INTO departamente(iddepartament, denumire)
# VALUES (5, 'administratie');

INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 1, 'manager resurse umane', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 2, 'sef departament facturare', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 2, 'contabil', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 2, 'contabil incepator', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 3, 'programator', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 3, 'manager proiect', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 3, 'dezvoltator C++', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 3, 'dezvoltator Java', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 3, 'programator incepator', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 4, 'controlor calitate', '-');
INSERT INTO functii(idfunctie, iddepartament, denumire, descriere)
 VALUES(DEFAULT, 4, 'controlor calitate incepator', '-');


# populeaza tabela angajati
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1220713379378', 'ZOTA', 'Daniel', '-', '0', 'zota.daniel@google.com', 'RO49 AAAA 1B31 0075 9384 0000', '1', 'angajat', '40', '4000', 'zota.daniel', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1160121873449', 'NEGREANU', 'Mircea', '-', '0', 'negreanu.mircea@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '2', 'angajat', '40', '3000', 'negreanu.mircea', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1560330668692', 'ROGOBETE', 'Mircea', '-', '0', 'rogobete.mircea@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '3', 'super-administrator', '40', '4000', 'rogobete.mircea', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2981007225570', 'GIUMALE', 'Petre', '-', '0', 'giumale.petre@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '4', 'administrator', '20', '2000', 'giumale.petre', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1350615180258', 'DRAGUSIN', 'Camelia', '-', '0', 'dragusin.camelia@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '5', 'administrator', '40', '5000', 'dragusin.camelia', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1860926240323', 'IONESCU', 'Tudor', '-', '0', 'ionescu.tudor@google.com', 'RO49 AAAA 1B31 0075 9384 0000', '6', 'administrator', '40', '1200', 'ionescu.tudor', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1921202602499', 'GEORGESCU', 'Monica', '-', '0', 'georgescu.monica@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '7', 'angajat', '40', '3000', 'georgescu.monica', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1320810148322', 'POPA', 'Monica', '-', '0', 'popa.monica@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '8', 'super-administrator', '40', '3000', 'popa.monica',  '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2390831245242', 'CHIVU', 'Camelia', '-', '0', 'chivu.camelia@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '9', 'super-administrator', '40', '4000', 'chivu.camelia', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1400515716087', 'POPA', 'Cristina', '-', '0', 'popa.cristina@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '10', 'super-administrator', '40', '2000', 'popa.cristina', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2740606757054', 'VASILESCU', 'Radu', '-', '0', 'vasilescu.radu@google.com', 'RO49 AAAA 1B31 0075 9384 0000', '11', 'angajat', '40', '3300', 'vasilescu.radu', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1181216988226', 'POPESCU', 'Ioana', '-', '0', 'popescu.ioana@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '12', 'angajat', '40', '4000', 'popescu.ioana', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1331019521959', 'VOITAN', 'Otilia', '-', '0', 'voitan.otilia@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '13', 'angajat', '40', '4000', 'voitan.otilia', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2740822491303', 'NEGREANU', 'Corina', '-', '0', 'negreanu.corina@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '14', 'angajat', '40', '4000', 'negreanu.corina', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1330818720833', 'RUSU', 'Teodora', '-', '0', 'rusu.teodora@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '15', 'angajat', '40', '2200', 'rusu.teodora', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1621205967255', 'NECULA', 'Cristina', '-', '0', 'necula.cristina@oracle.com', 'RO49 AAAA 1B31 0075 9384 0000', '16', 'angajat', '40', '4000', 'necula.cristina', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2430329797024', 'SERBAN', 'David', '-', '0', 'serban.david@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '17', 'angajat', '40', '4000', 'serban.david', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2910414242990', 'DUMITRESCU', 'Mircea', '-', '0', 'dumitrescu.mircea@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '18', 'administrator', '40', '3000',  'dumitrescu.mircea', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2820915291696', 'IONASCU', 'Tudor', '-', '0', 'ionascu.tudor@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '19', 'angajat', '40', '3300', 'ionascu.tudor', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1130423330580', 'NECULA', 'Ana Maria', '-', '0', 'necula.ana maria@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '20', 'angajat', '20', '4000', 'necula.ana maria', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1970809923368', 'SPIRIDON', 'Mihai', '-', '0', 'spiridon.mihai@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '21', 'angajat', '40', '4000', 'spiridon.mihai', '-', '2008-05-10');
INSERT INTO angajati (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1150928544482', 'SERBAN', 'David', '-', '0', 'serban.david@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '22', 'angajat', '40', '4000', 'serban.david', '-', '2008-05-10');

#asociere utilizator functie
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 1, 1);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 2, 2);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 7, 3);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 12, 4);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 13, 5);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 14, 6);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 15, 7);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 16, 8);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 17, 9);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 19, 10);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 20, 11);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 21, 1);
INSERT INTO asociereutilizatorfunctie (idasociere, idutilizator, idfunctie)
 VALUES(DEFAULT, 22, 2);

#proiecte

INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect1', 'descriere1 este bun frumos terminat ce bine ca e bine');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect2', 'descriere2');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect3', 'descriere3');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect4', 'descriere4 un proiect viitor foarte reusit cumparati');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect5', 'descriere5');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect6', 'descriere6');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect7', 'descriere7');

INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 1, 13);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 1, 13);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 2, 14);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 2, 14);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 3, 15);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 3, 15);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 4, 16);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 4, 16);

INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 1, 13, '2013-10-5', '2013-12-7');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 1, 17, '2013-10-25', '2013-12-27');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 2, 19, '2013-10-7', '2013-12-20');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 3, 14, '2013-09-08', '2013-12-12');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 4, 20, '2013-09-08', '2013-10-22');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 5, 15, '2013-10-15', '2013-10-30');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 6, 19, '2013-12-21', '2013-12-30');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 7, 16, '2013-10-15', '2013-10-30');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 8, 20, '2013-10-23', '2014-02-20');

/* functii si proceduri */

/* intoarce data la care se termina proiectul cu idul primit ca parametru */
DELIMITER //
CREATE FUNCTION dataSfarsitProiect(idproiect INT)
    RETURNS DATE
BEGIN
DECLARE dataIncheiere DATE;

select max(asoc.dataincheiere) into dataIncheiere
from 
    proiecte p, 
    angajati a, 
    echipe e, 
    asociereechipaangajat asoc 
where 
    e.idproiect = p.idproiect and 
    p.idproiect = idproiect and 
    asoc.idutilizator = a.idutilizator and 
    asoc.idechipa = e.idechipa;

RETURN dataIncheiere;
END; //
DELIMITER ;

/* intoarce cate zile s-a lucrat la un anumit proiect */
DELIMITER //
CREATE FUNCTION durataProiect(idproiect INT)
    RETURNS INT
BEGIN
DECLARE durataProiect INT;

select datediff(max(asoc.dataincheiere), min(asoc.datastart)) into durataProiect
from proiecte p, angajati a, echipe e, asociereechipaangajat asoc 
where 
	e.idproiect = p.idproiect and 
	p.idproiect = idproiect and 
	asoc.idutilizator = a.idutilizator and 
	asoc.idechipa = e.idechipa;

RETURN durataProiect;
END; //
DELIMITER ;

/* intoarce numarul de programatori sau controlori de calitate care au lucrat la un proiect */
DELIMITER //
CREATE FUNCTION numarAngajati(idproiect INT, numeDepartament VARCHAR(20))
    RETURNS INT
BEGIN
DECLARE numarAngajati INT;

select count(a.salariunegociat) into numarAngajati 
from 
    proiecte p,
    angajati a, 
    echipe e, 
    asociereechipaangajat asoc, 
    functii f, 
    asociereutilizatorfunctie asoc1, 
    departamente d 
where 
    e.idproiect = p.idproiect and 
    p.idproiect = idproiect and 
    asoc.idutilizator = a.idutilizator and 
    asoc.idechipa = e.idechipa and 
    f.idfunctie = asoc1.idfunctie and 
    asoc1.idutilizator = a.idutilizator and 
    f.iddepartament = d.iddepartament and 
    d.denumire = numeDepartament;

RETURN numarAngajati;
END; //
DELIMITER ;

/* 
 * calculeaza salariul mediu al tuturor angajatilor implicati in proiectul
 * cu id-ul primit ca parametru care fac parte din departamentul specificat
 */
DELIMITER //
CREATE FUNCTION salariuMediu(idproiect INT, numeDepartament VARCHAR(20))
    RETURNS DECIMAL(10,2)
BEGIN
DECLARE medieSalariu DECIMAL(10, 2);

select avg(a.salariunegociat)/20 into medieSalariu
from 
    proiecte p, 
    angajati a, 
    echipe e, 
    asociereechipaangajat asoc, 
    functii f, 
    asociereutilizatorfunctie asoc1, 
    departamente d 
where 
    e.idproiect = p.idproiect and 
    p.idproiect = idproiect and 
    asoc.idutilizator = a.idutilizator and 
    asoc.idechipa = e.idechipa and 
    f.idfunctie = asoc1.idfunctie and 
    asoc1.idutilizator = a.idutilizator and 
    f.iddepartament = d.iddepartament and 
    d.denumire = numeDepartament;

RETURN medieSalariu;
END; //
DELIMITER ;

/* 
 * calculeaza pretul de vanzare al unui proiect
 * id-ul proiectului se primeste ca parametru
 */
DELIMITER //
CREATE FUNCTION pretProiect(idproiect INT)
    RETURNS DECIMAL(10,2)
BEGIN
DECLARE pretProiect DECIMAL(10, 2);
DECLARE salariuMediuProgramator DECIMAL(10, 2);
DECLARE salariuMediuQA DECIMAL(10, 2);
DECLARE durataProiect INT;
DECLARE numarProgramatori INT;
DECLARE numarQA INT;

select salariuMediu(idproiect, 'programare') into salariuMediuProgramator;
select salariuMediu(idproiect, 'asigurarea calitatii') into salariuMediuQA;
select numarAngajati(idproiect, 'programare') into numarProgramatori;
select numarAngajati(idproiect, 'asigurarea calitatii') into numarQA;
select durataProiect(idproiect) into durataProiect;

RETURN 0.5 * durataProiect * (numarProgramatori * salariuMediuProgramator + numarQA * salariuMediuQA);
END; //
DELIMITER ;

/* 
 * calculeaza media voturilor unui proiect
 */
DELIMITER //
CREATE FUNCTION ratingProiect(idproiect INT)
    RETURNS DECIMAL(10,2)
BEGIN
DECLARE medieRating DECIMAL(10, 2);
DECLARE rating INT;
DECLARE nrvoturi INT;

select p.rating INTO rating from proiecte p where p.idproiect = idproiect;
select p.nrvoturi INTO nrvoturi from proiecte p where p.idproiect = idproiect;

if nrvoturi = 0 then set medieRating = 0;
else set medieRating = rating/nrvoturi;
end if;

return medieRating;

END; //
DELIMITER ;

