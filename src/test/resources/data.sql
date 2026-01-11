INSERT INTO users (email, password_hash, created_at) VALUES ('test@wp.pl', 'testhash','2026-01-01 14:30:00+02');

INSERT INTO tasks (user_id, title, description, priority, status) VALUES ((SELECT id FROM users WHERE email == 'test@wp.pl'), 'test title', 'description title', 'LOW', 'TODO')