package service1;

import java.util.ArrayList;
import java.util.Comparator;

public class Schedule {

    private int numberOfShips;
    static private int numberOfBulkShips;
    static private int numberOfLiquidShips;
    static private int numberOfContainerShips;
    private ArrayList<Ship> ships = new ArrayList<>();

    public Schedule() {
        int MAX_NUMBER_OF_SHIPS = 1200;
        int MIN_NUMBER_OF_SHIPS = 720;
        this.numberOfShips = (int) (Math.random() * (MAX_NUMBER_OF_SHIPS - MIN_NUMBER_OF_SHIPS + 1))
                + MIN_NUMBER_OF_SHIPS;
        for (int i = 0; i < this.numberOfShips; i++) {
            this.ships.add(new Ship());
            switch (this.ships.get(i).getCargo()) {
                case BULK -> numberOfBulkShips++;
                case LIQUID -> numberOfLiquidShips++;
                case CONTAINER -> numberOfContainerShips++;
            }
        }

        sortSchedule();
    }

    public Schedule(ArrayList<Ship> ships) {
        this.numberOfShips = ships.size();
        this.ships = ships;

        sortSchedule();
    }

    public void addShip(int arrivalDay, int arrivalTimeMin, String name, String cargo, int weight) {
        numberOfShips++;
        Ship.Cargo cargoType = Ship.Cargo.valueOf(cargo);
        switch (cargoType) {
            case BULK -> numberOfBulkShips++;
            case LIQUID -> numberOfLiquidShips++;
            case CONTAINER -> numberOfContainerShips++;
        }
        ships.add(new Ship(arrivalDay, arrivalTimeMin, name, cargoType, weight));

        sortSchedule();
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

    public static int getNumberOfBulkShips() {
        return numberOfBulkShips;
    }

    public static int getNumberOfLiquidShips() {
        return numberOfLiquidShips;
    }

    public static int getNumberOfContainerShips() {
        return numberOfContainerShips;
    }
}
