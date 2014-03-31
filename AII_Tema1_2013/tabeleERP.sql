CREATE DATABASE Grupa341C4_BadoiuSimonaAndreea;

USE Grupa341C4_BadoiuSimonaAndreea;

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

CREATE TABLE utilizatori (
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
ALTER TABLE utilizatori ADD CONSTRAINT email_chk CHECK (email LIKE '%@%.%');
ALTER TABLE utilizatori ADD CONSTRAINT tip_chk CHECK (tip IN ('administrator', 'angajat', 'super-administrator'));

CREATE TABLE asociereutilizatorfunctie (
	idasociere	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idutilizator	INT(10) UNSIGNED NOT NULL,
	idfunctie	INT(10) UNSIGNED NOT NULL,
	FOREIGN KEY (idutilizator) REFERENCES utilizatori(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (idfunctie) REFERENCES functii(idfunctie) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE asocieredepartamentresponsabil(
	idasociere	INT(10)	UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	iddepartament	INT(10) UNSIGNED NOT NULL,
	idresponsabil	INT(10) UNSIGNED NOT NULL,
	FOREIGN KEY (idresponsabil) REFERENCES utilizatori(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (iddepartament) REFERENCES departamente(iddepartament) ON UPDATE CASCADE ON DELETE CASCADE	
);

#CREATE TABLE detaliiangajati (
#	iddetaliu		        INT(10)	UNSIGNED AUTO_INCREMENT PRIMARY #KEY NOT NULL,	
#	zileconcediuodihna	    INT(10) NOT NULL DEFAULT 10,
#	startpauzademasa	    TIME NOT NULL,
#	terminarepauzademasa	TIME NOT NULL,
#	inceputprogram		    TIME NOT NULL,
#	terminareprogram	    TIME NOT NULL
#);

CREATE TABLE concedii (
	idconcediu	    INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idutilizator	INT(10) UNSIGNED NOT NULL,
	datainceput	    DATETIME NOT NULL,
	durata		    INT(10) NOT NULL,
	tip		        VARCHAR(20) NOT NULL DEFAULT 'odihna',
	FOREIGN KEY (idutilizator) REFERENCES utilizatori(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE concedii ADD CONSTRAINT tip_chk CHECK (tip IN ('odihna', 'medical', 'fara plata', 'motive speciale'));

CREATE TABLE activitatezilnica (
	idactivitate	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idutilizator	INT(10) UNSIGNED NOT NULL,
	orasosire	    DATETIME NOT NULL,
	oraplecare	    DATETIME NOT NULL,
	FOREIGN KEY (idutilizator) REFERENCES utilizatori(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE activitatezilnica ADD CONSTRAINT zi_chk CHECK (ziuasaptamanii IN ('luni', 'marti', 'miercuri', 'joi', 'vineri', 'sambata', 'duminica'));

CREATE TABLE salarii (
	idsalariu	    INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idutilizator	INT(10) UNSIGNED NOT NULL,
	luna		    VARCHAR(10) NOT NULL,
	an			INT(10) NOT NULL,
	valoaresalariu	INT(10) NOT NULL,
	FOREIGN KEY (idutilizator) REFERENCES utilizatori(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE salarii ADD CONSTRAINT luna_chk CHECK (luna IN ('ian', 'feb', 'mar', 'apr', 'mai', 'iun', 'iul', 'aug', 'sep', 'oct', 'nov', 'dec'));

CREATE TABLE proiecte (
	idproiect	    INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	denumire	    VARCHAR(30) NOT NULL,
	descriere	    VARCHAR(100) NOT NULL
);

CREATE TABLE versiuniproiecte (
	idversiune	    INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	idproiect	    INT(10) UNSIGNED NOT NULL,
	datastart	    DATE NOT NULL,
	dataterminare	DATE NOT NULL,
	denumire	    VARCHAR(10) NOT NULL,
	FOREIGN KEY (idproiect) REFERENCES proiecte(idproiect) ON UPDATE CASCADE ON DELETE CASCADE
);

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

#CREATE TABLE asociereproiectechipa (
#	idasociere	    INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
#	idproiect	INT(10) UNSIGNED NOT NULL,
#	idechipa	    INT(10) UNSIGNED NOT NULL,
#	FOREIGN KEY (idechipa) REFERENCES echipe(idechipa) ON UPDATE CASCADE ON DELETE CASCADE,
#	FOREIGN KEY (idproiect) REFERENCES proiecte(idproiect) ON UPDATE CASCADE ON DELETE CASCADE
#);
#ALTER TABLE asociereproiectechipa ADD CONSTRAINT echipa_exists UNIQUE (idechipa);

CREATE TABLE facturi (
	idfactura	    INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	nrfactura	    INT(10) UNSIGNED NOT NULL,
	idproiect	    INT(10) UNSIGNED NOT NULL,
	totalfactura	DECIMAL(8, 2) UNSIGNED NOT NULL,
	FOREIGN KEY (idproiect) REFERENCES proiecte(idproiect) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE defecte (
	iddefect		        INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	denumire		        VARCHAR(20) NOT NULL,
	severitate		        VARCHAR(20) NOT NULL,
	descriere		        VARCHAR(100) NOT NULL,
	idproiect		        INT(10) UNSIGNED NOT NULL,
	idversiune		        INT(10) UNSIGNED NOT NULL,
	reproductibilitate      VARCHAR(100) NOT NULL,
	statut			        VARCHAR(20) NOT NULL,
	rezultat		        VARCHAR(20) NOT NULL,
	ultimamodificare     	DATETIME NOT NULL,
	idutilizatormodificare	INT(10) UNSIGNED NOT NULL,
	FOREIGN KEY (idproiect) REFERENCES proiecte(idproiect) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (idversiune) REFERENCES versiuniproiecte(idversiune) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (idutilizatormodificare) REFERENCES utilizatori(idutilizator) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE defecte ADD CONSTRAINT severitate_chk CHECK (severitate IN ('nu poate fi testat', 'blocarea aplicatiei', 'cerinta esentiala', 'major', 'mediu', 'minor', 'intrebare', 'sugestie'));
ALTER TABLE defecte ADD CONSTRAINT statut_chk CHECK (statut IN ('neanalizat', 'nu poate fi reprodus', 'nu este defect', 'nu va fi corectat'
, 'nu poate fi corectat', 'corectat', 'trebuie corectat'));
ALTER TABLE defecte ADD CONSTRAINT rezultat_chk CHECK (rezultat IN ('defect nou', 'defect verificat', 'defect necorectat'));

CREATE TABLE comentariidefecte (
	idcomentariu	INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY NOT NULL,
	iddefect	    INT(10) UNSIGNED NOT NULL,
	comentariu	    VARCHAR(150) NOT NULL,
	data		    DATETIME NOT NULL,
	FOREIGN KEY (iddefect) REFERENCES defecte(iddefect) ON UPDATE CASCADE ON DELETE CASCADE
);

/* procedura care scade numarul de zile de concediu ale unui angajat din numarul total de zile de coce.. */
DELIMITER //
CREATE PROCEDURE updateZileConcediu(IN userid INT, IN nrzile INT)
BEGIN
UPDATE utilizatori 
SET zileconcediuramase = zileconcediuramase - nrzile
WHERE idutilizator = userid;
END; //
DELIMITER ;

CREATE TABLE debug(
    zi INT,
    data DATE
);
/* functie care intoarce cate zile de concediu mai are un angajat */
DELIMITER //
CREATE FUNCTION zileConcediuRamase(userid INT)
    RETURNS INT
BEGIN
DECLARE zileConcediu INT;
select zileconcediuramase into zileConcediu
from utilizatori
where
    idutilizator = userid;
RETURN zileConcediu;
END; //
DELIMITER ;

/* trigger care se ocupa de updatarea tabele utilizatori la adaugarea unui concediu de odihna */
DELIMITER //
CREATE TRIGGER propaga_concediu_utilizatori BEFORE INSERT ON concedii
FOR EACH ROW
BEGIN
    DECLARE zileRamase INT;
    IF NEW.tip = 'odihna' THEN
        IF zileConcediuRamase(New.idutilizator) >= New.durata THEN
            CALL updateZileConcediu(New.idutilizator, New.durata);
	ELSEIF zileConcediuRamase(New.idutilizator) < New.durata THEN
	    SET NEW = "Error: utilizatorul nu are destule zile de concediu ramase";
        END IF;
    END IF;
END;//
DELIMITER ;

/* functie care intoarce cate zile lucratoare sunt intr-o luna */
DELIMITER //
CREATE FUNCTION zileLucratoare(data DATE)
    RETURNS INT
BEGIN
DECLARE totalZile INT;
DECLARE a INT Default 0;
DECLARE zileLucratoare INT;
DECLARE dayOfWeekNumber INT;
DECLARE startDate DATE;
DECLARE dateDay INT;
DECLARE newDate DATE;
select (DAYOFMONTH(data) - 1) INTO dateDay;
SELECT (data - INTERVAL dateDay DAY) INTO startDate;
SELECT DAY(LAST_DAY(data)) INTO totalZile;
SET zileLucratoare = 0;
simple_loop: LOOP
    select (startDate + INTERVAL a DAY) INTO newDate;
	select DAYOFWEEK(newDate) INTO dayOfWeekNumber;
	IF (dayOfWeekNumber != 1 AND dayOfWeekNumber != 7) THEN
	    set zileLucratoare = zileLucratoare + 1;
	END IF;
    insert INTO debug VALUES(dayOfWeekNumber, newDate);
	SET a = a + 1;
	IF (a = totalZile) THEN
	    LEAVE simple_loop;
	END IF;	
END LOOP simple_loop;
RETURN zileLucratoare;
END; //
DELIMITER ;

/* o functie care intoarce numarul de zile lucratoare dintr-un interval */
DELIMITER //
CREATE FUNCTION zileLucratoareInterval(data1 DATE, data2 Date)
    RETURNS INT
BEGIN
DECLARE totalZile INT;
DECLARE a INT Default 0;
DECLARE zileLucratoare INT;
DECLARE dayOfWeekNumber INT;
DECLARE startDate DATE;
DECLARE dateDay INT;
DECLARE newDate DATE;
SET zileLucratoare = 0;
simple_loop: LOOP
    select (data1 + INTERVAL a DAY) INTO newDate;
	select DAYOFWEEK(newDate) INTO dayOfWeekNumber;
	IF (dayOfWeekNumber != 1 AND dayOfWeekNumber != 7) THEN
	    set zileLucratoare = zileLucratoare + 1;
	END IF;
    insert INTO debug VALUES(dayOfWeekNumber, newDate);
	SET a = a + 1;
    select (data1 + INTERVAL 1 DAY) INTO data1;
	IF (data1 > data2) THEN
	    LEAVE simple_loop;
	END IF;	
END LOOP simple_loop;
RETURN zileLucratoare;
END; //
DELIMITER ;

/* functie care intoarce o valoare intreaga pentru fiecare tip de severitate */
DELIMITER //
CREATE FUNCTION severitateValue(severitate VARCHAR(30))
    RETURNS INT
BEGIN
DECLARE valoare INT;
case severitate
	WHEN 'aplicatia nu poate fi testata' THEN set valoare = 1;
	WHEN 'blocarea aplicatiei' THEN set valoare = 2;
	WHEN 'cerinta esentiala' THEN set valoare = 3;
	WHEN 'major' THEN set valoare = 4;
	WHEN 'mediu' THEN set valoare = 5;
	WHEN 'minor' THEN set valoare = 6;
	WHEN 'intrebare' THEN set valoare = 7;
	WHEN 'sugestie' THEN set valoare = 8;
	ELSE
		BEGIN
		END;
END CASE;
RETURN valoare;
END; //
DELIMITER ;

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

INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1220713379378', 'ZOTA', 'Daniel', '-', '0', 'zota.daniel@google.com', 'RO49 AAAA 1B31 0075 9384 0000', '1', 'angajat', '40', '4000', 'zota.daniel', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1160121873449', 'NEGREANU', 'Mircea', '-', '0', 'negreanu.mircea@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '2', 'angajat', '40', '3000', 'negreanu.mircea', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1560330668692', 'ROGOBETE', 'Mircea', '-', '0', 'rogobete.mircea@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '3', 'super-administrator', '40', '4000', 'rogobete.mircea', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2981007225570', 'GIUMALE', 'Petre', '-', '0', 'giumale.petre@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '4', 'administrator', '20', '2000', 'giumale.petre', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1350615180258', 'DRAGUSIN', 'Camelia', '-', '0', 'dragusin.camelia@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '5', 'administrator', '40', '5000', 'dragusin.camelia', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1860926240323', 'IONESCU', 'Tudor', '-', '0', 'ionescu.tudor@google.com', 'RO49 AAAA 1B31 0075 9384 0000', '6', 'administrator', '40', '1200', 'ionescu.tudor', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1921202602499', 'GEORGESCU', 'Monica', '-', '0', 'georgescu.monica@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '7', 'angajat', '40', '3000', 'georgescu.monica', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1320810148322', 'POPA', 'Monica', '-', '0', 'popa.monica@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '8', 'super-administrator', '40', '3000', 'popa.monica',  '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2390831245242', 'CHIVU', 'Camelia', '-', '0', 'chivu.camelia@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '9', 'super-administrator', '40', '4000', 'chivu.camelia', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1400515716087', 'POPA', 'Cristina', '-', '0', 'popa.cristina@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '10', 'super-administrator', '40', '2000', 'popa.cristina', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2740606757054', 'VASILESCU', 'Radu', '-', '0', 'vasilescu.radu@google.com', 'RO49 AAAA 1B31 0075 9384 0000', '11', 'angajat', '40', '3300', 'vasilescu.radu', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1181216988226', 'POPESCU', 'Ioana', '-', '0', 'popescu.ioana@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '12', 'angajat', '40', '4000', 'popescu.ioana', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1331019521959', 'VOITAN', 'Otilia', '-', '0', 'voitan.otilia@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '13', 'angajat', '40', '4000', 'voitan.otilia', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2740822491303', 'NEGREANU', 'Corina', '-', '0', 'negreanu.corina@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '14', 'angajat', '40', '4000', 'negreanu.corina', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1330818720833', 'RUSU', 'Teodora', '-', '0', 'rusu.teodora@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '15', 'angajat', '40', '2200', 'rusu.teodora', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1621205967255', 'NECULA', 'Cristina', '-', '0', 'necula.cristina@oracle.com', 'RO49 AAAA 1B31 0075 9384 0000', '16', 'angajat', '40', '4000', 'necula.cristina', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2430329797024', 'SERBAN', 'David', '-', '0', 'serban.david@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '17', 'angajat', '40', '4000', 'serban.david', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2910414242990', 'DUMITRESCU', 'Mircea', '-', '0', 'dumitrescu.mircea@live.com', 'RO49 AAAA 1B31 0075 9384 0000', '18', 'administrator', '40', '3000',  'dumitrescu.mircea', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '2820915291696', 'IONASCU', 'Tudor', '-', '0', 'ionascu.tudor@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '19', 'angajat', '40', '3300', 'ionascu.tudor', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1130423330580', 'NECULA', 'Ana Maria', '-', '0', 'necula.ana maria@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '20', 'angajat', '20', '4000', 'necula.ana maria', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1970809923368', 'SPIRIDON', 'Mihai', '-', '0', 'spiridon.mihai@hotmail.com', 'RO49 AAAA 1B31 0075 9384 0000', '21', 'angajat', '40', '4000', 'spiridon.mihai', '-', '2008-05-10');
INSERT INTO utilizatori (idutilizator, CNP, nume, prenume, adresa, telefon, email, IBAN, nrcontract, tip, numarorecontract, salariunegociat, numeutilizator, parola, dataangajarii)
 VALUES(DEFAULT, '1150928544482', 'SERBAN', 'David', '-', '0', 'serban.david@yahoo.com', 'RO49 AAAA 1B31 0075 9384 0000', '22', 'angajat', '40', '4000', 'serban.david', '-', '2008-05-10');

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

INSERT INTO asocieredepartamentresponsabil(idasociere, iddepartament, idresponsabil)
 VALUES(DEFAULT, 1, 1);
INSERT INTO asocieredepartamentresponsabil(idasociere, iddepartament, idresponsabil)
 VALUES(DEFAULT, 2, 1);
INSERT INTO asocieredepartamentresponsabil(idasociere, iddepartament, idresponsabil)
 VALUES(DEFAULT, 3, 13);
INSERT INTO asocieredepartamentresponsabil(idasociere, iddepartament, idresponsabil)
 VALUES(DEFAULT, 4, 19);

INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'1', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'2', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'3', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'4', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'5', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'6', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-8 09:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-12 09:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'7', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-6 12:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-15 13:00', '2013-10-15 20:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'8', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-2 09:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-10 10:00', '2013-10-10 19:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-19 07:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-23 09:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'9', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-8 12:00', '2013-10-8 21:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-18 12:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-22 11:00', '2013-10-22 18:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-27 11:00', '2013-10-27 20:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'10', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-2 10:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-10 10:00', '2013-10-10 18:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-29 07:00', '2013-10-29 18:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'11', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-20 09:00', '2013-10-20 15:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-23 08:00', '2013-10-23 20:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'12', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-1 09:00', '2013-10-1 18:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-4 09:00', '2013-10-4 16:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-9 07:00', '2013-10-9 20:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'13', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'14', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'15', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'16', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'17', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'18', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'19', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'20', '2013-10-30 08:00', '2013-10-30 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-1 08:00', '2013-10-1 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-2 08:00', '2013-10-2 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-3 08:00', '2013-10-3 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-4 08:00', '2013-10-4 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-5 08:00', '2013-10-5 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-6 08:00', '2013-10-6 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-7 08:00', '2013-10-7 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-8 08:00', '2013-10-8 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-9 08:00', '2013-10-9 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-10 08:00', '2013-10-10 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-11 08:00', '2013-10-11 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-12 08:00', '2013-10-12 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-13 08:00', '2013-10-13 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-14 08:00', '2013-10-14 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-15 08:00', '2013-10-15 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-16 08:00', '2013-10-16 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-17 08:00', '2013-10-17 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-18 08:00', '2013-10-18 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-19 08:00', '2013-10-19 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-20 08:00', '2013-10-20 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-21 08:00', '2013-10-21 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-22 08:00', '2013-10-22 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-23 08:00', '2013-10-23 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-24 08:00', '2013-10-24 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-25 08:00', '2013-10-25 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-26 08:00', '2013-10-26 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-27 08:00', '2013-10-27 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-28 08:00', '2013-10-28 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-29 08:00', '2013-10-29 17:00');
INSERT INTO activitatezilnica (idactivitate, idutilizator, orasosire, oraplecare) VALUES (DEFAULT,'21', '2013-10-30 08:00', '2013-10-30 17:00');

INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect1', 'descriere1');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect2', 'descriere2');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect3', 'descriere3');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect4', 'descriere4');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect5', 'descriere5');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect6', 'descriere6');
INSERT INTO proiecte(idproiect, denumire, descriere)
 VALUES(DEFAULT, 'proiect7', 'descriere7');

INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 1, 13);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 2, 14);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 3, 15);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 4, 13);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 5, 14);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 6, 15);
INSERT INTO echipe (idechipa, idproiect, idresponsabil)
 VALUES(DEFAULT, 7, 13);

INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 1, 13, '2013-10-5', '2013-12-7');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 1, 14, '2013-10-5', '2013-12-7');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 2, 15, '2013-09-08', '2013-12-12');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 2, 19, '2013-09-08', '2013-12-12');
INSERT INTO asociereechipaangajat (idasociere, idechipa, idutilizator, datastart, dataincheiere)
 VALUES(DEFAULT, 3, 17, '2013-10-15', '2013-10-30');

