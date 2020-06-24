
CREATE TABLE persona_fisica(
persona_id VARCHAR(10) PRIMARY KEY NOT NULL,
nome VARCHAR(20) NOT NULL,
cognome VARCHAR(20) NOT NULL,
codice_fiscale VARCHAR(16) NOT NULL UNIQUE,
data_nascita DATE NOT NULL ,
luogo_nascita VARCHAR (20) NOT NULL,
residenza VARCHAR (20) NOT NULL,
indirizzo VARCHAR (30) NOT NULL,
cap VARCHAR (6) NOT NULL,
email VARCHAR (30) NOT NULL,
telefono VARCHAR (13) NOT NULL,
documento VARCHAR (10) NOT NULL UNIQUE
);

CREATE TABLE persona_giuridica(
azienda_id VARCHAR (10) PRIMARY KEY NOT NULL,
ragione_sociale VARCHAR (30) NOT NULL UNIQUE,
partita_iva VARCHAR (11) NOT NULL UNIQUE,
nome_rappresentante VARCHAR (20) NOT NULL,
cognome_rappresentante VARCHAR (20) NOT NULL,
sede_legale VARCHAR (20) NOT NULL,
indirizzo VARCHAR (30) NOT NULL,
cap VARCHAR (6) NOT NULL,
email VARCHAR (30) NOT NULL,
telefono VARCHAR (13) NOT NULL,
documento_rappresentante VARCHAR (10) NOT NULL UNIQUE
);

CREATE TABLE account(
account_id VARCHAR(20) PRIMARY KEY NOT NULL,
persona_id VARCHAR(10),
azienda_id VARCHAR(10),
FOREIGN KEY (persona_id) REFERENCES persona_fisica(persona_id) ON DELETE CASCADE,
FOREIGN KEY (azienda_id) REFERENCES persona_giuridica(azienda_id) ON DELETE CASCADE
);

CREATE TABLE conto_corrente(
iban VARCHAR(25) PRIMARY KEY NOT NULL,
account_id VARCHAR(20) NOT NULL,
saldo_disponibile FLOAT(10, 2) NOT NULL,
saldo_contabile FLOAT(10, 2) NOT NULL,
FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE
);


CREATE TABLE carta_di_credito(
conto VARCHAR (25) NOT NULL,
account_id VARCHAR (20) NOT NULL,
numero VARCHAR (16) PRIMARY KEY NOT NULL,
scadenza DATE NOT NULL,
cvv VARCHAR(3) NOT NULL,
isBlocked BOOLEAN DEFAULT false,
FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE,
FOREIGN KEY (conto) REFERENCES conto_corrente(iban) ON DELETE CASCADE
);

CREATE TABLE carta_prepagata(
account_id VARCHAR (20) NOT NULL,
credito_Residuo DECIMAL(6,2) NOT NULL,
numero VARCHAR (16) PRIMARY KEY NOT NULL,
scadenza DATE NOT NULL,
cvv VARCHAR(3) NOT NULL,
isBlocked BOOLEAN DEFAULT false,
FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE
);

CREATE TABLE movimenti_conto(
numero_transazione INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
data_transazione DATE NOT NULL,
orario_transazione TIME NOT NULL,
iban VARCHAR(25) NOT NULL,
nuovo_saldo FLOAT(10, 2) NOT NULL,
somma FLOAT(10, 2) NOT NULL,
is_accredito BOOLEAN NOT NULL,
FOREIGN KEY (iban) REFERENCES conto_corrente(iban) ON DELETE CASCADE
);
CREATE TABLE movimenti_carta_prepagata(
numero_transazione INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
data_transazione DATE NOT NULL,
orario_transazione TIME NOT NULL,
numero VARCHAR(16) NOT NULL,
nuovo_saldo FLOAT(10, 2) NOT NULL,
somma FLOAT(10, 2) NOT NULL,
is_accredito BOOLEAN NOT NULL,
FOREIGN KEY (numero) REFERENCES carta_prepagata(numero) ON DELETE CASCADE
);

CREATE TABLE token(
account_id VARCHAR(20) NOT NULL PRIMARY KEY,
data_transazione DATE NOT NULL,
orario_transazione TIME NOT NULL,
generated_token VARCHAR(6) NOT NULL,
FOREIGN KEY (account_id) REFERENCES account(account_id) ON DELETE CASCADE
);

INSERT INTO account (account_id, persona_id) VALUES ('1111111111','1111111111');
INSERT INTO conto_corrente (iban, account_id, saldo_disponibile, saldo_contabile) VALUES ('asdf1234qwer5678', '1111111111', '1000','1000');
INSERT INTO carta_di_credito (conto, account_id, numero, scadenza, cvv) VALUES ('asdf1234qwer5678','1111111111','2839176403921784','2020-07-07','123');
INSERT INTO carta_prepagata VALUES ('1111111111','1000','8671204139373831','2020-09-09','779');
INSERT INTO token VALUES ('1111111111', '2020/06/18','09:45:00','123456');
