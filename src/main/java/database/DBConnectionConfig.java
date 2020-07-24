package database;

import lombok.Data;

@Data
public class DBConnectionConfig {
    String username;
    String password;
    String url;
    String port;
}
