package api

import spock.lang.Specification

class APIServiceImplTest extends Specification {
    def "can get the accepted name from a general query"() {
        given: "An API Service"
        APIServiceImpl service = APIServiceImpl.getInstance()

        when: "we search for the accepted name"
        Species accepted = service.getAcceptedNameAndSynonyms(name).acceptedName

        then: "the accepted name is returned"
        accepted.getCanonicalName().toLowerCase() == acceptedName.toLowerCase()

        where:
        name << ["Vanda Cristata", "vanda cristata", "Trudelia Cristata", "trudelia cristata", "Alnus vulgaris", "Betula glutinosa", "Vanda striata"]
        acceptedName << ["Vanda Cristata", "Vanda Cristata", "Vanda Cristata", "Vanda Cristata", "Alnus glutinosa", "Alnus glutinosa", "Vanda cristata"]
    }

    def "can get a list of all synonyms"() {
        given: "An API service"
        APIServiceImpl service = APIServiceImpl.getInstance()

        and: "An accepted key"
        QueryResult result = service.getAcceptedNameAndSynonyms(name)

        when: "We search for synonyms"
        Species[] synonymsReturned = result.synonyms

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
        name << ["Vanda Cristata", "vanda cristata"]
        synonyms << [["Aerides cristata", "Vanda striata", "Luisia striata", "Trudelia cristata"], ["Aerides cristata", "Vanda striata", "Luisia striata", "Trudelia cristata"]]
    }

    def "can deserialize a synonym query response"() {
        given: "An API service"
        APIServiceImpl service = APIServiceImpl.getInstance()

        when: "We try to deserialize a json response"
        GBIFSynonymSearchReturnObject searchReturnObject = service.deserializeSynonymReturnObject(json)
        Species s = searchReturnObject.results[0]

        then: "All fields match"
        searchReturnObject.offset == 0
        searchReturnObject.limit == 20
        searchReturnObject.endOfRecords
        s.key == "2876271"
        s.canonicalName == "Alnus aurea"
        s.acceptedKey == "2876213"
        s.family == "Betulaceae"
        s.genus == "Alnus"
        s.species == "Alnus glutinosa"
        s.synonym
        s.authorship == "K.Koch"
        s.scientificName == "Alnus aurea K.Koch"

        where:
        json << ["""
            {
            "offset": 0,
            "limit": 20,
            "endOfRecords": true,
            "results": [
                    {
                        "key": 2876271,
                        "nubKey": 2876271,
                        "nameKey": 455495,
                        "taxonID": "gbif:2876271",
                        "sourceTaxonKey": 118319287,
                        "kingdom": "Plantae",
                        "phylum": "Tracheophyta",
                        "order": "Fagales",
                        "family": "Betulaceae",
                        "genus": "Alnus",
                        "species": "Alnus glutinosa",
                        "kingdomKey": 6,
                        "phylumKey": 7707728,
                        "classKey": 220,
                        "orderKey": 1354,
                        "familyKey": 4688,
                        "genusKey": 2876099,
                        "speciesKey": 2876213,
                        "datasetKey": "d7dddbf4-2cf0-4f39-9b2a-bb099caae36c",
                        "constituentKey": "d9a4eedb-e985-4456-ad46-3df8472e00e8",
                        "parentKey": 2876099,
                        "parent": "Alnus",
                        "acceptedKey": 2876213,
                        "accepted": "Alnus glutinosa (L.) Gaertn.",
                        "scientificName": "Alnus aurea K.Koch",
                        "canonicalName": "Alnus aurea",
                        "authorship": "K.Koch",
                        "nameType": "SCIENTIFIC",
                        "rank": "SPECIES",
                        "origin": "SOURCE",
                        "taxonomicStatus": "SYNONYM",
                        "nomenclaturalStatus": [],
                        "remarks": "",
                        "numDescendants": 0,
                        "lastCrawled": "2018-06-20T14:41:51.801+0000",
                        "lastInterpreted": "2018-06-20T14:36:21.699+0000",
                        "issues": [],
                        "synonym": true,
                        "class": "Magnoliopsida"
                    }
                    ]}"""]
    }

