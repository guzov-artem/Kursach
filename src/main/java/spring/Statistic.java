package spring;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private List<ShipStatistic> statLiquidList;
    private List<ShipStatistic> statLooseList;
    private List<ShipStatistic> statContainerList;

    public Statistic() {
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
        statLiquidList = Collections.synchronizedList(new ArrayList());
        statLooseList = Collections.synchronizedList(new ArrayList());
        statContainerList = Collections.synchronizedList(new ArrayList());
    }

    synchronized public  void addStatisticStruct(UnloadingTaskStatistic unloadingTaskStatistic) {
        switch (unloadingTaskStatistic.getType()) {
            case LIQUID: {
                statLiquidList = unloadingTaskStatistic.getShipsStatistics();
                amountLiquide = statLiquidList.size();
                liquideCranes = unloadingTaskStatistic.getCranes();
            }
            break;
            case LOOSE: {
                statLooseList = unloadingTaskStatistic.getShipsStatistics();
                amountLoose = statLooseList.size();
                looseCranes = unloadingTaskStatistic.getCranes();
            }
            break;
            case CONTAINERS: {
                statContainerList = unloadingTaskStatistic.getShipsStatistics();
                amountContainer = statContainerList.size();
                containerCranes = unloadingTaskStatistic.getCranes();
            }
            break;
        }
        unloadTime += unloadingTaskStatistic.getUnloadTime();
        waitingTime += unloadingTaskStatistic.getWaitingTime();
        delayTime += unloadingTaskStatistic.getDelayTime();
        fine += unloadingTaskStatistic.getFine();
        averageWaitingTime = waitingTime / (amountLoose + amountLiquide + amountContainer);
        averageDelayTime = delayTime / (amountLoose + amountLiquide + amountContainer);
        averageUnloadTime = unloadTime / (amountLoose + amountLiquide + amountContainer);
    }

    public void writeToFile(String name) throws IOException {
        JsonWriter writer = new JsonWriter(new FileWriter(System.getProperty("user.dir") + "/" + name));
        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").setPrettyPrinting().create();
        Type TYPE = new TypeToken<Statistic>() {
        }.getType();
        gson.toJson(this, TYPE, writer);
        writer.close();
    }


}


