CREATE TABLE tb_users (
    id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL,
    fgActive INTEGER NOT NULL DEFAULT 0,
    createdat TIMESTAMP DEFAULT NOW(),
    updatedAt TIMESTAMP DEFAULT NOW(),
    CONSTRAINT tb_users_pk PRIMARY KEY (id)
);