    def "can deserialize a match query response"() {
        given: "An API service"
        APIServiceImpl service = APIServiceImpl.getInstance()

        when: "we try to deserialize a response from a match query"
        GBIFMatchReturnObject returnObject = service.deserializeMatchReturnObject(json)

        then: "all fields match"
        returnObject.acceptedUsageKey == acceptedUsageKey
        returnObject.synonym == synonym


        where:
        json << ["""{
              "usageKey": 2783166,
              "scientificName": "Vanda cristata Lindl.",
              "canonicalName": "Vanda cristata",
              "rank": "SPECIES",
              "status": "ACCEPTED",
              "confidence": 99,
              "note": "Similarity: name=110; authorship=0; kingdom=0; rank=5; status=1; singleMatch=5",
              "matchType": "EXACT",
              "kingdom": "Plantae",
              "phylum": "Tracheophyta",
              "order": "Asparagales",
              "family": "Orchidaceae",
              "genus": "Vanda",
              "species": "Vanda cristata",
              "kingdomKey": 6,
              "phylumKey": 7707728,
              "classKey": 196,
              "orderKey": 1169,
              "familyKey": 7689,
              "genusKey": 2783121,
              "speciesKey": 2783166,
              "synonym": false,
              "class": "Liliopsida"
            }""",
                 """
            {
              "usageKey": 2783170,
              "acceptedUsageKey": 2783166,
              "scientificName": "Trudelia cristata (Wall. ex Lindl.) Senghas ex Roeth",
              "canonicalName": "Trudelia cristata",
              "rank": "SPECIES",
              "status": "SYNONYM",
              "confidence": 97,
              "note": "Similarity: name=110; authorship=0; kingdom=0; rank=5; status=0; 2 synonym homonyms; nextMatch=0",
              "matchType": "EXACT",
              "alternatives": [
                {
                  "usageKey": 7859517,
                  "acceptedUsageKey": 2783166,
                  "scientificName": "Trudelia cristata (Lindl.) Senghas",
                  "canonicalName": "Trudelia cristata",
                  "rank": "SPECIES",
                  "status": "SYNONYM",
                  "confidence": 97,
                  "note": "Similarity: name=110; authorship=0; kingdom=0; rank=5; status=0",
                  "matchType": "EXACT",
                  "kingdom": "Plantae",
                  "phylum": "Tracheophyta",
                  "order": "Asparagales",
                  "family": "Orchidaceae",
                  "genus": "Vanda",
                  "species": "Vanda cristata",
                  "kingdomKey": 6,
                  "phylumKey": 7707728,
                  "classKey": 196,
                  "orderKey": 1169,
                  "familyKey": 7689,
                  "genusKey": 2783121,
                  "speciesKey": 2783166,
                  "synonym": true,
                  "class": "Liliopsida"
                }
              ],
              "kingdom": "Plantae",
              "phylum": "Tracheophyta",
              "order": "Asparagales",
              "family": "Orchidaceae",
              "genus": "Vanda",
              "species": "Vanda cristata",
              "kingdomKey": 6,
              "phylumKey": 7707728,
              "classKey": 196,
              "orderKey": 1169,
              "familyKey": 7689,
              "genusKey": 2783121,
              "speciesKey": 2783166,
              "synonym": true,
              "class": "Liliopsida"
            }
            """,
                 """
                {
                      "confidence": 99,
                      "note": "Multiple equal matches for Betula glutinosa",
                      "matchType": "NONE",
                      "alternatives": [
                        {
                          "usageKey": 5331738,
                          "acceptedUsageKey": 2876213,
                          "scientificName": "Betula glutinosa (L.) Lam.",
                          "canonicalName": "Betula glutinosa",
                          "rank": "SPECIES",
                          "status": "SYNONYM",
                          "confidence": 115,
                          "note": "Similarity: name=110; authorship=0; kingdom=0; rank=5; status=0",
                          "matchType": "EXACT",
                          "kingdom": "Plantae",
                          "phylum": "Tracheophyta",
                          "order": "Fagales",
                          "family": "Betulaceae",
                          "genus": "Alnus",
                          "species": "Alnus glutinosa",
                          "kingdomKey": 6,
                          "phylumKey": 7707728,
                          "classKey": 220,
                          "orderKey": 1354,
                          "familyKey": 4688,
                          "genusKey": 2876099,
                          "speciesKey": 2876213,
                          "synonym": true,
                          "class": "Magnoliopsida"
                        },
                        {
                          "usageKey": 8056419,
                          "acceptedUsageKey": 2876213,
                          "scientificName": "Betula glutinosa L.",
                          "canonicalName": "Betula glutinosa",
                          "rank": "SPECIES",
                          "status": "SYNONYM",
                          "confidence": 115,
                          "note": "Similarity: name=110; authorship=0; kingdom=0; rank=5; status=0",
                          "matchType": "EXACT",
                          "kingdom": "Plantae",
                          "phylum": "Tracheophyta",
                          "order": "Fagales",
                          "family": "Betulaceae",
                          "genus": "Alnus",
                          "species": "Alnus glutinosa",
                          "kingdomKey": 6,
                          "phylumKey": 7707728,
                          "classKey": 220,
                          "orderKey": 1354,
                          "familyKey": 4688,
                          "genusKey": 2876099,
                          "speciesKey": 2876213,
                          "synonym": true,
                          "class": "Magnoliopsida"
                        },
                        {
                          "usageKey": 7472168,
                          "acceptedUsageKey": 7226602,
                          "scientificName": "Betula glutinosa Wallr.",
                          "canonicalName": "Betula glutinosa",
                          "rank": "SPECIES",
                          "status": "SYNONYM",
                          "confidence": 115,
                          "note": "Similarity: name=110; authorship=0; kingdom=0; rank=5; status=0",
                          "matchType": "EXACT",
                          "kingdom": "Plantae",
                          "phylum": "Tracheophyta",
                          "order": "Fagales",
                          "family": "Betulaceae",
                          "genus": "Betula",
                          "species": "Betula pubescens",
                          "kingdomKey": 6,
                          "phylumKey": 7707728,
                          "classKey": 220,
                          "orderKey": 1354,
                          "familyKey": 4688,
                          "genusKey": 2875008,
                          "speciesKey": 9118014,
                          "synonym": true,
                          "class": "Magnoliopsida"
                        }
                      ],
                      "synonym": false
                    }
            """
        ]
        acceptedUsageKey << ["2783166", "2783166", "2876213"]
        synonym << [false, true, false]
    }

