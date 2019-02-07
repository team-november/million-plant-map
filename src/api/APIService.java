package api;

/*
 * Implemented as a singleton so that API calls are
 * made from a single source
 */
public class APIService {

    private static APIService instance = new APIService();

    private APIService() {
    }

    public static APIService getInstance() {
        return instance;
    }

}
