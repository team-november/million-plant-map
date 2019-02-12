package api;

public class GBIFSynonymSearchReturnObject {
    private int offset;
    private int limit;
    private boolean endOfRecords;
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

    public Species[] getResults() {
        return results;
    }


}
