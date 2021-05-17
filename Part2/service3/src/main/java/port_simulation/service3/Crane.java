package port_simulation.service3;

import port_simulation.service1.Ship;
import java.util.concurrent.*;


import static java.lang.Thread.sleep;
import static port_simulation.service3.Port.currentAllCargos;


public class Crane implements Callable<LinkedBlockingQueue<ArrivingShip>>{

    final static public int COST = 30000;

    private final LinkedBlockingQueue<ArrivingShip> current;
    private final LinkedBlockingQueue<ArrivingShip> unloading;
    private final LinkedBlockingQueue<ArrivingShip> unloaded ;

    private ArrivingShip ship;
    private double capacity;
    private Ship.Cargo cargo;
    private int number;
    private Future timer;


    static long getTime() {
        long stop = System.currentTimeMillis();
        return stop - Port.getStart().get();
    }

    public Crane(Future timer, ArrivingShip ship, LinkedBlockingQueue<ArrivingShip> current, double capacity,
                 LinkedBlockingQueue<ArrivingShip> unloading, LinkedBlockingQueue<ArrivingShip> unloaded,
                 int number, Ship.Cargo cargo) {
        this.timer = timer;
        this.ship = ship;
        this.current = current;
        this.capacity = capacity;
        this.unloading = unloading;
        this.unloaded = unloaded;
        this.number = number;
        this.cargo = cargo;
    }

    @Override
    public LinkedBlockingQueue<ArrivingShip> call() {

        while (unloaded.size() != number) {

            if (!current.isEmpty()) {
                try {
                    ship = current.take();

                    Port.sumLengthAtCertain += currentAllCargos.size();
                    Port.numberOfCountsSum++;
                    currentAllCargos.take();

                    unloading.put(ship);
                    ship.setTimeWhenOneCraneStarts(getTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ship.setWaitingTimeInQueue(ship.getTimeWhenOneCraneStarts() - ship.getTimeWhenArrived());
                ship.setUnloadingCranesAmount(1);

            } else if (!unloading.isEmpty()) {

                try {
                    ship = unloading.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ship.setTimeWhenTwoCranesStarts(getTime());
                ship.setUnloadedWeight((int) ((ship.getTimeWhenTwoCranesStarts() - ship.getTimeWhenOneCraneStarts()) * capacity));

                if (ship.getWeight() <= ship.getUnloadedWeight()) {
                    ship.setUnloadingCranesAmount(1);

                    ship.setTimeWhenUnloaded(getTime());
                    ship.setDurationUnloading(ship.getTimeWhenUnloaded() - ship.getTimeWhenOneCraneStarts());
                    try {
                        unloaded.put(ship);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(Thread.currentThread().getName() + " adding " + ship.getName() + " to unloaded");
                    ship.setUnloadedWeight(ship.getWeight());
                    continue;
                }


                ship.setPeriodMin((int) ((ship.getWeight() - ship.getUnloadedWeight()) / (capacity * 2)));
                try {
                    ship.setUnloadingCranesAmount(2);
                    sleep(ship.getPeriodMin() / 10);
                    unloaded.put(ship);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println(Thread.currentThread().getName() + " adding " + ship.getName() + " to unloaded");
                ship.setUnloadedWeight(ship.getWeight());
                ship.setTimeWhenUnloaded(getTime());
                ship.setDurationUnloading(ship.getTimeWhenUnloaded() - ship.getTimeWhenOneCraneStarts());
            }
        }

        return unloaded;
    }


    public ArrivingShip getShip() {
        return ship;
    }

    public void setShip(ArrivingShip ship) {
        this.ship = ship;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public Ship.Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Ship.Cargo cargo) {
        this.cargo = cargo;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Future getTimer() {
        return timer;
    }

    public void setTimer(Future timer) {
        this.timer = timer;
    }
}