INSERT INTO versiuniproiecte (idversiune, idproiect, datastart, dataterminare, denumire)
 VALUES(DEFAULT, 1, '2013-06-03', '2013-08-01', '1.0.0.0');
INSERT INTO versiuniproiecte (idversiune, idproiect, datastart, dataterminare, denumire)
 VALUES(DEFAULT, 1, '2013-08-02', '2013-12-30', '1.0.1.0'); 
INSERT INTO versiuniproiecte (idversiune, idproiect, datastart, dataterminare, denumire)
 VALUES(DEFAULT, 2, '2013-05-04', '2013-08-01', '1.0.2.0');
INSERT INTO versiuniproiecte (idversiune, idproiect, datastart, dataterminare, denumire)
 VALUES(DEFAULT, 2, '2013-08-02', '2013-12-30', '1.0.3.0');
INSERT INTO versiuniproiecte (idversiune, idproiect, datastart, dataterminare, denumire)
 VALUES(DEFAULT, 3, '2013-05-15', '2013-12-30', '1.0.4.1');

INSERT INTO facturi (idfactura, nrfactura, idproiect, totalfactura)
 VALUES (DEFAULT, '15431', 1, 23450);
INSERT INTO facturi (idfactura, nrfactura, idproiect, totalfactura)
 VALUES (DEFAULT, '15443', 1, 40000);
