package com.mycompany.bookbridge;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Console application matching the BookBridgeApp operations in the UML. */
public class BookBridgeApp {
    private static final int REQUEST_COUNT = 5;
    private static final int MIN_LOAN_DAYS = 1;
    private static final int MAX_LOAN_DAYS = 30;

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("BOOKBRIDGE - Library Loan and Reservation System");
            demonstratePolymorphism();

            LibraryService service = new LibraryService();
            RequestResult[] results = new RequestResult[REQUEST_COUNT];

            System.out.printf(Locale.ROOT, "%nEnter %d requests with valid input.%n",
                REQUEST_COUNT);

            for (int i = 0; i < REQUEST_COUNT; i++) {
                System.out.printf(Locale.ROOT, "%nEnter request %d of %d:%n",
                    i + 1, REQUEST_COUNT);

                String memberId = readNonBlank(input, "Member ID: ");
                String memberName = readNonBlank(input, "Member name: ");
                String membershipType = readMembershipType(input);
                Member member = createMember(membershipType, memberId, memberName);

                String isbn = readNonBlank(input, "ISBN: ");
                String title = readNonBlank(input, "Book title: ");
                // Keep input validation at 1-30; the service decides eligibility.
                int requestedDays = readInteger(input, "Requested loan days (1-30): ",
                    MIN_LOAN_DAYS, MAX_LOAN_DAYS);
                boolean available = readYesNo(input, "Book available? (YES/NO): ");
                int lateDays = readInteger(input, "Late days (0 if none): ",
                    0, Integer.MAX_VALUE);

                LibraryBook book = new LibraryBook(isbn, title, available);
                results[i] = service.processRequest(member, book, requestedDays, lateDays);
                printResult(results[i]);
            }

            int approved = 0;
            int reserved = 0;
            int denied = 0;
            int greatestDays = 0;
            String greatestLabel = "";
            long totalLateCharge = 0;

            for (RequestResult result : results) {
                switch (result.getOutcome()) {
                    case APPROVED -> approved++;
                    case RESERVED -> reserved++;
                    case DENIED -> denied++;
                }
                totalLateCharge += result.getEstimatedFine();
                // Strict comparison means the first request wins a tie.
                if (result.getRequestedDays() > greatestDays) {
                    greatestDays = result.getRequestedDays();
                    greatestLabel = result.getMember().getName()
                        + " (" + result.getMember().getMemberId() + ")";
                }
            }

            printOperationalSummary(approved, reserved, denied, greatestDays,
                greatestLabel, totalLateCharge);

            
            System.out.println("\nThank you for using BookBridge! Program finished.");
        } catch (NoSuchElementException exception) {
            System.out.println(
                "\nInput ended before all five requests were completed. BookBridge closed.");
        }
    }

    private static Member createMember(String type, String memberId, String memberName) {
        return switch (type) {
            case "STUDENT" -> new StudentMember(memberId, memberName);
            case "FACULTY" -> new FacultyMember(memberId, memberName);
            case "GUEST" -> new GuestMember(memberId, memberName);
            default -> throw new IllegalArgumentException("Unsupported membership type.");
        };
    }

    private static void demonstratePolymorphism() {
        // One Member array holds three different subclass objects.
        Member[] examples = {
            new StudentMember("DEMO-S", "Student example"),
            new FacultyMember("DEMO-F", "Faculty example"),
            new GuestMember("DEMO-G", "Guest example")
        };
        System.out.println("Membership policies:");
        for (Member member : examples) {
            printPolicyLine(member);
        }
    }

    private static void printPolicyLine(Member member) {
        // Java selects the overriding methods for the actual subclass.
        System.out.printf(Locale.ROOT,
            "  %-7s : maximum %2d days | late fee PHP %d.00/day%n",
            member.getMembershipType(), member.getMaximumLoanDays(), member.getDailyFine());
    }

    private static void printResult(RequestResult result) {
        Member member = result.getMember();
        LibraryBook book = result.getBook();
        System.out.println("\n====================================================");
        System.out.println("BOOKBRIDGE REQUEST SUMMARY");
        System.out.println("Member ID          : " + member.getMemberId());
        System.out.println("Member name        : " + member.getName());
        System.out.println("Membership type    : " + member.getMembershipType());
        System.out.println("ISBN               : " + book.getIsbn());
        System.out.println("Book title         : " + book.getTitle());
        System.out.println("Requested loan days: " + result.getRequestedDays());
        System.out.println("Maximum loan days  : " + member.getMaximumLoanDays());
        System.out.println("Availability       : "
            + (result.isAvailableAtRequest() ? "AVAILABLE" : "UNAVAILABLE"));
        System.out.println("Late days          : " + result.getLateDays());
        System.out.printf(Locale.ROOT, "Daily late fee     : PHP %d.00%n",
            member.getDailyFine());
        System.out.printf(Locale.ROOT, "Total late fee     : PHP %d.00%n",
            result.getEstimatedFine());
        System.out.println("Request status     : " + result.getOutcome());
        System.out.println("Details            : " + result.getDetail());
        System.out.println("====================================================");
    }

    private static void printOperationalSummary(int approved, int reserved, int denied,
                                                int greatestDays, String greatestLabel,
                                                long totalLateCharge) {
        System.out.println("\n====================================================");
        System.out.println("BOOKBRIDGE OPERATIONAL SUMMARY");
        System.out.printf(Locale.ROOT, "Approved requests        : %d%n", approved);
        System.out.printf(Locale.ROOT, "Reserved requests        : %d%n", reserved);
        System.out.printf(Locale.ROOT, "Denied requests          : %d%n", denied);
        System.out.printf(Locale.ROOT, "Greatest requested days  : %d days - %s%n",
            greatestDays, greatestLabel);
        System.out.printf(Locale.ROOT, "Total estimated late fee : PHP %d.00%n",
            totalLateCharge);
        System.out.println("====================================================");
    }

    private static String readMembershipType(Scanner input) {
        while (true) {
            String type = readNonBlank(input, "Membership type (STUDENT/FACULTY/GUEST): ");
            switch (type) {
                case "STUDENT", "FACULTY", "GUEST" -> { return type; }
                default -> System.out.println(
                    "Invalid type. Enter STUDENT, FACULTY, or GUEST in uppercase.");
            }
        }
    }

    private static String readNonBlank(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = input.nextLine().strip();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This field cannot be blank. Please try again.");
        }
    }

    private static int readInteger(Scanner input, String prompt, int minimum, int maximum) {
        while (true) {
            String value = readNonBlank(input, prompt);
            try {
                int number = Integer.parseInt(value);
                if (number >= minimum && number <= maximum) {
                    return number;
                }
                System.out.printf(Locale.ROOT, "Enter a whole number from %d to %d.%n",
                    minimum, maximum);
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid whole number within the integer range.");
            }
        }
    }

    private static boolean readYesNo(Scanner input, String prompt) {
        while (true) {
            String value = readNonBlank(input, prompt).toUpperCase(Locale.ROOT);
            switch (value) {
                case "YES" -> { return true; }
                case "NO" -> { return false; }
                default -> System.out.println("Enter YES or NO.");
            }
        }
    }
}