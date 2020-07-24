import database.DBConnection;
import database.DBConnectionConfig;
import database.DBConnectionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionPoolTest {
    ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();
    @Mock
    DBConnectionFactory connectionFactory;
    @Mock DBConnection dbConnection;
    @Mock DBConnectionConfig connectionConfig;

    @Before
    public void setup() {
        Mockito.when(connectionFactory.getConnection(Mockito.any(DBConnectionConfig.class)))
                .thenReturn(dbConnection);
    }
    @Test
    public void validateNonNullConnectionObjectReturn() {
        Connection connection = connectionPool.connect(connectionConfig);
        Assert.assertNotNull(connection);
    }

    @Test
    public void testConnectionDBConnectionCalled() {
        ConnectionPool connectionPool = new ConnectionPoolImpl(1, connectionFactory);

        Connection connection = connectionPool.connect(connectionConfig);
        connection.execute("");
        Mockito.verify(dbConnection).execute(Mockito.anyString());
    }

    @Test(expected = IllegalStateException.class)
    public void testTestCallFailsWithMoreObject() {
        ConnectionPool connectionPool = new ConnectionPoolImpl(1, connectionFactory);
        connectionPool.connect(connectionConfig);
        connectionPool.connect(connectionConfig);
    }

    @Test
    public void testWithReturnConnection() {
        ConnectionPool connectionPool = new ConnectionPoolImpl(1, connectionFactory);
        Connection con = connectionPool.connect(connectionConfig);
        connectionPool.release(con);
        Assert.assertNotNull(connectionPool.connect(connectionConfig));
    }

    @Test
    public void testWithDirtyConnection() {
        ConnectionPool connectionPool = new ConnectionPoolImpl(1, connectionFactory);
        Connection con = connectionPool.connect(connectionConfig);
        connectionPool.release(con);
        con.execute("");
    }

    @After
    public void tearDown() {

    }
}
