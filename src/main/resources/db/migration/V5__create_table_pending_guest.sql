CREATE TABLE pending_guest (
    id BIGINT auto_increment NOT NULL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    guest_name varchar(100) NOT NULL,
    guest_email varchar(100) NULL,
    CONSTRAINT pending_guest_task_FK FOREIGN KEY (task_id) REFERENCES tasks(id)
)