CREATE TABLE team_tbl(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(50) NULL,
    country VARCHAR(50) NULL,
    api_football_id INTEGER NULL,
    founded INTEGER NULL,
    logo VARCHAR(60) NULL
);

ALTER TABLE player_tbl
    ADD CONSTRAINT fk_player_team FOREIGN KEY (team_id) REFERENCES team_tbl (id);