INSERT INTO facturi (idfactura, nrfactura, idproiect, totalfactura)
 VALUES (DEFAULT, '15643', 2, 40000);
INSERT INTO facturi (idfactura, nrfactura, idproiect, totalfactura)
 VALUES (DEFAULT, '16646', 2, 80000);
INSERT INTO facturi (idfactura, nrfactura, idproiect, totalfactura)
 VALUES (DEFAULT, '11010', 3, 12000);
INSERT INTO facturi (idfactura, nrfactura, idproiect, totalfactura)
 VALUES (DEFAULT, '12423', 3, 40000);
INSERT INTO facturi (idfactura, nrfactura, idproiect, totalfactura)
 VALUES (DEFAULT, '13232', 4, 20000);

INSERT INTO defecte (iddefect, denumire, severitate, descriere, idproiect, idversiune, reproductibilitate, statut, rezultat, ultimamodificare, idutilizatormodificare)
 VALUES(DEFAULT, 'defect1', 'blocarea aplicatiei', 'descriere1', 1, 1, '-', 'neanalizat', 'defect nou', '2013-10-22 12:00', 5);
INSERT INTO defecte (iddefect, denumire, severitate, descriere, idproiect, idversiune, reproductibilitate, statut, rezultat, ultimamodificare, idutilizatormodificare)
 VALUES(DEFAULT, 'defect2', 'blocarea aplicatiei', 'descriere2', 1, 1, '-', 'corectat', 'defect verificat', '2013-10-22 14:00', 6);
