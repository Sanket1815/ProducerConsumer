Overview:-

This Java program demonstrates the Producer-Consumer problem using a LinkedBlockingQueue from the java.util.concurrent package. It consists of producers that generate and add items to a shared buffer and consumers that remove and process these items from the buffer. The program allows multiple producers and consumers to operate concurrently.

Requirements:-
Java Development Kit (JDK) 8 or higher.

Structure:-
Buffer: A thread-safe class using LinkedBlockingQueue to store integers. It provides add and remove methods to interact with the queue.
class Buffer {
private LinkedBlockingQueue<Integer> queue;

    //constructor
    public Buffer(int size) {
        this.queue = new LinkedBlockingQueue<>(size);
    }

    public void add(int value) throws InterruptedException {
        queue.add(value);
    }

    public Integer remove() throws InterruptedException {
        return queue.remove();
    }

}

Producer: Implements Runnable. Producers generate items (integers) and add them to the buffer.
class Producer implements Runnable {
private Buffer buffer;
private int totalItems;

    //constructor for producer class
    public Producer(Buffer buffer, int totalItems) {
        this.buffer = buffer;
        this.totalItems = totalItems;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < totalItems; i++) {
                buffer.add(i);
                System.out.println("Produced: " + i);
            }
            buffer.add(-1); // Indicate end of production
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

Consumer: Implements Runnable. Consumers remove items from the buffer and process them.
class Consumer implements Runnable {
private Buffer buffer;

    //Constructor for Consumer
    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer value = buffer.remove();
                if (value == -1) {
                    buffer.add(-1); // Signal other consumers
                    break;
                }
                System.out.println("Consumed: " + value);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

ProducerConsumerExample: Contains the main method to run the program, handling user input for setting up producers and consumers.

Compilation and Execution:-
Compile:-
javac ProducerConsumerExample.java

Execute:-
java ProducerConsumerExample
