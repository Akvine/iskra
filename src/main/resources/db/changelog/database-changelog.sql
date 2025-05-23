--liquibase formatted sql logicalFilePath:db/changelog/database-changelog.sql

--changeset akvine:ISKRA-1-1
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'CONNECTION_ENTITY'
CREATE TABLE CONNECTION_ENTITY (
    ID BIGINT               PRIMARY KEY,
    CONNECTION_NAME         VARCHAR(255)        NOT NULL,
    DATABASE_NAME           VARCHAR(255),
    SCHEMA                  VARCHAR(255),
    HOST                    VARCHAR(255)        NOT NULL,
    PORT                    VARCHAR(255)        NOT NULL,
    USERNAME                VARCHAR(255),
    PASSWORD                VARCHAR(255),
    DATABASE_TYPE           VARCHAR(255)        NOT NULL,
    CREATED_DATE            TIMESTAMP           NOT NULL,
    UPDATED_DATE            TIMESTAMP,
    IS_DELETED              BOOLEAN             NOT NULL,
    DELETED_DATE            TIMESTAMP
);
CREATE SEQUENCE SEQ_CONNECTION_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX UX_CONNECTION_ENTITY_ID ON CONNECTION_ENTITY (ID);
CREATE UNIQUE INDEX UX_CONNECTION_ENTITY_NAME ON CONNECTION_ENTITY (CONNECTION_NAME);
--rollback not required

--changeset akvine:ISKRA-1-2
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'PLAN_ENTITY'
CREATE TABLE PLAN_ENTITY (
    ID                  BIGINT NOT NULL PRIMARY KEY,
    UUID                VARCHAR(64)         NOT NULL,
    NAME                VARCHAR(255)        NOT NULL,
    CREATED_DATE        TIMESTAMP           NOT NULL,
    UPDATED_DATE        TIMESTAMP,
    CONNECTION_ID       BIGINT              NOT NULL,
    FOREIGN KEY (CONNECTION_ID) REFERENCES CONNECTION_ENTITY(ID) ON DELETE SET NULL
);
CREATE SEQUENCE SEQ_PLAN_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX PLAN_ENTITY_ID_INDX ON PLAN_ENTITY (ID);
CREATE INDEX PLAN_ENTITY_UUID_INDX ON PLAN_ENTITY (UUID);

--changeset akvine:ISKRA-1-3
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'TABLE_PROCESS_ENTITY'
CREATE TABLE TABLE_PROCESS_ENTITY (
    ID                  BIGINT NOT NULL PRIMARY KEY,
    PID                 VARCHAR(255) NOT NULL,
    TABLE_NAME          VARCHAR(255) NOT NULL,
    SUCCESS_ROWS_COUNT  BIGINT,
    STATE               VARCHAR(255) NOT NULL,
    STARTED_DATE        TIMESTAMP,
    COMPLETED_DATE      TIMESTAMP,
    ERROR_MESSAGE       VARCHAR(512),
    CREATED_DATE        TIMESTAMP NOT NULL,
    UPDATED_DATE        TIMESTAMP,
    IS_DELETED          BOOLEAN NOT NULL,
    DELETED_DATE        TIMESTAMP,
    PLAN_ID             BIGINT,
    FOREIGN KEY (PLAN_ID) REFERENCES PLAN_ENTITY(ID) ON DELETE SET NULL
);
CREATE SEQUENCE SEQ_TABLE_PROCESS_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX TABLE_PROCESS_ENTITY_ID_INDX ON TABLE_PROCESS_ENTITY (ID);
CREATE INDEX TABLE_PROCESS_ENTITY_PID_STATE_INDX ON TABLE_PROCESS_ENTITY (PID, STATE);
--rollback not required

--changeset akvine:ISKRA-1-4
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'TABLE_ENTITY'
CREATE TABLE TABLE_ENTITY (
    ID                      BIGINT                  NOT NULL PRIMARY KEY,
    NAME                    VARCHAR(255)            NOT NULL,
    SCHEMA                  VARCHAR(128),
    DATABASE                VARCHAR(128),
    CREATED_DATE            TIMESTAMP               NOT NULL,
    UPDATED_DATE            TIMESTAMP,
    PLAN_ID                 BIGINT,
    FOREIGN KEY (PLAN_ID) REFERENCES PLAN_ENTITY(ID) ON DELETE SET NULL
);
CREATE SEQUENCE SEQ_TABLE_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX TABLE_ENTITY_ID_INDX ON TABLE_PROCESS_ENTITY (ID);
--rollback not required

