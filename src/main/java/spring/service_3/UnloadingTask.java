package spring.service_3;

import spring.Ship;
import spring.UnloadingTaskStatistic;
import spring.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Semaphore;

public class UnloadingTask extends Thread {
    private final PortSimulator portSimulator;
    private ArrayList<Ship> ships;
    private Calendar currentDate;
    private UnloadingTaskStatistic unloadingTaskStatistic;
    private Ship.Cargo.CargoType type;


    public UnloadingTask(PortSimulator portSimulator, ArrayList<Ship> ships, Ship.Cargo.CargoType type) {
        this.portSimulator = portSimulator;
        this.ships = ships;
        this.unloadingTaskStatistic = new UnloadingTaskStatistic(type);
        this.currentDate = new GregorianCalendar();
        this.type = type;
    }

    @Override
    public void run() {
        if (ships.size() == 0) {
            this.unloadingTaskStatistic = new UnloadingTaskStatistic(type);
            return;
        }
        try {
            int cranes = 1;
            UnloadingTaskStatistic currentstatisticThread = simulate(cranes);
            UnloadingTaskStatistic nextUnloadingTaskStatistic = simulate(++cranes);
            while (currentstatisticThread.getFine() > nextUnloadingTaskStatistic.getFine()) {
                currentstatisticThread = simulate(cranes++);
                nextUnloadingTaskStatistic = simulate(cranes);
            }
            this.unloadingTaskStatistic = currentstatisticThread;
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private boolean isUnloaded(ArrayList<Ship> shipsCheck) {
        for (Ship ship : shipsCheck) {
            if (!ship.isUnloaded()) {
                return false;
            }
        }
        return shipsCheck.get(shipsCheck.size() - 1).getUploadingDelay() <= 0;
    }

    UnloadingTaskStatistic simulate(int cranes) throws CloneNotSupportedException {
        UnloadingTaskStatistic unloadingTaskStatistic = new UnloadingTaskStatistic(this.unloadingTaskStatistic.getType());
        unloadingTaskStatistic.setCranes(cranes);

        ArrayList<CraneThread> threads = new ArrayList<>();
        ArrayList<Ship> temp = new ArrayList<Ship>();
        for (Ship ship : this.ships) {
            temp.add((Ship) ship.clone());
        }

        this.currentDate = (Calendar) this.portSimulator.getStartDate().clone();
        Object mutexEndUnloading = new Object();
        for (int i = 0; i < cranes; i++) {
            threads.add(new CraneThread(temp, unloadingTaskStatistic, this.currentDate,
                    new Semaphore(0), new Semaphore(1), mutexEndUnloading));
            threads.get(i).start();
        }

        while (!isUnloaded(temp)) {
            for (CraneThread task : threads) {
                try {
                    task.getSemaphore1().acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countQueue(unloadingTaskStatistic);
            this.currentDate.setTimeInMillis(this.currentDate.getTimeInMillis() + 60 * 1000);
            for (CraneThread task : threads) {

                task.getSemaphore2().release();
            }
        }
        for (CraneThread task : threads) {
            task.stopWorking();
            task.getSemaphore2().release();
        }
        return unloadingTaskStatistic;
    }
    private void countQueue(UnloadingTaskStatistic unloadingTaskStatistic) {
        int queue = 0;
        for (Ship ship : ships) {
            if (!ship.isStartedUnloading()
                  && (Utils.getMinutes(ship.getArriveDate()) + ship.getDelay() >= Utils.getMinutes(this.currentDate))) {
                queue++;
            }
        }
        unloadingTaskStatistic.addQueueSum(queue);
    }

    public UnloadingTaskStatistic getStatisticStruct() {
        return this.unloadingTaskStatistic;
    }
}