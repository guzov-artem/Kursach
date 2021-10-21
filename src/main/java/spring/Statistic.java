package spring;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Statistic {

    private int unloadTime;
    private int waitingTime;
    private int delayTime;
    private double averageWaitingTime;
    private double averageDelayTime;
    private double averageUnloadTime;
    private int fine;
    private int amountLiquide;
    private int amountLoose;
    private int amountContainer;
    private int liquideCranes;
    private int containerCranes;
    private int looseCranes;
    private double averageLiquidQueue;
    private double averageLooseQueue;
    private double averageContainerQueue;
    private List<ShipStatistic> statLiquidList;
    private List<ShipStatistic> statLooseList;
    private List<ShipStatistic> statContainerList;
    private transient Calendar begin;
    private transient Calendar end;

    public Statistic(Calendar begin, Calendar end) {
        this.unloadTime = 0;
        this.waitingTime = 0;
        this.delayTime = 0;
        this.averageWaitingTime = 0;
        this.averageDelayTime = 0;
        this.averageUnloadTime = 0;
        this.fine = 0;
        this.amountLiquide = 0;
        this.amountLoose = 0;
        this.amountContainer = 0;
        this.liquideCranes = 0;
        this.containerCranes = 0;
        this.looseCranes = 0;
        this.statLiquidList = Collections.synchronizedList(new ArrayList());
        this.statLooseList = Collections.synchronizedList(new ArrayList());
        this.statContainerList = Collections.synchronizedList(new ArrayList());
        this.begin = begin;
        this.end = end;
    }

    synchronized public  void addStatisticStruct(UnloadingTaskStatistic unloadingTaskStatistic) {
        long unloadingDuration = Utils.getMinutes(end) - Utils.getMinutes(begin);
        switch (unloadingTaskStatistic.getType()) {
            case LIQUID: {
                this.statLiquidList = unloadingTaskStatistic.getShipsStatistics();
                this.amountLiquide = statLiquidList.size();
                this.liquideCranes = unloadingTaskStatistic.getCranes();
                this.averageLiquidQueue = (double) unloadingTaskStatistic.getQueueSum() / unloadingDuration;
            }
            break;
            case LOOSE: {
                this.statLooseList = unloadingTaskStatistic.getShipsStatistics();
                this.amountLoose = statLooseList.size();
                this.looseCranes = unloadingTaskStatistic.getCranes();
                this.averageLooseQueue = (double) unloadingTaskStatistic.getQueueSum() / unloadingDuration;
            }
            break;
            case CONTAINERS: {
                this.statContainerList = unloadingTaskStatistic.getShipsStatistics();
                this.amountContainer = statContainerList.size();
                this.containerCranes = unloadingTaskStatistic.getCranes();
                this.averageContainerQueue = (double) unloadingTaskStatistic.getQueueSum() / unloadingDuration;
            }
            break;
        }
        this.unloadTime += unloadingTaskStatistic.getUnloadTime();
        this.waitingTime += unloadingTaskStatistic.getWaitingTime();
        this.delayTime += unloadingTaskStatistic.getDelayTime();
        this.fine += unloadingTaskStatistic.getFine();
        if ((amountLoose + amountLiquide + amountContainer) > 0) {
            this.averageWaitingTime = waitingTime / (amountLoose + amountLiquide + amountContainer);
            this.averageDelayTime = delayTime / (amountLoose + amountLiquide + amountContainer);
            this.averageUnloadTime = unloadTime / (amountLoose + amountLiquide + amountContainer);
        }
    }
}