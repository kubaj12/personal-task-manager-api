CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP--TIMESTAMPTZ w postgresql
);

CREATE TABLE tasks (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    priority VARCHAR(20) NOT NULL DEFAULT 'LOW',
    status VARCHAR(20) NOT NULL DEFAULT 'TODO',
    deadline TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_tasks_users
                   FOREIGN KEY(user_id)
                       REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT check_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),

    CONSTRAINT check_status CHECK (status IN ('TODO','IN_PROGRESS','DONE'))

);

CREATE TABLE tags (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_tags_users FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE task_tag (
    id_task BIGINT NOT NULL,
    id_tag BIGINT NOT NULL,

    CONSTRAINT fk_task_tag_id_task FOREIGN KEY(id_task) REFERENCES tasks(id) ON DELETE CASCADE,

    CONSTRAINT fk_task_tag_id_tag FOREIGN KEY(id_tag) REFERENCES tags(id) ON DELETE CASCADE
)