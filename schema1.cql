CREATE KEYSPACE IF NOT EXISTS VOTING_SYSTEM1 WITH REPLICATION ={
    'class':'SimpleStrategy',
    'replication_factor':1
    }
    AND tablets = {'enabled': false};

USE VOTING_SYSTEM1;

CREATE TABLE IF NOT EXISTS election
(
    election_id   UUID PRIMARY KEY,
    election_name TEXT,
    voting_type   TEXT,
    max_votes     INT,
    start_at      TIMESTAMP,
    end_at        TIMESTAMP,
    is_active      BOOLEAN,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    start_time    TEXT,
    end_time      TEXT
);


CREATE TABLE IF NOT EXISTS users
(
    user_id    UUID PRIMARY KEY,
    full_name  TEXT,
    email      TEXT,
    role       TEXT,
    password   TEXT,
    is_active  BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);


CREATE TABLE IF NOT EXISTS candidate
(
    election_id  UUID,
    candidate_id UUID,
    full_name    TEXT,
    candidate_email TEXT,
    candidate_age INT,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,
    PRIMARY KEY (election_id, candidate_id)
);

CREATE TABLE votes_by_user_per_day
(
    user_id      UUID,
    election_id  UUID,
    vote_date    DATE,
    candidate_id UUID,
    vote_count   INT,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP,

    PRIMARY KEY (user_id, election_id, vote_date,candidate_id)
);


CREATE TABLE votes_count_by_candidate
(
    election_id  UUID,
    candidate_id UUID,
    vote_count   COUNTER,

    PRIMARY KEY ((election_id), candidate_id)
);

CREATE TABLE votes_count_by_election
(
    election_id UUID,
    vote_count  COUNTER,
    PRIMARY KEY (election_id)
);

CREATE INDEX users_by_email on users (email);
