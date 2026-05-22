-- Flyway managed version of 001_consent_logs.sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS consent_logs (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id      VARCHAR(64)  NOT NULL,
    full_name       VARCHAR(255) NOT NULL,
    dob             DATE         NOT NULL,
    mobile_number   VARCHAR(15)  NOT NULL,
    email           VARCHAR(255) NOT NULL,
    pan_card        VARCHAR(10)  NOT NULL,
    consent_text    TEXT         NOT NULL,
    consent_given   BOOLEAN      NOT NULL DEFAULT FALSE,
    ip_address      VARCHAR(45),
    user_agent      TEXT,
    consented_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_consent_logs_session_id ON consent_logs(session_id);
CREATE INDEX IF NOT EXISTS idx_consent_logs_pan_card   ON consent_logs(pan_card);
CREATE INDEX IF NOT EXISTS idx_consent_logs_mobile     ON consent_logs(mobile_number);
CREATE INDEX IF NOT EXISTS idx_consent_logs_created_at ON consent_logs(created_at DESC);
