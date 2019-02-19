-- DROP TABLE accounts;

CREATE TABLE accounts
(
    id uuid PRIMARY KEY,
    currency character varying(15),
    created timestamp,
    last_changed bigint,
    balance numeric,
    first_name character varying(255),
    last_name character varying(255),
    gender smallint,
    birthday date
)

ALTER TABLE accounts
    OWNER to minipay;

GRANT ALL ON TABLE accounts TO minipay;