INSERT INTO defecte (iddefect, denumire, severitate, descriere, idproiect, idversiune, reproductibilitate, statut, rezultat, ultimamodificare, idutilizatormodificare)
 VALUES(DEFAULT, 'defect3', 'blocarea aplicatiei', 'descriere3', 2, 3, '-', 'neanalizat', 'defect nou', '2013-10-22 12:00', 5);
INSERT INTO defecte (iddefect, denumire, severitate, descriere, idproiect, idversiune, reproductibilitate, statut, rezultat, ultimamodificare, idutilizatormodificare)
 VALUES(DEFAULT, 'defect4', 'blocarea aplicatiei', 'descriere4', 2, 3, '-', 'corectat', 'defect verificat', '2013-10-22 14:00', 6);

INSERT INTO comentariidefecte (idcomentariu, iddefect, comentariu, data)
 VALUES(DEFAULT, 1, 'comentariu1', '2013-10-22 12:02');
INSERT INTO comentariidefecte (idcomentariu, iddefect, comentariu, data)
 VALUES(DEFAULT, 1, 'comentariu2', '2013-10-22 12:20');
INSERT INTO comentariidefecte (idcomentariu, iddefect, comentariu, data)
 VALUES(DEFAULT, 2, 'comentariu3', '2013-10-22 12:15');
