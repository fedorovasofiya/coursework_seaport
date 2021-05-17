package service2;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import service1.Schedule;
import service1.Ship;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class JSONGenerator {
    public static void JSONSerialization(Schedule schedule) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            FileOutputStream fileOutputStream = new FileOutputStream("file.json", true);
            writer.writeValue(fileOutputStream, schedule.getShips());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Schedule JSONDeserialization() {
        ArrayList<Ship> ships = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectReader reader = mapper.readerFor(Ship.class);
            MappingIterator<Ship> shipMappingIterator = reader.readValues(new File("file.json"));
            while (shipMappingIterator.hasNext()) {
                ships.add(shipMappingIterator.nextValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Schedule(ships);
    }
}
