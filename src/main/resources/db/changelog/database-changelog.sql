--liquibase formatted sql logicalFilePath:db/changelog/database-changelog.sql

--changeset akvine:ISKRA-1
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'USER_ENTITY'
CREATE TABLE USER_ENTITY (
    ID                  BIGINT               NOT NULL PRIMARY KEY,
    UUID                VARCHAR(255)         NOT NULL,
    USERNAME            VARCHAR(255)         NOT NULL,
    EMAIL               VARCHAR(255)         NOT NULL,
    PASSWORD            VARCHAR(255)         NOT NULL,
    LANGUAGE            VARCHAR(64)          NOT NULL,
    CREATED_DATE        TIMESTAMP            NOT NULL,
    UPDATED_DATE        TIMESTAMP,
    IS_DELETED          BOOLEAN              NOT NULL,
    DELETED_DATE        TIMESTAMP
);
CREATE SEQUENCE SEQ_USER_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX USER_ID_INDX ON USER_ENTITY (ID);
CREATE UNIQUE INDEX USER_UUID_IS_DELETED_INDX ON USER_ENTITY (UUID, IS_DELETED);
CREATE UNIQUE INDEX USER_USERNAME_IS_DELETED_INDX ON USER_ENTITY (USERNAME, IS_DELETED);
CREATE INDEX USER_EMAIL_IS_DELETED_INDX ON USER_ENTITY (EMAIL, IS_DELETED);
--rollback not required

--changeset akvine:ISKRA-2
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'SPRING_SESSION' and table_schema = 'public';
CREATE TABLE SPRING_SESSION
(
    PRIMARY_ID            VARCHAR(36)    NOT NULL,
    SESSION_ID            VARCHAR(36),
    CREATION_TIME         NUMERIC(19, 0) NOT NULL,
    LAST_ACCESS_TIME      NUMERIC(19, 0) NOT NULL,
    MAX_INACTIVE_INTERVAL NUMERIC(10, 0) NOT NULL,
    EXPIRY_TIME           NUMERIC(19, 0) NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);
CREATE INDEX SPRING_SESSION_INDX ON SPRING_SESSION(LAST_ACCESS_TIME);
--rollback not required

--changeset akvine:ISKRA-3
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'SPRING_SESSION_ATTRIBUTES' and table_schema = 'public';
CREATE TABLE SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID VARCHAR(36),
    ATTRIBUTE_NAME     VARCHAR(200),
    ATTRIBUTE_BYTES    BYTEA,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
);
CREATE INDEX SPRING_SESSION_ATTRIBUTES_INDX on SPRING_SESSION_ATTRIBUTES (SESSION_PRIMARY_ID);
--rollback not required

--changeset akvine:ISKRA-4
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
    USER_ID                 BIGINT              NOT NULL,
    DATABASE_TYPE           VARCHAR(255)        NOT NULL,
    CREATED_DATE            TIMESTAMP           NOT NULL,
    UPDATED_DATE            TIMESTAMP,
    IS_DELETED              BOOLEAN             NOT NULL,
    DELETED_DATE            TIMESTAMP,
    FOREIGN KEY (USER_ID) REFERENCES USER_ENTITY(ID)
);
CREATE SEQUENCE SEQ_CONNECTION_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX UX_CONNECTION_ENTITY_ID ON CONNECTION_ENTITY (ID);
CREATE UNIQUE INDEX UX_CONNECTION_ENTITY_NAME ON CONNECTION_ENTITY (CONNECTION_NAME);
--rollback not required

--changeset akvine:ISKRA-5
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'PLAN_ENTITY'
CREATE TABLE PLAN_ENTITY (
    ID                  BIGINT NOT NULL PRIMARY KEY,
    UUID                VARCHAR(64)         NOT NULL,
    NAME                VARCHAR(255)        NOT NULL,
    CREATED_DATE        TIMESTAMP           NOT NULL,
    UPDATED_DATE        TIMESTAMP,
    CONNECTION_ID       BIGINT              NOT NULL,
    USER_ID             BIGINT              NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES USER_ENTITY(ID),
    FOREIGN KEY (CONNECTION_ID) REFERENCES CONNECTION_ENTITY(ID) ON DELETE SET NULL
);
CREATE SEQUENCE SEQ_PLAN_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX PLAN_ENTITY_ID_INDX ON PLAN_ENTITY (ID);
CREATE INDEX PLAN_ENTITY_UUID_INDX ON PLAN_ENTITY (UUID);

--changeset akvine:ISKRA-6
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

