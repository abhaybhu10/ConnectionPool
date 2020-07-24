package database;

public class DBConnectionFactory {
   public DBConnection getConnection(DBConnectionConfig config) {
        return new DBConnection();
    }
}
