
DROP TABLE IF EXISTS patient_allergie;
DROP TABLE IF EXISTS patient_medicine;
DROP TABLE IF EXISTS allergie;
DROP TABLE IF EXISTS medicine;
DROP TABLE IF EXISTS medicalrecord;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS firestation;


CREATE TABLE firestation (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	name VARCHAR(250) NOT NULL
);

CREATE TABLE address(
	id INT AUTO_INCREMENT  PRIMARY KEY,
	address VARCHAR(255) NOT NULL,
	city VARCHAR(100) NOT NULL,
	zip CHAR(5) NOT NULL,
	firestation_id INT,
	FOREIGN KEY (firestation_id) REFERENCES firestation(id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE person (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	first_name VARCHAR(250) NOT NULL,
	last_name VARCHAR(250) NOT NULL,
	address_id INT NOT NULL,
	phone VARCHAR(10) NOT NULL,
	email VARCHAR(250) NOT NULL,
	FOREIGN KEY (address_id) REFERENCES address(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE medicalrecord (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	first_name VARCHAR(250) NOT NULL,
	last_name VARCHAR(250) NOT NULL,
	birthday DATE NOT NULL
);

CREATE TABLE medicine (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	dosage_mg INT NOT NULL
);

CREATE TABLE patient_medicine (
    id INT AUTO_INCREMENT  PRIMARY KEY,
	quantity INT NOT NULL,
	medicalrecord_id INT NOT NULL,
	medicine_id INT NOT NULL,
	FOREIGN KEY (medicalrecord_id) REFERENCES medicalrecord(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (medicine_id) REFERENCES medicine(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE allergie (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	name VARCHAR(100) NOT NULL
);

CREATE TABLE patient_allergie (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	medicalrecord_id INT NOT NULL,
	allergie_id INT NOT NULL,
	FOREIGN KEY (medicalrecord_id) REFERENCES medicalrecord(id) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY (allergie_id) REFERENCES allergie(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

INSERT INTO firestation (name) VALUES ("Fire Station 1"), ("Fire Station 2"), ("Fire Station 3"), ("Fire Station 4");

INSERT INTO address (address, city, zip, firestation_id) VALUES ("1509 Culver St", "Culver", "97451", 3), ("29 15th St", "Culver", "97451", 2), ("834 Binoc Ave", "Culver", "97451", 3), ("644 Gershwin Cir", "Culver", "97451", 1), ("748 Townings Dr", "Culver", "97451", 3), ("112 Steppes Pl", "Culver", "97451", 4), ("489 Manchester St", "Culver", "97451", 4), ("892 Downing Ct", "Culver", "97451", 2), ("908 73rd St", "Culver", "97451", 1), ("947 E. Rose Dr", "Culver", "97451", 1), ("951 LoneTree Rd", "Culver", "97451", 1);

INSERT INTO person (first_name, last_name, address_id, phone, email) VALUES ("John", "Boyd", 1, "8418746512", "jaboyd@email.com"), ( "Jacob", "Boyd", 1, "8418746513", "drk@email.com"), ("Tenley", "Boyd", 1, "8418746512", "tenz@email.com"), ("Roger", "Boyd", 1, "8418746512", "jaboyd@email.com"), ("Felicia", "Boyd", 1, "8418746544", "jaboyd@email.com"), ("Jonanathan", "Marrack", 2, "8418746513", "drk@email.com"), ("Tessa", "Carman", 3, "8418746512", "tenz@email.com"), ("Peter", "Duncan", 4, "8418746512", "jaboyd@email.com"), ("Foster", "Shepard", 5, "8418746544", "jaboyd@email.com"), ("Tony", "Cooper", 6, "8418746874", "tcoop@ymail.com"), ("Lily", "Cooper", 7, "8418749845", "lily@email.com"), ("Sophia", "Zemicks", 8, "8418747878", "soph@email.com"), ("Warren", "Zemicks", 8, "8418747512", "ward@email.com"), ("Zach", "Zemicks", 8, "8418747512", "zarc@email.com"), ("Reginold", "Walker", 9, "8418748547", "reg@email.com"), ("Jamie", "Peters", 9, "8418747462", "jpeter@email.com"),  ("Ron", "Peters", 6, "8418748888", "jpeter@email.com"), ("Allison", "Boyd", 6, "8418749888", "aly@imail.com"), ("Brian", "Stelzer", 10, "8418747784", "bstel@email.com"), ("Shawna", "Stelzer", 10, "8418747784", "ssanw@email.com"), ("Kendrik", "Stelzer", 10, "8418747784", "bstel@email.com"), ("Clive", "Ferguson", 5, "8418746741", "clivfd@ymail.com"), ("Eric", "Cadigan", 11, "8418747458", "gramps@email.com");

INSERT INTO medicalrecord  (first_name, last_name, birthday) VALUES ("John", "Boyd", "1984-03-06"), ("Jacob", "Boyd", "1989-03-06"), ("Tenley", "Boyd", "2012-02-18"), ("Roger", "Boyd", "2017-06-09"), ("Felicia", "Boyd", "1986-08-01"), ("Jonanathan", "Marrack", "1989-03-01"), ("Tessa", "Carman", "2012-02-18"), ("Peter", "Duncan", "2000-09-06"), ("Foster", "Shepard", "1980-01-08"), ("Tony", "Cooper", "1994-03-06"), ("Lily", "Cooper", "1994-03-06"), ("Sophia", "Zemicks", "1988-06-03"), ("Warren", "Zemicks", "1985-03-06"), ("Zach", "Zemicks", "2017-03-06"), ("Reginold", "Walker", "1979-08-30"), ("Jamie", "Peters", "1982-03-06"), ("Ron", "Peters", "1965-04-06"), ("Allison", "Boyd", "1965-03-15"), ("Brian", "Stelzer", "1975-12-06"), ("Shawna", "Stelzer", "1980-07-08"), ("Kendrik", "Stelzer", "2014-03-06"), ("Clive", "Ferguson", "1994-03-06"), ("Eric", "Cadigan", "1945-08-06");

INSERT INTO medicine (name, dosage_mg) VALUES ("aznol", 60),  ("aznol", 200), ("aznol", 350), ("dodoxadin", 30), ("hydrapermazol", 100), ("ibupurin", 200), ("noxidian", 100), ("noznazol", 250), ("pharmacol", 2500), ("tetracyclaz", 650), ("terazine", 10), ("terazine", 500), ("thradox", 700), ("tradoxidine", 400);

INSERT INTO patient_medicine (quantity, medicalrecord_id, medicine_id) VALUES (1, 1, 3), (1, 1, 5), (2, 2, 9), (1, 2, 11), (1, 2, 8), (1, 5, 10), (3, 10, 5), (1, 10, 4), (1, 12, 1), (9, 12, 5), (2, 12, 9), (1, 12, 12), (1, 15, 13), (1, 18, 2), (1, 19, 6), (4, 19, 5), (1, 21, 7), (1, 21, 9), (1, 23, 14);

INSERT INTO allergie (name) VALUES ("aznol"), ("illisoxian"), ("nillacilan"), ("peanut"), ("shellfish"), ("xilliathal");

INSERT INTO patient_allergie (medicalrecord_id, allergie_id) VALUES (1, 3), (3, 4), (5, 6), (8, 5), (10, 5), (12, 4), (12, 5), (12, 1), (15, 2), (18, 3), (19, 3);

