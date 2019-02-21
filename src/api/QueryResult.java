package api;

import java.util.Iterator;

public class QueryResult implements Iterable<Species> {
    private Species acceptedName;
    private Species[] synonyms;

    public QueryResult(Species acceptedName, Species[] synonyms) {
        this.acceptedName = acceptedName;
        this.synonyms = synonyms;
        setBasionymFields();
    }

    public Species getAcceptedName() {
        return acceptedName;
    }

    public Species[] getSynonyms() {
        return synonyms;
    }

    private void setBasionymFields() {
        String basionymKey = getBasionymKey();
        for (Species s : this) {
            if (s.getKey().equals(basionymKey)) {
                s.setBasionym(true);
            }
        }
    }

    private String getBasionymKey() {
        String basionymKey = "";

        for (Species s : this) {
            if (s.getBasionymKey() != null) {
                basionymKey = s.getBasionymKey();
            }
        }
        return basionymKey;
    }

    @Override
    public Iterator<Species> iterator() {
        return new Iterator<Species>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < synonyms.length + 1;
            }

            @Override
            public Species next() {
                Species result;
                if (index == 0) {
                    result = acceptedName;
                } else {
                    result = synonyms[index - 1];
                }
                index++;
                return result;
            }
        };
    }
}
