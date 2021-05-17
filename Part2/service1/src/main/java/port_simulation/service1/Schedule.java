package port_simulation.service1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Schedule {

    private int numberOfShips;
    private int numberOfBulkShips;
    private int numberOfLiquidShips;
    private int numberOfContainerShips;
    private ArrayList<Ship> ships = new ArrayList<>();

    public Schedule() {
        int MAX_NUMBER_OF_SHIPS = 1000;
        int MIN_NUMBER_OF_SHIPS = 720;
        this.numberOfShips = (int) (Math.random() * (MAX_NUMBER_OF_SHIPS - MIN_NUMBER_OF_SHIPS + 1))
                + MIN_NUMBER_OF_SHIPS;
        for (int i = 0; i < this.numberOfShips; i++) {
            this.ships.add(new Ship());
            switch (this.ships.get(i).getCargo()) {
                case BULK:
                    numberOfBulkShips++;
                    break;
                case LIQUID:
                    numberOfLiquidShips++;
                    break;
                case CONTAINER:
                    numberOfContainerShips++;
                    break;
            }
        }

        sortSchedule();
    }

    public Schedule(ArrayList<Ship> ships) {
        this.numberOfShips = ships.size();
        this.ships = ships;

        for (int i = 0; i < this.numberOfShips; i++) {
            switch (this.ships.get(i).getCargo()) {
                case BULK:
                    numberOfBulkShips++;
                    break;
                case LIQUID:
                    numberOfLiquidShips++;
                    break;
                case CONTAINER:
                    numberOfContainerShips++;
                    break;
            }
        }

        sortSchedule();
    }

    public void addShip(int arrivalDay, int arrivalTimeMin, String name, String cargo, int weight) {
        numberOfShips++;
        Ship.Cargo cargoType = Ship.Cargo.valueOf(cargo);
        switch (cargoType) {
            case BULK:
                numberOfBulkShips++;
                break;
            case LIQUID:
                numberOfLiquidShips++;
                break;
            case CONTAINER:
                numberOfContainerShips++;
                break;
        }
        ships.add(new Ship(arrivalDay, arrivalTimeMin, name, cargoType, weight));

        sortSchedule();
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


    private void sortSchedule() {
        Comparator<Ship> comp = Comparator.comparingInt(Ship::getArrivalDay).thenComparingInt(Ship::getArrivalTimeMin);
        ships.sort(comp);
    }

    public void printInfo() {
        System.out.println("Number of ships: " + this.numberOfShips);
        for (int i = 0; i < numberOfShips; i++) {
            System.out.print(ships.get(i).toString());
        }
    }


    public int getNumberOfShips() {
        return numberOfShips;
    }

    public void setNumberOfShips(int numberOfShips) {
        this.numberOfShips = numberOfShips;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public int getNumberOfBulkShips() {
        return numberOfBulkShips;
    }

    public int getNumberOfLiquidShips() {
        return numberOfLiquidShips;
    }

    public int getNumberOfContainerShips() {
        return numberOfContainerShips;
    }
}
