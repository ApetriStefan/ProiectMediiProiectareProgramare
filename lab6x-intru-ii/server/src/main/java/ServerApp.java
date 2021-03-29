import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerApp {
    public static void main(String[] args) {
        System.out.println("Server starting...");

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("intrusii.server.Config");
    }
}
