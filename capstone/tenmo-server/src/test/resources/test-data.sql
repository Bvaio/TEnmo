BEGIN TRANSACTION;

CREATE TABLE transfer_type (
	transfer_type_id serial NOT NULL,
	transfer_type_desc varchar(10) NOT NULL,
	CONSTRAINT PK_transfer_type PRIMARY KEY (transfer_type_id)
);

CREATE TABLE transfer_status (
	transfer_status_id serial NOT NULL,
	transfer_status_desc varchar(10) NOT NULL,
	CONSTRAINT PK_transfer_status PRIMARY KEY (transfer_status_id)
);

CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance decimal(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;

CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	transfer_type_id int NOT NULL,
	transfer_status_id int NOT NULL,
	account_from int NOT NULL,
	account_to int NOT NULL,
	amount decimal(13, 2) NOT NULL,
	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT FK_transfer_account_from FOREIGN KEY (account_from) REFERENCES account (account_id),
	CONSTRAINT FK_transfer_account_to FOREIGN KEY (account_to) REFERENCES account (account_id),
	CONSTRAINT FK_transfer_transfer_status FOREIGN KEY (transfer_status_id) REFERENCES transfer_status (transfer_status_id),
	CONSTRAINT FK_transfer_transfer_type FOREIGN KEY (transfer_type_id) REFERENCES transfer_type (transfer_type_id),
	CONSTRAINT CK_transfer_not_same_account CHECK (account_from <> account_to),
	CONSTRAINT CK_transfer_amount_gt_0 CHECK (amount > 0)
);

INSERT INTO transfer_status (transfer_status_desc) VALUES ('Pending');
INSERT INTO transfer_status (transfer_status_desc) VALUES ('Approved');
INSERT INTO transfer_status (transfer_status_desc) VALUES ('Rejected');

INSERT INTO transfer_type (transfer_type_desc) VALUES ('Request');
INSERT INTO transfer_type (transfer_type_desc) VALUES ('Send');

INSERT INTO tenmo_user (user_id, username, password_hash)
VALUES (500, 'weLovesTests', 'unknown');
INSERT INTO tenmo_user (user_id, username, password_hash)
VALUES (501, 'sleepyTester', 'unknown');
INSERT INTO tenmo_user (user_id, username, password_hash)
VALUES (502, 'almostDone', 'unknown') ;
INSERT INTO tenmo_user (user_id, username, password_hash)
VALUES (503, 'funTimes', 'unknown');

INSERT INTO account (account_id, user_id, balance) VALUES (400,500, 500) ;
INSERT INTO account (account_id, user_id, balance) VALUES (401, 501, 1000) ;
INSERT INTO account (account_id, user_id, balance) VALUES (402, 502, 500) ;
INSERT INTO account (account_id, user_id, balance) VALUES (403, 503, 50) ;

INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES (300, 1, 2, 400, 401, 50);
INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES (301, 1, 2, 401, 402, 100);
INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES (302, 1, 2, 402, 403, 100);

COMMIT;
