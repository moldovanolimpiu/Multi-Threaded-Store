# Description

This is a project which simulates a list of clients being processed at a store, during a period of time, also known as multi-threading. Clients are separated into queues (threads) and then processed according to the order of their arrival.  
Relevant statistics are shown once the process is finished.

## Details

Once the program is running, the threads are updated every second, until a specified time (an input before running the program). Clients will be distributed in the threads based on one of two strategies:
  - Shortest Time: the thread with the shortest accumulated service time will be chosen to dispatch the next client to (more details later)
  - Shortest Queue: the thread with the least number of clients being processed is chosen to dispatch the next client to.

Each client from the list has an *arrival time* which dictates the second that a client will be put into one of the threads, a *servicing time* which indicates how many seconds that client will spend being serviced, and a unique ID. The clients are ordered based on their arrival time, and once the program reaches that certain time threshold, all the clients with that arrival time are distributed in the queues, based on the two aforementioned strategies. The clients start being serviced the moment they reach the head of the queue. Each second, their service time decreased, until it reaches zero. Once that happens, the client is removed from the queue, and the next client will be processed. Each queue's accumulated service time is comprised of all the clients' service times. The thread's service time serves as a criterion for the *Shortest Time* strategy.  
At the end, three statistics will be displayed:
  - Peak Hour: the moment when there were the most amount of clients across all threads.
  - Average Waiting Time
  - Average Service Time

 ## User guide

 Once the app is started, a menu will pop up which will give the user control over the simulation. The inputs are as follows:
   - Number of clients
   - Number of threads (queues in the store)
   - Time limit
   - Max/Min time limit
   - Max/Min processing time
   - Strategy choice: Shortest Time or Shortest Queue

Once the inputs are all set, the user can press the *Open Store* button to begin

