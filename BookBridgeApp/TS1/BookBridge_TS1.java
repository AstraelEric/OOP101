/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.bookbridge;

/**
 *
 * @author Enrico Garcia
 */
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * BookBridge: a console prototype for library loans and reservation requests.
 * Requires Java 17 or later. Compile: javac BookBridgeApp.java
 * Run: java BookBridgeApp
 *
 * Rules:
 * - Only uppercase STUDENT, FACULTY, and GUEST membership types are accepted.
 * - Requested loan days must be an integer from 1 to 30; late days cannot be negative.
 * - A request within 1-30 but above the member's limit is DENIED, not rejected at input.
 * - APPROVED when available and within the limit; RESERVED when unavailable;
 *   DENIED when the request exceeds the membership limit.
 * - Late days calculate an existing late fee; they do not block a new request.
 * - Collect exactly five valid requests, print each summary after its request,
 *   then print an operational summary (counts, greatest request, total late charge).
 *
 * This prototype holds requests in memory for the current run. It does not
 * update inventory or maintain a persistent reservation queue.
 */
public class BookBridge_TS1 {
    // ARRAY: the only membership types the system accepts.
    private static final String[] MEMBERSHIP_TYPES = {
        "STUDENT", "FACULTY", "GUEST"
    };
    private static final int REQUEST_COUNT = 5;
    private static final int MIN_LOAN_DAYS = 1;
    private static final int MAX_LOAN_DAYS = 30;

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            printPolicyBanner();

            // COORDINATED ARRAYS: the same index identifies one complete request.
            String[] memberIds = new String[REQUEST_COUNT];
            String[] memberNames = new String[REQUEST_COUNT];
            String[] membershipTypes = new String[REQUEST_COUNT];
            String[] isbns = new String[REQUEST_COUNT];
            String[] bookTitles = new String[REQUEST_COUNT];
            int[] requestedLoanDays = new int[REQUEST_COUNT];
            boolean[] availability = new boolean[REQUEST_COUNT];
            int[] lateDays = new int[REQUEST_COUNT];
            // Outcome of each request, stored so the operational summary can count them.
            String[] decisions = new String[REQUEST_COUNT];

            System.out.printf(Locale.ROOT, "%nEnter %d valid requests.%n", REQUEST_COUNT);

            // INPUT LOOP: advance only after every field of this request is valid.
            for (int i = 0; i < REQUEST_COUNT; i++) {
                System.out.printf(Locale.ROOT,
                    "%nEnter request %d of %d:%n", i + 1, REQUEST_COUNT);

                memberIds[i] = readNonBlank(input, "Member ID: ");
                memberNames[i] = readNonBlank(input, "Member name: ");
                membershipTypes[i] = readMembershipType(input);
                // Identifiers stay as text to preserve leading zeros and hyphens.
                isbns[i] = readNonBlank(input, "ISBN: ");
                bookTitles[i] = readNonBlank(input, "Book title: ");

                // VALIDATION RANGE IS 1-30. Values above the membership limit are
                // accepted here and classified as DENIED later, not re-prompted.
                requestedLoanDays[i] = readInteger(input,
                    "Requested loan days (1-30): ", MIN_LOAN_DAYS, MAX_LOAN_DAYS);

                availability[i] = readYesNo(input, "Book available? (YES/NO): ");
                lateDays[i] = readInteger(input,
                    "Late days (0 if none): ", 0, Integer.MAX_VALUE);

                // Print this request's result immediately, and keep its decision.
                decisions[i] = printSummary(memberIds[i], memberNames[i],
                    membershipTypes[i], isbns[i], bookTitles[i],
                    requestedLoanDays[i], availability[i], lateDays[i]);
            }

            // AGGREGATE: counts, greatest request, total estimated late charge.
            long totalLateCharge = calculateTotalLateCharge(lateDays, membershipTypes);
            printOperationalSummary(memberIds, memberNames, decisions,
                requestedLoanDays, totalLateCharge);

