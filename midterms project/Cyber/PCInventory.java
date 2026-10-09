import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PCInventory {

    public static final BigDecimal HOURLY_RATE =
            new BigDecimal("20.00");

    private final Map<String, PC> pcList =
            new LinkedHashMap<>();

    private final List<RentalRecord> rentalHistory =
            new ArrayList<>();

    public PCInventory() {
        for (int i = 1; i <= 3; i++) {
            String pcID = "PC-" + i;
            pcList.put(pcID, new PC(pcID));
        }
    }

    public PC getPC(String pcID) {
        PC pc = pcList.get(normalize(pcID));

        if (pc == null) {
            throw new IllegalArgumentException(
                    "PC not found. Choose PC-1, PC-2, or PC-3."
            );
        }

        refreshPCStatus(pc);
        return pc;
    }

    public List<PC> getAllPCs() {
        refreshAllStatuses();

        return Collections.unmodifiableList(
                new ArrayList<>(pcList.values())
        );
    }

    public List<RentalRecord> getRentalHistory(String pcID) {
        String id = normalize(pcID);
        List<RentalRecord> result = new ArrayList<>();

        for (RentalRecord record : rentalHistory) {
            if (record.getPcID().equals(id)) {
                result.add(record);
            }
        }

        return Collections.unmodifiableList(result);
    }

    public Optional<RentalRecord> getActiveRental(String pcID) {
        String id = normalize(pcID);

        for (int i = rentalHistory.size() - 1; i >= 0; i--) {
            RentalRecord record = rentalHistory.get(i);

            if (record.getPcID().equals(id) && record.isActive()) {
                return Optional.of(record);
            }
        }

        return Optional.empty();
    }

    public Optional<RentalRecord> getLatestRental(String pcID) {
        List<RentalRecord> records = getRentalHistory(pcID);

        if (records.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(records.get(records.size() - 1));
    }

    public RentalRecord createRentalRecord(
            String pcID,
            double hours,
            BigDecimal amountPaid,
            LocalDateTime startTime) {

        PC pc = getPC(pcID);

        if (!pc.isAvailable()) {
            throw new IllegalStateException(
                    pcID + " already has an active rental."
            );
        }

        if (!pc.isPCOpen()) {
            throw new IllegalStateException(
                    pcID + " must be powered on first."
            );
        }

        if (startTime == null) {
            throw new IllegalArgumentException(
                    "Start time is required."
            );
        }

        LocalDateTime now = LocalDateTime.now(
                ZoneId.of("Asia/Manila")
        );

        if (startTime.isAfter(now)) {
            throw new IllegalArgumentException(
                    "Start time cannot be in the future."
            );
        }

        BigDecimal required = HOURLY_RATE
                .multiply(BigDecimal.valueOf(hours))
                .setScale(2, RoundingMode.HALF_UP);

        if (amountPaid == null || amountPaid.compareTo(required) < 0) {
            throw new IllegalArgumentException(
                    "Payment must be at least PHP "
                            + required.toPlainString() + "."
            );
        }

        RentalRecord record = new RentalRecord(
                pcID, hours, amountPaid, startTime
        );

        if (!record.getEndTime().isAfter(now)) {
            throw new IllegalArgumentException(
                    "This rental would already be expired."
            );
        }

        rentalHistory.add(record);
        pc.setActiveRental(record);
        pc.setStatus(PC.PCStatus.IN_USE);

        return record;
    }

    public void refreshAllStatuses() {
        for (PC pc : pcList.values()) {
            refreshPCStatus(pc);
        }
    }

    private void refreshPCStatus(PC pc) {
        Optional<RentalRecord> active =
                getActiveRentalWithoutRefresh(pc.getPcID());

        if (active.isPresent()) {
            pc.setActiveRental(active.get());
            pc.setStatus(PC.PCStatus.IN_USE);
        } else {
            pc.setActiveRental(null);
            pc.setStatus(PC.PCStatus.AVAILABLE);
        }
    }

    private Optional<RentalRecord> getActiveRentalWithoutRefresh(
            String pcID) {

        for (int i = rentalHistory.size() - 1; i >= 0; i--) {
            RentalRecord record = rentalHistory.get(i);

            if (record.getPcID().equals(pcID)
                    && record.isActive()) {
                return Optional.of(record);
            }
        }

        return Optional.empty();
    }

    private String normalize(String pcID) {
        return pcID == null ? "" : pcID.trim().toUpperCase();
    }
}