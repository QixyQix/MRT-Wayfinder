/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentv1;

import java.util.*;
import javax.swing.*;

/**
 *
 * @author QiXiang
 */
public class Route {

    private String start;
    private String end;
    private String interchange;
    private int noStations;
    private ArrayList<String> lines = new ArrayList<String>();

    /**
     * Constructor
     *
     * @param start Start Station name
     * @param end End Station name
     * @param interchange Interchange Station name (put null if does not apply)
     * @param noStations Number of stations in the route
     * @param lines ArrayList of the lines used
     */
    public Route(String start, String end, String interchange, int noStations, ArrayList<String> lines) {
        this.start = start;
        this.end = end;
        this.interchange = interchange;
        this.noStations = noStations;
        this.lines = lines;
    }

    /**
     * Displays all the stations in a route
     *
     * @return
     */
    public String routeSummary() {
        if (interchange == null) {
            return start + " > " + end;
        } else if (interchange.equals(start) || interchange.equals(end)) {

        }
        return start + " > " + interchange + " > " + end;
    }

    /**
     * Turns the array of lines into a string.
     *
     * @return
     */
    public String StringifyLines() {
        String reString = "";
        for (String line : lines) {
            reString += "[" + line + "] ";
        }
        return reString;
    }

    public int getNoStns() {
        return this.noStations;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }

    public String getInterchange() {
        return this.interchange;
    }

    public ArrayList<String> getLines() {
        return this.lines;
    }

    public boolean containsLine(String inLine) {
        boolean containing = false;
        for (String line : lines) {
            if (inLine.equals(line)) {
                containing = true;
            }
        }
        return containing;
    }
}
