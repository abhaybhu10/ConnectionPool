package database;

import lombok.Data;

/* Connection config for DB*/
@Data
public class DBConnectionConfig {
    String username;
    String password;
    String url;
    String port;
}
