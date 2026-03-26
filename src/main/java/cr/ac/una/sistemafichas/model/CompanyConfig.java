package cr.ac.una.sistemafichas.model;


public class CompanyConfig {
    private String companyName;
    private String logoPath;
    private String adminPin;

    public CompanyConfig() {}

    public String getCompanyName() {
        return companyName; 
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogoPath() {
        return logoPath; 
    }
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath; 
    }

    public String getAdminPin() { 
        return adminPin;
    }
    public void setAdminPin(String adminPin) {
        this.adminPin = adminPin;
    }
}
