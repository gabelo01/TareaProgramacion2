package cr.ac.una.sistemafichas.util;

public class EmployeeSessionManager {
    
    private static String branchName;
    private static String stationName;

    public static void setBranch(String b) { branchName = b; }
    public static void setStation(String s) { stationName = s; }

    public static String getBranch() { return branchName; }
    public static String getStation() { return stationName; }

    public static void clear() {
        branchName = null;
        stationName = null;
    }
    
}
