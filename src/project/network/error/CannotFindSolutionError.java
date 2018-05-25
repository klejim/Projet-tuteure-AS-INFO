package project.network.error;

/**
 *
 * @author Jimenez
 */
public class CannotFindSolutionError extends CriticalNetworkError {
    private final NetworkError error;
    public CannotFindSolutionError(NetworkError e){
        super();
        error = e;
    }
    public NetworkError getError() {
        return error;
    }
}
