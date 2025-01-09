# CPU Scheduling Simulation

This project implements a CPU scheduling simulator with support for the **First-Come-First-Serve (FCFS)** and **Round Robin (RR)** algorithms. Users can input processes with custom or random burst times and select a scheduling algorithm for process execution. The program also provides a Gantt chart and calculates the average waiting time for the processes.

## Features

- **Process Input:** Allows the user to input process data such as creation time, burst time, and priority. The burst times can either be entered manually or generated randomly.
- **CPU Scheduling Algorithms:**
  - **FCFS (First-Come-First-Serve):** A simple scheduling algorithm where processes are executed in the order of their arrival.
  - **Round Robin (RR):** A time-sharing scheduling algorithm that assigns a fixed time quantum for each process and rotates through them until all are completed.
- **Gantt Chart:** A graphical representation of the execution of processes over time.
- **Average Waiting Time Calculation:** Calculates the average waiting time for the processes.

## How to Run

1. Clone the repository to your local machine:

    ```bash
    git clone <repository_url>
    ```

2. Compile the Java code:

    ```bash
    javac cpuscheduling/CPUScheduling.java
    ```

3. Run the program:

    ```bash
    java cpuscheduling.CPUScheduling
    ```

## Program Flow

1. The program prompts the user to enter the number of processes (maximum of 15).
2. The user is asked whether they want to enter custom burst times or use random burst times.
3. The user is shown a list of processes with their creation time, burst time, and priority.
4. The user selects one of the CPU scheduling algorithms:
   - **FCFS**: Displays the Gantt chart and calculates the average waiting time.
   - **Round Robin**: Prompts the user for the time quantum and displays the Gantt chart and average waiting time.
5. The program asks if the user wants to continue or exit.

## Code Structure

### Classes:
- **`Process`**: Represents a process with attributes like name, creation time, burst time, priority, and waiting time.
- **`RoundRobinProcess`**: Extends the `Process` class and adds attributes for the remaining burst time and completion time.
- **`CPUScheduling`**: Main class that handles input, scheduling algorithms, Gantt chart generation, and waiting time calculation.

### Methods:
- **`inputProcesses()`**: Handles input of processes and algorithm selection.
- **`runFCFS()`**: Executes the First-Come-First-Serve scheduling algorithm.
- **`runRoundRobin()`**: Executes the Round Robin scheduling algorithm.
- **`calculateWaitingTimes()`**: Calculates waiting times for all processes.
- **`displayGanttChart()`**: Displays the Gantt chart for process execution.
- **`calculateAverageWaitingTime()`**: Calculates the average waiting time for all processes.

