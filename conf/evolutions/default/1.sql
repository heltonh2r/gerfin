# Gerfin schema

# --- !Ups

CREATE sequence tipo_despesa_seq;

CREATE TABLE tipo_despesa(
    id integer NOT NULL DEFAULT nextval('tipo_despesa_seq'),
    name varchar(255),
    id_despesa_pai integer
);

CREATE sequence centro_custo_seq;
CREATE TABLE CENTRO_CUSTO(
    id integer not null default nextval('centro_custo_seq'),
    name varchar(255),
    budget decimal(10,2),
    closing_date date
);


CREATE sequence despesa_seq;
CREATE TABLE despesa(
   id integer not null default nextval('despesa_seq'),
   description varchar(255),
   cost_value decimal(10,2),
   parcel_numbers integer
);

CREATE sequence lancamento_seq;

CREATE TABLE lancamento_despesa(
   id integer not null default nextval('lancamento_seq'),
   cost_date timestamp,
   parcel_value decimal(10,2),
   id_centro_custo integer,
   id_despesa integer
);

# --- !Downs
DROP TABLE  tipo_despesa;
DROP TABLE  despesa;
DROP TABLE  lancamento_despesa;
DROP TABLE  CENTRO_CUSTO;

DROP SEQUENCE tipo_despesa_seq;
DROP SEQUENCE centro_custo_seq;
DROP SEQUENCE despesa_seq;
DROP SEQUENCE lancamento_seq;