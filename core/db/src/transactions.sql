-- DROP TABLE transactions;

CREATE TABLE transactions
(
    id_from uuid NOT NULL,
    id_to uuid NOT NULL,
    currency character varying(15),
    amount numeric,
    "timestamp" timestamp
);

ALTER TABLE transactions
    OWNER to minipay;

GRANT ALL ON TABLE transactions TO minipay;