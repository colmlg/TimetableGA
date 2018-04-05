package timetablega;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;

public class is15148823 {

    private int generation, populationSize, students, modules, modulesInCourse, examSessions, crossoverProb, mutationProb, reproductionProb, examsPerSession;
    private int[][] studentSchedules;
    
    class Ordering implements Comparable<Ordering> {
        int[] elements;
        int fitness;
        
        public Ordering(int[] elements, int fitness) {
            this.elements = elements;
            this.fitness = fitness;
        }

        @Override
        public int compareTo(Ordering ordering) {
            if (ordering.fitness == fitness) {
                return 0;
            }
            return fitness > ordering.fitness ? 1 : -1;
        }
    }
    
    public static void main(String[] args) {
        is15148823 algorithm = new is15148823();
//        algorithm.getDataFromUser();
        algorithm.setVariablesForTest();
        algorithm.studentSchedules = algorithm.generateStudentSchedules();
        algorithm.printStudentSchedules();
        Ordering[] firstPopulation = algorithm.createFirstPopulation();
        algorithm.waitForEndUser();
        algorithm.printPopulation(firstPopulation);
        
        Arrays.sort(firstPopulation);
        algorithm.applySelection(firstPopulation);
        algorithm.printPopulation(firstPopulation);

    }

    private is15148823() {
    }
    
    private void setVariablesForTest() {
        generation = 100;
        populationSize = 23;
        students = 10;
        modules = 4;
        modulesInCourse = 2;
        examSessions = 2;
        crossoverProb = 50;
        mutationProb = 30;
        reproductionProb = 100 - (crossoverProb + mutationProb);
        examsPerSession = (int) Math.ceil(modules / examSessions);
    }

    private void getDataFromUser() {
        generation = requestInput("Enter the number of generations:", 1);
        populationSize = requestInput("Enter the population size:", 1);
        students = requestInput("Enter the number of students:", 1);
        modules = requestInput("Enter the number of modules:", 1);
        modulesInCourse = requestInput("Enter the number of modules in the course:", 1);

        while (modules <= modulesInCourse) {
            JOptionPane.showMessageDialog(null, "Number of modules must be greater than the modules in the course.\nPlease re-enter these values.");
            modules = requestInput("Enter the number of modules:", 1);
            modulesInCourse = requestInput("Enter the number of modules in the course:", 1);
        }

        examSessions = requestInput("Enter the number of exam sessions:", 1);
        crossoverProb = requestInput("Enter the crossover probablility:", 1, 99);
        mutationProb = requestInput("Enter the mutation probablility:", 1, 99);
        reproductionProb = 100 - (crossoverProb + mutationProb);
        examsPerSession = (int) Math.ceil(modules / examSessions);
    }

    private int requestInput(String message, int minValue) {
        return requestInput(message, minValue, Integer.MAX_VALUE);
    }

    private int requestInput(String message, int minValue, int maxValue) {
        int value = Integer.MIN_VALUE;
        while (value < minValue || value > maxValue) {
            String response = JOptionPane.showInputDialog(message);
            if (response == null) {
                System.exit(0);
            }
            try {
                value = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input \"" + response + "\".\nInput must be an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (value < minValue) {
                JOptionPane.showMessageDialog(null, "Value must be greater than " + (minValue - 1) + ".");
            } else if (value > maxValue) {
                JOptionPane.showMessageDialog(null, "Value must be less than " + (maxValue + 1) + ".");
            }
        }
        return value;
    }

    private int[][] generateStudentSchedules() {
        int[][] schedule = new int[students][modulesInCourse];
        for (int s = 0; s < students; s++) {
            for (int m = 0; m < modulesInCourse; m++) {
                int randomModule = 0;
                while (arrayContains(schedule[s], randomModule)) {
                    randomModule = (int) (Math.random() * modules) + 1;
                }
                schedule[s][m] = randomModule;
            }
        }
        return schedule;
    }

    private boolean arrayContains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }

    private void printStudentSchedules() {
        for (int i = 0; i < students; i++) {
            System.out.print("Student " + (i + 1) + ":");

            for (int j = 0; j < modulesInCourse; j++) {
                System.out.print(" " + studentSchedules[i][j]);
            }
            System.out.println();
        }
    }

    private Ordering[] createFirstPopulation() {
        Ordering[] population = new Ordering[populationSize];
        for (int i = 0; i < populationSize; i++) {
            int[] newOrderingElements = createRandomOrdering();
            while(populationContains(population, newOrderingElements)) {
                newOrderingElements = createRandomOrdering();
            }
            int fitness = evaluateFitnessCost(newOrderingElements);
            Ordering newOrdering = new Ordering(newOrderingElements, fitness);
            population[i] = newOrdering;
        }
        return population;
    }
    
    private boolean populationContains(Ordering[] population, int[] newOrdering) {
        for(int j = 0; j < population.length; j++) {
            if(population[j] == null) {
                break;
            }
            int[] ordering = population[j].elements;
            
            boolean areEqual = true;
            for(int i = 0; i < ordering.length; i++) {
                if(ordering[i] != newOrdering[i]) {
                    areEqual = false;
                }
            }
            
            if(areEqual) {
                return true;
            }
        }
        return false;
    }
    
    private int[] createRandomOrdering() {
        int[] ordering = new int[modules];
        for (int i = 0; i < modules; i++) {
            int randomModule = 0;
            while (arrayContains(ordering, randomModule)) {
                randomModule = (int) (Math.random() * modules) + 1;
            }
            ordering[i] = randomModule;
        }
        return ordering;
    }

    private void printPopulation(Ordering[] population) {
        System.out.println("Population\n");
        
        for (int i = 0; i < populationSize; i++) {
            System.out.print("Ordering " + (i + 1) + ":");
            for (int j = 0; j < modules; j++) {
                System.out.print(" " + population[i].elements[j]);
            }
            System.out.print(" : Fitness Cost: " + population[i].fitness);
            System.out.println();
        }
    }

    private int evaluateFitnessCost(int[] ordering) {
        int fitnessCost = 0;
        int[][] sessions = segmentOrdering(ordering);
        
        for(int[] session : sessions) {
            for(int[] schedule : studentSchedules) {
                int fitnessEvaluator = -1;
                for(int i = 0; i < examsPerSession; i++) {
                    if(arrayContains(schedule, session[i])) {
                        fitnessEvaluator++;
                    }
                }
                if (fitnessEvaluator > 0) {
                    fitnessCost += fitnessEvaluator;
                }
            }
        }
        return fitnessCost;
    }

    private int[][] segmentOrdering(int[] ordering) {
        int[][] sessions = new int[examSessions][examsPerSession];
        int orderingIndex = 0;
        for (int i = 0; i < examSessions; i++) {
            for (int j = 0; j < examsPerSession; j++) {
                if (orderingIndex >= ordering.length) {
                    sessions[i][j] = -1;
                } else {
                    sessions[i][j] = ordering[orderingIndex];
                }
                orderingIndex++;
            }
        }
        return sessions;
    }
    
    private void waitForEndUser() {
        Scanner keyIn = new Scanner(System.in);
        System.out.print("Waiting for the end user... Enter to continue");
        keyIn.nextLine();
    }
    
    private void applySelection(Ordering[] population) {
        int sectionLength = (int) Math.ceil(population.length / 3.0);
        for(int i = 0; i < sectionLength; i++) {
            int newLocation = i + (2 * sectionLength);
            if (newLocation > population.length - 1) {
                break;
            }
            population[newLocation] = population[i];
        }
    }
}
