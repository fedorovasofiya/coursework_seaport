package port_simulation.service2;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import port_simulation.service1.Schedule;
import port_simulation.service1.Ship;
import port_simulation.service3.Result;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class JSONGenerator {
    public static File serializeSchedule(Schedule schedule, String fileName) {
        try {
            Files.deleteIfExists(Path.of(fileName));
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
            writer.writeValue(fileOutputStream, schedule.getShips());
            fileOutputStream.close();
            System.out.println("Schedule was serialized to file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(fileName);
    }

    public static void serializeResult(Result result) {
        try {
            String fileName = "result.json";
            Files.deleteIfExists(Path.of(fileName));
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
            writer.writeValue(fileOutputStream, result);
            fileOutputStream.close();
            System.out.println("Result was serialized to file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Schedule deserializeSchedule(String fileName) {
        ArrayList<Ship> ships = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectReader reader = mapper.readerFor(Ship.class);
            MappingIterator<Ship> shipMappingIterator = reader.readValues(new File(fileName));
            while (shipMappingIterator.hasNext()) {
                ships.add(shipMappingIterator.nextValue());
            }
            System.out.println("Schedule was deserialized from file: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Schedule(ships);
    }
}
