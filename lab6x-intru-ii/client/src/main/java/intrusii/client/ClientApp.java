package intrusii.client;

import intrusii.client.UI.Console;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ClientApp {
        public static void main(String[] args) {

                AnnotationConfigApplicationContext context =
                        new AnnotationConfigApplicationContext("intrusii.client.Config");

                Console console = context.getBean(Console.class);
                console.runConsole();
        }
}

