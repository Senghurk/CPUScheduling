package cpuscheduling;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CPUScheduling {
    static class Process {
        String name;
        int creationTime;
        int burstTime;
        int priority;
        int waitingTime;
        
        Process(String name, int creationTime, int burstTime, int priority) {
            this.name = name;
            this.creationTime = creationTime;
            this.burstTime = burstTime;
            this.priority = priority;
            this.waitingTime = 0;
        }
    }
    
    static class RoundRobinProcess extends Process {
        int remainingBurstTime;
        int completionTime;
        
        RoundRobinProcess(Process p) {
            super(p.name, p.creationTime, p.burstTime, p.priority);
            this.remainingBurstTime = p.burstTime;
            this.completionTime = 0;
        }
    }
    
    private final ArrayList<Process> processes;
    private final Scanner scanner;
    private final Random random;
    
    public CPUScheduling() {
        processes = new ArrayList<>();
        scanner = new Scanner(System.in);
        random = new Random();
    }
    
    private int getValidIntegerInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Invalid input: Input cannot be empty.");
                continue;
            }
            
            try {
                float floatValue = Float.parseFloat(input);
                if (floatValue % 1 != 0) {
                    System.out.println("Invalid input: Please enter a whole number.");
                    continue;
                }
                
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Invalid input: Number must be between " + min + " and " + max + ".");
                    continue;
                }
                
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: Please enter a valid number.");
            }
        }
    }
    
    public void inputProcesses() {
        System.out.println("\t\tCPU SCHEDULING");
        System.out.println("\t\t==============");
        
        int n = getValidIntegerInput("Input the number of process (max 15): ", 2, 15);
        
        System.out.print("Enter 'R' for random burst times or 'C' for custom burst times: ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equalsIgnoreCase("C")) {
            // Custom burst time input
            for (int i = 0; i < n; i++) {
                int creationTime = i;
                int priority = random.nextInt(5) + 1;  // Random priority (1-5)
                System.out.print("Enter burst time for Process P" + (i+1) + ": ");
                int burstTime = getValidIntegerInput("", 1, 100);
                processes.add(new Process("P" + (i+1), creationTime, burstTime, priority));
            }
        } else {
            // Random generation for burst times
            for (int i = 0; i < n; i++) {
                int creationTime = i;
                int burstTime = random.nextInt(100) + 1;  // Random burst time (1-100)
                int priority = random.nextInt(5) + 1;     // Random priority (1-5)
                processes.add(new Process("P" + (i+1), creationTime, burstTime, priority));
            }
        }
        
        // Display the processes
        System.out.println("\nProcess Creation_time Burst_time Priority");
        System.out.println("------- ------------ ---------- --------");
        for (Process p : processes) {
            System.out.printf("%-7s %-12d %-10d %-8d\n", 
                p.name, p.creationTime, p.burstTime, p.priority);
        }

        // Algorithm selection
        System.out.println("\nCPU Scheduling Algorithms");
        System.out.println("=======================");
        System.out.println("1. FCFS");
        System.out.println("2. Round Robin");
        System.out.print("\nSelect one of the CPU scheduling algorithms by entering its choice number: ");
        
        int algorithmChoice = scanner.nextInt();
        
        switch(algorithmChoice) {
            case 1:
                runFCFS();
                break;
            case 2:
                runRoundRobin();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void runFCFS() {
        System.out.println("\nFCFS CPU Scheduling Algorithm");
        System.out.println("============================");
        calculateWaitingTimes();
        displayGanttChart();
    }
    
    private void calculateWaitingTimes() {
        int currentTime = 0;
        
        for (int i = 0; i < processes.size(); i++) {
            Process currentProcess = processes.get(i);
            
            if (currentTime < currentProcess.creationTime) {
                currentTime = currentProcess.creationTime;
            }
            
            currentProcess.waitingTime = currentTime - currentProcess.creationTime;
            currentTime += currentProcess.burstTime;
        }
    }
    
    private void displayGanttChart() {
        System.out.println("\nGantt Chart <with starting time is zero>:");
        int currentTime = 0;
        
        for (Process p : processes) {
            int startTime = Math.max(currentTime, p.creationTime);
            int endTime = startTime + p.burstTime;
            System.out.print("| " + p.name + "(" + startTime + "-" + endTime + ") ");
            currentTime = endTime;
        }
        System.out.println("|");
        
        System.out.println("\nWaiting Times:");
        for (Process p : processes) {
            System.out.println("Waiting time of process " + p.name + " = " + p.waitingTime);
        }
        
        double avgWaitingTime = calculateAverageWaitingTime();
        System.out.printf("\nThe Average Process Waiting Time = %.2f ms.\n", avgWaitingTime);
    }
    
    private void runRoundRobin() {
        System.out.println("\nRound Robin CPU Scheduling Algorithm");
        System.out.println("==================================");
        
        System.out.print("Enter time quantum: ");
        int quantum = scanner.nextInt();
        
        ArrayList<RoundRobinProcess> queue = new ArrayList<>();
        for (Process p : processes) {
            queue.add(new RoundRobinProcess(p));
        }
        
        int currentTime = 0;
        ArrayList<String> ganttChart = new ArrayList<>();
        
        // Keep track of waiting times for each process
        int[] waitingTimes = new int[processes.size()];
        int[] lastCompletionTime = new int[processes.size()];
        
        // Initialize lastCompletionTime with creation times
        for (int i = 0; i < processes.size(); i++) {
            lastCompletionTime[i] = processes.get(i).creationTime;
        }
        
        // Continue until all processes are completed
        while (!queue.isEmpty()) {
            RoundRobinProcess currentProcess = queue.get(0);
            queue.remove(0);
            
            // Get process index (remove "P" and subtract 1)
            int processIndex = Integer.parseInt(currentProcess.name.substring(1)) - 1;
            
            // If process hasn't arrived yet, advance time and add it back to queue
            if (currentTime < currentProcess.creationTime) {
                currentTime = currentProcess.creationTime;
            }
            
            // Add waiting time for this process
            if (currentTime > lastCompletionTime[processIndex]) {
                waitingTimes[processIndex] += (currentTime - lastCompletionTime[processIndex]);
            }
            
            // Calculate execution time for this quantum
            int executeTime = Math.min(quantum, currentProcess.remainingBurstTime);
            
            // Add to Gantt chart
            ganttChart.add(String.format("%s(%d-%d)", 
                currentProcess.name, currentTime, currentTime + executeTime));
            
            // Update remaining burst time and current time
            currentProcess.remainingBurstTime -= executeTime;
            currentTime += executeTime;
            
            // Update last completion time for this process
            lastCompletionTime[processIndex] = currentTime;
            
            // If process is not complete, add it back to queue
            if (currentProcess.remainingBurstTime > 0) {
                queue.add(currentProcess);
            }
        }
        
        // Display Gantt chart
        System.out.println("\nGantt Chart <with starting time is zero>:");
        System.out.print("|");
        for (String entry : ganttChart) {
            System.out.print(" " + entry + " |");
        }
        
        // Display waiting times
        System.out.println("\n\nWaiting Times:");
        double totalWaitingTime = 0;
        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            p.waitingTime = waitingTimes[i];
            System.out.println("Waiting time of process " + p.name + " = " + p.waitingTime);
            totalWaitingTime += p.waitingTime;
        }
        
        // Display average waiting time
        double avgWaitingTime = totalWaitingTime / processes.size();
        System.out.printf("\nThe Average Process Waiting Time = %.2f ms.\n", avgWaitingTime);
    }
    
    private double calculateAverageWaitingTime() {
        int totalWaitingTime = 0;
        for (Process p : processes) {
            totalWaitingTime += p.waitingTime;
        }
        return (double) totalWaitingTime / processes.size();
    }

    public static void main(String[] args) {
        while (true) {
            CPUScheduling scheduler = new CPUScheduling();
            scheduler.inputProcesses();
            
            System.out.println("\nPress Y to continue or ANY KEY & ENTER to EXIT from the simulation:");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            
            if (!choice.equalsIgnoreCase("Y")) {
                break;
            }
        }
    }
}