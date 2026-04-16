package cr.ac.una.sistemafichas.util;

public class EmployeeSessionManager {

    private static String branchName;
    private static String stationName;

    public static void setBranchName(String branch) {
        branchName = branch;
    }

    public static String getBranchName() {
        return branchName;
    }

    public static void setStationName(String station) {
        stationName = station;
    }

    public static String getStationName() {
        return stationName;
    }
}
