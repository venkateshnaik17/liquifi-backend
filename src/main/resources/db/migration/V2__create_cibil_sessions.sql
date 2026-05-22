CREATE TYPE IF NOT EXISTS session_status AS ENUM (
    'PENDING','BROWSER_LAUNCHED','FORM_FILLED','OTP_AWAITED',
    'OTP_SUBMITTED','EXTRACTING','COMPLETED','FAILED','EXPIRED'
);

CREATE TABLE IF NOT EXISTS cibil_sessions (
    id              UUID           PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id      VARCHAR(64)    NOT NULL UNIQUE,
    full_name       VARCHAR(255)   NOT NULL,
    dob             DATE           NOT NULL,
    mobile_number   VARCHAR(15)    NOT NULL,
    email           VARCHAR(255)   NOT NULL,
    pan_card        VARCHAR(10)    NOT NULL,
    status          session_status NOT NULL DEFAULT 'PENDING',
    error_message   TEXT,
    worker_node     VARCHAR(128),
    retry_count     INT            NOT NULL DEFAULT 0,
    expires_at      TIMESTAMPTZ    NOT NULL DEFAULT (NOW() + INTERVAL '30 minutes'),
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_cibil_sessions_session_id ON cibil_sessions(session_id);
CREATE INDEX IF NOT EXISTS idx_cibil_sessions_status     ON cibil_sessions(status);
CREATE INDEX IF NOT EXISTS idx_cibil_sessions_expires_at ON cibil_sessions(expires_at);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$ BEGIN NEW.updated_at = NOW(); RETURN NEW; END; $$ LANGUAGE plpgsql;

CREATE TRIGGER trg_cibil_sessions_updated_at
    BEFORE UPDATE ON cibil_sessions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
