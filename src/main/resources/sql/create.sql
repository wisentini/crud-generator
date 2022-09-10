CREATE TABLE "Regiao" (
    codigo INT         NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    nome   VARCHAR(31) NOT NULL
);

CREATE TABLE IF NOT EXISTS "Estado" (
    codigo       INT         NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    nome         VARCHAR(63) NOT NULL,
    uf           CHAR(2)     NOT NULL,
    codigoRegiao INT         NOT NULL
);

CREATE TABLE IF NOT EXISTS "Municipio" (
    codigo INT          NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    nome   VARCHAR(255) NOT NULL,
    uf     CHAR(2)      NOT NULL
);
