package pl.michalmilej.notifmicros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class NotifMicrosApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifMicrosApplication.class, args);
    }

}
