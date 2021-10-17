package spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnloadingTaskStatistic {
    private static final int CRANE_COST = 30000;
    private List<ShipStatistic> shipsStatistics;
    private double fine;
    private int waitingTime;
    private int cranes;
    public int delayTime;
    private int unloadTime;
    private Ship.Cargo.CargoType type;

    public UnloadingTaskStatistic(Ship.Cargo.CargoType type){
        this.shipsStatistics = Collections.synchronizedList(new ArrayList());
        this.type = type;
        this.fine = 0;
        this.waitingTime = 0;
        this.cranes = 0;
        this.delayTime = 0;
        this.unloadTime = 0;
    }


    synchronized public double getFine() {
        return fine;
    }

    public void setCranes(int cranes) {
        this.cranes = cranes;
    }

    public int getCranes() {
        return cranes;
    }

    public Ship.Cargo.CargoType getType() {
        return type;
    }

    public List<ShipStatistic> getShipsStatistics() {
        return shipsStatistics;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public int getUnloadTime() {
        return unloadTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    synchronized private void addShipStatistic(ShipStatistic shipStatistic) {
        this.shipsStatistics.add(shipStatistic);
        if (shipStatistic.getWaitingTime() > 0) {
            this.waitingTime += shipStatistic.getWaitingTime();
        }
        this.delayTime += shipStatistic.getDelay();
        this.unloadTime += shipStatistic.getUnloadTime();
        this.fine = (this.waitingTime / 60.0) * 100.0 + (this.cranes - 1) * this.CRANE_COST;
    }
    synchronized public void addShipToStatistic(Ship ship) {
        this.addShipStatistic(new ShipStatistic(ship));
    }
}