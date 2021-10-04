package spring.service_1;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import spring.Ship;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TimeTable {
    public TimeTable(int size) throws IOException {
        readTimeTableParams();
        this.size = size;
        shipArrayList = new ArrayList[3];
        for (int i = 0; i < shipArrayList.length; i++) {
            shipArrayList[i] = new ArrayList<Ship>();
        }
    }
    private void readTimeTableParams() throws IOException {
        try (JsonReader reader = new JsonReader(new FileReader(System.getProperty("user.dir")
                + "/firstServiceDirectory/timetableParams.json"))) {
            Map gsonParser = new Gson().fromJson(reader, Map.class);
            speed = (ArrayList<Double>) gsonParser.get("speed");
            names = (ArrayList<String>) gsonParser.get("names");
        }
    }
    public void generateShips() {
        for (int i = 0; i < size; i++) {
            Ship ship = makeRandomShip();
            shipArrayList[ship.getCargo().getType().getInt()].add(ship);
        }
        addShipFromConsole();
    }

    private void addShipFromConsole() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        Date date = new Date();
        while (true) {
            System.out.println("Do you want to add a ship manualy? y/n");
            answer = scanner.next();
            while (!answer.equals("y") && !answer.equals("n")) {
                System.out.println("Do you want to add a ship manualy? y/n");
                answer = scanner.next();
            }

            if (answer.equals("n")) {
                return;
            }

            System.out.println("Print ship`s name:");
            String name = scanner.next();
            System.out.println("Print ship`s type LOOSE LIQUID CONTAINER 0/1/2:");
            int type = scanner.nextInt();
            System.out.println("Print ship`s weight:");
            Double weight = scanner.nextDouble();
            System.out.print("Print year of arrive:");
            int year = scanner.nextInt();
            System.out.print("Print month of arrive:");
            int month = scanner.nextInt();
            System.out.print("Print day of arrive:");
            int day = scanner.nextInt();
            System.out.print("Print hours of arrive:");
            int hour = scanner.nextInt();
            System.out.print("Print minuted of arrive:");
            int minute = scanner.nextInt();
            System.out.print("Print seconds of arrive:");
            int second = scanner.nextInt();
            Ship newShip = new Ship(name, new Ship.Cargo(Ship.Cargo.makeCargoType(type), weight, this.speed.get(type)),
                    new GregorianCalendar(year - 1900, month - 1, day, hour, minute, second));
            shipArrayList[type].add(newShip);
        }
    }

    public ArrayList<Ship>[] getShipArrayList() {
        return shipArrayList;
    }

    private static Ship makeRandomShip()
    {
      return new Ship(names.get(random.nextInt(names.size() - 1)), makeRandomCargo(),
              new GregorianCalendar(2021,        3, random.nextInt(30), random.nextInt(24),
              random.nextInt(60), random.nextInt(60)));
    }
    private static Ship.Cargo makeRandomCargo()
    {
        Integer temp = random.nextInt(3);
        Ship.Cargo.CargoType type = Ship.Cargo.makeCargoType(temp);
        return new Ship.Cargo(type, random.nextDouble()* MAX_WEIGHT, speed.get(temp));
    }

    private final static int MAX_WEIGHT = 1000;
    private static final Random random = new Random();
    private static ArrayList<String> names;
    private static ArrayList<Double> speed;
    private ArrayList<Ship>[] shipArrayList;
    private Integer size;
}