--changeset akvine:ISKRA-7
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

--changeset akvine:ISKRA-8
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'DICTIONARY_ENTITY'
CREATE TABLE DICTIONARY_ENTITY (
    ID                  BIGINT                  NOT NULL PRIMARY KEY,
    NAME                VARCHAR(255)            NOT NULL,
    DESCRIPTION         VARCHAR(255),
    IS_SYSTEM           BOOLEAN                 NOT NULL,
    VALUES              TEXT                    NOT NULL,
    CREATED_DATE        TIMESTAMP               NOT NULL,
    UPDATED_DATE        TIMESTAMP,
    USER_ID             BIGINT,
    FOREIGN KEY (USER_ID) REFERENCES USER_ENTITY(ID)
);
CREATE SEQUENCE SEQ_DICTIONARY_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE INDEX DICTIONARY_ENTITY_NAME_INDX ON DICTIONARY_ENTITY (NAME);
CREATE UNIQUE INDEX DICTIONARY_ENTITY_ID_INDX ON DICTIONARY_ENTITY (ID);
--rollback not required

--changeset akvine:ISKRA-9
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

--changeset akvine:ISKRA-10
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_SELECTED' and upper(table_name) = 'TABLE_ENTITY';
ALTER TABLE TABLE_ENTITY ADD IS_SELECTED BOOLEAN DEFAULT FALSE;

--changeset akvine:ISKRA-11
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_SELECTED' and upper(table_name) = 'COLUMN_ENTITY';
ALTER TABLE COLUMN_ENTITY ADD IS_SELECTED BOOLEAN DEFAULT TRUE;

--changeset akvine:ISKRA-12
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
    COLUMN_ID               BIGINT          NOT NULL,
    CREATED_DATE            TIMESTAMP       NOT NULL,
    UPDATED_DATE            TIMESTAMP,
    FOREIGN KEY (COLUMN_ID) REFERENCES COLUMN_ENTITY(ID) ON DELETE CASCADE
);
CREATE SEQUENCE SEQ_COLUMN_CONFIGURATION_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX COLUMN_CONFIGURATION_ENTITY_ID_IDX ON COLUMN_CONFIGURATION_ENTITY (ID);
--rollback not required

--changeset akvine:ISKRA-13
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'COLUMN_CONFIGURATION_DICTIONARY_ENTITY';
CREATE TABLE COLUMN_CONFIGURATION_DICTIONARY_ENTITY (
    ID                      BIGINT          NOT NULL PRIMARY KEY,
    COLUMN_CONFIGURATION_ID BIGINT          NOT NULL,
    DICTIONARY_ID           BIGINT          NOT NULL,
    CREATED_DATE            TIMESTAMP       NOT NULL,
    UPDATED_DATE            TIMESTAMP,
    IS_DELETED              BOOLEAN         NOT NULL,
    DELETED_DATE            TIMESTAMP,
    FOREIGN KEY (COLUMN_CONFIGURATION_ID) REFERENCES COLUMN_CONFIGURATION_ENTITY(ID),
    FOREIGN KEY (DICTIONARY_ID) REFERENCES DICTIONARY_ENTITY(ID)
);
CREATE SEQUENCE SEQ_COLUMN_CONFIGURATION_DICTIONARY_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE INDEX COLUMN_CONFIGURATION_DICTIONARY_ENTITY_ID_IDX ON COLUMN_CONFIGURATION_DICTIONARY_ENTITY (ID);
CREATE INDEX COLUMN_CONFIGURATION_ID_DICTIONARY_ID_IDX ON COLUMN_CONFIGURATION_DICTIONARY_ENTITY (COLUMN_CONFIGURATION_ID, DICTIONARY_ID);
--rollback not required

--changeset akvine:ISKRA-14
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'LANGUAGE' and upper(table_name) = 'DICTIONARY_ENTITY';
ALTER TABLE DICTIONARY_ENTITY ADD LANGUAGE VARCHAR(64) DEFAULT 'RU';

--changeset akvine:ISKRA-15
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'CONSTRAINTS' and upper(table_name) = 'COLUMN_ENTITY';
ALTER TABLE COLUMN_ENTITY ADD CONSTRAINTS VARCHAR(256);

--changeset akvine:ISKRA-16
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'PROCESS_UUID' and upper(table_name) = 'TABLE_PROCESS_ENTITY';
ALTER TABLE TABLE_PROCESS_ENTITY ADD PROCESS_UUID VARCHAR(128) NOT NULL;