--changeset akvine:ISKRA-1-5
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'DICTIONARY_ENTITY'
CREATE TABLE DICTIONARY_ENTITY (
    ID           BIGINT                  NOT NULL PRIMARY KEY,
    NAME         VARCHAR(255)            NOT NULL,
    DESCRIPTION  VARCHAR(255),
    IS_SYSTEM    BOOLEAN                 NOT NULL,
    VALUES       TEXT                    NOT NULL,
    CREATED_DATE TIMESTAMP               NOT NULL,
    UPDATED_DATE TIMESTAMP
);
CREATE SEQUENCE SEQ_DICTIONARY_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX DICTIONARY_ENTITY_ID_INDX ON DICTIONARY_ENTITY (ID);

--rollback not required

--changeset akvine:ISKRA-1-6
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'COLUMN_ENTITY'
CREATE TABLE COLUMN_ENTITY (
    ID                   BIGINT          NOT NULL PRIMARY KEY,
    UUID                 VARCHAR(64),
    COLUMN_NAME          VARCHAR(255)    NOT NULL,
    RAW_DATA_TYPE        VARCHAR(255)    NOT NULL,
    ORDER_INDEX          INT             NOT NULL,
    SIZE                 INT             NOT NULL,
    IS_GENERATED_ALWAYS  BOOLEAN         NOT NULL,
    PRIMARY_KEY          BOOLEAN         NOT NULL,
    DATABASE             VARCHAR(128),
    SCHEMA               VARCHAR(128),
    TABLE_ID             BIGINT,
    CREATED_DATE         TIMESTAMP       NOT NULL,
    UPDATED_DATE         TIMESTAMP,
    CREATED_BY           VARCHAR(50),
    UPDATED_BY           VARCHAR(50),
    FOREIGN KEY (TABLE_ID) REFERENCES TABLE_ENTITY(ID) ON DELETE CASCADE
);
CREATE SEQUENCE SEQ_COLUMN_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX COLUMN_ENTITY_ID_IDX ON COLUMN_ENTITY (ID);
--rollback not required

--changeset akvine:ISKRA-1-7
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_SELECTED' and upper(table_name) = 'TABLE_ENTITY';
ALTER TABLE TABLE_ENTITY ADD IS_SELECTED BOOLEAN DEFAULT FALSE;

--changeset akvine:ISKRA-1-8
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_SELECTED' and upper(table_name) = 'COLUMN_ENTITY';
ALTER TABLE COLUMN_ENTITY ADD IS_SELECTED BOOLEAN DEFAULT TRUE;

--changeset akvine:ISKRA-1-9
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'COLUMN_CONFIGURATION_ENTITY';

CREATE TABLE COLUMN_CONFIGURATION_ENTITY (
    ID                      BIGINT          NOT NULL PRIMARY KEY,
    NAME                    VARCHAR(255)    NOT NULL,
    SELECTED                BOOLEAN         NOT NULL,
    TYPE                    VARCHAR(255)    NOT NULL,
    GENERATION_STRATEGY     VARCHAR(255)    NOT NULL,
    IS_UNIQUE               BOOLEAN         NOT NULL,
    IS_NOT_NULL             BOOLEAN         NOT NULL,
    RANGE_TYPE              VARCHAR(255),
    START                   VARCHAR(255),
    END_VAR                 VARCHAR(255),
    STEP                    VARCHAR(255),
    IS_VALID                BOOLEAN,
    REGEXPS                 TEXT,
    DICTIONARY_ID           BIGINT,
    COLUMN_ID               BIGINT          NOT NULL,
    CREATED_DATE            TIMESTAMP       NOT NULL,
    UPDATED_DATE            TIMESTAMP,
    FOREIGN KEY (DICTIONARY_ID) REFERENCES DICTIONARY_ENTITY(ID) ON DELETE SET NULL,
    FOREIGN KEY (COLUMN_ID) REFERENCES COLUMN_ENTITY(ID) ON DELETE CASCADE
);
CREATE SEQUENCE SEQ_COLUMN_CONFIGURATION_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX COLUMN_CONFIGURATION_ENTITY_ID_IDX ON COLUMN_CONFIGURATION_ENTITY (ID);
--rollback not required

--changeset akvine:ISKRA-1-10
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'LANGUAGE' and upper(table_name) = 'DICTIONARY_ENTITY';
ALTER TABLE DICTIONARY_ENTITY ADD LANGUAGE VARCHAR(64) DEFAULT 'RU';

--changeset akvine:ISKRA-1-11
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'CONSTRAINTS' and upper(table_name) = 'COLUMN_ENTITY';
ALTER TABLE COLUMN_ENTITY ADD CONSTRAINTS VARCHAR(256);

--changeset akvine:ISKRA-1-12
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'PROCESS_UUID' and upper(table_name) = 'TABLE_PROCESS_ENTITY';
ALTER TABLE TABLE_PROCESS_ENTITY ADD PROCESS_UUID VARCHAR(128) NOT NULL;

--changeset akvine:ISKRA-1-13
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'LAST_PROCESS_UUID' and upper(table_name) = 'TABLE_PROCESS_ENTITY';
ALTER TABLE PLAN_ENTITY ADD LAST_PROCESS_UUID VARCHAR(128);