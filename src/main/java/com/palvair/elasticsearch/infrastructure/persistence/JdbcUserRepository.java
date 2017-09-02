package com.palvair.elasticsearch.infrastructure.persistence;

import com.palvair.elasticsearch.domain.User;
import com.palvair.elasticsearch.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        final String sql = "SELECT users.nom," +
                " users.prenom " +
                " FROM users";

        return jdbcTemplate.query(sql, this::mapUser);
    }

    private User mapUser(final ResultSet resultSet, final int i) throws SQLException {
        return new User(resultSet.getString("nom"),
                resultSet.getString("prenom"));
    }
}