--changeset akvine:ISKRA-17
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'LAST_PROCESS_UUID' and upper(table_name) = 'TABLE_PROCESS_ENTITY';
ALTER TABLE PLAN_ENTITY ADD LAST_PROCESS_UUID VARCHAR(128);

--changeset akvine:ISKRA-18
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'TABLE_CONFIGURATION_ENTITY';
CREATE TABLE TABLE_CONFIGURATION_ENTITY (
    ID                      BIGINT          NOT NULL PRIMARY KEY,
    NAME                    VARCHAR(255)    NOT NULL,
    ROWS_COUNT              INTEGER         NOT NULL,
    BATCH_SIZE              INTEGER         NOT NULL,
    TABLE_ID                BIGINT          NOT NULL,
    CREATED_DATE            TIMESTAMP       NOT NULL,
    UPDATED_DATE            TIMESTAMP,
    FOREIGN KEY (TABLE_ID) REFERENCES TABLE_ENTITY(ID)
);
CREATE SEQUENCE SEQ_TABLE_CONFIGURATION_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE INDEX TABLE_CONFIGURATION_ENTITY_NAME_IDX ON TABLE_CONFIGURATION_ENTITY(NAME);
CREATE UNIQUE INDEX TABLE_CONFIGURATION_ENTITY_ID_IDX ON TABLE_CONFIGURATION_ENTITY (ID);
--rollback not required

--changeset akvine:ISKRA-19
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'CONFIGURATION_ID' and upper(table_name) = 'TABLE_ENTITY';
ALTER TABLE TABLE_ENTITY ADD COLUMN CONFIGURATION_ID BIGINT;

--changeset akvine:ISKRA-20
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.table_constraints where constraint_type = 'FOREIGN KEY' and upper(constraint_name) = 'FK_CONFIGURATION' and upper(table_name) = 'TABLE_ENTITY';
ALTER TABLE TABLE_ENTITY ADD CONSTRAINT FK_CONFIGURATION FOREIGN KEY (CONFIGURATION_ID) REFERENCES TABLE_CONFIGURATION_ENTITY (ID);

--changeset akvine:ISKRA-21
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'REPEATABLE' and upper(table_name) = 'COLUMN_CONFIGURATION_ENTITY';
ALTER TABLE COLUMN_CONFIGURATION_ENTITY ADD REPEATABLE BOOLEAN NOT NULL DEFAULT TRUE;

--changeset akvine:ISKRA-22
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'TOTAL_ROWS_COUNT' and upper(table_name) = 'TABLE_PROCESS_ENTITY';
ALTER TABLE TABLE_PROCESS_ENTITY ADD TOTAL_ROWS_COUNT BIGINT NOT NULL DEFAULT 0;

--changeset akvine:ISKRA-23
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'DELETE_DATA_BEFORE_START' and upper(table_name) = 'TABLE_CONFIGURATION_ENTITY';
ALTER TABLE TABLE_CONFIGURATION_ENTITY ADD DELETE_DATA_BEFORE_START BOOLEAN NOT NULL DEFAULT FALSE;

--changeset akvine:ISKRA-24
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'DELETE_MODE' and upper(table_name) = 'TABLE_CONFIGURATION_ENTITY';
ALTER TABLE TABLE_CONFIGURATION_ENTITY ADD DELETE_MODE VARCHAR(64);

--changeset akvine:ISKRA-25
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'CLEAR_SCRIPT' and upper(table_name) = 'TABLE_CONFIGURATION_ENTITY';
ALTER TABLE TABLE_CONFIGURATION_ENTITY ADD CLEAR_SCRIPT VARCHAR(256);

--changeset akvine:ISKRA-26
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_CONVERT_TO_STRING' and upper(table_name) = 'COLUMN_CONFIGURATION_ENTITY';
ALTER TABLE COLUMN_CONFIGURATION_ENTITY ADD IS_CONVERT_TO_STRING BOOLEAN NOT NULL DEFAULT FALSE;

--changeset akvine:ISKRA-27
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'CONVERTERS' and upper(table_name) = 'COLUMN_CONFIGURATION_ENTITY';
ALTER TABLE COLUMN_CONFIGURATION_ENTITY ADD CONVERTERS TEXT;

--changeset akvine:ISKRA-28
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'POST_CONVERTERS' and upper(table_name) = 'COLUMN_CONFIGURATION_ENTITY';
ALTER TABLE COLUMN_CONFIGURATION_ENTITY ADD POST_CONVERTERS TEXT;

