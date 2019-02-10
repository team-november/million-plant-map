package api;

public class APIMatchReturnObject {
    private String usageKey;
    private String acceptedUsageKey;
    private boolean synonym;

    public String getUsageKey() {
        return usageKey;
    }

    public String getAcceptedUsageKey() {
        return acceptedUsageKey;
    }

    public boolean isSynonym() {
        return synonym;
    }
}
