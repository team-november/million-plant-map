package api;

public class GBIFNameSearchReturnObject {
    private int offset;
    private int limit;
    private boolean endOfRecords;
    private int count;
    private Species[] results;

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public boolean isEndOfRecords() {
        return endOfRecords;
    }

    public int getCount() {
        return count;
    }

    public Species[] getResults() {
        return results;
    }


}
