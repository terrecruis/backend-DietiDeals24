



---------------------------------------- DOMINI ----------------------------------------


CREATE DOMAIN DOMINIO_TIPOASTA AS VARCHAR
    CHECK (VALUE IN ('incrementale', 'tempoFisso'));
 
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
	nomeCompleto VARCHAR NOT NULL,
	telephoneNumber VARCHAR NOT NULL,
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
	nomeCompleto VARCHAR NOT NULL,
	telephoneNumber VARCHAR NOT NULL,
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
    categoria VARCHAR NOT NULL,
    tipoAsta DOMINIO_TIPOASTA NOT NULL,
    dataScadenza DATE,
    timer INTEGER,
    sogliaMinimaSegreta DECIMAL(12,2),
    basePubblica DECIMAL(12,2),
    sogliaRialzo DECIMAL(12,2),
    StatoAsta DOMINIO_STATOASTA NOT NULL DEFAULT 'in corso',
    emailVenditore DOMINIO_EMAIL NOT NULL,
    luogo VARCHAR,

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
);

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

--VISTA CHE MOSTRA LE ASTE ATTIVE
CREATE OR REPLACE VIEW AsteAttive AS
    SELECT *
    FROM ASTA
    WHERE StatoAsta = 'in corso';

--Vista che mostra per ogni asta la puntata più alta
CREATE OR REPLACE VIEW AstaImportoAttuale AS
    SELECT idAsta, MAX(importo) AS puntataPiùAlta
    FROM PUNTATA
    GROUP BY idAsta;

-- TUTTI GLI UTENTI DEL SISTEMA
CREATE OR REPLACE VIEW UTENTI AS
    SELECT email, nomeCompleto, 'compratore' AS tipoUtente
    FROM COMPRATORE
    UNION
    SELECT email, nomeCompleto, 'venditore' AS tipoUtente
    FROM VENDITORE
    ORDER BY email;

-- MOSTRA LE ASTE ATTIVE CON IL PREZZO CORRENTE 
CREATE OR REPLACE VIEW VistaAsteAttiveConPuntata AS
SELECT a.idAsta, a.titolo, a.descrizione, a.luogo, a.foto, a.categoria, a.tipoAsta, TO_CHAR(a.dataScadenza, 'DD/MM/YYYY'), a.timer, a.sogliaMinimaSegreta, a.basePubblica, a.sogliaRialzo, a.StatoAsta,a.dataScadenza, a.emailVenditore, 
       MAX(p.importo) AS prezzoMassimo
FROM ASTA a
LEFT JOIN PUNTATA p ON a.idAsta = p.idAsta
WHERE a.StatoAsta = 'in corso'
GROUP BY a.idAsta, a.titolo, a.descrizione,a.luogo, a.foto, a.categoria, a.tipoAsta, a.dataScadenza, a.timer, a.sogliaMinimaSegreta, a.basePubblica, a.sogliaRialzo, a.StatoAsta, a.emailVenditore;

    -----------------------------------------------------------------------------------

--____________________________________________________________________________________________________________________________________________________________________________________________________________________--

--__________________________________________________________________TRIGGER CHE SERVONO PER AGGIORNAMENTO STATO ASTA E INVIO DELLE NOTIFICHE : _______________________________________________________________________--

/*
    AGGIORNA LO STATO ASTA :
    Questa funzione viene chiamata ogni volta che l'utente fa un accesso all'applicazione in maniera tale da rendere
    automatico l'aggiornamento dello stato delle aste. Inoltre mantiene in maniera persistente lo stato dei dati, in maniera tale
    che la funzione viene fatta on de mand cosi da evitare cicli ogni tot di tempo sulla tabella peggiorando le prestezioni.

    --per chiamare la funzione scrivere CALL aggiornaStatoAsta();
    TEST : FUNZIONA 
*/

