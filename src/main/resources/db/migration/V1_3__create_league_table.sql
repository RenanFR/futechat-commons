CREATE TABLE league_tbl(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    league_type VARCHAR(50) NULL,
    country VARCHAR(50) NULL,
    api_football_id INTEGER NULL,
    logo VARCHAR(60) NULL
);

ALTER TABLE team_tbl
  ADD league_id INTEGER;
  
ALTER TABLE team_tbl
    ADD CONSTRAINT fk_team_league FOREIGN KEY (league_id) REFERENCES league_tbl (id);