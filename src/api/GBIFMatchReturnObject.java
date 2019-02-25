package api;

public class GBIFMatchReturnObject {
    private String usageKey;
    private String acceptedUsageKey;
    private boolean synonym;

    private Alternative[] alternatives;

    public String getAcceptedUsageKey() throws MalformedQueryException {
        if (acceptedUsageKey == null & usageKey != null) {
            return usageKey;
        } else if (alternatives != null) {
            return alternatives[0].acceptedUsageKey;
        } else {
            throw new MalformedQueryException();
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
