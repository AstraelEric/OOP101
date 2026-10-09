import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        PCInventory inventory = new PCInventory();
        Admin admin = new Admin("ADMIN-001", "Shop Admin");

        StaffMember operator = admin;

        boolean running = true;

        System.out.println("========================================");
        System.out.println("   CYBERTECH PC MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println("Welcome, " + operator.getName());
        System.out.println(operator.getRoleDescription());
        System.out.println("Hourly rate: PHP 20.00");
        System.out.println("Managed PCs: PC-1, PC-2, PC-3");

        while (running) {
            printMenu();

            int choice = readInt(input, "Enter your choice: ", 0, 7);

            try {
                switch (choice) {
                    case 1:
                        inputRental(input, inventory);
                        break;
                    case 2:
                        viewAllPCs(inventory);
                        break;
                    case 3:
                        viewSpecificPC(input, inventory);
                        break;
                    case 4:
                        controlPower(input, admin, inventory, true);
                        break;
                    case 5:
                        controlPower(input, admin, inventory, false);
                        break;
                    case 6:
                        viewRentalHistory(input, inventory);
                        break;
                    case 7:
                        showHelp();
                        break;
                    case 0:
                        System.out.println(
                                "Thank you for using CyberTech!"
                        );
                        running = false;
                        break;
                }
            } catch (IllegalArgumentException
                     | IllegalStateException ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            if (running) {
                System.out.println();
                System.out.print(
                        "Press Enter to return to the menu..."
                );
                input.nextLine();
            }
        }

        input.close();
    }

    private static void printMenu() {
        System.out.println("\n--------------- MENU ---------------");
        System.out.println("1. Input PC Rental Data");
        System.out.println("2. View All PCs (Summary)");
        System.out.println("3. View Specific PC (Details)");
        System.out.println("4. Turn PC ON");
        System.out.println("5. Turn PC OFF");
        System.out.println("6. View Rental History");
        System.out.println("7. Help");
        System.out.println("0. Exit");
        System.out.println("------------------------------------");
    }

    private static void inputRental(
            Scanner input, PCInventory inventory) {

        System.out.println("\n--- INPUT RENTAL DATA ---");

        String pcID = readPCID(input);
        PC pc = inventory.getPC(pcID);

        if (!pc.isAvailable()) {
            System.out.println(pcID + " is already in use.");
            return;
        }

        if (!pc.isPCOpen()) {
            System.out.println(
                    "Turn the PC on before starting a rental."
            );
            return;
        }

        double hours = readHours(input);

        BigDecimal required = PCInventory.HOURLY_RATE
                .multiply(BigDecimal.valueOf(hours))
                .setScale(2, java.math.RoundingMode.HALF_UP);

        System.out.println(
                "Required payment: PHP " + required.toPlainString()
        );

        BigDecimal paid = readMoney(input);
        LocalDateTime start = readStartTime(input);

        RentalRecord record = inventory.createRentalRecord(
                pcID, hours, paid, start
        );

        System.out.println("\nRental recorded successfully.");
        printRentalDetails(record);
    }

    private static void viewAllPCs(PCInventory inventory) {
        System.out.println("\n--- ALL PCS ---");

        System.out.printf(
                "%-8s %-10s %-12s %-10s %-12s %-14s%n",
                "PCID", "POWER", "STATUS",
                "HOURS", "PAID", "REMAINING"
        );

        for (PC pc : inventory.getAllPCs()) {
            Optional<RentalRecord> active =
                    inventory.getActiveRental(pc.getPcID());

            String hours = "-";
            String paid = "-";
            String remaining = "-";

            if (active.isPresent()) {
                RentalRecord record = active.get();

                hours = String.valueOf(record.getHoursPurchased());
                paid = "PHP " + record.getAmountPaid().toPlainString();

                remaining = formatDuration(
                        record.calculateRemainingTime()
                );
            }

            System.out.printf(
                    "%-8s %-10s %-12s %-10s %-12s %-14s%n",
                    pc.getPcID(),
                    pc.isPCOpen() ? "ON" : "OFF",
                    pc.getStatus(),
                    hours,
                    paid,
                    remaining
            );
        }
    }

    private static void viewSpecificPC(
            Scanner input, PCInventory inventory) {

        String pcID = readPCID(input);
        PC pc = inventory.getPC(pcID);

        System.out.println("\n--- PC DETAILS ---");
        System.out.println("PCID: " + pc.getPcID());
        System.out.println(
                "Power: " + (pc.isPCOpen() ? "ON" : "OFF")
        );
        System.out.println("Status: " + pc.getStatus());

        Optional<RentalRecord> record =
                inventory.getActiveRental(pcID);

        if (record.isEmpty()) {
            record = inventory.getLatestRental(pcID);
        }

        if (record.isPresent()) {
            printRentalDetails(record.get());
        } else {
            System.out.println("No rental records found.");
        }
    }

    private static void controlPower(
            Scanner input,
            Admin admin,
            PCInventory inventory,
            boolean turnOn) {

        PC pc = inventory.getPC(readPCID(input));

        boolean success = turnOn
                ? admin.turnPCOn(pc)
                : admin.turnPCOff(pc);

        if (success) {
            System.out.println(
                    pc.getPcID() + " power is now "
                            + (pc.isPCOpen() ? "ON" : "OFF")
                            + " (simulated)."
            );
        }  else {
            System.out.println(
                    "No change. The PC may already be in that state."
            );
        }
    }

    private static void viewRentalHistory(
            Scanner input, PCInventory inventory) {

        String pcID = readPCID(input);
        List<RentalRecord> records =
                inventory.getRentalHistory(pcID);

        System.out.println("\n--- RENTAL HISTORY: " + pcID + " ---");

        if (records.isEmpty()) {
            System.out.println("No rental history found.");
            return;
        }

        for (int i = 0; i < records.size(); i++) {
            RentalRecord record = records.get(i);

            System.out.println("\nRecord " + (i + 1));
            printRentalDetails(record);
            System.out.println(
                    "Session: "
                            + (record.isActive()
                            ? "ACTIVE"
                            : "ENDED/EXPIRED")
            );
        }
    }

    private static void printRentalDetails(RentalRecord record) {
        System.out.println("PCID: " + record.getPcID());
        System.out.println(
                "Hours purchased: " + record.getHoursPurchased()
        );
        System.out.println(
                "Amount paid: PHP " + record.getAmountPaid().toPlainString()
        );
        System.out.println(
                "Start time: " + FORMAT.format(record.getStartTime())
        );
        System.out.println(
                "Expected end: " + FORMAT.format(record.getEndTime())
        );
        System.out.println(
                "Remaining time: "
                        + formatDuration(record.calculateRemainingTime())
        );
    }

    private static void showHelp() {
        System.out.println("\n--- SYSTEM HELP ---");
        System.out.println("The system manages PC-1, PC-2, and PC-3.");
        System.out.println("The rental rate is PHP 20.00 per hour.");
        System.out.println("Turn a PC on before renting it.");
        System.out.println("A PC in use cannot be turned off.");
        System.out.println("Data is stored in memory only.");
        System.out.println("All data resets when the program closes.");
    }

    private static String readPCID(Scanner input) {
        while (true) {
            System.out.print("Enter PCID (PC-1, PC-2, PC-3): ");

            String id = input.nextLine().trim().toUpperCase();

            if (id.matches("PC-[1-3]")) {
                return id;
            }

            System.out.println("Invalid PCID. Try again.");
        }
    }

    private static int readInt(
            Scanner input, String prompt, int min, int max) {

        while (true) {
            System.out.print(prompt);

            try {
                int value = Integer.parseInt(
                        input.nextLine().trim()
                );

                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ex) {
                // Ask again.
            }

            System.out.println(
                    "Enter a whole number from " + min + " to " + max + "."
            );
        }
    }

    private static double readHours(Scanner input) {
        while (true) {
            System.out.print("Hours purchased: ");

            try {
                double hours = Double.parseDouble(
                        input.nextLine().trim()
                );

                if (Double.isFinite(hours)
                        && hours > 0
                        && hours <= 24) {
                    return hours;
                }
            } catch (NumberFormatException ex) {
                // Ask again.
            }

            System.out.println("Enter more than 0 and no more than 24 hours.");
        }
    }

    private static BigDecimal readMoney(Scanner input) {
        while (true) {
            System.out.print("Amount paid (PHP): ");

            try {
                BigDecimal money = new BigDecimal(
                        input.nextLine().trim()
                );

                if (money.signum() >= 0 && money.scale() <= 2) {
                    return money.setScale(2);
                }
            } catch (NumberFormatException ex) {
                // Ask again.
            }

            System.out.println("Enter a valid amount, such as 40.00.");
        }
    }

    private static LocalDateTime readStartTime(Scanner input) {
        while (true) {
            System.out.print(
                    "Start time (yyyy-MM-dd HH:mm, blank = now): "
            );

            String value = input.nextLine().trim();

            if (value.isEmpty()) {
                return LocalDateTime.now(
                        ZoneId.of("Asia/Manila")
                ).withSecond(0).withNano(0);
            }

            try {
                return LocalDateTime.parse(value, FORMAT);
            } catch (java.time.format.DateTimeParseException ex) {
                System.out.println(
                        "Invalid format. Example: 2026-10-09 21:00"
                );
            }
        }
    }

    private static String formatDuration(Duration duration) {
        long seconds = Math.max(0, duration.getSeconds());

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }

        return minutes + "m";
    }
}