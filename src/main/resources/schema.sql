CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_user BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    priority VARCHAR(20) NOT NULL DEFAULT 'LOW',
    status VARCHAR(20) NOT NULL DEFAULT 'TODO',
    deadline TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_tasks_users
                   FOREIGN KEY(id_user)
                       REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT check_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    CONSTRAINT check_status CHECK (status IN ('TODO','IN_PROGRESS','DONE'))
);

CREATE TABLE tags (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE task_tag (
    id_task BIGINT NOT NULL,
    id_tag BIGINT NOT NULL,

    PRIMARY KEY (id_task, id_tag),

    CONSTRAINT fk_task_tag_id_task FOREIGN KEY(id_task) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_tag_id_tag FOREIGN KEY(id_tag) REFERENCES tags(id) ON DELETE CASCADE
);

CREATE TABLE roles (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    date_added TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO roles (name, description) VALUES
('ADMINISTRATOR', 'Full access to all application functions; manages users.'),
('USER', 'Access to owned assets; can manage their own tasks.'),
('GUEST', 'Cannot modify tasks; view-only access.');

CREATE TABLE users_roles (
    id_user BIGINT NOT NULL,
    id_role BIGINT NOT NULL,

    PRIMARY KEY (id_user, id_role),

    CONSTRAINT fk_users_roles_id_user FOREIGN KEY(id_user) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_users_roles_id_role FOREIGN KEY(id_role) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE INDEX idx_tasks_user_id ON tasks(id_user);
CREATE INDEX idx_task_tag_id_task ON task_tag(id_task);
CREATE INDEX idx_task_tag_id_tag ON task_tag(id_tag);
CREATE INDEX idx_users_roles_id_user ON users_roles(id_user);
CREATE INDEX idx_users_roles_id_role ON users_roles(id_role);