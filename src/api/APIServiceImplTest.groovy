package api

import spock.lang.Specification

class APIServiceImplTest extends Specification {
    def "can get the accepted name from a general query"() {
        given: "An API Service"
        APIServiceImpl service = APIServiceImpl.getInstance()

        when: "we search for the accepted name"
        Species accepted = service.getAcceptedSpecies(name)

        then: "the accepted name is returned"
        accepted.getCanonicalName().toLowerCase() == acceptedName.toLowerCase()

        where:
        name << ["Vanda Cristata", "Trudelia Cristata", "Alnus vulgaris", "Betula glutinosa"]
        acceptedName << ["Vanda Cristata", "Vanda Cristata", "Alnus glutinosa", "Alnus glutinosa"]
    }

    def "can get a list of all synonyms"() {
        given: "An API service"
        APIServiceImpl service = APIServiceImpl.getInstance()

        and: "An accepted key"
        String acceptedKey = service.getAcceptedKey(name)

        when: "We search for synonyms"
        Species[] synonymsReturned = service.getSynonyms(acceptedKey)

        and: "convert it to a list of canonical names"
        def canonicalNames = []
        for (Species s : synonymsReturned) {
            String name = s.getCanonicalName()
            if (!canonicalNames.contains(name)) {
                canonicalNames.add(name)
            }
        }

        and: "sort the expected and actual lists alphabetically"
        canonicalNames.sort()
        synonyms.sort()

        then: "the list of synonyms returned should match"
        canonicalNames == synonyms

        where:
        name << ["Vanda Cristata"]
        synonyms << [["Aerides cristata", "Vanda striata", "Luisia striata", "Trudelia cristata"]]
    }

}
