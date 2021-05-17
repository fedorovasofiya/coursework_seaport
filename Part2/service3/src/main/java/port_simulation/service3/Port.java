package port_simulation.service3;

import port_simulation.service1.Schedule;
import port_simulation.service1.Ship;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Port {

    private int numberOfBulkCranes;
    private int numberOfLiquidCranes;
    private int numberOfContainerCranes;

    private static int numberOfBulkShips;
    private static int numberOfLiquidShips;
    private static int numberOfContainerShips;

    private static AtomicLong start;

    public static LinkedBlockingQueue<ArrivingShip> currentBulkCargos;
    public static LinkedBlockingQueue<ArrivingShip> currentLiquidCargos;
    public static LinkedBlockingQueue<ArrivingShip> currentContainerCargos ;

    public static LinkedBlockingQueue<ArrivingShip> currentAllCargos ;

    private static LinkedBlockingQueue<ArrivingShip> unloadingDryCargos ;
    private static LinkedBlockingQueue<ArrivingShip> unloadingLiquidCargos ;
    private static LinkedBlockingQueue<ArrivingShip> unloadingContainerCargos ;

    private static LinkedBlockingQueue<ArrivingShip> unloadedContainer;
    private static LinkedBlockingQueue<ArrivingShip> unloadedBulk;
    private static LinkedBlockingQueue<ArrivingShip> unloadedLiquid;

    private static ArrayList<ArrivingShip> unloadedShips;

    private static final ArrayList<ArrivingShip> actualAllShips  = new ArrayList<>();

    public static int sumLengthAtCertain ;
    public static int numberOfCountsSum ;

    public static class BulkCrane extends Crane {
        public static final int BULK_CRANE_CAPACITY = 4;
        
        public BulkCrane(Future future, ArrivingShip ship) {
            super(future, ship, currentBulkCargos, BULK_CRANE_CAPACITY, unloadingDryCargos, unloadedBulk,
                    numberOfBulkShips, Ship.Cargo.BULK);
        }
    }

    public static class LiquidCrane extends Crane {
        public static final int LIQUID_CRANE_CAPACITY = 4;
        
        public LiquidCrane(Future future, ArrivingShip ship) {
            super(future, ship, currentLiquidCargos, LIQUID_CRANE_CAPACITY, unloadingLiquidCargos, unloadedLiquid,
                    numberOfLiquidShips, Ship.Cargo.LIQUID);
        }
    }

    public static class ContainerCrane extends Crane {
        public static final int CONTAINER_CRANE_CAPACITY = 4;
        public ContainerCrane(Future future, ArrivingShip ship) {
            super(future, ship, currentContainerCargos, CONTAINER_CRANE_CAPACITY, unloadingContainerCargos,
                    unloadedContainer, numberOfContainerShips, Ship.Cargo.CONTAINER);
        }

    }


    public Port(Schedule schedule) {
        for (int i = 0; i < schedule.getNumberOfShips(); i++) {
            ArrivingShip arrivingShip = new ArrivingShip(schedule.getShips().get(i));
            actualAllShips.add(arrivingShip);
        }
        numberOfBulkShips = schedule.getNumberOfBulkShips();
        numberOfContainerShips = schedule.getNumberOfContainerShips();
        numberOfLiquidShips = schedule.getNumberOfLiquidShips();


        actualAllShips.sort(ArrivingShip::compareTo);
    }
    public Port() {

    }


    public ArrayList<ArrivingShip> startModeling(int bulkCranes, int liquidCranes, int containerCranes) {

        currentBulkCargos = new LinkedBlockingQueue<>();
        currentLiquidCargos = new LinkedBlockingQueue<>();
        currentContainerCargos = new LinkedBlockingQueue<>();

        currentAllCargos = new LinkedBlockingQueue<>();

        unloadingDryCargos = new LinkedBlockingQueue<>();
        unloadingLiquidCargos = new LinkedBlockingQueue<>();
        unloadingContainerCargos = new LinkedBlockingQueue<>();

        unloadedContainer = new LinkedBlockingQueue<>();
        unloadedBulk = new LinkedBlockingQueue<>();
        unloadedLiquid = new LinkedBlockingQueue<>();

        unloadedShips = new ArrayList<>();
        start = new AtomicLong();

        sumLengthAtCertain = 0;
        numberOfCountsSum = 0;


        ExecutorService timer = Executors.newSingleThreadExecutor();
        Future futureTimer = timer.submit(new TimeManager());
        start.set(System.currentTimeMillis());

        numberOfBulkCranes = bulkCranes;
        numberOfLiquidCranes = liquidCranes;
        numberOfContainerCranes = containerCranes;

        ExecutorService bulkPool = Executors.newFixedThreadPool(numberOfBulkCranes, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        ExecutorService liquidPool = Executors.newFixedThreadPool(numberOfLiquidCranes, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        ExecutorService containerPool = Executors.newFixedThreadPool(numberOfContainerCranes, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        Future<LinkedBlockingQueue<ArrivingShip>> futureBulk = null;
        Future<LinkedBlockingQueue<ArrivingShip>> futureLiquid = null;
        Future<LinkedBlockingQueue<ArrivingShip>> futureContainer = null;


        for (int i = 0; i < numberOfBulkCranes; i++) {
            futureBulk = bulkPool.submit(new BulkCrane(futureTimer,
                    currentBulkCargos.isEmpty() ? null : currentBulkCargos.peek()));
        }

        for (int i = 0; i < numberOfLiquidCranes;i++) {
            futureLiquid = liquidPool.submit(new LiquidCrane(futureTimer,
                    currentLiquidCargos.isEmpty()? null : currentLiquidCargos.peek()));
        }

        for (int i = 0; i < numberOfContainerCranes; i++) {
            futureContainer = containerPool.submit(new ContainerCrane(futureTimer,
                    currentContainerCargos.isEmpty() ? null : currentContainerCargos.peek()));
        }

        timer.shutdown();
        bulkPool.shutdown();
        liquidPool.shutdown();
        containerPool.shutdown();

        try {

            unloadedBulk = futureBulk.get();
            unloadedLiquid = futureLiquid.get();
            unloadedContainer = futureContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try {
            int size = unloadedBulk.size();
            for (int i = 0; i < size; i++) {
                unloadedShips.add(unloadedBulk.take());
            }

            size = unloadedLiquid.size();
            for (int i = 0; i < size; i++) {
                unloadedShips.add(unloadedLiquid.take());
            }

            size = unloadedContainer.size();
            for (int i = 0; i < size; i++) {
                unloadedShips.add(unloadedContainer.take());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        unloadedShips.sort(ArrivingShip::compareTo);

        return unloadedShips;

    }

    public int getSumLengthAtCertain() {
        return sumLengthAtCertain;
    }

    public int getNumberOfCountsSum() {
        return numberOfCountsSum;
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

    public static AtomicLong getStart() {
        return start;
    }

    public static ArrayList<ArrivingShip> getUnloadedShips() {
        return unloadedShips;
    }

    public static ArrayList<ArrivingShip> getActualAllShips() {
        return actualAllShips;
    }

}
