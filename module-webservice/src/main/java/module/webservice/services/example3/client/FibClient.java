package module.webservice.services.example3.client;

/**
 * Created by root on 2015/12/30.
 */
public class FibClient {
    public static void main(String[] args) {
        RabbitCounterService service = new RabbitCounterService();
        RabbitCounter port = service.getRabbitCounterPort();
        try {
            int n = -45;
            System.out.println("fib(" + n + ") = " + port.countRabbits(n));
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
