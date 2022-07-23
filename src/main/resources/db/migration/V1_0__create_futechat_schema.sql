CREATE TABLE player_tbl(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    api_football_id INTEGER NOT NULL,
    team_id INTEGER NOT NULL,
    height VARCHAR(50) NULL
);