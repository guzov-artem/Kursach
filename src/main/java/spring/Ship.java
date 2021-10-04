package spring;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Optional;

public class Ship implements Cloneable {

    public static class Cargo implements Cloneable {

        public enum CargoType implements Cloneable {
            LOOSE(0),
            LIQUID(1),
            CONTAINERS(2);

            CargoType(int num) {
                number = num;
            }

            public int getInt() {
                return number;
            }

            private final int number;
            private double speed;
        }

        private final CargoType type;
        private double weight;

        public Cargo(CargoType type, Double weight, Double speed) {
            this.type = type;
            this.weight = weight;
            type.speed = speed;
        }

        public static CargoType makeCargoType(int type) throws RuntimeException {
            switch (type) {
                case 0: {
                    return CargoType.LOOSE;
                }
                case 1: {
                    return CargoType.LIQUID;
                }
                case 2:
                    return CargoType.CONTAINERS;
            }
            throw new RuntimeException("type must be from 0 to 2!");
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public CargoType getType() {
            return type;
        }

        public double getWeight() {
            return weight;
        }

        public double getSpeed_() {
            return type.speed;
        }

        public void changeWeight(double number) {
            if (-number > weight) {
                weight = 0.0;
            } else {
                weight += number;
            }
        }

        @Override
        public String toString() {
            return "Cargo{" +
                    "type_=" + type +
                    ", weight_=" + weight +
                    ", speed_=" + type.speed +
                    '}';
        }
    }

    private String name;
    private Cargo cargo;//tons
    private Calendar arriveDate;
    private Integer numberCranes;
    private Integer delay;
    private Optional<Calendar> startUploading;
    private Optional<Calendar> endUploading;
    private Optional<Integer> uploadingDelay;

    public Ship(String name, Cargo cargo, Calendar date) {
        this.name = name;
        this.cargo = cargo;
        this.arriveDate = date;
        this.startUploading = Optional.empty();
        this.endUploading = Optional.empty();
        this.uploadingDelay = Optional.empty();
        this.numberCranes = 0;
        this.delay = 0;
    }

    private Ship() {
        this.numberCranes = 0;
        this.delay = 0;
    }

    public Object clone() throws CloneNotSupportedException {
        Ship temp = new Ship();
        temp.name = this.name;
        temp.cargo = (Cargo) this.cargo.clone();
        temp.arriveDate = (Calendar) this.arriveDate.clone();
        temp.numberCranes = this.numberCranes;
        temp.delay = this.delay;
        temp.uploadingDelay = this.uploadingDelay;
        if (startUploading.isPresent()) {
            temp.startUploading = Optional.of((Calendar) this.startUploading.get().clone());
        } else {
            temp.startUploading = Optional.empty();
        }
        if (endUploading.isPresent()) {
            temp.endUploading = Optional.of((Calendar) this.endUploading.get().clone());
        } else {
            temp.endUploading = Optional.empty();
        }
        return temp;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public boolean isStartedUnloading() {
        return startUploading.isPresent();
    }

    public String getName() {
        return name;
    }

    public void decreaseCargo(Double number) {
        cargo.changeWeight(-number);
    }

    public int getNumberCranes() {
        return numberCranes;
    }

    public void changeNumberCranes(int number) {
        this.numberCranes += number;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getUploadingDelay() {
        return uploadingDelay.orElseThrow();
    }

    public void setUploadingDelay(int uploadingDelay) {
        this.uploadingDelay = Optional.of(uploadingDelay);
    }

    public int getDelay() {
        return delay;
    }

    public Calendar getArriveDate() {
        return arriveDate;
    }

    public void setStartUploading(Calendar startUploading) {
        this.startUploading = Optional.ofNullable(startUploading);
    }

    public Calendar getStartUploading() {
        return startUploading.orElseThrow();
    }

    public void setEndUnloading(Calendar endUploading) {
        this.endUploading = Optional.ofNullable(endUploading);
    }

    public Calendar getEndUnloading() {
        return endUploading.orElseThrow();
    }

    public boolean isUnloaded() {
        return endUploading.isPresent();
    }

    @Override
    public String toString() {
        return "Ship{" +
                "name_='" + name + '\'' +
                ", cargo_=" + cargo +
                ", data_=" + arriveDate +
                '}';
    }

    public static class ShipComparator implements Comparator<Ship> {
        public int compare(Ship first, Ship second) {
            return (int) ((Utils.getMinutes(first.getArriveDate()) + first.getDelay())
                    - (Utils.getMinutes(second.getArriveDate()) + second.getDelay()));
        }
    }
}
