package port_simulation.service1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Ship {

    public enum Cargo {
        BULK,
        LIQUID,
        CONTAINER
    }

    protected int arrivalDay;
    protected int arrivalTimeMin;
    protected String name;
    protected Cargo cargo;
    protected int weight;
    protected int periodMin;

    public Ship() {
        this.arrivalDay = generateRandomIntNumber(30, 0);
        this.arrivalTimeMin = generateRandomIntNumber(1439, 0);
        this.name = generateRandomName();
        this.cargo = Cargo.values()[generateRandomIntNumber(2, 0)];
        this.weight = generateRandomIntNumber(60000, 30000);
        this.periodMin = generatePeriod();
    }

    public Ship(int arrivalDay, int arrivalTimeMin, String name, Cargo cargo, int weight) {
        this.arrivalDay = arrivalDay;
        this.arrivalTimeMin = arrivalTimeMin;
        this.name = name;
        this.cargo = cargo;
        this.weight = weight;
        this.periodMin = generatePeriod();
    }

    private int generateRandomIntNumber(int max, int min) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private String generateRandomName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int nameLength = (int) (Math.random() * (10 - 3 + 1)) + 3;

        StringBuilder builder = new StringBuilder(nameLength);
        for (int i = 0; i < nameLength; i++) {
            int randomLimitedInt = (int) (Math.random() * (rightLimit - leftLimit + 1)) + leftLimit;
            builder.append((char) randomLimitedInt);
        }
        return builder.toString();
    }


    private int generatePeriod() {
        int CONTAINER_CRANE_CAPACITY = 4;
        int BULK_CRANE_CAPACITY = 4;
        int LIQUID_CRANE_CAPACITY = 4;

        switch (this.cargo) {
            case LIQUID:
                return this.weight / LIQUID_CRANE_CAPACITY;
            case BULK:
                return this.weight / BULK_CRANE_CAPACITY;
            case CONTAINER:
                return this.weight / CONTAINER_CRANE_CAPACITY;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return "Ship{" +
                "arrivalDay=" + arrivalDay +
                ", arrivalTimeMin=" + arrivalTimeMin +
                ", name='" + name + '\'' +
                ", cargo=" + cargo +
                ", weight=" + weight +
                ", periodMin=" + periodMin +
                "}\n";
    }

    public int getArrivalDay() {
        return arrivalDay;
    }

    public int getArrivalTimeMin() {
        return arrivalTimeMin;
    }

    public String getName() {
        return name;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public int getWeight() {
        return weight;
    }

    public int getPeriodMin() {
        return periodMin;
    }

    public void setPeriodMin(int periodMin) {
        this.periodMin = periodMin;
    }
}
