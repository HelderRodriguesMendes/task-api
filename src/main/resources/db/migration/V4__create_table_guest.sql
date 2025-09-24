CREATE TABLE guests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_guest_task FOREIGN KEY (task_id) REFERENCES tasks(id),
    CONSTRAINT fk_guest_user FOREIGN KEY (user_id) REFERENCES users(id)
);