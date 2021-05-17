package port_simulation.service1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping(value = "/getSchedule")
    public Schedule getSchedule() {
        return new Schedule();
    }
}
