DROP TABLE socotra_servers;

CREATE TABLE socotra_servers (
   server_id          NUMBER         NOT NULL
  ,server_name        VARCHAR2(50)   NOT NULL
  ,ip_address         VARCHAR2(50)   NOT NULL
  ,port               NUMBER
  ,state              VARCHAR2(15)   NOT NULL
  ,thinking_time      NUMBER
  ,time_extensions    NUMBER
  ,private_flag       NUMBER(1)      NOT NULL
  ,available_places   NUMBER         NOT NULL
)

ALTER TABLE socotra_servers
   ADD CONSTRAINT socotra_servers_pk PRIMARY KEY (server_id);

ALTER TABLE socotra_servers
   ADD CONSTRAINT socotra_servers_u1 UNIQUE (server_name);