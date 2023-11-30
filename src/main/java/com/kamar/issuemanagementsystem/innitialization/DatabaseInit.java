package com.kamar.issuemanagementsystem.innitialization;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * class to initialize the database at startup.
 * @author kamar baraka.*/

@Component
@RequiredArgsConstructor
public class DatabaseInit implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {

        String query = "create schema if not exists issue_management";

        jdbcTemplate.execute(query);
    }

}