CREATE OR REPLACE PROCEDURE aggiornaStatoAsta()
AS $$
DECLARE
    c_aste CURSOR IS (
        SELECT idAsta, tipoAsta, dataScadenza, sogliaMinimaSegreta, timer
        FROM ASTA
        WHERE StatoAsta = 'in corso'
    );

    v_idAsta DOMINIO_ID;
    v_tipoAsta DOMINIO_TIPOASTA;
    v_dataScadenza DATE;
    v_sogliaMinimaSegreta DECIMAL(12,2);
    v_timer INTEGER;
BEGIN
    OPEN c_aste;
    LOOP
        FETCH c_aste INTO v_idAsta, v_tipoAsta, v_dataScadenza, v_sogliaMinimaSegreta, v_timer;
        EXIT WHEN NOT FOUND;
        --tipo asta tempo fisso
        IF v_tipoAsta = 'tempoFisso' THEN
            IF (v_dataScadenza < CURRENT_DATE AND v_sogliaMinimaSegreta < (SELECT MAX(importo) FROM PUNTATA WHERE idAsta = v_idAsta)) THEN
                UPDATE ASTA
                SET StatoAsta = 'conclusa'
                WHERE idAsta = v_idAsta;
            ELSIF v_dataScadenza < CURRENT_DATE THEN
                UPDATE ASTA
                SET StatoAsta = 'fallita'
                WHERE idAsta = v_idAsta;
            END IF;
        --tipo asta incrementale
        ELSIF v_tipoAsta = 'incrementale' THEN
            IF (v_timer = 0 AND (SELECT COUNT(*) FROM PUNTATA WHERE idAsta = v_idAsta) > 0) THEN
                UPDATE ASTA
                SET StatoAsta = 'conclusa'
                WHERE idAsta = v_idAsta;
            ELSIF (v_timer = 0) THEN
                UPDATE ASTA
                SET StatoAsta = 'fallita'
                WHERE idAsta = v_idAsta;
            END IF;
        END IF;
    END LOOP;
    CLOSE c_aste;
END;
$$ LANGUAGE plpgsql;


/*
    AGGIORNAMENTO DELLE NOTFICHE :
    TRIGGER : quando aggiorno l'asta da "in corso" a "conclusa" o "fallita" allora creo le notifiche nel modo seguente : 
    1. Se l'asta passa da in corso a conclusa allora si invia al compratore con la massima puntata (ovvero il vincitore dell'asta) una notifica la cui descrizione è "hai vinto l'asta" e come titolo il titolo dell'asta;
        e una notifica al venditore la cui descrizione è "l'asta è stata vinta da un compratore"  con titolo il titolo dell'asta;
        mentre a tutti gli altri compratori che hanno effettuato almeno una puntata per quell'asta si invia una notifica la cui descrizione è "l'asta è stata vinta da un altro compratore" con titolo il titolo dell'asta;
    2. Se l'asta passa da in corso a fallita allora si invia al venditore e a tutti i compratori una notifica la cui descrizione è "l'asta è fallita"


    TEST : FUNZIONA
*/

CREATE OR REPLACE FUNCTION aggiornanotifiche()
RETURNS TRIGGER AS $$
DECLARE
    vincitore DOMINIO_EMAIL;
