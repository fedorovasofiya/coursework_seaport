package port_simulation.service3;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@JsonAutoDetect
public class Result {

    private int number;
    private int averageLengthQueue;
    private long averageTimeInQueue;
    private long maxDelayInUnloading;
    private long averageDelayInUnloading;
    private int penaltyAmount;
    private int numberOfBulkCranes;
    private int numberOfLiquidCranes;
    private int numberOfContainerCranes;

    private int penaltyBulk = 0;
    private int penaltyLiquid = 0;
    private int penaltyContainer = 0;

    public Result() {
    }

    public Result(Port port) {
        this.number = Port.getUnloadedShips().size();
        this.averageLengthQueue = port.getSumLengthAtCertain() / port.getNumberOfCountsSum();

        long sumTimeInQueue = 0;
        for (int i = 0; i < this.number; i++) {
            long waitingTime = Port.getUnloadedShips().get(i).getWaitingTimeInQueue();
            sumTimeInQueue += waitingTime;
            switch (Port.getUnloadedShips().get(i).getCargo()) {
                case BULK:
                    this.penaltyBulk += Math.floor(waitingTime / 60.0) * 100;
                    break;
                case LIQUID:
                    this.penaltyLiquid += Math.floor(waitingTime / 60.0) * 100;
                    break;
                case CONTAINER:
                    this.penaltyContainer += Math.floor(waitingTime / 60.0) * 100;
                    break;
            }
            this.penaltyAmount = penaltyBulk + penaltyLiquid + penaltyContainer;
        }
        this.averageTimeInQueue = sumTimeInQueue/ this.number;

        List<Long> delays = new LinkedList();
        long sumDelays = 0;
        for (int i = 0; i < this.number; i++) {
            long delay = Port.getUnloadedShips().get(i).getDelayToUnload();
            delays.add(delay);
            sumDelays += delay;
        }
        this.maxDelayInUnloading = Collections.max(delays);
        this.averageDelayInUnloading = sumDelays / this.number;
        this.numberOfBulkCranes = port.getNumberOfBulkCranes();
        this.numberOfLiquidCranes = port.getNumberOfLiquidCranes();
        this.numberOfContainerCranes = port.getNumberOfContainerCranes();

    }

    public int getPenaltyBulk() {
        return penaltyBulk;
    }

    public int getPenaltyLiquid() {
        return penaltyLiquid;
    }

    public int getPenaltyContainer() {
        return penaltyContainer;
    }

    public int getNumber() {
        return number;
    }

    public int getAverageLengthQueue() {
        return averageLengthQueue;
    }

    public long getAverageTimeInQueue() {
        return averageTimeInQueue;
    }

    public long getMaxDelayInUnloading() {
        return maxDelayInUnloading;
    }

    public long getAverageDelayInUnloading() {
        return averageDelayInUnloading;
    }

    public int getPenaltyAmount() {
        return penaltyAmount;
    }

    public int getNumberOfBulkCranes() {
        return numberOfBulkCranes;
    }

    public int getNumberOfLiquidCranes() {
        return numberOfLiquidCranes;
    }

    public int getNumberOfContainerCranes() {
        return numberOfContainerCranes;
    }

    @Override
    public String toString() {
        return "Result{" +
                "number: " + number +
                ", averageLengthQueue: " + averageLengthQueue +
                ", averageTimeInQueue: " + averageTimeInQueue / (24 * 60) + ':' + averageTimeInQueue % (24 * 60) / 60  + ':' + averageTimeInQueue % (24 * 60) % 60 +
                ", maxDelayInUnloading: " + maxDelayInUnloading / (24 * 60) + ':' + maxDelayInUnloading % (24 * 60) / 60  + ':' + maxDelayInUnloading % (24 * 60) % 60 +
                ", averageDelayInUnloading: " + averageDelayInUnloading / (24 * 60) + ':' + averageDelayInUnloading % (24 * 60) / 60  + ':' + averageDelayInUnloading % (24 * 60) % 60 +
                ", allPenaltyAmount: " + penaltyAmount +
                ", penaltyBulk: " + penaltyBulk +
                ", penaltyLiquid: " + penaltyLiquid +
                ", penaltyContainer: " + penaltyContainer +
                ", numberOfBulkCranes: " + numberOfBulkCranes +
                ", numberOfLiquidCranes: " + numberOfLiquidCranes +
                ", numberOfContainerCranes: " + numberOfContainerCranes +
                "}\n";
    }
}
