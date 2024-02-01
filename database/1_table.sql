



---------------------------------------- DOMINI ----------------------------------------


CREATE DOMAIN DOMINIO_TIPOASTA AS VARCHAR
    CHECK (VALUE IN ('incrementale', 'tempoFisso'));

CREATE DOMAIN DOMINIO_CATEGORIA AS VARCHAR
    CHECK (VALUE IN ('Arte', 'Sport', 'Musica', 'Giochi', 'Elettronica', 'Altro'));

CREATE DOMAIN DOMINIO_STATOASTA AS VARCHAR
    CHECK (VALUE IN ('in corso', 'conclusa', 'fallita'));

CREATE DOMAIN DOMINIO_TIPOUTENTE AS VARCHAR
    CHECK (VALUE IN ('compratore', 'venditore'));

CREATE DOMAIN DOMINIO_EMAIL AS VARCHAR
    CHECK (VALUE LIKE '%@%.%');

CREATE DOMAIN DOMINIO_ID AS VARCHAR
    CHECK(VALUE LIKE('ID-%'));


---------------------------------------- TABELLE ----------------------------------------

CREATE TABLE IF NOT EXISTS COMPRATORE
(
	email DOMINIO_EMAIL NOT NULL,
    password VARCHAR NOT NULL,
	nomeCompleto VARCHAR NOT NULL,
	number VARCHAR NOT NULL,
    foto VARCHAR,
	descrizione CHAR(500),
	nazionalita VARCHAR,
    link1 VARCHAR,
    link2 VARCHAR,

    CONSTRAINT compratore_pk PRIMARY KEY(email)
);

CREATE TABLE IF NOT EXISTS VENDITORE
(
	email DOMINIO_EMAIL NOT NULL,
    password VARCHAR NOT NULL,
	nomeCompleto VARCHAR NOT NULL,
	number VARCHAR NOT NULL,
    foto VARCHAR,
	descrizione CHAR(500),
	nazionalita VARCHAR,
    link1 VARCHAR,
    link2 VARCHAR,

    CONSTRAINT venditore_pk PRIMARY KEY(email)
);

CREATE TABLE IF NOT EXISTS ASTA
(
    idAsta DOMINIO_ID NOT NULL,
    titolo VARCHAR(150) NOT NULL, 
    descrizione VARCHAR NOT NULL,
    foto VARCHAR,
    categoria DOMINIO_CATEGORIA NOT NULL,
    tipoAsta DOMINIO_TIPOASTA NOT NULL,
    dataScadenza DATE,
    timer INTEGER,
    sogliaMinimaSegreta DECIMAL(12,2),
    basePubblica DECIMAL(12,2),
    sogliaRialzo DECIMAL(12,2),
    StatoAsta DOMINIO_STATOASTA NOT NULL DEFAULT 'in corso',
    emailVenditore DOMINIO_EMAIL NOT NULL,

    CONSTRAINT asta_pk PRIMARY KEY(idAsta),
    CONSTRAINT astaFK1 FOREIGN KEY(emailVenditore) REFERENCES VENDITORE(email)
        ON UPDATE CASCADE,
    CONSTRAINT vincolo1 CHECK((dataScadenza IS NOT NULL AND sogliaMinimaSegreta IS NOT NULL AND tipoAsta='tempoFisso') OR (timer IS NOT NULL AND sogliaRialzo >= 10 AND basePubblica IS NOT NULL AND tipoAsta= 'incrementale'))

);

CREATE TABLE IF NOT EXISTS PUNTATA
(
    idAsta DOMINIO_ID NOT NULL,
    emailCompratore VARCHAR NOT NULL,
    importo DECIMAL(12,2) NOT NULL,

    CONSTRAINT puntata_pk PRIMARY KEY(idAsta, emailCompratore, importo),

    CONSTRAINT puntataFK1 FOREIGN KEY(idAsta) REFERENCES ASTA(idAsta)
        ON UPDATE CASCADE,
    CONSTRAINT puntataFK2 FOREIGN KEY(emailCompratore) REFERENCES COMPRATORE(email)
        ON UPDATE CASCADE
);:

CREATE TABLE IF NOT EXISTS NOTIFICA
(
    idNotifica INTEGER generated always as identity primary key, --GENERA AUTOMATICAMENTE UN ID
    titoloAsta VARCHAR NOT NULL,
    descrizione VARCHAR NOT NULL,
    tempo DATE NOT NULL,
    immagine VARCHAR,
    emailCompratore DOMINIO_EMAIL,
    emailVenditore DOMINIO_EMAIL,

    CONSTRAINT notificaFK1 FOREIGN KEY(emailCompratore) REFERENCES COMPRATORE(email)
        ON UPDATE CASCADE,
    CONSTRAINT notificaFK2 FOREIGN KEY(emailVenditore) REFERENCES VENDITORE(email)
        ON UPDATE CASCADE
);



---------------------------------------- VISTE ----------------------------------------

-- PUNTATA PIU ALTA PER OGNI ASTA CON CORRISPETTIVO COMPRATORE

CREATE OR REPLACE VIEW PUNTATAPIUALTA AS
    SELECT ASTA.titolo, ASTA.idAsta, PUNTATA.importo, PUNTATA.emailCompratore, ASTA.StatoAsta
    FROM ASTA, PUNTATA
    WHERE ASTA.idAsta = PUNTATA.idAsta
    AND PUNTATA.importo = (SELECT MAX(importo) FROM PUNTATA WHERE PUNTATA.idAsta = ASTA.idAsta)
    ORDER BY ASTA.StatoAsta;