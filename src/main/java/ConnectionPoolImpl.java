import com.sun.corba.se.spi.activation.InitialNameService;
import database.DBConnectionConfig;
import database.DBConnectionFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolImpl implements ConnectionPool {
    AtomicInteger counter = new AtomicInteger(0);
    final int maxPoolSize;
    final DBConnectionFactory connectionFactory;

    final private ConcurrentLinkedDeque<Connection> availableConnection;
    final private ConcurrentLinkedDeque<Connection> occupiedConnection;
    private static ConnectionPool INSTANCE = null;

    private ConnectionPoolImpl() {
        maxPoolSize = 10;
        counter = new AtomicInteger(0);
        connectionFactory = new DBConnectionFactory();
        availableConnection = new ConcurrentLinkedDeque<>();
        occupiedConnection = new ConcurrentLinkedDeque<>();
    }

    protected ConnectionPoolImpl(int maxPoolSize, DBConnectionFactory connectionFactory) {
        this.maxPoolSize = maxPoolSize;
        this.connectionFactory = connectionFactory;
        availableConnection = new ConcurrentLinkedDeque<>();
        occupiedConnection = new ConcurrentLinkedDeque<>();
        counter = new AtomicInteger(0);
    }

    public synchronized static ConnectionPool  getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionPoolImpl();
        }
        return INSTANCE;
    }


    @Override
    public Connection connect(DBConnectionConfig config) throws IllegalStateException {
        if (counter.get() == maxPoolSize) {
            throw new IllegalStateException("No connection available");
        }
        Connection connection = null;
        if (availableConnection.size() > 0) {
           connection = availableConnection.removeFirst();
        } else {
            connection = new ConnectionImpl(INSTANCE, connectionFactory.getConnection(config));
            counter.incrementAndGet();
        }
        occupiedConnection.addFirst(connection);
        return connection;
    }

    @Override
    public void release(Connection con) {
        counter.decrementAndGet();
        availableConnection.add(con);
    }
}
