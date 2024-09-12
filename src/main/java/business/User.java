package main.java.business;

import java.sql.Connection;
import java.util.UUID;

public class User {
    private final Connection connection;

    private UUID id;
    private String name;
    private String email;

    public User(UUID id, String name, String email, Connection connection) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.connection = connection;
    }

    public User(Connection connection) {
        this.connection = connection;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