BEGIN
    IF OLD.StatoAsta = 'in corso' AND (NEW.StatoAsta = 'conclusa' OR NEW.StatoAsta = 'fallita') THEN
        -- Ottenere l'email del vincitore (massima puntata)
        SELECT emailCompratore INTO vincitore
        FROM PUNTATA
        WHERE idAsta = NEW.idAsta
        ORDER BY importo DESC
        LIMIT 1;

        -- Notifica asta conclusa
        IF NEW.StatoAsta = 'conclusa' THEN
            -- Notifica al vincitore
            INSERT INTO NOTIFICA(titoloAsta, descrizione, tempo, emailCompratore, emailVenditore)
            VALUES (NEW.titolo, 'hai vinto l''asta', CURRENT_DATE, vincitore, OLD.emailVenditore);

            -- Notifica al venditore
            INSERT INTO NOTIFICA(titoloAsta, descrizione, tempo, emailCompratore, emailVenditore)
            VALUES (NEW.titolo, 'l''asta è stata vinta da un compratore', CURRENT_DATE, NULL, OLD.emailVenditore);

            -- Notifica agli altri compratori
            INSERT INTO NOTIFICA(titoloAsta, descrizione, tempo, emailCompratore, emailVenditore)
            SELECT NEW.titolo, 'l''asta è stata vinta da un altro compratore', CURRENT_DATE, c.emailCompratore, OLD.emailVenditore
            FROM PUNTATA c
            WHERE c.idAsta = NEW.idAsta AND c.emailCompratore != vincitore;
        -- Notifica asta fallita
        ELSIF NEW.StatoAsta = 'fallita' THEN
            -- Notifica al venditore
            INSERT INTO NOTIFICA(titoloAsta, descrizione, tempo, emailCompratore, emailVenditore)
            VALUES (NEW.titolo, 'l''asta è fallita', CURRENT_DATE, NULL, OLD.emailVenditore);

            -- Notifica a tutti i compratori
            INSERT INTO NOTIFICA(titoloAsta, descrizione, tempo, emailCompratore, emailVenditore)
            SELECT NEW.titolo, 'l''asta è fallita', CURRENT_DATE, c.emailCompratore, OLD.emailVenditore
            FROM PUNTATA c
            WHERE c.idAsta = NEW.idAsta;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER triggerAggiornaNotifiche
AFTER UPDATE ON ASTA
FOR EACH ROW
WHEN (OLD.StatoAsta = 'in corso' AND (NEW.StatoAsta = 'conclusa' OR NEW.StatoAsta = 'fallita'))
EXECUTE PROCEDURE aggiornanotifiche();

--____________________________________________________________________________________________________________________________________________________________________________________________________________________--
--____________________________________________________________________________________________________________________________________________________________________________________________________________________--

--------------------------------------- RIEMPIMENTO DATABASE ----------------------------------------


