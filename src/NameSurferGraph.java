/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is 
 * resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.*;

@SuppressWarnings("serial")
public class NameSurferGraph extends GCanvas implements NameSurferConstants,
        ComponentListener {

    private List<NameSurferEntry> entries = new ArrayList<NameSurferEntry>();

    /**
     * Creates a new NameSurferGraph object that displays the data.
     */
    public NameSurferGraph() {
        addComponentListener(this);
    }

    /**
     * Clears the list of name surfer entries stored in the entries array.
     */
    public void clear() {
        entries.clear();
        update();
    }

    /**
     * Adds a new NameSurferEntry to the list of entries on the display. Note
     * that this method does not actually draw the graph, but simply stores the
     * entry; the graph is drawn by calling update.
     */
    public void addEntry(NameSurferEntry entry) {
        entries.add(entry);
        update();
    }

    /**
     * Updates the display image by deleting all the graphical objects from the
     * canvas and then reassembling the display according to the list of
     * entries. 
     */
    public void update() {
        this.removeAll();
        makeGrid();
        if (!entries.isEmpty()) {
            drawGraphs();
        }
    }

    /**
     * Finds out if the rank is zero.  If it is assigns the maximum value 1,000
     * so that the graph displays it at the bottom of the screen.
     * @param rank, int.
     * @return rank, int.
     */
    private int isZero(int rank) {
        if (rank == 0) {
            return MAX_RANK;
        }
        return rank;
    }

    /**
     * Finds out if the rank is zero.  If it is returns an * for a nicer visual
     * representation. 
     * @param score, String, the ranking of the name.
     * @return String, * if score is zero, otherwise the score.  
     */
    private String isZero(String score) {
        if (score.equals("0")) {
            return "*";
        }
        return score;
    }

    /**
     * Draws the graphs for any NameSurferEntry objects in the entries array.
     */
    private void drawGraphs() {
        int lineInterval = this.getWidth() / NDECADES;
        for (int i = 0; i < entries.size(); i++) {
            NameSurferEntry entry = entries.get(i);
            for (int j = 0; j < NDECADES; j++) {
                int firstPoint = entry.getRank(j);
                int secondPoint = 0;
                if (j < NDECADES -1) {
                    secondPoint = entry.getRank(j + 1); 
                    secondPoint = isZero(secondPoint);
                }
                String score = Integer.toString(entry.getRank(j));
                firstPoint = isZero(firstPoint);
                
                score = isZero(score);
                double heightRatio = (double) (getHeight() - 2 * GRAPH_MARGIN_SIZE)
                        / MAX_RANK;
                
                this.add(markScore(entry, score, firstPoint, lineInterval,
                        heightRatio, j));
                if (j < NDECADES -1) {
                    this.add(drawLine(lineInterval, firstPoint, secondPoint,
                            heightRatio, i, j));
                }
            }
        }
    }
    
    /**
     * Draws a line from one decade to the next one based on the rank.
     * @param lineInterval, int, the visual distance between each decade.
     * @param firstPoint, int, the y coordinate of the line's starting point.
     * @param secondPoint, int, the y coordinate of the line's ending point.
     * @param heightRatio, double, the ratio between the max rank number and 
     * the current size of the window.
     * @param i
     * @param j
     * @return GLine, a line.
     */
    private GLine drawLine(int lineInterval, int firstPoint, int secondPoint,
            double heightRatio, int i, int j) {
        GLine line = new GLine(lineInterval * j, (firstPoint * heightRatio)
                + GRAPH_MARGIN_SIZE, lineInterval * (j + 1),
                (secondPoint * heightRatio) + GRAPH_MARGIN_SIZE);
        line.setColor(chooseColor(i));
        return line;
    }

    /**
     * Adds the ranking information to the display for each decade. 
     * @param entry, the entry of the ranking.
     * @param score, the ranking.
     * @param firstPoint, int, the y coordinate of the line's starting point.
     * @param lineInterval, int, the visual distance between each decade.
     * @param heightRatio, double, the ratio between the max rank number and 
     * the current size of the window.
     * @param j
     * @return GLabel, visual representation of each decade's ranking.
     */
    private GLabel markScore(NameSurferEntry entry, String score,
            int firstPoint, int lineInterval, double heightRatio, int j) {
        GLabel rank = new GLabel(" " + entry.getName() + " " + score);
        this.add(rank, lineInterval * j, (firstPoint * heightRatio)
                + GRAPH_MARGIN_SIZE);
        return rank;
    }

    /**
     * Assigns the color of the graph using a cycle of four colors.
     * @param i
     * @return Color, red, blue, magenta, or black.
     */
    private Color chooseColor(int i) {
        if (i > 3) {
            i = i % 4;
        }
        if (i == 0) {
            return Color.RED;
        }
        if (i == 1) {
            return Color.BLUE;
        }
        if (i == 2) {
            return Color.MAGENTA;
        }
        if (i == 3) {
            return Color.BLACK;
        }
        return null;
    }

    /**
     * Creates the basic grid, without any graphs. 
     */
    private void makeGrid() {
        int lowerBound = this.getHeight();
        int startYear = 1900;
        int lineInterval = this.getWidth() / NDECADES;
        for (int i = 1; i < NDECADES + 1; i++) {
            if (i < NDECADES) {
                GLine decade = new GLine(lineInterval * i, 0, lineInterval * i,
                        lowerBound);
                this.add(decade);
            }
            Integer thisYear = startYear + (i - 1) * 10;
            GLabel year = new GLabel(thisYear.toString());
            this.add(year, lineInterval * (i - 1), lowerBound
                    - (GRAPH_MARGIN_SIZE - year.getHeight()));
        }

        GLine upperLine = new GLine(0, GRAPH_MARGIN_SIZE, this.getWidth(),
                GRAPH_MARGIN_SIZE);
        GLine lowerLine = new GLine(0, lowerBound - GRAPH_MARGIN_SIZE,
                this.getWidth(), lowerBound - GRAPH_MARGIN_SIZE);
        this.add(upperLine);
        this.add(lowerLine);
    }

    /* Implementation of the ComponentListener interface */
    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        update();
    }

    public void componentShown(ComponentEvent e) {
    }
}
