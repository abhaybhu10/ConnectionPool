import database.DBConnectionConfig;

public interface ConnectionPool {
    Connection connect(DBConnectionConfig config) throws IllegalStateException;
    void release(Connection connection);
}
