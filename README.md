# Description

This is a project which simulates a list of clients being processed at a store, during a period of time. Clients are separated into queues (threads) and then processed according to the order of their arrival.  
Relevant statistics are shown once the process is finished.

## Details

Once the program is running, the threads are updated every second, until a specified time (an input before running the program). Clients will be distributed in the threads based on one of two strategies:
  - Shortest time: the thread with the shortest accumulated service time will be chosen to dispatch the next client to (more details later)
  - Shortest queue: the thread with the least number of clients being processed is chosen to dispatch the next client to.

Each client from the list has an *arrival time* which dictates the second that a client will be put into one of the threads, a *servicing time* which indicates how much 

