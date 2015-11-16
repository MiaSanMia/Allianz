DROP TABLE IF EXISTS universe_comment;
DROP TABLE IF EXISTS universe_comment_del;
DROP TABLE IF EXISTS universe_comment_ext;

CREATE TABLE IF NOT EXISTS universe_comment 
(
  id              BIGINT NOT NULL PRIMARY KEY,
  type            SMALLINT UNSIGNED NOT NULL,
  author_id	  INT NOT NULL,
  entry_id	  BIGINT NOT NULL,
  entry_owner_id  INT NOT NULL,
  content	  VARCHAR(4096) DEFAULT "" NOT NULL,
  created_time	  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  to_user_id      INT DEFAULT 0 NOT NULL,
  to_comment_id   INT DEFAULT 0 NOT NULL,
  whisper_to_id   INT DEFAULT 0 NOT NULL,
  flag            INT UNSIGNED DEFAULT 0 NOT NULL,
  metadata        VARCHAR(4096) DEFAULT "" NOT NULL
);

CREATE TABLE IF NOT EXISTS universe_comment_del
(
  id              BIGINT NOT NULL PRIMARY KEY,
  type            SMALLINT UNSIGNED NOT NULL,
  author_id	  INT NOT NULL,
  del_user_id     INT NOT NULL,
  entry_id	  BIGINT NOT NULL,
  entry_owner_id  INT NOT NULL,
  content	  VARCHAR(4096) DEFAULT "" NOT NULL,
  created_time	  TIMESTAMP NOT NULL,
  del_time        TIMESTAMP NOT NULL,
  to_user_id      INT DEFAULT 0 NOT NULL,
  to_comment_id   BIGINT DEFAULT 0 NOT NULL,
  whisper_to_id   INT DEFAULT 0 NOT NULL,
  flag            INT UNSIGNED DEFAULT 0 NOT NULL,
  metadata        VARCHAR(4096) DEFAULT "" NOT NULL,
  status          INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS universe_comment_ext
(
  id              BIGINT NOT NULL PRIMARY KEY,
  entry_id        BIGINT NOT NULL,
  entry_owner_id  INT NOT NULL,
  ext_content     MEDIUMTEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS universe_comment_voice
(
  id              BIGINT NOT NULL PRIMARY KEY,
  type            SMALLINT,
  ugc_comment_id  BIGINT,
  entry_id        BIGINT NOT NULL,
  entry_owner_id  INT NOT NULL,
  url             VARCHAR(1024) NOT NULL,
  length          INT,
  rate            INT,
  voice_size      INT,
  play_count      INT default 0,
  created_time    TIMESTAMP NOT NULL,
  ext_type        INT
);

