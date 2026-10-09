import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class RentalRecord {

    private final String pcID;
    private final double hoursPurchased;
    private final BigDecimal amountPaid;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public RentalRecord(
            String pcID,
            double hoursPurchased,
            BigDecimal amountPaid,
            LocalDateTime startTime) {

        if (pcID == null || pcID.isBlank()) {
            throw new IllegalArgumentException("PCID is required.");
        }

        if (!Double.isFinite(hoursPurchased)
                || hoursPurchased <= 0
                || hoursPurchased > 24) {
            throw new IllegalArgumentException(
                    "Hours must be greater than 0 and at most 24."
            );
        }

        if (amountPaid == null || amountPaid.signum() < 0) {
            throw new IllegalArgumentException(
                    "Payment cannot be negative."
            );
        }

        this.pcID = pcID;
        this.hoursPurchased = hoursPurchased;
        this.amountPaid = amountPaid;
        this.startTime = Objects.requireNonNull(
                startTime, "Start time is required."
        );

        long minutes = Math.round(hoursPurchased * 60.0);

        this.endTime = startTime.plusMinutes(minutes);
    }

    public String getPcID() {
        return pcID;
    }

    public double getHoursPurchased() {
        return hoursPurchased;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration calculateRemainingTime() {
        LocalDateTime now = LocalDateTime.now(
                ZoneId.of("Asia/Manila")
        );

        Duration remaining = Duration.between(now, endTime);

        return remaining.isNegative()
                ? Duration.ZERO
                : remaining;
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now(
                ZoneId.of("Asia/Manila")
        );

        return !now.isBefore(startTime)
                && now.isBefore(endTime);
    }
}