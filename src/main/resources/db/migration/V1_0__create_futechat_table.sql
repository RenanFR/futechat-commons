CREATE TABLE player_tbl(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    api_football_id INTEGER NOT NULL,
    team_id INTEGER NOT NULL,
    height VARCHAR(50) NULL,
    birth date NULL,
    place_of_birth VARCHAR(50) NULL,
    country_of_birth VARCHAR(50) NULL,
    nationality VARCHAR(50) NULL,
    weight VARCHAR(50) NULL,
    photo VARCHAR(60) NULL
);