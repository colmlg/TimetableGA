package timetablega;

import java.util.ArrayList;
import javax.swing.*;

public class TimetableGeneticAlgorithm {

    private int generation, populationSize, students, modules, modulesInCourse, examSessions, crossoverProb, mutationProb, reproductionProb, examsPerSession;

    public static void main(String[] args) {
        TimetableGeneticAlgorithm algorithm = new TimetableGeneticAlgorithm();
//        algorithm.getDataFromUser();
        algorithm.setVariablesForTest(); //TODO: remove this
        int[][] schedule = algorithm.generateStudentSchedules();
        algorithm.printStudentSchedules(schedule);
        int[][] firstPopulation = algorithm.createFirstPopulation();
        algorithm.printPopulation(firstPopulation, schedule);
    }

    private TimetableGeneticAlgorithm() {
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

    //For testing purposes only
    private void setVariablesForTest() {
        generation = 5;
        populationSize = 2;
        students = 1;
        modules = 8;
        modulesInCourse = 4;
        examSessions = 5;
        crossoverProb = 50;
        mutationProb = 30;
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

    private void printStudentSchedules(int[][] schedule) {
        for (int i = 0; i < students; i++) {
            System.out.print("Student " + (i + 1) + ":");

            for (int j = 0; j < modulesInCourse; j++) {
                System.out.print(" " + schedule[i][j]);
            }
            System.out.println();
        }
    }

    private int[][] createFirstPopulation() {
        int[][] population = new int[populationSize][];
        for (int i = 0; i < populationSize; i++) {
            population[i] = createRandomOrdering();
        }
        return population;
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

    private void printPopulation(int[][] population, int[][] studentSchedules) {
        for (int i = 0; i < populationSize; i++) {
            System.out.print("Ordering " + (i + 1) + ":");
            for (int j = 0; j < modules; j++) {
                System.out.print(" " + population[i][j]);
            }
            System.out.print(" : Fitness Cost: " + evaluateFitnessCost(population[i], studentSchedules));
            System.out.println();
        }
    }

    private int evaluateFitnessCost(int[] ordering, int[][] studentSchedules) {
        int fitnessCost = 0;
        int[][] sessions = segmentOrdering(ordering);
        for (int[] schedule : studentSchedules) {
            for (int i = 0; i < examsPerSession; i++) {
                int fitnessEvaluator = -1;
                for (int[] session : sessions) {
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
}
