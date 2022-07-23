CREATE TABLE team_tbl(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(50) NULL,
    country VARCHAR(50) NOT NULL,
    api_football_id INTEGER NOT NULL,
    founded INTEGER NULL
);

ALTER TABLE player_tbl
    ADD CONSTRAINT fk_player_team FOREIGN KEY (team_id) REFERENCES team_tbl (id);