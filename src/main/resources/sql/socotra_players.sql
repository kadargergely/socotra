DROP TABLE socotra_players;

CREATE TABLE socotra_players (
   player_id        NUMBER          NOT NULL
  ,server_id        NUMBER          NOT NULL
  ,player_name      VARCHAR2(50)    NOT NULL
  ,player_type      VARCHAR2(15)    NOT NULL
  ,connected_flag   NUMBER(1)       NOT NULL
  ,password         VARCHAR2(100)
  ,loaded_flag      NUMBER(1)
)

ALTER TABLE socotra_players
   ADD CONSTRAINT socotra_players_pk PRIMARY KEY (player_id);

ALTER TABLE socotra_players
   ADD CONSTRAINT socotra_players_u1 UNIQUE (server_id, player_name);

ALTER TABLE socotra_players
   ADD CONSTRAINT socotra_players_fk FOREIGN KEY (server_id) REFERENCES socotra_servers (server_id);