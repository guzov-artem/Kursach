package spring;

import java.util.Calendar;

class ShipStatistic {
    private String name;
    private Calendar arrivalTime;
    private long waitingTime;
    private Calendar startUnloadTime;
    private Calendar endUnloadingTime;
    private long unloadTime;
    private Ship.Cargo.CargoType type;
    private int delay;

    public long getWaitingTime() {
        return waitingTime;
    }

    public int getDelay() {
        return delay;
    }

    public long getUnloadTime() {
        return unloadTime;
    }

    public ShipStatistic(Ship ship) {
        this.name = ship.getName();
        this.arrivalTime = ship.getArriveDate();
        this.startUnloadTime = ship.getStartUploading();

        this.unloadTime = Utils.getMinutes(ship.getEndUnloading())
                - Utils.getMinutes(ship.getStartUploading());
        this.type = ship.getCargo().getType();
        this.delay = ship.getDelay();
        this.endUnloadingTime = ship.getEndUnloading();
        this.waitingTime = Utils.getMinutes(this.startUnloadTime) - Utils.getMinutes(this.arrivalTime)
                - this.delay;
    }
}