CREATE TABLE Persona_Fisica(
Account_Number VARCHAR (10) PRIMARY KEY NOT NULL,
Nome VARCHAR(20) NOT NULL,
Cognome VARCHAR(20) NOT NULL,
Codice_Fiscale VARCHAR(16) NOT NULL,
Data_Nascita DATE NOT NULL,
Luogo_Nascita VARCHAR (20) NOT NULL,
Residenza VARCHAR (20) NOT NULL,
Indirizzo VARCHAR (30) NOT NULL,
CAP VARCHAR (6) NOT NULL,
Email VARCHAR (30) NOT NULL,
Telefono VARCHAR (13) NOT NULL,
Documento VARCHAR (10) NOT NULL
);

CREATE TABLE Persona_Giuridica(
Account_Number VARCHAR (10) PRIMARY KEY NOT NULL,
Ragione_Sociale VARCHAR (30) NOT NULL,
Partita_Iva VARCHAR (11) NOT NULL,
Nome_Rappresentante VARCHAR (20) NOT NULL,
Cognome_Rappresentante VARCHAR (20) NOT NULL,
Sede_Legale VARCHAR (20) NOT NULL,
Indirizzo VARCHAR (30) NOT NULL,
CAP VARCHAR (6) NOT NULL,
Email VARCHAR (30) NOT NULL,
Telefono VARCHAR (13) NOT NULL,
Documento VARCHAR (10) NOT NULL
);
