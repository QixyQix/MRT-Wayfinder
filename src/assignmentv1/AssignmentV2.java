/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentv1;

import java.io.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author QiXiang
 */
public class AssignmentV2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String start = null;
        String end = null;
        boolean error;
        int userAction = 0;

        //create object for methods
        Methods methods = new Methods();
        //Generate the hashmap from the text file.
        methods.readFile();

        do {
            error = false;
            try {
                userAction = Integer.parseInt(JOptionPane.showInputDialog("Welcome to MRT Assistant. Please select what you would like to do."
                        + "\n1. Get line route."
                        + "\n2. Calculate route from 2 stations."));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error == true);

        if (userAction == 2) {
            //Get the start and end station from the user.
            do {
                //NOTE: error in this section in the code is flipped
                //error = true means the station exists
                //error = false means the station does NOT exist
                error = true;
                start = JOptionPane.showInputDialog("Welcome to MRT Journey Planner.\nPlease enter your start station.").trim();
                end = JOptionPane.showInputDialog("Welcome to MRT Journey Planner.\nPlease enter your end station.").trim();
                

                error = methods.checkExists(start);
                if (error == false) {
                    JOptionPane.showMessageDialog(null, "Error: Start Station does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                error = true;
                error = methods.checkExists(end);
                if (error == false) {
                    JOptionPane.showMessageDialog(null, "Error: End Station does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } while (error == false);

            methods.calculate(start, end);

            methods.showRoutes();
        } else {
            error = false;
            String print = "";
            int option = 0;
            do {
                error = false;
                try {
                    option = Integer.parseInt(JOptionPane.showInputDialog(null, "View Line Route. Search by:\n1. Station Name \n2. Station Code"));
                    if (option != 1 && option != 2) {
                        JOptionPane.showMessageDialog(null, "Error: Please enter a value of either 1 or 2.", "Error", JOptionPane.ERROR_MESSAGE);
                        error = true;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error: Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }
            } while (error == true);

            if (option == 2) {
                do{
                error = false;
                String stnCode = JOptionPane.showInputDialog(null, "Enter Station code/line code to get line route.");
                stnCode = stnCode.substring(0, 2);
                try{
                for (Station aStation : methods.getLineStations(stnCode)) {
                    print += "\n" + aStation.getStnCode() + "   " + aStation.getStnName();
                }
                JOptionPane.showMessageDialog(null, "Displaying route for " + stnCode + " line." + print);
                }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Error: Incorrect station code.","Error",JOptionPane.ERROR_MESSAGE);
                error = true;
                }
                }while(error == true);
            } else {
                String stn;
                do {
                    error = false;
                    stn = JOptionPane.showInputDialog(null, "Enter station name");
                    error = !(methods.checkExists(stn));
                    if (error == true) {
                        JOptionPane.showMessageDialog(null, "Station does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } while (error == true);
                ArrayList<String> stationLines = methods.getStationLine(stn);
                for (String line : stationLines) {
                    print = "Displaying route for "+stn+" on "+line+" line.";
                    for (Station aStation : methods.getLineStations(line)) {
                        print += "\n" + aStation.getStnCode() + "   " + aStation.getStnName();
                    }
                    JOptionPane.showMessageDialog(null, print);
                }
            }
        }
    }
}
