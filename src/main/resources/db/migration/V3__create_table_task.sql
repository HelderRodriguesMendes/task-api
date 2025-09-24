CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_id BIGINT NOT NULL,
    CONSTRAINT fk_task_user FOREIGN KEY (created_id) REFERENCES users(id)
);