package com.reetesh.distributed;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

public class DistributedLedgerSystem {

    // --- SIMULATED DATABASE ---
    static Map<String, Account> accountsDb = new ConcurrentHashMap<>();
    static Set<String> processedRequestIds = Collections.synchronizedSet(new HashSet<>());
    static List<String> auditLog = Collections.synchronizedList(new ArrayList<>());

    // --- MODELS ---
    static class Account {
        String id;
        BigDecimal balance;
        long version; // For Optimistic Locking

        Account(String id, BigDecimal balance, long version) {
            this.id = id;
            this.balance = balance;
            this.version = version;
        }
    }

    // --- CORE SERVICE ---
    static class LedgerService {

        public synchronized String processTransfer(String reqId, String fromId, String toId, BigDecimal amount) {
            // 1. IDEMPOTENCY CHECK
            if (processedRequestIds.contains(reqId)) {
                return "[REJECTED] Duplicate Request ID: " + reqId;
            }

            // 2. FETCH ACCOUNTS
            Account from = accountsDb.get(fromId);
            Account to = accountsDb.get(toId);

            // 3. CONCURRENCY CHECK (Optimistic Locking Simulation)
            // In a real DB, the 'version' check happens at the UPDATE SQL level
            long currentVersion = from.version;

            // 4. BUSINESS LOGIC
            if (from.balance.compareTo(amount) < 0) {
                return "[FAILED] Insufficient funds in " + fromId;
            }

            // Perform the "Double Entry"
            from.balance = from.balance.subtract(amount);
            to.balance = to.balance.add(amount);

            // Increment Version
            from.version++;
            to.version++;

            // 5. COMMIT (Atomic update to DB and Idempotency set)
            processedRequestIds.add(reqId);
            String logEntry = String.format("TXN %s: %s -> %s | Amount: $%s", reqId, fromId, toId, amount);
            auditLog.add(logEntry);

            return "[SUCCESS] " + logEntry;
        }
    }

    // --- MAIN METHOD ---
    public static void main(String[] args) throws InterruptedException {
        LedgerService service = new LedgerService();

        // Initialize Sample Data
        accountsDb.put("ACC_001", new Account("ACC_001", new BigDecimal("1000.00"), 1));
        accountsDb.put("ACC_002", new Account("ACC_002", new BigDecimal("500.00"), 1));

        System.out.println("--- INITIAL BALANCES ---");
        printBalances();

        // Scenario 1: Normal Transfer
        System.out.println("\nExecuting Scenario 1: Normal Transfer...");
        System.out.println(service.processTransfer("REQ_101", "ACC_001", "ACC_002", new BigDecimal("100.00")));

        // Scenario 2: Idempotency Test (Retry with same Request ID)
        System.out.println("\nExecuting Scenario 2: Retrying same Request ID...");
        System.out.println(service.processTransfer("REQ_101", "ACC_001", "ACC_002", new BigDecimal("100.00")));

        // Scenario 3: Insufficient Funds
        System.out.println("\nExecuting Scenario 3: Over-drafting...");
        System.out.println(service.processTransfer("REQ_102", "ACC_001", "ACC_002", new BigDecimal("5000.00")));

        // Scenario 4: Concurrent High-Speed Transfers
        System.out.println("\nExecuting Scenario 4: Simulating Concurrent Requests...");
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            final int idSuffix = i;
            executor.submit(() -> {
                System.out.println(service.processTransfer("CONC_REQ_" + idSuffix, "ACC_001", "ACC_002", new BigDecimal("10.00")));
            });
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);

        System.out.println("\n--- FINAL STATE ---");
        printBalances();
        System.out.println("\n--- AUDIT LOG (The Truth) ---");
        auditLog.forEach(System.out::println);
    }

    private static void printBalances() {
        accountsDb.values().forEach(a ->
                System.out.println("Account: " + a.id + " | Balance: $" + a.balance + " | Ver: " + a.version));
    }
}
