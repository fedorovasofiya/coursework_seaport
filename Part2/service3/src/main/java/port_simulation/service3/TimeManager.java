package port_simulation.service3;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import port_simulation.service1.Ship;

import static java.lang.Thread.sleep;
import static port_simulation.service3.Port.currentBulkCargos;
import static port_simulation.service3.Port.currentLiquidCargos;
import static  port_simulation.service3.Port.*;

class TimeManager implements Runnable {

    private final HashMap<Ship.Cargo, LinkedBlockingQueue<ArrivingShip>> current = new HashMap<>();
    private long lastShipTime;

    TimeManager() {
        this.current.put(Ship.Cargo.BULK, currentBulkCargos);
        this.current.put(Ship.Cargo.LIQUID, currentLiquidCargos);
        this.current.put(Ship.Cargo.CONTAINER, currentContainerCargos);
        lastShipTime = 0;
    }

    @Override
    public void run() {
        for (ArrivingShip ship : Port.getActualAllShips()) {

            try {
                sleep(((long) (ship.getArrivalDay()) * 24 * 60 + ship.getArrivalTimeMin() +
                        (long) ship.getScheduleDeviation() * 24 * 60 - lastShipTime) / 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lastShipTime = (long) (ship.getArrivalDay()) * 24 * 60 + ship.getArrivalTimeMin()
                    + (long) ship.getScheduleDeviation() * 24 * 60;

            current.get(ship.getCargo()).add(ship);
            currentAllCargos.add(ship);
            ship.setTimeWhenArrived(Crane.getTime());
        }
    }
}
