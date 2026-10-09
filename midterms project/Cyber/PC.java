public class PC implements PowerControllable {
    private PCStatus status;
    private final String pcID;
    private boolean poweredOn;
    private RentalRecord activeRental;

    public PC(String pcID) {
        this.pcID = pcID;
        this.status = PCStatus.AVAILABLE;
        this.poweredOn = false;
        this.activeRental = null;
    }

    public String getPcID() {
        return pcID;
    }


    public enum PCStatus {
            IN_USE,
            AVAILABLE
    }

    public PCStatus getStatus() {
        return status;
    }


    public void setStatus(PCStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == PCStatus.AVAILABLE;
    }

    public RentalRecord getActiveRental() {
        return activeRental;
    }

    public void setActiveRental(RentalRecord activeRental) {
        this.activeRental = activeRental;
    }

    public boolean isPoweredOn() {
        return poweredOn;
    }

    @Override
    public boolean isPCOpen() {
        return poweredOn;
    }

    @Override
    public boolean turnOnPC() {
        if (poweredOn) {
            return false;
        }

        poweredOn = true;
        return true;
    }

    @Override
    public boolean turnOffPC() {
        if (!poweredOn) {
            return false;
        }

        poweredOn = false;
        return true;
    }
}