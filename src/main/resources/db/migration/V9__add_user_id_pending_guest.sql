ALTER TABLE pending_guest ADD COLUMN user_id BIGINT NULL;

ALTER TABLE pending_guest
    ADD CONSTRAINT fk_pending_user
        FOREIGN KEY (user_id) REFERENCES users(id);