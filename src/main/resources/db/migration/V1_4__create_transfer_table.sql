CREATE TABLE transfer_tbl(
    id SERIAL PRIMARY KEY,
    team_in_id INTEGER NOT NULL,
    team_out_id INTEGER NOT NULL,
    player_id INTEGER NOT NULL,
    transfer_date date NULL,
    transfer_type VARCHAR(50) NOT NULL
);

ALTER TABLE transfer_tbl
    ADD CONSTRAINT fk_transfer_team_in FOREIGN KEY (team_in_id) REFERENCES team_tbl (id);
    
ALTER TABLE transfer_tbl
    ADD CONSTRAINT fk_transfer_team_out FOREIGN KEY (team_out_id) REFERENCES team_tbl (id);

ALTER TABLE transfer_tbl
    ADD CONSTRAINT fk_transfer_player FOREIGN KEY (player_id) REFERENCES player_tbl (id);