# User Guide

## Introduction
This document the covers installation and operation guide for `player-communicator` project.

The goal of this project is to communicate two players until the **10th message** has been received and sent by the 
**_initiator(Player1)_**. Until that time client connections to the server should live.

- `player-communicator-server` module is the server side of the `player-communicator` project and responsible for
  facilitating communication between two players in a client-server architecture. The server acts as a central hub,
  managing connections from multiple players and enabling them to exchange messages with each other.

Key features:
1. Multiple player support
2. Player authentication
3. Message broadcasting
4. Server management
5. Graceful disconnection
6. Multithreading usage for concurrent communication


- `player-communicator-client` module is the client side of the `player-communicator` project and represents
  individual players and enabling them to connect to the communication server.
  Players can interact with each other through the server, exchange messages, and perform specific roles 
  in the communication system.

Key features:
1. Player initialization
2. Server connection
3. Message sending
4. Acknowledgment handling (for the number of messages send and receive)
5. Graceful disconnection


**Note:**

This design of the project is focused on running clients in a separate java process as it is more close to realtime
usage of the chat application. For two clients there will be no need. It can also be changed to the work on the same process as both approaches have their
advantages and disadvantages.
1. Pros & Cons for separate java process for each client
- Better Scalibility
- Resource isolation
- Complexity (requires additional mechanism like IPC)
2. Pros & Cons of Same java process for all clients
- Resource efficiency
- Simplified communication
- Lower latency (in process communication)


## Getting Started
Please follow the next steps to prepare the environment to run the `player-communicator` application.

### 1. Requirements
The `player-communicator` project is a maven project that uses core Java 1.8
It only uses testing framework dependencies such as;

    ARTIFACT | VERSION
    Junit | 4.13.1
    Mockito | 4.9.0


### 2. Demonstration of running project locally

[Click here to watch demo on Youtube](https://youtu.be/p3JRQsG5mcs)

### 3. How to run the project locally

You will need Java 1.8+ and Maven 3.6+ installed on your machine. 
Default port used for the application is 8080. From your favorite terminal program, check for the 
port 8080 whether it is open on your local or not 
```
$ netstat -ano | findstr 8080
```
If not, please change the port number to any available port on the server and client scripts [**`start-server.sh`**](start-server.sh), 
[**`start-client1.sh`**](start-client1.sh), [**`start-client2.sh`**](start-client2.sh) before running it.
Then type from the terminal to execute the script inside the project directory

Open a new terminal to run each script.
```
$ pwd
/home/kaydemir/IdeaProjects/player-communicator
$ bash start-server.sh
```

```
$ pwd
/home/kaydemir/IdeaProjects/player-communicator
$ bash start-client1.sh
```

```
$ pwd
/home/kaydemir/IdeaProjects/player-communicator
$ bash start-client2.sh
```
