package service3;

import service1.Ship;

public class ArrivingShip extends Ship implements Comparable<ArrivingShip>{

    private int scheduleDeviation;
    private int delayToUnload;

    private int unloadedWeight = 0;
    private int unloadingCranesAmount = 0;

    private long timeWhenOneCraneStarts = -1;
    private long timeWhenTwoCranesStarts = -1;
    private long timeWhenUnloaded = -1;

    private long waitingTimeInQueue = -1;
    private long durationUnloading = -1;

    private long timeWhenArrived = -1;


    public ArrivingShip(Ship ship) {

        super(ship.getArrivalDay(),ship.getArrivalTimeMin(), ship.getName(), ship.getCargo(), ship.getWeight());

        this.scheduleDeviation = (int) (Math.random() * (7 + 1));
        if (this.arrivalDay + this.scheduleDeviation < 0) {
            this.arrivalDay = 0;
            this.scheduleDeviation = 0;
        }
        this.delayToUnload = (int) (Math.random() * (1440 + 1));

        this.periodMin += this.delayToUnload;
    }

    public int getScheduleDeviation() {
        return scheduleDeviation;
    }

    public void setScheduleDeviation(int scheduleDeviation) {
        this.scheduleDeviation = scheduleDeviation;
    }

    public int getDelayToUnload() {
        return delayToUnload;
    }

    public void setDelayToUnload(int delayToUnload) {
        this.delayToUnload = delayToUnload;
    }

    public int getUnloadedWeight() {
        return unloadedWeight;
    }

    public void setUnloadedWeight(int unloadedWeight) {
        this.unloadedWeight = unloadedWeight;
    }

    public long getTimeWhenOneCraneStarts() {
        return timeWhenOneCraneStarts;
    }

    public void setTimeWhenOneCraneStarts(long timeWhenOneCraneStarts) {
        this.timeWhenOneCraneStarts = timeWhenOneCraneStarts;
    }

    public int getUnloadingCranesAmount() {
        return unloadingCranesAmount;
    }

    public void setUnloadingCranesAmount(int unloadingCranesAmount) {
        this.unloadingCranesAmount = unloadingCranesAmount;
    }

    public long getTimeWhenTwoCranesStarts() {
        return timeWhenTwoCranesStarts;
    }

    public void setTimeWhenTwoCranesStarts(long timeWhenTwoCranesStarts) {
        this.timeWhenTwoCranesStarts = timeWhenTwoCranesStarts;
    }

    public long getTimeWhenUnloaded() {
        return timeWhenUnloaded;
    }

    public void setTimeWhenUnloaded(long timeWhenUnloaded) {
        this.timeWhenUnloaded = timeWhenUnloaded;
    }

    public long getWaitingTimeInQueue() {
        return waitingTimeInQueue;
    }

    public void setWaitingTimeInQueue(long waitingTimeInQueue) {
        this.waitingTimeInQueue = waitingTimeInQueue;
    }

    public long getDurationUnloading() {
        return durationUnloading;
    }

    public void setDurationUnloading(long durationUnloading) {
        this.durationUnloading = durationUnloading;
    }

    public long getTimeWhenArrived() {
        return timeWhenArrived;
    }

    public void setTimeWhenArrived(long timeWhenArrived) {
        this.timeWhenArrived = timeWhenArrived;
    }

    @Override
    public String toString() {
        return "ArrivingShip{" +
                "name: '" + name + '\'' +
                ", arrival time: " + (arrivalDay + scheduleDeviation) + ':' + arrivalTimeMin / 60 + ':'
                + arrivalTimeMin % 60 +
                ", waiting time: " + waitingTimeInQueue / (24 * 60) + ':' + waitingTimeInQueue % (24 * 60) / 60 + ':'
                + waitingTimeInQueue % (24 * 60) % 60 +
                ", start time: " + timeWhenOneCraneStarts / (24 * 60) + ':' + timeWhenOneCraneStarts % (24 * 60) / 60
                + ':' + timeWhenOneCraneStarts % (24 * 60) % 60 +
                ", unloading duration: " + durationUnloading / (24 * 60) + ':' + durationUnloading % (24 * 60) / 60
                + ':' + durationUnloading % (24 * 60) % 60 +
                '}';
    }

    @Override
    public int compareTo(ArrivingShip o) {
        return this.arrivalDay * 24 * 60 + this.arrivalTimeMin - (o.arrivalDay * 24 * 60 + o.arrivalTimeMin)
                + this.scheduleDeviation * 24 * 60 - o.scheduleDeviation * 24 * 60;
    }
}
