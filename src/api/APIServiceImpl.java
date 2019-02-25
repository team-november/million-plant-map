package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

/*
 * Implemented as a singleton so that API calls are
 * made from a single source
 */
public class APIServiceImpl implements APIService {

    private static APIServiceImpl instance = new APIServiceImpl();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson = gsonBuilder.create();

    private APIServiceImpl() {
    }

    public static APIServiceImpl getInstance() {
        return instance;
    }


    private String submitQuery(String query) {
        HttpURLConnection connection = openConnection(query);
        StringBuffer response = null;
        try {
            if (connection.getResponseCode() == HTTP_OK) {
                String readLine;
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    private HttpURLConnection openConnection(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Species getAcceptedSpecies(String name) throws MalformedQueryException {
        String query = queryBuilder.searchForAcceptedName(getAcceptedKey(name));
        return gson.fromJson(submitQuery(query), Species.class);
    }

    private Species[] getSynonyms(String acceptedKey) {
        String query = queryBuilder.searchForSynonyms(acceptedKey);
        String json = submitQuery(query);
        return deserializeSynonymReturnObject(json).getResults();
    }

    @Override
    public QueryResult getAcceptedNameAndSynonyms(String name) {
        Species acceptedName = null;
        try {
            acceptedName = getAcceptedSpecies(name);
            Species[] synonyms = getSynonyms(acceptedName.getKey());
            return new QueryResult(acceptedName, synonyms);
        } catch (MalformedQueryException e) {
            return new QueryResult();
        }
    }

    private String getAcceptedKey(String name) throws MalformedQueryException {
        String query = queryBuilder.searchForSpecies(name);
        String json = submitQuery(query);
        return deserializeMatchReturnObject(json).getAcceptedUsageKey();
    }

    public GBIFSynonymSearchReturnObject deserializeSynonymReturnObject(String json) {
        return gson.fromJson(json, GBIFSynonymSearchReturnObject.class);
    }

    public GBIFMatchReturnObject deserializeMatchReturnObject(String json) {
        return gson.fromJson(json, GBIFMatchReturnObject.class);
    }
}
