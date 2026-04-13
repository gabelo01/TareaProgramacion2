package cr.ac.una.sistemafichas.util;

import cr.ac.una.sistemafichas.model.Client;

public class KioskSessionManager {

    private static Client currentClient = null;
    private static String branchName;
    private static String stationName;

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    public static void setBranch(String branch) {
        branchName = branch;
    }

    public static String getBranch() {
        return branchName;
    }

    public static void setStation(String station) {
        stationName = station;
    }

    public static String getStation() {
        return stationName;
    }

    public static void clearClient() {
        currentClient = null;
    }
    
   public static void clearAll() {
        currentClient = null;
        stationName = null;
        branchName = null;
    }

    public static boolean isGuest() {
        return currentClient == null;
    }
}