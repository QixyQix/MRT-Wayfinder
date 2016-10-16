/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentv1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author QiXiang
 */
public class Methods {

    private HashMap<String, ArrayList<Station>> Stations = new HashMap<String, ArrayList<Station>>();
    private ArrayList<Route> routes = new ArrayList<Route>();

    /*
    Hashmap that stores the stations
    Key: Line which station belongs to
    Value: ArrayList of stations belonging to that line.
     */
    //private HashMap<String, ArrayList<Station>> Stations;
    /**
     * Reads MRT text file and then returns the data in a HashMap.
     */
    public void readFile() {
        //HashMap to be returned.
        HashMap<String, ArrayList<Station>> readHashMap = new HashMap<String, ArrayList<Station>>();
        //Line of the file
        String line = null;

        try {
            ArrayList<Station> LineStations = null;
            String MRTLine = null;
            BufferedReader reader = new BufferedReader(new FileReader("MRT.txt"));
            while ((line = reader.readLine()) != null) {

                if (line.equals("(start)")) {
                    LineStations = new ArrayList<Station>();
                    line = reader.readLine().trim();
                    //Get Line to put in the hashmap (key)
                    MRTLine = line.substring(0, 2);
                    //Method to add new station object
                    LineStations.add(new Station(line, reader.readLine().trim()));
                } else if (line.equals("(end)")) {
                    readHashMap.put(MRTLine, LineStations);
                } else {
                    LineStations.add(new Station(line, reader.readLine().trim()));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Station not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
        this.Stations = readHashMap;
    }

    /**
     * Checks if station exists
     *
     * @param station Queried station name e.g. Yew Tee
     * @return true = station exists false = station does not exist
     */
    public boolean checkExists(String station) {
        for (String key : this.Stations.keySet()) {
            for (Station stn : this.Stations.get(key)) {
                if (station.equals(stn.getStnName())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get the lines a station serves
     *
     * @param station Station name
     * @return An ArrayList<String> with of the lines the station serves
     */
    public ArrayList<String> getStationLine(String station) {
        ArrayList<String> linesServed = new ArrayList<String>();
        for (String key : this.Stations.keySet()) {
            for (Station stn : this.Stations.get(key)) {
                if (station.equals(stn.getStnName())) {
                    linesServed.add(key);
                    break;
                }
            }
        }
        return linesServed;
    }

    /**
     * Calculates the possible routes.
     *
     * @param start Name of start station
     * @param end Name of end station
     */
    public void calculate(String start, String end) {
        ArrayList<String> startLines = new ArrayList<String>();
        ArrayList<String> endLines = new ArrayList<String>();
        //ArrayList<String> lines = new ArrayList<String>();

        int startIndex = 0;
        int endIndex = 0;
        //int intIndex1 = 0;
        //int intIndex2 = 0;
        int noStations = 0;

        ArrayList<String> possibleDirectRoutes = new ArrayList<String>();

        //Get lines served by start and end points
        startLines = getStationLine(start);
        endLines = getStationLine(end);

        //Check for possible direct routes without interchange.
        for (String linesrt : startLines) {
            for (String lineend : endLines) {
                if (linesrt.equals(lineend)) {
                    possibleDirectRoutes.add(linesrt);
                }
            }
        }

        //Foreach possible direct route line
        for (String line : possibleDirectRoutes) {
            ArrayList<String> lines = new ArrayList<String>();
            startIndex = -1;
            endIndex = -1;
            ArrayList<Station> lineAL = this.Stations.get(line);
            for (Station stn : lineAL) {
                if (stn.getStnName().equals(start)) {
                    startIndex = lineAL.indexOf(stn);
                }
                if (stn.getStnName().equals(end)) {
                    endIndex = lineAL.indexOf(stn);
                }
            }
            if (startIndex != 0 || endIndex != 0) {
                noStations = startIndex - endIndex;
                if (noStations < 0) {
                    noStations = (noStations * -1);
                }
                lines.clear();
                lines.add(line);
                this.routes.add(new Route(start, end, null, noStations, lines));
            }
        }

        for (String linestart : startLines) {
            for (String lineend : endLines) {
                if (linestart.equals(lineend)) {
                    //Do NOTHING.
                } else {
                    //find interchange station
                    //For each station in the arraylist for the start station's line
                    for (Station stn : this.Stations.get(linestart)) {
                        //Array to store lines used in route
                        ArrayList<String> lines = new ArrayList<String>();
                        //For wach station in the arraylist for the end station's line
                        for (Station stn2 : this.Stations.get(lineend)) {
                            //If the station in both lines match i.e. interchange station
                            if (stn.getStnName().equals(stn2.getStnName())) {
                                //Calculate the number of stations it takes for this journey.
                                noStations = getIndex(linestart, start) - getIndex(linestart, stn.getStnName());
                                if (noStations < 0) {
                                    noStations *= -1;
                                }
                                int noStationstmp = getIndex(lineend, end) - getIndex(lineend, stn.getStnName());
                                if (noStationstmp < 0) {
                                    noStationstmp *= -1;
                                }
                                noStations += (noStationstmp - 1);

                                //Check if the interchange is the same as the start or end
                                //Prevents addition of useless routes
                                if (!(stn.getStnName().equals(start) || stn.getStnName().equals(end))) {
                                    lines.clear();
                                    lines.add(linestart);
                                    lines.add(lineend);
                                    this.routes.add(new Route(start, end, stn.getStnName(), noStations, lines));
                                    System.out.println("");
                                }
                            }//End if interchange
                        }//End for loop for end station line ArrayList
                    }//End for loop for start station line ArrayList 
                }//End if stations on the same line
            }//End for loop for end station's lines
        }//End for loop for start station's lines
    }//End calculate method

    /**
     * Gets the index of the station in the ArrayList it belongs to within the
     * HashMap.
     *
     * @param line
     * @param stnName
     * @return
     */
    public int getIndex(String line, String stnName) {
        int index = 0;
        for (Station currStn : this.Stations.get(line)) {
            if (currStn.getStnName().equals(stnName)) {
                index = this.Stations.get(line).indexOf(currStn);
            }
        }
        return index;
    }
    
    /**
     * Retrieves the ArrayList of station objects for a line
     * @return 
     */
    public ArrayList<Station> getLineStations(String line){
    return this.Stations.get(line);
    }

    /**
     * Displays a summary of the possible routes
     */
    public void showRoutes() {
        String display = "Enter the number for the corresponding route for more details\n\n";
        String routeDetail = "";
        int i = 0;
        int selection = 0;
        
        ArrayList<Route> sortedRoutes = sortRoutes(this.routes);

        for (Route aRoute : sortedRoutes) {
            display += i + ": " + aRoute.routeSummary() + "\nNo of stations: " + aRoute.getNoStns() + "\nLines used: " + aRoute.StringifyLines() + "\n\n";
            i++;
        }
        display+=i+": Exit";

        boolean error;
        do {
            try {
                error = false;
                selection = Integer.parseInt(JOptionPane.showInputDialog(null, display, "Possible Routes", JOptionPane.QUESTION_MESSAGE));
                if (selection > i || selection < 0) {
                    JOptionPane.showMessageDialog(null, "Number is out of range.\nPlease enter a number between 0 and " + (i - 1), "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                }
                if(selection == i){
                JOptionPane.showMessageDialog(null, "Thank you, Have a nice day.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter a number corresponding to the trip you wish to view more details.", "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
            
            if(error == false && selection != i){
            Route selectedRoute = sortedRoutes.get(selection);
            String start = selectedRoute.getStart();
            String end = selectedRoute.getEnd();
            String interchange = selectedRoute.getInterchange();
            ArrayList<String> lines = selectedRoute.getLines();

            if (interchange != null) {
                routeDetail = "Displaying route from "+start+" to "+end+"with transfer at "+interchange+"\nNo of stations: "+selectedRoute.getNoStns()+"\n\nTravel on "+lines.get(0)+"L\n";
                routeDetail += start + displayStationsTo(start, interchange, lines.get(0));
                routeDetail += "\nInterchange to " + lines.get(1) + "L\n";
                routeDetail += interchange + displayStationsTo(interchange, end, lines.get(1));
            } else {
                routeDetail = "Displaying direct route from "+start+" to "+end+"\nNo of stations: "+selectedRoute.getNoStns()+"\n\nTravel on "+lines.get(0)+"L\n";
                routeDetail += start + displayStationsTo(start, end, lines.get(0));
            }

            JOptionPane.showMessageDialog(null, routeDetail);
            
            error = true;
            }
        } while (error == true);

    }

    /**
     * Makes a string displaying all the stations from start to end
     * @param start
     * name of start station
     * @param end
     * name of end station/interchange station
     * @param line
     * line of section
     * @return 
     */
    public String displayStationsTo(String start, String end, String line) {
        int indexStart = 0;
        int indexEnd = 0;

        ArrayList<Station> lineStns = Stations.get(line);

        indexStart = getIndex(line, start);
        indexEnd = getIndex(line, end);

        String detail = "";

        if (indexStart < indexEnd) {
            for (int i = indexStart + 1; i <= indexEnd; i++) {
                detail += " > " + lineStns.get(i).getStnName();
            }
        } else {
            for (int i = indexStart - 1; i >= indexEnd; i--) {
                detail += " > " + lineStns.get(i).getStnName();
            }
        }

        return detail;
    }
    
    public ArrayList<Route> sortRoutes(ArrayList<Route> inRoutes){
        int minimum;
        
        Route bestRoute = null;
        ArrayList<Route> sortedRoute = new ArrayList<Route>(); 
        
        while(inRoutes.size()!= 0){
            minimum = inRoutes.get(0).getNoStns();
        for(Route aRoute : inRoutes){
            if(aRoute.getNoStns() <= minimum){
            minimum = aRoute.getNoStns();
            bestRoute = aRoute;
            }
        }
        sortedRoute.add(bestRoute);
        inRoutes.remove(bestRoute);
        }
        return sortedRoute;
    }

}
