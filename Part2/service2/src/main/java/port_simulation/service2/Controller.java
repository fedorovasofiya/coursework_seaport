package port_simulation.service2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import port_simulation.service1.Schedule;
import port_simulation.service3.Result;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static port_simulation.service2.JSONGenerator.*;

@RestController
public class Controller {

    @GetMapping(value = "/getJsonSchedule")
    public File getJsonSchedule() {
        String fileName = "schedule.json";
        RestTemplate restTemplate = new RestTemplate();
        Schedule schedule = restTemplate.getForObject("http://localhost:8081/getSchedule", Schedule.class);
        return serializeSchedule(schedule, fileName);
    }

    @GetMapping(value = "/deserializeJsonSchedule")
    public ResponseEntity<Schedule> deserializeJsonSchedule(@RequestParam String fileName) throws IncorrectFilenameException {
        if (!Files.exists(Path.of(fileName))){
            throw new IncorrectFilenameException("Incorrect filename: " + fileName);
        }
        else {
            Schedule schedule = deserializeSchedule(fileName);
            return new ResponseEntity<>(schedule, HttpStatus.OK);
        }
    }

    @PostMapping( value =  "/saveResult")
    public void saveStatistics(@RequestBody Result result){
        serializeResult(result);
    }

}














