
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