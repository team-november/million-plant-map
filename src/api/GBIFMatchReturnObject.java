package api;

public class GBIFMatchReturnObject {
    private String usageKey;
    private String acceptedUsageKey;
    private boolean synonym;

    private Alternative[] alternatives;

    public String getAcceptedUsageKey() {
        if (acceptedUsageKey == null & usageKey != null) {
            return usageKey;
        } else {
            return alternatives[0].acceptedUsageKey;
        }
    }

    public boolean isSynonym() {
        return synonym;
    }

    private class Alternative {
        String usageKey;
        String acceptedUsageKey;

    }
}
