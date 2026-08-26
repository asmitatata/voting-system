package com.hamropatro.query;

public class VotesQuery {

    public static final String SAVE_VOTE = "UPDATE voting_system1.votes_by_user_per_day " +
            "SET vote_count = ?, created_at = ? , updated_at = ? " +
            "WHERE user_id = ? " +
            "AND election_id = ? " +
            "AND vote_date = ? " +
            "AND candidate_id = ?";

    public static final String UPDATE_USER_VOTE =
            "UPDATE voting_system1.votes_by_user_per_day " +
                    "SET vote_count = ?, " +
                    "updated_at = ? " +
                    "WHERE user_id = ? " +
                    "AND election_id = ? " +
                    "AND vote_date = ? " +
                    "AND candidate_id = ? " +
                    "IF vote_count = ?";

    public static final String UPDATE_VOTE_COUNT_BY_CANDIDATE_QUERY = "UPDATE voting_system1.votes_count_by_candidate " +
            "SET vote_count = vote_count + ?" +
            "WHERE election_id = ? AND candidate_id = ?";
    public static final String UPDATE_VOTE_COUNT_BY_ELECTION_QUERY = "UPDATE voting_system1.votes_count_by_election " +
            "SET vote_count = vote_count + ? " +
            "WHERE election_id = ?";
    public static final String GET_VOTE_COUNT_BY_CANDIDATE_QUERY = "SELECT vote_count FROM voting_system1.votes_count_by_candidate " +
            "WHERE election_id = ? AND candidate_id = ?";
    public static final String GET_VOTE_COUNT_BY_ELECTION_QUERY = "SELECT vote_count FROM voting_system1.votes_count_by_election " +
            "WHERE election_id = ?";
    public static final String GET_USER_VOTE_COUNT_QUERY = "SELECT  vote_count FROM voting_system1.votes_by_user_per_day " +
            "WHERE user_id = ? AND election_id = ? AND vote_date = ? AND candidate_id = ? ";
    public static final String GET_USER_VOTE_QUERY = "SELECT * FROM voting_system1.votes_by_user_per_day";
}
