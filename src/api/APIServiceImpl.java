package api;

/*
 * Implemented as a singleton so that API calls are
 * made from a single source
 */
public class APIServiceImpl implements APIService {

    private static APIServiceImpl instance = new APIServiceImpl();

    private APIServiceImpl() {
    }

    public static APIServiceImpl getInstance() {
        return instance;
    }

    @Override
    public APIReturnObject queryAPI(String name) {
        return null;
    }
}
