public interface Connection {
    String execute(String query);
    String prepareStatement(String query);
    void release();
}
