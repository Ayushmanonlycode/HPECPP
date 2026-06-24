-- User Service seed data
-- Provides a test user record matching the Keycloak testuser account.
-- Password stored as BCrypt hash of 'testpass123'

INSERT INTO users (id, username, email, password, first_name, last_name, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'testuser',
    'testuser@jpetstore.local',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQyCl4F5ySk.UQiR.B5P6JeKe',
    'Test',
    'User',
    'ACTIVE'
)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (id, username, email, password, first_name, last_name, status)
VALUES (
    '00000000-0000-0000-0000-000000000002',
    'admin',
    'admin@jpetstore.local',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQyCl4F5ySk.UQiR.B5P6JeKe',
    'Admin',
    'User',
    'ACTIVE'
)
ON CONFLICT (username) DO NOTHING;
