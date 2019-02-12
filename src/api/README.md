**How to use the API Service**

Get an instance through the following: 
`APIServiceImpl.getInstance()`

Exposes several useful methods:
* `Species getAcceptedSpecies(String name)` 
    * takes a name e.g. "vanda cristata" and returns a Species object of the accepted version
* `Species[] getSynonyms(String acceptedKey)`
    * takes the key associated with the accepted version of the plant and returns all synonyms
* `QueryResult getAcceptedNameAndSynonyms(String name)`
    * Combines the above two and returns a QueryResult object which contains an array of synonyms and the accepted Species
* `String getAcceptedKey(String name)`
    * Returns a string of the accepted key for a given plant name
* By name it's assuming some kind of scientific name but if it's missing details then it will return the closest match e.g. if you only give "Vanda" rather than "Vanda cristata" it will match to whatever the API deems to be closest
* So it's best to provide the full latin name if possible