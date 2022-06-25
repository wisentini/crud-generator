--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2 (Debian 14.2-1.pgdg110+1)
-- Dumped by pg_dump version 14.2 (Debian 14.2-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE ONLY public."Regiao" DROP CONSTRAINT "Regiao_pkey";
ALTER TABLE ONLY public."Municipio" DROP CONSTRAINT "Municipio_pkey";
ALTER TABLE ONLY public."Estado" DROP CONSTRAINT "Estado_pkey";
DROP TABLE public."Regiao";
DROP TABLE public."Municipio";
DROP TABLE public."Estado";
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: Estado; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Estado" (
    codigo integer NOT NULL,
    nome character varying(63) NOT NULL,
    uf character(2) NOT NULL,
    codigoregiao integer NOT NULL
);


ALTER TABLE public."Estado" OWNER TO postgres;

--
-- Name: Estado_codigo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Estado" ALTER COLUMN codigo ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Estado_codigo_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: Municipio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Municipio" (
    codigo integer NOT NULL,
    nome character varying(255) NOT NULL,
    uf character(2) NOT NULL
);


ALTER TABLE public."Municipio" OWNER TO postgres;

--
-- Name: Municipio_codigo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Municipio" ALTER COLUMN codigo ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Municipio_codigo_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: Regiao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Regiao" (
    codigo integer NOT NULL,
    nome character varying(31) NOT NULL
);


ALTER TABLE public."Regiao" OWNER TO postgres;

--
-- Name: Regiao_codigo_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."Regiao" ALTER COLUMN codigo ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Regiao_codigo_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: Estado; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Estado" (codigo, nome, uf, codigoregiao) FROM stdin;
1	Acre	AC	1
2	Alagoas	AL	2
3	Amapá	AP	1
4	Amazonas	AM	1
5	Bahia	BA	2
6	Ceará	CE	2
7	Distrito Federal	DF	5
8	Espírito Santo	ES	3
9	Goiás	GO	5
10	Maranhão	MA	2
11	Mato Grosso	MT	5
12	Mato Grosso do Sul	MS	5
13	Minas Gerais	MG	3
14	Pará	PA	1
15	Paraíba	PB	2
16	Paraná	PR	4
17	Pernambuco	PE	2
18	Piauí	PI	2
19	Rio de Janeiro	RJ	3
20	Rio Grande do Norte	RN	2
21	Rio Grande do Sul	RS	4
22	Rondônia	RO	1
23	Roraima	RR	1
24	Santa Catarina	SC	4
25	São Paulo	SP	3
26	Sergipe	SE	2
27	Tocantins	TO	1
\.


--
-- Data for Name: Municipio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Municipio" (codigo, nome, uf) FROM stdin;
1	Aceguá	RS
2	Água Santa	RS
3	Agudo	RS
4	Ajuricaba	RS
5	Alecrim	RS
6	Alegrete	RS
7	Alegria	RS
8	Almirante Tamandaré do Sul	RS
9	Alpestre	RS
10	Alto Alegre	RS
11	Alto Feliz	RS
12	Alvorada	RS
13	Amaral Ferrador	RS
14	Ametista do Sul	RS
15	André da Rocha	RS
16	Anta Gorda	RS
17	Antônio Prado	RS
18	Arambaré	RS
19	Araricá	RS
20	Aratiba	RS
\.


--
-- Data for Name: Regiao; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Regiao" (codigo, nome) FROM stdin;
1	Norte
2	Nordeste
3	Sudeste
4	Sul
5	Centro-Oeste
\.


--
-- Name: Estado_codigo_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Estado_codigo_seq"', 27, true);


--
-- Name: Municipio_codigo_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Municipio_codigo_seq"', 20, true);


--
-- Name: Regiao_codigo_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Regiao_codigo_seq"', 5, true);


--
-- Name: Estado Estado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Estado"
    ADD CONSTRAINT "Estado_pkey" PRIMARY KEY (codigo);


--
-- Name: Municipio Municipio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Municipio"
    ADD CONSTRAINT "Municipio_pkey" PRIMARY KEY (codigo);


--
-- Name: Regiao Regiao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Regiao"
    ADD CONSTRAINT "Regiao_pkey" PRIMARY KEY (codigo);


--
-- PostgreSQL database dump complete
--

