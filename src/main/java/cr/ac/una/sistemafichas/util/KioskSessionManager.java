package cr.ac.una.sistemafichas.util;

import cr.ac.una.sistemafichas.model.Client;

public class KioskSessionManager {

    private static Client currentClient = null;

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