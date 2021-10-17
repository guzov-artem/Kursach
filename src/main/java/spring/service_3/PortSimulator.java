package spring.service_3;

import spring.Ship;
import spring.Statistic;

import java.util.*;

public class PortSimulator {
    private ArrayList<Ship>[] ships;
    private Statistic statistic;
    private Calendar startDate;
    private Calendar endDate;
    private final int MAX_DELAY = 60*24*7;
    private final int MAX_UNLOADING_DELAY = 1400;

    public PortSimulator(ArrayList<Ship>[] ships, Calendar startDate, Calendar endDate) {
        this.ships = ships;
        this.startDate = startDate;
        this.endDate = endDate;
        this.statistic = new Statistic();
    }
    private void generateDelays() {
        Random random = new Random();
        for (int i = 0; i < this.ships.length; i++) {
            for (int j = 0; j < ships[i].size(); j++) {
                this.ships[i].get(j).setDelay(random.nextInt(2 * this.MAX_DELAY) - this.MAX_DELAY);
                this.ships[i].get(j).setUploadingDelay(random.nextInt(this.MAX_UNLOADING_DELAY));
            }
        }
    }
    private void sortShips() {
        for (int i =0; i < ships.length; i++) {
            ships[i].sort(new Ship.ShipComparator());
        }
    }

    public void findBestStatistic() throws InterruptedException {
        generateDelays();
        sortShips();
        List<UnloadingTask> threads = new ArrayList<>();
        for (int i =0; i < ships.length; i++) {
            threads.add(new UnloadingTask(this, ships[i]));
            threads.get(i).start();
        }
        for (int i =0; i < ships.length; i++) {
            threads.get(i).join();
            statistic.addStatisticStruct(threads.get(i).getStatisticStruct());
        }
    }

    public Statistic getStatistic()
    {
        return statistic;
    }

    public Calendar getStartDate() {
        return startDate;
    }
}
