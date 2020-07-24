import database.DBConnection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConnectionImpl implements Connection {
    final private ConnectionPool connectionPool;
    private boolean isDirty = false;
    final private DBConnection dbConnection;

    @Override
    public String execute(String query) {
        validate();
        return dbConnection.execute(query);
    }

    @Override
    public String prepareStatement(String query) {
        validate();
        return dbConnection.preparedStatement(query);
    }

    @Override
    public void release() {
        this.isDirty = true;
        connectionPool.release(this);
    }

    private void validate() {
        if (isDirty) {
            throw new IllegalStateException("Connection is already closed");
        }
    }
}
