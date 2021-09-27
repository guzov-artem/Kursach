package spring.service_3;

import spring.Ship;
import spring.UnloadingTaskStatistic;
import spring.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

class CraneThread extends Thread {
    private List<Ship> ships;
    private Ship currentShip;
    private UnloadingTaskStatistic unloadingTaskStatistic;
    private boolean free;
    private boolean isWorking;
    private Semaphore semaphore1;
    private Semaphore semaphore2;
    private Object endUnloadingMutex;
    private Calendar date;

    public CraneThread(ArrayList<Ship> ships, UnloadingTaskStatistic unloadingTaskStatistic, Calendar currentDate,
                       Semaphore semaphore1, Semaphore semaphore2, Object endUnloadingMutex) {
        this.ships = Collections.synchronizedList(ships);
        this.unloadingTaskStatistic = unloadingTaskStatistic;
        this.date = currentDate;
        this.semaphore1 = semaphore1;
        this.semaphore2 = semaphore2;
        this.isWorking = true;
        this.free = true;
        this.endUnloadingMutex = endUnloadingMutex;
    }

    public void stopWorking() {
        isWorking = false;
    }

    public Semaphore getSemaphore1() {
        return semaphore1;
    }

    public Semaphore getSemaphore2() {
        return semaphore2;
    }

    @Override
    public void run() {
        while (isWorking) {
            try {
                semaphore2.acquire();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (free) {

                for (Ship ship : ships) {
                    if ((ship.getNumberCranes() < 2) && (ship.getCargo().getWeight() > 0)
                            && (Utils.getMinutes(ship.getArriveDate()) + ship.getDelay())
                            <= Utils.getMinutes(date)) {
                        currentShip = ship;
                        currentShip.changeNumberCranes(1);
                        if (!currentShip.isStartedUnloading()) {
                            currentShip.setStartUploading((Calendar) date.clone());
                        }
                        free = false;
                        break;
                    }
                }
            }
            if (currentShip != null) {
                currentShip.decreaseCargo(currentShip.getCargo().getSpeed_());
                if (currentShip.getCargo().getWeight() <= 0) {
                    synchronized (endUnloadingMutex) {
                        if (!currentShip.isUnloaded()) {
                            Calendar temp = (Calendar) date.clone();
                            temp.setTimeInMillis(temp.getTimeInMillis() + currentShip.getUploadingDelay() * 1000);
                            currentShip.setEndUnloading(temp);
                            unloadingTaskStatistic.addShipToStatistic(currentShip);
                        }
                    }
                    if (currentShip.getUploadingDelay() <= 0) {
                        free = true;
                    } else {
                        currentShip.setUploadingDelay(currentShip.getUploadingDelay() - 1);
                    }
                }
            }
            semaphore1.release();
        }
    }
}
