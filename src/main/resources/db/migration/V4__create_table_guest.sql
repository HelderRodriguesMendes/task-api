CREATE TABLE guests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_guests_task FOREIGN KEY (task_id)
        REFERENCES tasks(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_guests_user FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);