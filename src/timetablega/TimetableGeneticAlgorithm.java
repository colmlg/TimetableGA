package timetablega;
import javax.swing.*;

public class TimetableGeneticAlgorithm  {
    private int generation, population, students, modules, modulesInCourse, examSessions, crossoverProb, mutationProb, reproductionProb;

    public static void main(String[] args) {
        TimetableGeneticAlgorithm algorithm = new TimetableGeneticAlgorithm();
        algorithm.getDataFromUser();
        int[][] schedule = algorithm.generateStudentSchedules();
        algorithm.printStudentSchedules(schedule);   
    }
    
    private TimetableGeneticAlgorithm() {}
    
    private void getDataFromUser() {
        generation = requestInput("Enter the number of generations:", 1);
        population = requestInput("Enter the population size:", 1);
        students = requestInput("Enter the number of students:", 1);
        modules = requestInput("Enter the number of modules:", 1);
        modulesInCourse = requestInput("Enter the number of modules in the course:", 1);
        
        while (modules <= modulesInCourse) {
            JOptionPane.showMessageDialog(null, "Number of modules must be greater than the module in the course.\nPlease re-enter these values.");
            modules = requestInput("Enter the number of modules:", 1);
            modulesInCourse = requestInput("Enter the number of modules in the course:", 1);
        }
        
        examSessions = requestInput("Enter the number of exam sessions:", 1);
        crossoverProb = requestInput("Enter the crossover probablility:", 1, 99);
        mutationProb = requestInput("Enter the mutation probablility:", 1, 99);
        reproductionProb = 100 - (crossoverProb + mutationProb);
        
        while ((reproductionProb + crossoverProb + mutationProb) != 100) {
            JOptionPane.showMessageDialog(null, "reproductionProb + crossoverProb + mutationProb must equal 100. +.\nPlease re-enter these values.");
            crossoverProb = requestInput("Enter the crossover probablility:", 1, 99);
            mutationProb = requestInput("Enter the mutation probablility:", 1, 99);
            reproductionProb = 100 - (crossoverProb + mutationProb);
        }
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
            } catch(NumberFormatException e) { 
                JOptionPane.showMessageDialog(null, "Invalid input \"" + response + "\".\nInput must be an integer.");
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
        for(int s = 0; s < students; s++) {
            for(int m = 0; m < modulesInCourse; m++) {
                int randomModule = (int)(Math.random() * modules) + 1;
                
                while(arrayContains(schedule[s], randomModule)) {
                    randomModule = (int)(Math.random() * modules) + 1;
                }
                
                schedule[s][m] = randomModule;
            }
        }
        return schedule;
    }
    
    private boolean arrayContains(int[] array, int value) {
        for(int i = 0; i < array.length; i++) {
            if(array[i] == value) {
                return true;
            }
        }
        return false;
    }
    
    private void printStudentSchedules(int[][] schedule) {
        for(int i = 0; i < students; i++) {
            System.out.print("Student " + i + ":");

            for(int j = 0; j < modulesInCourse; j++) {
                System.out.print(" " + schedule[i][j]);
            }
            System.out.println();
        }
    }
}