INSERT INTO comentariidefecte (idcomentariu, iddefect, comentariu, data)
 VALUES(DEFAULT, 3, 'comentariu3', '2013-10-22 12:25');
INSERT INTO comentariidefecte (idcomentariu, iddefect, comentariu, data)
 VALUES(DEFAULT, 4, 'comentariu1', '2013-10-22 14:25');

INSERT INTO concedii (idconcediu, idutilizator, datainceput, durata, tip)
 VALUES(DEFAULT, 1, '2013-10-02', 4, 'odihna');
INSERT INTO concedii (idconcediu, idutilizator, datainceput, durata, tip)
 VALUES(DEFAULT, 2, '2013-10-07', 3, 'fara plata');
INSERT INTO concedii (idconcediu, idutilizator, datainceput, durata, tip)
 VALUES(DEFAULT, 3, '2013-10-15', 5, 'medical');
INSERT INTO concedii (idconcediu, idutilizator, datainceput, durata, tip)
 VALUES(DEFAULT, 3, '2013-10-20', 5, 'odihna');
INSERT INTO concedii (idconcediu, idutilizator, datainceput, durata, tip)
 VALUES(DEFAULT, 5, '2013-10-01', 2, 'motive speciale');
INSERT INTO concedii (idconcediu, idutilizator, datainceput, durata, tip)
 VALUES(DEFAULT, 5, '2013-10-20', 2, 'fara plata');