-- COMPRATORE table
INSERT INTO COMPRATORE (email, nomeCompleto, telephoneNumber, foto, descrizione, nazionalita, link1, link2)
VALUES
  ('buyer1@example.com',  'John Doe', '123456789', NULL, 'Art enthusiast', 'USA', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer2@example.com',  'Alice Johnson', '987654321', NULL, 'Book collector', 'UK', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer3@example.com',  'Emma White', '654123789', NULL, 'Art connoisseur and collector', 'France', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer4@example.com',  'Robert Davis', '789456123', NULL, 'Game enthusiast and collector', 'Germany', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer5@example.com',  'Sophie Turner', '456789012', NULL, 'Music lover and collector', 'Italy', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer6@example.com',  'Daniel Brown', '012345678', NULL, 'Electronics aficionado', 'Spain', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer7@example.com',  'Olivia Green', '789012345', NULL, 'Passionate about art and culture', 'USA', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer8@example.com',  'Ryan Clark', '234567890', NULL, 'Dedicated sports collector', 'Canada', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer9@example.com',  'Isabella Martinez', '567890123', NULL, 'Music and instrument collector', 'France', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer10@example.com',  'Mason Lee', '890123456', NULL, 'Enthusiastic gamer and collector', 'Germany', 'http://buyerlink1.com', 'http://buyerlink2.com');



-- VENDITORE table
INSERT INTO VENDITORE (email, nomeCompleto, telephoneNumber, foto, descrizione, nazionalita, link1, link2)
VALUES
  ('seller1@example.com',  'Michael Smith', '234567890', NULL, 'Art dealer and curator', 'Canada', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller2@example.com',  'Thomas Johnson', '456789123', NULL, 'Tech gadgets and innovations seller', 'Canada', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller3@example.com',  'Emily Brown', '345678912', NULL, 'Specialized in rare artworks', 'USA', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller4@example.com',  'Thomas Johnson', '456789123', NULL, 'Tech gadgets and innovations seller', 'Canada', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller5@example.com',  'Sophie Turner', '567890234', NULL, 'Owner of a music store', 'Italy', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller6@example.com',  'Daniel Miller', '678901345', NULL, 'Expert in cutting-edge electronics', 'Spain', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller7@example.com',  'Olivia Green', '789012456', NULL, 'Promoter of art and cultural items', 'USA', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller8@example.com',  'Ryan Clark', '890123567', NULL, 'Specialized in rare sports memorabilia', 'Canada', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller9@example.com',  'Isabella Martinez', '901234678', NULL, 'Passionate about musical instruments', 'France', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller10@example.com',  'Mason Lee', '012345789', NULL, 'Leading authority on rare video games', 'Germany', 'http://sellerlink1.com', 'http://sellerlink2.com');



-- ASTA table
INSERT INTO ASTA (idAsta, titolo, descrizione, foto, categoria, tipoAsta, dataScadenza, timer, sogliaMinimaSegreta, basePubblica, sogliaRialzo, StatoAsta, emailVenditore)
VALUES
  ('ID-001', 'Modern Art Exhibition', 'Contemporary artworks showcase', NULL, 'Arte', 'incrementale', NULL, 20, NULL, 150.00, 15.00, 'in corso', 'seller3@example.com'),
  ('ID-002', 'Tech Gadgets Auction', 'Latest technology gadgets on auction', NULL, 'Elettronica', 'tempoFisso', '2024-03-10', NULL, 80.00, NULL, NULL, 'in corso', 'seller4@example.com'),
  ('ID-003', 'Rare Musical Instruments', 'A collection of rare musical instruments', NULL, 'Musica', 'incrementale', NULL, 25, NULL, 120.00, 12.00, 'in corso', 'seller5@example.com'),
  ('ID-004', 'Vintage Video Games Sale', 'Rare and vintage video games auction', NULL, 'Giochi', 'tempoFisso', '2024-04-05', NULL, 60.00, NULL, NULL, 'in corso', 'seller6@example.com'),
  ('ID-005', 'Cultural Artifacts Auction', 'Auction of cultural and historical artifacts', NULL, 'Altro', 'incrementale', NULL, 15, NULL, 200.00, 20.00, 'in corso', 'seller7@example.com'),
  ('ID-006', 'Classic Sports Memorabilia', 'Classic and valuable sports memorabilia', NULL, 'Sport', 'tempoFisso', '2024-05-20', NULL, 90.00, NULL, NULL, 'in corso', 'seller8@example.com'),
  ('ID-007', 'Jazz Music Collection Sale', 'Rare jazz music collection on sale', NULL, 'Musica', 'incrementale', NULL, 35, NULL, 180.00, 18.00, 'in corso', 'seller9@example.com'),
  ('ID-008', 'Electronic Gadgets Extravaganza', 'Auction of cutting-edge electronic gadgets', NULL, 'Elettronica', 'tempoFisso', '2024-06-15', NULL, 70.00, NULL, NULL, 'in corso', 'seller10@example.com'),
  ('ID-009', 'Abstract Art Showcase', 'Auction featuring abstract art pieces', NULL, 'Arte', 'incrementale', NULL, 20, NULL, 150.00, 15.00, 'in corso', 'seller1@example.com'),
  ('ID-010', 'Sci-Fi and Fantasy Book Auction', 'Auction of rare sci-fi and fantasy books', NULL, 'Altro', 'tempoFisso', '2024-07-01', NULL, 80.00, NULL, NULL, 'in corso', 'seller2@example.com');



-- PUNTATA table
INSERT INTO PUNTATA (idAsta, emailCompratore, importo)
VALUES
  ('ID-001', 'buyer1@example.com', 120.00),
  ('ID-001', 'buyer2@example.com', 150.00),
  ('ID-002', 'buyer3@example.com', 90.00),
  ('ID-002', 'buyer4@example.com', 120.00),
  ('ID-003', 'buyer5@example.com', 200.00),
  ('ID-003', 'buyer6@example.com', 220.00),
  ('ID-004', 'buyer7@example.com', 110.00),
  ('ID-004', 'buyer8@example.com', 140.00),
  ('ID-005', 'buyer9@example.com', 180.00),
  ('ID-005', 'buyer10@example.com', 210.00);

---------------------------------------------------------------------------------------------------------------------------_-

