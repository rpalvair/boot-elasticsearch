package com.palvair.elasticsearch.application;

import com.palvair.elasticsearch.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EmbeddedDatabaseTestConfiguration.class}, webEnvironment = NONE)
@ActiveProfiles("test")
public class EmbeddedDatabaseIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void should_insert_users_and_retrieve_them() {

        final String sql = "SELECT users.nom," +
                " users.prenom " +
                " FROM users";

        final List<User> users = jdbcTemplate.query(sql, this::mapUser);
        assertThat(users)
                .isNotEmpty()
                .hasSize(1);
    }

    private User mapUser(final ResultSet resultSet, final int i) throws SQLException {
        return new User(resultSet.getString("nom"),
                resultSet.getString("prenom"));
    }
}
