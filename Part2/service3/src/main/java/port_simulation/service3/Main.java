package port_simulation.service3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import port_simulation.service1.Schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/getJsonSchedule";
        File file = restTemplate.getForObject(url, File.class);
        System.out.println("Schedule was serialized to file " + file.getName());

        System.out.println("Enter the name of file with schedule: ");
        Scanner in = new Scanner(System.in);
        String fileName = in.nextLine();

        url = "http://localhost:8082/deserializeJsonSchedule?fileName=";
        ResponseEntity<Schedule> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(url + fileName, Schedule.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting schedule from " + file.getName() + ", because there isn't file " + fileName);
            responseEntity = restTemplate.getForEntity(url + file.getName(), Schedule.class);
        }

        Schedule newSchedule = responseEntity.getBody();
        Port port = new Port(newSchedule);
        System.out.println("Start minimizing penalty");
        Result result = minimizePenalty(port);

        url = "http://localhost:8082/saveResult";
        restTemplate.postForObject(url, result, Void.class);
    }



    public static Result minimizePenalty(Port port) {
        boolean bulkMinimized;
        boolean liquidMinimized;
        boolean containerMinimized;

        int bulkCranes = 1;
        int liquidCranes = 1;
        int containerCranes = 1;

        int count = 0;

        while (true) {
            count++;
            System.out.println("Iteration " + count);
            ArrayList<ArrivingShip> ships = port.startModeling(bulkCranes, liquidCranes, containerCranes);

            Result statistics = new Result(port);

            bulkMinimized = (statistics.getPenaltyBulk() / Crane.COST == 0);
            liquidMinimized = (statistics.getPenaltyLiquid() / Crane.COST == 0);
            containerMinimized = (statistics.getPenaltyContainer() / Crane.COST == 0);

            if (bulkMinimized && liquidMinimized && containerMinimized) {
                System.out.println(ships.toString());
                System.out.println("\n Final statistics");
                System.out.println(statistics.toString());
                return statistics;
            }
            if (!bulkMinimized) {
                bulkCranes += statistics.getPenaltyBulk() / Crane.COST;
            }
            if (!liquidMinimized) {
                liquidCranes += statistics.getPenaltyLiquid() / Crane.COST;
            }
            if (!containerMinimized) {
                containerCranes += statistics.getPenaltyContainer() / Crane.COST;
            }
        }
    }

}
