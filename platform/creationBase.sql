
CREATE SEQUENCE seq_joueur
    START 1
    INCREMENT 1
    MAXVALUE 2147483647
    MINVALUE 1
    CACHE 1;


CREATE TABLE partie (
    idpartie integer NOT NULL,
    "type" integer NOT NULL,
    annonce boolean NOT NULL,
    dtdebut timestamp with time zone NOT NULL,
    dtfin timestamp with time zone NOT NULL,
    pseudo character varying(80)[] NOT NULL,
    score integer[] NOT NULL,
    idtournoi integer
);


CREATE TABLE joueur (
    cdjoueur integer DEFAULT nextval('seq_joueur'::text) NOT NULL,
    pseudo character varying(80) NOT NULL,
    avatar character varying(255) NOT NULL,
    privilege integer NOT NULL,
    dtmodif timestamp with time zone DEFAULT now() NOT NULL,
    couleur character(7) NOT NULL,
    avatarlow character varying(255)
);

ALTER TABLE ONLY partie
    ADD CONSTRAINT pk_partie PRIMARY KEY (idpartie, dtdebut);

ALTER TABLE ONLY joueur
    ADD CONSTRAINT pk_joueur PRIMARY KEY (cdjoueur);

-- Ajouts le 20/07/05
create table PARAM (
    CD_PARAM     numeric(2)      not null,
    LIB_PARAM    varchar(40)     not null,
    VAL_PARAM    varchar(200)    not null,
        constraint PK_PARAM primary key (CD_PARAM)
);

create sequence SEQ_PARAM;

alter table PARAM alter CD_PARAM set default nextval('SEQ_PARAM');
