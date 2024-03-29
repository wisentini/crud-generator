DROP TABLE IF EXISTS "Regiao";

DROP TABLE IF EXISTS "Estado";

DROP TABLE IF EXISTS "Municipio";

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

INSERT INTO "Regiao" (nome) VALUES ('Norte');
INSERT INTO "Regiao" (nome) VALUES ('Nordeste');
INSERT INTO "Regiao" (nome) VALUES ('Sudeste');
INSERT INTO "Regiao" (nome) VALUES ('Sul');
INSERT INTO "Regiao" (nome) VALUES ('Centro-Oeste');

INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Acre', 'AC', 1);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Alagoas', 'AL', 2);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Amapá', 'AP', 1);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Amazonas', 'AM', 1);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Bahia', 'BA', 2);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Ceará', 'CE', 2);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Distrito Federal', 'DF', 5);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Espírito Santo', 'ES', 3);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Goiás', 'GO', 5);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Maranhão', 'MA', 2);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Mato Grosso', 'MT', 5);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Mato Grosso do Sul', 'MS', 5);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Minas Gerais', 'MG', 3);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Pará', 'PA', 1);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Paraíba', 'PB', 2);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Paraná', 'PR', 4);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Pernambuco', 'PE', 2);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Piauí', 'PI', 2);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Rio de Janeiro', 'RJ', 3);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Rio Grande do Norte', 'RN', 2);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Rio Grande do Sul', 'RS', 4);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Rondônia', 'RO', 1);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Roraima', 'RR', 1);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Santa Catarina', 'SC', 4);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('São Paulo', 'SP', 3);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Sergipe', 'SE', 2);
INSERT INTO "Estado" (nome, uf, codigoRegiao) VALUES ('Tocantins', 'TO', 1);

INSERT INTO "Municipio" (nome, uf) VALUES ('Aceguá', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Água Santa', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Agudo', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Ajuricaba', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Alecrim', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Alegrete', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Alegria', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Almirante Tamandaré do Sul', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Alpestre', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Alto Alegre', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Alto Feliz', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Alvorada', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Amaral Ferrador', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Ametista do Sul', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('André da Rocha', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Anta Gorda', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Antônio Prado', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Arambaré', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Araricá', 'RS');
INSERT INTO "Municipio" (nome, uf) VALUES ('Aratiba', 'RS');

