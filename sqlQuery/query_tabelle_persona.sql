CREATE TABLE persona_fisica(
persona_id VARCHAR (10) PRIMARY KEY NOT NULL,
nome VARCHAR(20) NOT NULL,
cognome VARCHAR(20) NOT NULL,
codice_fiscale VARCHAR(16) NOT NULL,
data_nascita DATE NOT NULL,
luogo_nascita VARCHAR (20) NOT NULL,
residenza VARCHAR (20) NOT NULL,
indirizzo VARCHAR (30) NOT NULL,
cap VARCHAR (6) NOT NULL,
email VARCHAR (30) NOT NULL,
telefono VARCHAR (13) NOT NULL,
documento VARCHAR (10) NOT NULL
);

CREATE TABLE persona_giuridica(
azienda_id VARCHAR (10) PRIMARY KEY NOT NULL,
ragione_sociale VARCHAR (30) NOT NULL,
partita_iva VARCHAR (11) NOT NULL,
nome_rappresentante VARCHAR (20) NOT NULL,
cognome_rappresentante VARCHAR (20) NOT NULL,
sede_legale VARCHAR (20) NOT NULL,
indirizzo VARCHAR (30) NOT NULL,
cap VARCHAR (6) NOT NULL,
email VARCHAR (30) NOT NULL,
telefono VARCHAR (13) NOT NULL,
documento_rappresentante VARCHAR (10) NOT NULL
);

CREATE TABLE account(
account_id VARCHAR(20) PRIMARY KEY NOT NULL,
persona_id VARCHAR(10),
azienda_id VARCHAR(10),
FOREIGN KEY (persona_id) REFERENCES persona_fisica(persona_id),
FOREIGN KEY (azienda_id) REFERENCES persona_giuridica(azienda_id)
);

CREATE TABLE conto_corrente(
iban VARCHAR(25) PRIMARY KEY NOT NULL,
account_id VARCHAR(20) NOT NULL,
saldo_disponibile FLOAT(10, 2) NOT NULL,
saldo_contabile FLOAT(10, 2) NOT NULL,
FOREIGN KEY (account_id) REFERENCES account(account_id)
);

CREATE TABLE carta_di_credito(
conto VARCHAR (10) NOT NULL,
account_id VARCHAR (20) NOT NULL,
numero VARCHAR (16) PRIMARY KEY NOT NULL,
scadenza DATE NOT NULL,
cvv VARCHAR(3) NOT NULL,
FOREIGN KEY (account_id) REFERENCES account(account_id),
FOREIGN KEY (conto) REFERENCES conto_corrente(iban)
);

CREATE TABLE carta_prepagata(
account_id VARCHAR (20) NOT NULL,
credito_Residuo DECIMAL(6,2) NOT NULL,
numero VARCHAR (16) PRIMARY KEY NOT NULL,
scadenza DATE NOT NULL,
cvv VARCHAR(3) NOT NULL,
FOREIGN KEY (account_id) REFERENCES account(account_id)
);

CREATE TABLE movimenti_conto(
numero_transazione INT NOT NULL PRIMARY KEY,
iban VARCHAR(25) NOT NULL,
nuovo_saldo FLOAT(10, 2) NOT NULL,
somma FLOAT(10, 2) NOT NULL,
is_accredito BOOLEAN NOT NULL,
FOREIGN KEY (iban) REFERENCES conto_corrente(iban)
);

CREATE TABLE movimenti_carta_prepagata(
numero_transazione INT NOT NULL PRIMARY KEY,
numero VARCHAR(16) NOT NULL,
nuovo_saldo FLOAT(10, 2) NOT NULL,
somma FLOAT(10, 2) NOT NULL,
is_accredito BOOLEAN NOT NULL,
FOREIGN KEY (numero) REFERENCES carta_prepagata(numero)
);
