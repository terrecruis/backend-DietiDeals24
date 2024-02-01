--------------------------------------- RIEMPIMENTO DATABASE ----------------------------------------


-- COMPRATORE table
INSERT INTO COMPRATORE (email, password, nomeCompleto, number, foto, descrizione, nazionalita, link1, link2)
VALUES
  ('buyer1@example.com', 'pass123', 'John Doe', '123456789', NULL, 'Art enthusiast', 'USA', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer2@example.com', 'secure456', 'Alice Johnson', '987654321', NULL, 'Book collector', 'UK', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer3@example.com', 'artlover567', 'Emma White', '654123789', NULL, 'Art connoisseur and collector', 'France', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer4@example.com', 'buy4me!789', 'Robert Davis', '789456123', NULL, 'Game enthusiast and collector', 'Germany', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer5@example.com', 'mypass123', 'Sophie Turner', '456789012', NULL, 'Music lover and collector', 'Italy', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer6@example.com', 'ilovegadgets', 'Daniel Brown', '012345678', NULL, 'Electronics aficionado', 'Spain', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer7@example.com', 'passart123', 'Olivia Green', '789012345', NULL, 'Passionate about art and culture', 'USA', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer8@example.com', 'sports4life', 'Ryan Clark', '234567890', NULL, 'Dedicated sports collector', 'Canada', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer9@example.com', 'musicluvr789', 'Isabella Martinez', '567890123', NULL, 'Music and instrument collector', 'France', 'http://buyerlink1.com', 'http://buyerlink2.com'),
  ('buyer10@example.com', 'passionategamer', 'Mason Lee', '890123456', NULL, 'Enthusiastic gamer and collector', 'Germany', 'http://buyerlink1.com', 'http://buyerlink2.com');



-- VENDITORE table
INSERT INTO VENDITORE (email, password, nomeCompleto, number, foto, descrizione, nazionalita, link1, link2)
VALUES
  ('seller1@example.com', 'artseller567', 'Michael Smith', '234567890', NULL, 'Art dealer and curator', 'Canada', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller2@example.com', 'gadgetmaster', 'Thomas Johnson', '456789123', NULL, 'Tech gadgets and innovations seller', 'Canada', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller3@example.com', 'artdealer123', 'Emily Brown', '345678912', NULL, 'Specialized in rare artworks', 'USA', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller4@example.com', 'gadgetmaster', 'Thomas Johnson', '456789123', NULL, 'Tech gadgets and innovations seller', 'Canada', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller5@example.com', 'musicstoreowner', 'Sophie Turner', '567890234', NULL, 'Owner of a music store', 'Italy', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller6@example.com', 'electronicsguru', 'Daniel Miller', '678901345', NULL, 'Expert in cutting-edge electronics', 'Spain', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller7@example.com', 'artandculture', 'Olivia Green', '789012456', NULL, 'Promoter of art and cultural items', 'USA', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller8@example.com', 'sportscollector', 'Ryan Clark', '890123567', NULL, 'Specialized in rare sports memorabilia', 'Canada', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller9@example.com', 'musicalinstruments', 'Isabella Martinez', '901234678', NULL, 'Passionate about musical instruments', 'France', 'http://sellerlink1.com', 'http://sellerlink2.com'),
  ('seller10@example.com', 'gamingexpert', 'Mason Lee', '012345789', NULL, 'Leading authority on rare video games', 'Germany', 'http://sellerlink1.com', 'http://sellerlink2.com');



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
  ('ID-010', 'Sci-Fi and Fantasy Book Auction', 'Auction of rare sci-fi and fantasy books', NULL, 'Libri', 'tempoFisso', '2024-07-01', NULL, 80.00, NULL, NULL, 'in corso', 'seller2@example.com');



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



-- NOTIFICA table--------------------------------------------
INSERT INTO NOTIFICA (idNotifica, descrizione, tempo, immagine, emailCompratore, emailVenditore)
VALUES
  ('ID-N003', 'Auction Ended', '2024-02-05', NULL, 'buyer3@example.com', 'seller3@example.com'),
  ('ID-N004', 'New Bid Placed', '2024-02-08', NULL, 'buyer4@example.com', 'seller4@example.com'),
  ('ID-N005', 'Item Shipped', '2024-02-12', NULL, 'buyer5@example.com', 'seller5@example.com'),
  ('ID-N006', 'Payment Received', '2024-02-15', NULL, 'buyer6@example.com', 'seller6@example.com'),
  ('ID-N007', 'Congratulations! You won', '2024-02-20', NULL, 'buyer7@example.com', 'seller7@example.com'),
  ('ID-N008', 'Important Auction Update', '2024-02-25', NULL, 'buyer8@example.com', 'seller8@example.com'),
  ('ID-N009', 'Bid Confirmation', '2024-03-02', NULL, 'buyer9@example.com', 'seller9@example.com'),
  ('ID-N010', 'Item Delivered', '2024-03-05', NULL, 'buyer10@example.com', 'seller10@example.com');


---------------------------------------------------------------------------------------------------------------------------_-

