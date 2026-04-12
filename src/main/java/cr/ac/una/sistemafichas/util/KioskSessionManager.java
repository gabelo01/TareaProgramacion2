package cr.ac.una.sistemafichas.util;

import cr.ac.una.sistemafichas.model.Client;
import cr.ac.una.sistemafichas.model.Station;

public class KioskSessionManager {

    private static Client currentClient = null;

    private static Station currentStation;

    public static void setStation(Station s) {
        currentStation = s;
    }

    public static Station getStation() {
        return currentStation;
    }

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    public static void clearSession() {
        currentClient = null;
    }

    public static boolean isGuest() {
        return currentClient == null;
    }
}
