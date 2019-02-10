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
        println(acceptedKey)

        when: "Wse search for synonyms"
        Species[] synonymsReturned = service.getSynonyms(acceptedKey)

        then:
        for (Species s : synonymsReturned) {
            synonyms.contains(s.getCanonicalName())
            println(s.getCanonicalName())
        }

        where:
        name << ["Vanda Cristata", "Alnus glutinosa"]
        synonyms << [["Aerides cristata", "Vanda striata", "Luisia striata", "Trudelia cristata"], ["Alnus vulgaris", "Betula alnus", "Betula glutinosa"]]
    }

    def "GetAcceptedNameAndSynonyms"() {
    }

}