--changeset akvine:ISKRA-29
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'UUID' and upper(table_name) = 'DICTIONARY_ENTITY';
ALTER TABLE DICTIONARY_ENTITY ADD UUID VARCHAR(64) NOT NULL;

--changeset akvine:ISKRA-30
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'RELATIONS_MATRIX_JSON' and upper(table_name) = 'PLAN_ENTITY';
ALTER TABLE PLAN_ENTITY ADD RELATIONS_MATRIX_JSON TEXT;

--changeset akvine:ISKRA-31
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'REGEX_ENTITY';
CREATE TABLE REGEX_ENTITY (
    ID                  BIGINT                  PRIMARY KEY NOT NULL,
    UUID                VARCHAR(255)            NOT NULL,
    NAME                VARCHAR(255)            NOT NULL,
    PATTERN             TEXT                    NOT NULL,
    IS_SYSTEM           BOOLEAN                 NOT NULL,
    DESCRIPTION         VARCHAR(255),
    CREATED_DATE        TIMESTAMP               NOT NULL,
    UPDATED_DATE        TIMESTAMP,
    IS_DELETED          BOOLEAN                 NOT NULL,
    DELETED_DATE        TIMESTAMP,
    USER_ID             BIGINT,
    FOREIGN KEY (USER_ID) REFERENCES USER_ENTITY(ID)
);
CREATE SEQUENCE SEQ_REGEX_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE INDEX DICTIONARY_ENTITY_USER_ID_NAME_IS_DELETED_INDX ON REGEX_ENTITY (USER_ID, NAME, IS_DELETED);
CREATE UNIQUE INDEX REGEX_ENTITY_ID_INDX ON REGEX_ENTITY (ID);
--rollback not required

--changeset akvine:ISKRA-32
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_GENERATE_SCRIPTS_FOR_NOT_NULL' and upper(table_name) = 'PLAN_ENTITY';
ALTER TABLE PLAN_ENTITY ADD IS_GENERATE_SCRIPTS_FOR_NOT_NULL BOOLEAN DEFAULT FALSE;

--changeset akvine:ISKRA-33
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_GENERATE_SCRIPTS_FOR_INDEX' and upper(table_name) = 'PLAN_ENTITY';
ALTER TABLE PLAN_ENTITY ADD IS_GENERATE_SCRIPTS_FOR_INDEX BOOLEAN DEFAULT TRUE;

--changeset akvine:ISKRA-34
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_GENERATE_SCRIPTS_FOR_PRIMARY_KEY' and upper(table_name) = 'PLAN_ENTITY';
ALTER TABLE PLAN_ENTITY ADD IS_GENERATE_SCRIPTS_FOR_PRIMARY_KEY BOOLEAN DEFAULT TRUE;

--changeset akvine:ISKRA-35
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_GENERATE_SCRIPTS_FOR_TRIGGER' and upper(table_name) = 'PLAN_ENTITY';
ALTER TABLE PLAN_ENTITY ADD IS_GENERATE_SCRIPTS_FOR_TRIGGER BOOLEAN DEFAULT TRUE;

--changeset akvine:ISKRA-36
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_GENERATE_SCRIPTS_FOR_UNIQUE' and upper(table_name) = 'PLAN_ENTITY';
ALTER TABLE PLAN_ENTITY ADD IS_GENERATE_SCRIPTS_FOR_UNIQUE BOOLEAN DEFAULT FALSE;

--changeset akvine:ISKRA-37
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_GENERATE_SCRIPTS_FOR_DEFAULT' and upper(table_name) = 'PLAN_ENTITY';
ALTER TABLE PLAN_ENTITY ADD IS_GENERATE_SCRIPTS_FOR_DEFAULT BOOLEAN DEFAULT TRUE;

--changeset akvine:ISKRA-38
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'IS_GENERATE_SCRIPTS_FOR_CHECK' and upper(table_name) = 'PLAN_ENTITY';
ALTER TABLE PLAN_ENTITY ADD IS_GENERATE_SCRIPTS_FOR_CHECK BOOLEAN DEFAULT FALSE;

--changeset akvine:ISKRA-39
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'DROP_SCRIPTS' and upper(table_name) = 'TABLE_CONFIGURATION_ENTITY';
ALTER TABLE TABLE_CONFIGURATION_ENTITY ADD DROP_SCRIPTS TEXT;

--changeset akvine:ISKRA-40
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(column_name) = 'CREATE_SCRIPTS' and upper(table_name) = 'TABLE_CONFIGURATION_ENTITY';
ALTER TABLE TABLE_CONFIGURATION_ENTITY ADD CREATE_SCRIPTS TEXT;