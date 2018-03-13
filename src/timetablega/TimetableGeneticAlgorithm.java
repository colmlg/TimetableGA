package timetablega;
import javax.swing.*;

public class TimetableGeneticAlgorithm  {
    
    public static void main(String[] args) {
        int generation = requestInput("Enter the number of generations:", 1);
        int population = requestInput("Enter the population size:", 1);
        int students = requestInput("Enter the number of students:", 1);
    }
    
    private static int requestInput(String message, int minValue) {
       int value = Integer.MIN_VALUE;
       while (value < minValue) {
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
            }
       }
       return value;
    }
    
}
