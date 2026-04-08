public class EvenOddThreadExample {
    private int counter = 1;
    private final int limit = 10;

    // Shared method for printing odd numbers
    public synchronized void printOdd() {
        while (counter <= limit) {
            // Wait if the current number is even
            while (counter % 2 == 0) {
                try { wait(); } catch (InterruptedException e) { }
            }
            if (counter <= limit) {
                System.out.println(Thread.currentThread().getName() + ": " + counter++);
            }
            notify(); // Wake up the even thread
        }
    }

    // Shared method for printing even numbers
    public synchronized void printEven() {
        while (counter <= limit) {
            // Wait if the current number is odd
            while (counter % 2 != 0) {
                try { wait(); } catch (InterruptedException e) { }
            }
            if (counter <= limit) {
                System.out.println(Thread.currentThread().getName() + ": " + counter++);
            }
            notify(); // Wake up the odd thread
        }
    }

    public static void main(String[] args) {
        EvenOddThreadExample printer = new EvenOddThreadExample();

        Thread t1 = new Thread(printer::printOdd, "OddThread");
        Thread t2 = new Thread(printer::printEven, "EvenThread");

        t1.start();
        t2.start();
    }
}
