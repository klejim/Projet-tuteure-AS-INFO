import network.Network;
import view.View;
/**
 *
 * @author Jimenez
 */
public class Test {
    public static void main(String... args){
        Network network = new Network(1, 2, 10);
        View view = new View();
        System.out.print(view.rapport(network));
    }
}
