package api;

public class QueryResult {
    private Species acceptedName;
    private Species[] synonyms;

    public QueryResult(Species acceptedName, Species[] synonyms) {
        this.acceptedName = acceptedName;
        this.synonyms = synonyms;
    }

    public Species getAcceptedName() {
        return acceptedName;
    }

    public void setAcceptedName(Species acceptedName) {
        this.acceptedName = acceptedName;
    }

    public Species[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Species[] synonyms) {
        this.synonyms = synonyms;
    }
}