            System.out.println("\nThank you for using BookBridge! Program finished.");
        } catch (NoSuchElementException exception) {
            // Exit cleanly if console input ends before all five requests are complete.
            System.out.println("\nInput ended before all requests were completed. BookBridge closed.");
        }
    }

    /** Print the fixed membership policy table at startup. */
    private static void printPolicyBanner() {
        System.out.println("BOOKBRIDGE - Library Loan and Reservation System");
        System.out.println("Membership policies:");
        // ENHANCED FOR LOOP, ARRAY ACCESS, and METHOD CALLS.
        for (String type : MEMBERSHIP_TYPES) {
            System.out.printf(Locale.ROOT,
                "  %-7s : maximum %2d days | late fee PHP %d.00/day%n",
                type, getMaximumDays(type), getDailyFine(type));
        }
    }

    /** Required method: return true only for a supported membership type. */
    public static boolean validateMembershipType(String membershipType) {
        for (String allowedType : MEMBERSHIP_TYPES) {
            if (allowedType.equals(membershipType)) {
                return true;
            }
        }
        return false;
    }

    /** Required method: return the membership's maximum loan days. */
    public static int getMaximumDays(String membershipType) {
        // ENHANCED SWITCH EXPRESSION; default guards unsupported input.
        return switch (membershipType) {
            case "STUDENT" -> 14;
            case "FACULTY" -> 30;
            case "GUEST" -> 7;
            default -> throw new IllegalArgumentException(
                "Unsupported membership type: " + membershipType);
        };
    }

    /** Required method: return the late fee in whole PHP per day. */
    public static int getDailyFine(String membershipType) {
        return switch (membershipType) {
            case "STUDENT" -> 5;
            case "FACULTY" -> 2;
            case "GUEST" -> 10;
            default -> throw new IllegalArgumentException(
                "Unsupported membership type: " + membershipType);
        };
    }

    /**
     * Required method: classify a request using the documented outcome names.
     * The membership limit is checked before availability, so a request beyond
     * the member's limit is DENIED even if the book is also unavailable.
     */
    public static String classifyRequest(String membershipType,
                                         int requestedLoanDays,
                                         boolean available) {
        int maximumDays = getMaximumDays(membershipType);

        // Defensive only: readInteger already enforces 1-30 at input time.
        if (requestedLoanDays < MIN_LOAN_DAYS || requestedLoanDays > MAX_LOAN_DAYS) {
            return "DENIED - requested loan days must be 1-30";
        }

        if (requestedLoanDays > maximumDays) {
            return "DENIED - exceeds the " + maximumDays + "-day membership limit";
        }

        // TERNARY-FREE explicit branch for readability in the decision rule.
        if (available) {
            return "APPROVED";
        }
        return "RESERVED";
    }

    /**
     * Required method: print one request's formatted result and return its
     * decision so main can store it for the operational summary.
     */
    public static String printSummary(String memberId, String memberName,
                                      String membershipType, String isbn,
                                      String bookTitle, int requestedLoanDays,
                                      boolean available, int lateDays) {
        int maximumDays = getMaximumDays(membershipType);
        int dailyFine = getDailyFine(membershipType);
        // MULTIPLICATION: long prevents integer overflow for large late-day inputs.
        long totalFine = (long) lateDays * dailyFine;
        String decision = classifyRequest(membershipType, requestedLoanDays, available);

        System.out.println("====================================================");
        System.out.println("BOOKBRIDGE REQUEST SUMMARY");
        System.out.println("Member ID          : " + memberId);
        System.out.println("Member name        : " + memberName);
        System.out.println("Membership type    : " + membershipType);
        System.out.println("ISBN               : " + isbn);
        System.out.println("Book title         : " + bookTitle);
        System.out.println("Requested loan days: " + requestedLoanDays);
        System.out.println("Maximum loan days  : " + maximumDays);
        // TERNARY OPERATOR: choose the availability label.
        System.out.println("Availability       : "
            + (available ? "AVAILABLE" : "UNAVAILABLE"));
        System.out.println("Late days          : " + lateDays);
        System.out.printf(Locale.ROOT, "Daily late fee     : PHP %d.00%n", dailyFine);
        System.out.printf(Locale.ROOT, "Total late fee     : PHP %d.00%n", totalFine);
        System.out.println("Request status     : " + decision);
        System.out.println("====================================================");

        return decision;
    }

    /**
     * Sum the late charge across all five requests.
     * The specification does not restrict this to approved requests, so all
     * recorded late days are included in the estimated total.
     */
    public static long calculateTotalLateCharge(int[] lateDays,
                                                String[] membershipTypes) {
        long total = 0;
        for (int i = 0; i < lateDays.length; i++) {
            total += (long) lateDays[i] * getDailyFine(membershipTypes[i]);
        }
        return total;
    }

    /** Return the index of the request with the greatest requested days (first wins ties). */
    public static int findGreatestRequestIndex(int[] requestedLoanDays) {
        int greatestIndex = 0;
        for (int i = 1; i < requestedLoanDays.length; i++) {
            // Strict greater-than keeps the earliest request when days tie.
            if (requestedLoanDays[i] > requestedLoanDays[greatestIndex]) {
                greatestIndex = i;
            }
        }
        return greatestIndex;
    }

    /** Count how many decisions begin with the given outcome token. */
    public static int countOutcome(String[] decisions, String outcomeToken) {
        int count = 0;
        for (String decision : decisions) {
            if (decision != null && decision.startsWith(outcomeToken)) {
                count++;
            }
        }
        return count;
    }

    /** Print counts, the greatest request, and the total estimated late charge. */
    public static void printOperationalSummary(String[] memberIds,
                                               String[] memberNames,
                                               String[] decisions,
                                               int[] requestedLoanDays,
                                               long totalLateCharge) {
        int approved = countOutcome(decisions, "APPROVED");
        int reserved = countOutcome(decisions, "RESERVED");
        int denied = countOutcome(decisions, "DENIED");
        int greatestIndex = findGreatestRequestIndex(requestedLoanDays);

        System.out.println();
        System.out.println("====================================================");
        System.out.println("BOOKBRIDGE OPERATIONAL SUMMARY");
        System.out.printf(Locale.ROOT, "Approved requests        : %d%n", approved);
        System.out.printf(Locale.ROOT, "Reserved requests        : %d%n", reserved);
        System.out.printf(Locale.ROOT, "Denied requests          : %d%n", denied);
        System.out.printf(Locale.ROOT,
            "Greatest requested days  : %d days - %s (%s)%n",
            requestedLoanDays[greatestIndex],
            memberNames[greatestIndex], memberIds[greatestIndex]);
        System.out.printf(Locale.ROOT,
            "Total estimated late fee : PHP %d.00%n", totalLateCharge);
        System.out.println("====================================================");
    }

    /** Retry until a membership type is one of the three permitted values. */
    private static String readMembershipType(Scanner input) {
        while (true) {
            String membershipType = readNonBlank(input,
                "Membership type (STUDENT/FACULTY/GUEST): ");
            if (validateMembershipType(membershipType)) {
                return membershipType;
            }
            System.out.println(
                "Invalid type. Enter STUDENT, FACULTY, or GUEST in uppercase.");
        }
    }

    /** Retry until a field contains text after surrounding whitespace is removed. */
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

    /** Retry blank, non-integer, overflowing, and out-of-range numeric input. */
    private static int readInteger(Scanner input, String prompt,
                                   int minimum, int maximum) {
        while (true) {
            String value = readNonBlank(input, prompt);
            try {
                int number = Integer.parseInt(value);
                // COMBINED CONDITION: both boundaries must be satisfied.
                if (number >= minimum && number <= maximum) {
                    return number;
                }
                System.out.printf(Locale.ROOT,
                    "Enter a whole number from %d to %d.%n", minimum, maximum);
            } catch (NumberFormatException exception) {
                System.out.println(
                    "Enter a valid whole number within the integer range.");
            }
        }
    }

    /** Explicit YES/NO validation prevents unrecognized text becoming false. */
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