Tree
=

The goal of this project is to demonstrate how to use [ReentrantReadWriteLock](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReentrantReadWriteLock.html) and mutual excluse lock to accecss shared
resources. IT IS TEST REPOSITORY

You will need Java 11 and maven 3 to run the code.

Question:
-
In a concurrent environment, one job is building (expanding) a tree data structure, where all other jobs are dumping periodically the current number of nodes. 
Use any language or technology to solve the task.


Solution
=
**Hypothesis**: 
in order to achieve the best performance we need to know following answers.

    -How important is to have a accurate size of tree
    -How many read/write operation per second are performed (period time)
    -How big is the size of tree
    
However, I tried to implement a solution to be applied to all scenarios. to achieve this, I had to implement 3 different strategies.

***Read Uncommitted strategy***: This is the strategy to apply eventually consistency model, this is a best solution for a tree with
a lot of loads where getting accurate size of the tree is not a requirement.


***Synchronized strategy***: This is a strong consistency model when every read and write operation should get a lock to access shared resources.
In this strategy you will always get the correct data however it may be a bottle neck when frequency of writing data is less than
accessing(reading) data.

***Reentrant lock strategy***: When frequency of reading data is higher than writing data using `Synchronized strategy` is not optimal. This strategy
is a better performed solution than `Synchronized strategy`. The idea is to have separate locks, one for read-only operations and one for writing.
The read lock may be held simultaneously by multiple reader threads, so long as there are no writers. The write lock is exclusive.
A thread successfully acquiring the read lock will see all updates made upon previous release of the write lock.
A read-write lock allows for a greater level of concurrency in accessing shared data than that permitted by a mutual exclusion lock. 
It exploits the fact that while only a single thread at a time (a writer thread) can modify the shared data, in many cases any number of threads can 
concurrently read the data (hence reader threads). In theory, the increase in concurrency permitted by the use of a read-write lock will lead to 
performance improvements over the use of a mutual exclusion lock. In practice this increase in concurrency will only be fully realized on a multi-processor, 
and then only if the access patterns for the shared data are suitable.
[ReentrantLock.java](https://docs.oracle.com/javase/8/docs/api/?java/util/concurrent/locks/ReentrantLock.html "Java Doc")


Comparing actual execution statistics
-
To demonstrate different behaviours of these strategies. I have created a class named
`TreeSimulator.java` This class simulates the scenario of the question. here are the result
of running 3 different strategies where 1 thread is writing data and 5 other thread accessing
the shared data. 


```
****************************************************************************************
* Type: READ_UNCOMMITTED
* Number of READ thread: 5
* Number of WRITE thread: 1
* Number of READ operation: 5000
* Number of WRITE operation: 2000
* Number of INCORRECT read operation: 372
* Time spend on WRITE operation: 2667
* Time spend on READ operation: [1247, 1245, 1246, 1245, 1251]
****************************************************************************************
```

```
****************************************************************************************
* Type: REENTRANT_LOCK
* Number of READ thread: 5
* Number of WRITE thread: 1
* Number of READ operation: 5000
* Number of WRITE operation: 2000
* Number of INCORRECT read operation: 0
* Time spend on WRITE operation: 2829
* Time spend on READ operation: [1388, 1389, 1389, 1390, 1389]
****************************************************************************************
```

```
****************************************************************************************
* Type: SYNCHRONIZED
* Number of READ thread: 5
* Number of WRITE thread: 1
* Number of READ operation: 5000
* Number of WRITE operation: 2000
* Number of INCORRECT read operation: 0
* Time spend on WRITE operation: 2843
* Time spend on READ operation: [1408, 1408, 1408, 1407, 1407]
****************************************************************************************
```

Conclusion
-
As you can see, without being fully `ACID` (READ_UNCOMMITTED strategy) we can gain a lot better
performance; however, with mutual exclusive lock 'SYNCHRONIZED strategy' we can be fully 'ACID' but it
is not an optimal solution when multiple reader are accessing shared resources.

at the middle REENTRANT_LOCK strategy seems be a better solution for this question. it supports 'ACID'
and don't lock reader threads unless there is a writer thread manipulating data at the same time.


How to run
-
to simulate the same in your machine you can follow this steps.

    git clone url
    mvn clean install
    

then run `adigozalpour.morteza.tree.example.Main`