    def "update the basionym field properly"() {
        given: "An API Service"
        APIServiceImpl service = APIServiceImpl.getInstance()

        when: "We search for a particular plant"
        QueryResult result = service.getAcceptedNameAndSynonyms(name)

        then: "All the basionym is set appropriately for all values that are returned"
        if (result.acceptedName.scientificName == basionym) {
            result.acceptedName.basionym
        }

        Iterator<Species> iterator = result.iterator()
        while (iterator.hasNext()) {
            Species s = iterator.next()
            if (s.scientificName == basionym) {
                s.basionym
            } else {
                !s.basionym
            }
        }
        where:
        basionym << ["Vanda cristata Lindl."]
        name << ["Trudelia cristata"]
    }

    def "can use autocomplete to finish search queries"() {
        given: "An API Service"
        APIServiceImpl service = APIServiceImpl.getInstance()

        when: "We try to autocomplete for a particular search"
        String[] result = service.autocomplete(name)

        then: "The autocomplete returns the appropriate results"
        for (String s : result) {
            match.contains(s)
        }
        for (String s : match) {
            result.contains(s)
        }

        where:
        name << ["Trudelia", "Vanda Cristata", "asoflaksfloais"]
        match << [
                ["Trudelia Garay", "Trudelia jainii (A.S.Chauhan) S.Misra", "Trudelia chlorosantha Garay", "Trudelia griffithii (Lindl.) Garay", "Trudelia alpina (Lindl.) Garay", "Trudelia cristata (Wall. ex Lindl.) Senghas ex Roeth", "Trudelia cristata (Lindl.) Senghas", "Trudelia pumila (Hook.f.) Senghas", "Trudelianda Garay"],
                ["Vanda cristata Lindl."],
                []
        ]
    }

}
