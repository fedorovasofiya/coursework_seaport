package service2;

import service1.Schedule;
import service3.ArrivingShip;
import service3.Crane;
import service3.Port;
import service3.Statistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static service2.JSONGenerator.JSONSerialization;

public class Main {
    public static void main(String[] args) {
        try {
        Schedule schedule = new Schedule();
        enterShip(schedule);
        JSONSerialization(schedule);
        schedule.printInfo();

        Port port = new Port();
        ArrayList<ArrivingShip> ships;

        boolean bulkMinimized ;
        boolean liquidMinimized ;
        boolean containerMinimized ;

        int bulkCranes = 1;
        int liquidCranes = 1;
        int containerCranes = 1;

        while (true) {
            ships = port.startModeling(bulkCranes, liquidCranes, containerCranes);

            Statistics statistics = new Statistics(port);
            System.out.println(statistics.toString());

            bulkMinimized = (statistics.getPenaltyBulk() / Crane.COST == 0);
            liquidMinimized = (statistics.getPenaltyLiquid() / Crane.COST == 0);
            containerMinimized = (statistics.getPenaltyContainer() / Crane.COST == 0);


            if (bulkMinimized && liquidMinimized && containerMinimized) {
                for (ArrivingShip ship : ships) {
                    System.out.println(ship.toString());
                }
                System.out.println("\n Final statistics");
                System.out.println(statistics.toString());
                break;
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
    } finally {
            try {
                Files.deleteIfExists(Paths.get("file.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static public void enterShip(Schedule schedule){
        Scanner in = new Scanner(System.in);

        try (in) {
            System.out.print("Number of ships to enter: ");
            int number = in.nextInt();
            for (int i = 0; i < number; i++) {
                in.skip("\n");
                System.out.print("Enter name: ");
                String name = in.nextLine();
                System.out.print("Enter arrival day (0 - 30): ");
                int arrivalDay = in.nextInt();
                System.out.print("Enter arrival time in minutes (0 - 1439): ");
                int arrivalTimeMin = in.nextInt();
                in.skip("\n");
                System.out.print("Enter cargo (BULK, LIQUID, CONTAINER): ");
                String cargo = in.nextLine();
                System.out.print("Enter weight: ");
                int weight = in.nextInt();

                schedule.addShip(arrivalDay, arrivalTimeMin, name, cargo, weight);
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }

    }
}

