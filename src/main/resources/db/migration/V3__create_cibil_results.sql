CREATE TABLE IF NOT EXISTS cibil_results (
    id                   UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id           VARCHAR(64)  NOT NULL UNIQUE,
    mobile_number        VARCHAR(15)  NOT NULL,
    pan_card             VARCHAR(10)  NOT NULL,
    full_name            VARCHAR(255) NOT NULL,
    credit_score         INT,
    score_band           VARCHAR(50),
    score_as_of_date     DATE,
    credit_summary       JSONB,
    loan_eligibility     JSONB,
    raw_html_snapshot    TEXT,
    screenshot_path      VARCHAR(512),
    extracted_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_at           TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_cibil_results_session_id   ON cibil_results(session_id);
CREATE INDEX IF NOT EXISTS idx_cibil_results_pan_card     ON cibil_results(pan_card);
CREATE INDEX IF NOT EXISTS idx_cibil_results_mobile       ON cibil_results(mobile_number);
CREATE INDEX IF NOT EXISTS idx_cibil_results_credit_score ON cibil_results(credit_score DESC);
CREATE INDEX IF NOT EXISTS idx_cibil_results_summary_gin  ON cibil_results USING GIN (credit_summary);
CREATE INDEX IF NOT EXISTS idx_cibil_results_elig_gin     ON cibil_results USING GIN (loan_eligibility);
