package com.example.web_app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTestRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseTestRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            String dbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
            System.out.println("Koneksi berhasil! Database: " + dbName);
        } catch (Exception e) {
            System.err.println("Gagal koneksi: " + e.getMessage());
        }
    }
}