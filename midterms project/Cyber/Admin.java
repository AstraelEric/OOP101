public class Admin extends StaffMember {

    public Admin(String staffID, String name) {
        super(staffID, name);
    }

    @Override
    public String getRoleDescription() {
        return "Administrator - manages PCs and rentals";
    }

    public boolean turnPCOn(PC pc) {
        if (pc == null) {
            return false;
        }

        PowerControllable device = pc;
        return device.turnOnPC();
    }

    public boolean turnPCOff(PC pc) {
        if (pc == null) {
            return false;
        }

        PowerControllable device = pc;
        return device.turnOffPC();
    }
}