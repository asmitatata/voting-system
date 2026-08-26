package com.hamropatro.query;

public class CandidateQuery {

    public static final String CREATE_CANDIDATE = "INSERT INTO voting_system1.candidate" +
            "(" +
            "candidate_id,election_id,full_name,candidate_email,candidate_age,created_at,updated_at" +
            ")" +
            " VALUES (?,?,?,?,?,?,?)";


    public static final String FIND_BY_CANDIDATE_ID_QUERY = "SELECT * FROM voting_system1.candidate WHERE candidate_id=? ";
    public static final String FIND_ALL_CANDIDATE_QUERY = "SELECT * FROM voting_system1.candidate";

    public static final String FIND_CANDIDATES_BY_ELECTION_ID_QUERY = "SELECT * FROM voting_system1.candidate WHERE election_id=? ";
    public static final String UPDATE_CANDIDATE_QUERY = "UPDATE voting_system1.candidate SET" +
            " full_name=?, " +
            " candidate_email=?, " +
            " candidate_age=?," +
            " updated_at = ? " +
            " WHERE candidate_id = ? AND election_id = ?";
    public static final String DELETE_CANDIDATE_QUERY = "DELETE FROM voting_system1.candidate WHERE candidate_id=? ";

}
