public abstract class StaffMember {

    private final String staffID;
    private String name;

    public StaffMember(String staffID, String name) {
        this.staffID = staffID;
        this.name = name;
    }

    public String getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getRoleDescription();
}