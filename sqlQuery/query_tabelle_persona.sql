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

CREATE TABLE carta_di_credito(
conto VARCHAR (10) NOT NULL,
account_id VARCHAR (10) NOT NULL,
numero VARCHAR (16) PRIMARY KEY NOT NULL,
scadenza DATE NOT NULL,
cvv VARCHAR(3) NOT NULL
);

CREATE TABLE carta_prepagata(
account_id VARCHAR (10) NOT NULL,
credito_Residuo DECIMAL(6,2) NOT NULL,
numero VARCHAR (16) PRIMARY KEY NOT NULL,
scadenza DATE NOT NULL,
cvv VARCHAR(3) NOT NULL
);
