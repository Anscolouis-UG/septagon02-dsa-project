import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final java.util.List<String> entries = new java.util.ArrayList<>();

    public void logInsert(String table, Object record, String userId) {
        entries.add(formatEntry("INSERT", table, record, userId));
    }

    public void logUpdate(String table, Object oldRecord, Object newRecord, String userId) {
        entries.add(formatEntry("UPDATE", table, oldRecord + " -> " + newRecord, userId));
    }

    public void logDelete(String table, Object record, String userId) {
        entries.add(formatEntry("DELETE", table, record, userId));
    }

    private String formatEntry(String operation, String table, Object details, String userId) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        return String.format("%s | %s | %s | %s | %s", timestamp, operation, table, userId, details == null ? "null" : details.toString());
    }

    public java.util.List<String> getEntries() {
        return java.util.Collections.unmodifiableList(entries);
    }

    public void clear() {
        entries.clear();
    }

    public int size() {
        return entries.size();
    }
}