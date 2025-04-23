--liquibase formatted sql logicalFilePath:db/changelog/database-changelog.sql

--changeset akvine:ISKRA-1-1
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'USER_ENTITY'
CREATE TABLE TABLE_PROCESS_ENTITY (
    ID                  BIGINT NOT NULL PRIMARY KEY,
    UUID                VARCHAR(64)         NOT NULL,
    PID                 VARCHAR(255) NOT NULL,
    TABLE_NAME          VARCHAR(255) NOT NULL,
    SUCCESS_ROWS_COUNT  BIGINT,
    STATE               VARCHAR(255) NOT NULL,
    STARTED_DATE        TIMESTAMP,
    COMPLETED_DATE      TIMESTAMP,
    ERROR_MESSAGE       VARCHAR(255),
    CREATED_DATE        TIMESTAMP NOT NULL,
    UPDATED_DATE        TIMESTAMP,
    IS_DELETED          BOOLEAN NOT NULL,
    DELETED_DATE        TIMESTAMP
);

CREATE SEQUENCE SEQ_TABLE_PROCESS_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX TABLE_PROCESS_ENTITY_ID_INDX ON TABLE_PROCESS_ENTITY (ID);
CREATE INDEX TABLE_PROCESS_ENTITY_PID_STATE_INDX ON TABLE_PROCESS_ENTITY (PID, STATE);
--rollback not required