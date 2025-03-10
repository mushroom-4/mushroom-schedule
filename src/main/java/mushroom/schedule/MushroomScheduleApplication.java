package mushroom.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MushroomScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MushroomScheduleApplication.class, args);
    }

}
