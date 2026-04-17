INSERT INTO users (email, password)
SELECT 'admin@test.com', '$2a$10$N.Zq2NxKqfN8R3VYKh4QyeMqQqYqYqYqYqYqYqYqYqYqYqYqYqY'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@test.com');

INSERT INTO users (email, password)
SELECT 'user@test.com', '$2a$10$M.Zq2MxKqfN8R3VYKh4QyeMqQqYqYqYqYqYqYqYqYqYqYqYqYqY'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user@test.com');