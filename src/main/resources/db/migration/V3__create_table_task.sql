CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    local VARCHAR(255) NOT NULL,
    date_time DATETIME NOT NULL,
    created_id BIGINT,
    CONSTRAINT fk_tasks_created_user FOREIGN KEY (created_id)
        REFERENCES users(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);