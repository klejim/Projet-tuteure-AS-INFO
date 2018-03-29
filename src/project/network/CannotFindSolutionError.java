package project.network;

/**
 *
 * @author Jimenez
 */
public class CannotFindSolutionError extends CriticalNetworkError {
    private final NetworkError error;
    CannotFindSolutionError(NetworkError e){
        super();
        error = e;
    }
    public NetworkError getError() {
        return error;
    }
}
