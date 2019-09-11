package controller;

import entity.HouseHolder;
import entity.Manager;

public class Context {
    public static StageManager stageManager = new StageManager();
    public static Factory factory = new Factory();
    public static Manager manager = new Manager();
    public static HouseHolder houseHolder = new HouseHolder();
    public static int accountNumber;
}
