import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Database implements AutoCloseable {
    private final java.sql.Connection connection;

    private Database(java.sql.Connection connection) {
        this.connection = connection;
    }

    public static Database connect(String jdbcUrl) throws java.sql.SQLException {
        java.sql.Connection conn = java.sql.DriverManager.getConnection(jdbcUrl);
        conn.setAutoCommit(true);
        return new Database(conn);
    }

    public static Database connectFile(String path) throws java.sql.SQLException {
        return connect("jdbc:sqlite:" + path);
    }

    public java.sql.Connection getConnection() {
        return connection;
    }

    public void initializeSchema(String schemaPath) throws IOException, java.sql.SQLException {
        String sql = Files.readString(Path.of(schemaPath), StandardCharsets.UTF_8);
        for (String statement : splitSqlStatements(sql)) {
            if (!statement.isBlank()) {
                try (java.sql.Statement stmt = connection.createStatement()) {
                    stmt.execute(statement);
                }
            }
        }
    }

    private static java.util.List<String> splitSqlStatements(String sql) {
        java.util.List<String> statements = new java.util.ArrayList<>();
        String[] parts = sql.split(";");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                statements.add(trimmed);
            }
        }
        return statements;
    }

    public int executeUpdate(String sql, Object... params) throws java.sql.SQLException {
        try (java.sql.PreparedStatement stmt = prepareStatement(sql, params)) {
            return stmt.executeUpdate();
        }
    }

    public java.sql.PreparedStatement prepareStatement(String sql, Object... params) throws java.sql.SQLException {
        java.sql.PreparedStatement stmt = connection.prepareStatement(sql);
        setParameters(stmt, params);
        return stmt;
    }

    private static void setParameters(java.sql.PreparedStatement stmt, Object... params) throws java.sql.SQLException {
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            int index = i + 1;
            if (param == null) {
                stmt.setObject(index, null);
            } else if (param instanceof Integer) {
                stmt.setInt(index, (Integer) param);
            } else if (param instanceof Long) {
                stmt.setLong(index, (Long) param);
            } else if (param instanceof Double) {
                stmt.setDouble(index, (Double) param);
            } else if (param instanceof Float) {
                stmt.setDouble(index, ((Float) param).doubleValue());
            } else {
                stmt.setString(index, param.toString());
            }
        }
    }

    @Override
    public void close() throws java.sql.SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}