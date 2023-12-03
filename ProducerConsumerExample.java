import java.util.concurrent.LinkedBlockingQueue;
import java.util.Scanner;

// Buffer class 
class Buffer {
    private LinkedBlockingQueue<Integer> queue;

    // Constructor for Buffer
    public Buffer(int size) {
        this.queue = new LinkedBlockingQueue<>(size);
    }

    // Method to add value to the queue
    public void add(int value) throws InterruptedException {
        queue.put(value);
    }

    // Method to remove  
    public Integer remove() throws InterruptedException {
        return queue.poll();
    }
}

// Producer class 
class Producer implements Runnable {
    private Buffer buffer;
    private int totalItems;
    private int sleepTime; // Sleep time for producer

    // Constructor for Producer
    public Producer(Buffer buffer, int totalItems, int sleepTime) {
        this.buffer = buffer;
        this.totalItems = totalItems;
        this.sleepTime = sleepTime;
    }

    // Run method where producer adds items to the buffer
    @Override
    public void run() {
        try {
            for (int i = 0; i < totalItems; i++) {
                buffer.add(i);
                System.out.println("Produced: " + i);
                Thread.sleep(sleepTime); // Producer sleeps
            }
            buffer.add(-1); // Indicate end of production
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Consumer class 
class Consumer implements Runnable {
    private Buffer buffer;
    private int sleepTime; // Sleep time for consumer

    // Constructor for Consumer
    public Consumer(Buffer buffer, int sleepTime) {
        this.buffer = buffer;
        this.sleepTime = sleepTime;
    }

    // Run method where consumer takes items from the buffer
    @Override
    public void run() {
        try {
            while (true) {
                Integer value = buffer.remove();
                if (value == null) {
                    Thread.sleep(sleepTime);
                    continue; // Continue if buffer is empty
                }
                if (value == -1) {
                    buffer.add(-1); // Signal other consumers
                    break;
                }
                System.out.println("Consumed: " + value);
                Thread.sleep(sleepTime); // Consumer sleeps
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Main class to set up and run the Producer-Consumer example
public class ProducerConsumerExample {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of test cases: ");
        int numTestCases = scanner.nextInt();

        for (int t = 0; t < numTestCases; t++) {
            System.out.println("Test Case " + (t + 1));

            System.out.print("Enter number of producers: ");
            int numProducers = scanner.nextInt();

            System.out.print("Enter number of consumers: ");
            int numConsumers = scanner.nextInt();

            System.out.print("Enter producer sleep time (ms): ");
            int producerSleepTime = scanner.nextInt();

            System.out.print("Enter consumer sleep time (ms): ");
            int consumerSleepTime = scanner.nextInt();

            Buffer buffer = new Buffer(100); // Buffer with capacity of 100

            Thread[] producerThreads = new Thread[numProducers];
            Thread[] consumerThreads = new Thread[numConsumers];

            long startTime = System.currentTimeMillis();

            // Creating and starting producer threads
            for (int i = 0; i < numProducers; i++) {
                producerThreads[i] = new Thread(new Producer(buffer, 10, producerSleepTime));
                producerThreads[i].start();
            }

            // Creating and starting consumer threads
            for (int i = 0; i < numConsumers; i++) {
                consumerThreads[i] = new Thread(new Consumer(buffer, consumerSleepTime));
                consumerThreads[i].start();
            }

            // Waiting for all producer threads to finish
            for (int i = 0; i < numProducers; i++) {
                producerThreads[i].join();
            }

            // Waiting for all consumer threads to finish
            for (int i = 0; i < numConsumers; i++) {
                consumerThreads[i].join();
            }

            long endTime = System.currentTimeMillis();
            long overallTurnaroundTime = endTime - startTime;
            System.out.println("Overall Turnaround Time for Test Case " + (t + 1) + ": " + overallTurnaroundTime + "ms\n");
        }

        scanner.close(); // Closing the scanner
    }
}
