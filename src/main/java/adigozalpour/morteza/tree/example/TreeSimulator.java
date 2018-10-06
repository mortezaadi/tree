package adigozalpour.morteza.tree.example;

import adigozalpour.morteza.tree.Node;
import adigozalpour.morteza.tree.Tree;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Analyzer to simulate the tree expansion thread and readers threads and accumulate the execution statistics.
 * it is simulating with 1 write tread and 5 read threads.
 */
public class TreeSimulator {

    private static final int NUMBER_OF_READ_THREAD = 5;
    private static final int NUMBER_OF_READ_OPERATION_EACH_THREAD = 1000;
    private static final int NUMBER_OF_WRITE_OPERATION_EACH_THREAD = 2000;
    private static final int SECONDS_WAIT_BEFORE_TERMINATION = 5;
    private AtomicInteger countIncorrectRead = new AtomicInteger( 0 );
    private volatile long writeTime = 0;
    private volatile long[] readTimes = new long[5];
    private final Tree<Integer> tree;


    public TreeSimulator( Tree<Integer> tree ) {
        this.tree = tree;
        IntStream.range( 1, 10 ).forEach( ( i ) -> tree.insert( Node.of( i - 1 ), Node.of( i ) ) );
    }

    public void simulate() {
        new Thread( () -> {
            long start = System.currentTimeMillis();
            IntStream.range( 0, NUMBER_OF_WRITE_OPERATION_EACH_THREAD ).forEach( ( i ) -> {
                try {
                    Thread.sleep( 1 );
                    tree.insert( Node.of( 0 ), Node.of( 0 ) );
                }
                catch ( Exception e ) {
                }
            } );
            writeTime = System.currentTimeMillis() - start;
        } ).start();

        var service = Executors.newFixedThreadPool( 10 );
        IntStream.range( 0, NUMBER_OF_READ_THREAD ).forEach( (j) -> service.submit(
            new ReadExecutor(j,tree,countIncorrectRead,readTimes)
        ));
        try {
            if (service.awaitTermination( SECONDS_WAIT_BEFORE_TERMINATION, TimeUnit.SECONDS)) {
            } else {
                service.shutdownNow();
            }
        }
        catch ( InterruptedException e ) {
            e.printStackTrace(); //deliberate suppression
        }
        printStatictics();
    }

    void printStatictics() {
        System.out.println("****************************************************************************************");
        System.out.println("* Type: " + tree.getStrategy());
        System.out.println("* Number of READ thread: " + NUMBER_OF_READ_THREAD);
        System.out.println("* Number of WRITE thread: " + 1);
        System.out.println("* Number of READ operation: " + NUMBER_OF_READ_THREAD * NUMBER_OF_READ_OPERATION_EACH_THREAD);
        System.out.println("* Number of WRITE operation: " + NUMBER_OF_WRITE_OPERATION_EACH_THREAD);
        System.out.println("* Number of INCORRECT read operation: " + countIncorrectRead);
        System.out.println("* Time spend on WRITE operation: " + writeTime);
        System.out.println("* Time spend on READ operation: " + Arrays.toString(readTimes));
        System.out.println("****************************************************************************************");
    }


    private static class ReadExecutor implements Runnable {

        private final int name;
        private final AtomicInteger countIncorrectRead;
        private final Tree<Integer> tree;
        private final long[] readTimes;

        private ReadExecutor( int name, Tree<Integer> tree, AtomicInteger countIncorrectRead, long[] readTimes) {
            this.name = name;
            this.countIncorrectRead = countIncorrectRead;
            this.tree = tree;
            this.readTimes = readTimes;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            IntStream.range( 1, NUMBER_OF_READ_OPERATION_EACH_THREAD ).forEach( ( i ) -> {
                try {
                    Thread.sleep( 1 );
                    int size = tree.getSize();
                    if(size ==11) {
                        countIncorrectRead.incrementAndGet();
                    }
                }catch ( Exception e ) {
                    //Suppressed deliberately
                    e.printStackTrace();
                }
            });
            readTimes[name] = System.currentTimeMillis() - start;
        }
    }
}
