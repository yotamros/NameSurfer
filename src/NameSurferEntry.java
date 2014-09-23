/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import java.util.*;

public class NameSurferEntry implements NameSurferConstants {

    private String name;
    private int[] rank = new int[NDECADES];

    /**
     * Creates a new NameSurferEntry from a data line as it appears in the data
     * file. Each line begins with the name, which is followed by integers
     * giving the rank of that name for each decade.
     */
    public NameSurferEntry(String line) {
        this.name = line.substring(0, line.indexOf(" "));
        int startLocation = line.indexOf(" ") + 1;
        int endLocation = line.indexOf(" ", startLocation);
        for (int i = 0; i < rank.length - 1; i++) {
            int score = Integer.parseInt(line.substring(startLocation,
                    endLocation));
            rank[i] = score;
            startLocation = endLocation + 1;
            endLocation = line.indexOf(" ", startLocation);
        }
        rank[rank.length-1] = Integer.parseInt(line.substring(startLocation));
        if (rank.length != NDECADES) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the name associated with this entry.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the rank associated with an entry for a particular decade. The
     * decade value is an integer indicating how many decades have passed since
     * the first year in the database, which is given by the constant
     * START_DECADE. If a name does not appear in a decade, the rank value is 0.
     */
    public int getRank(int decade) {
        return rank[decade];
    }

    /**
     * Returns a string that makes it easy to see the value of a
     * NameSurferEntry.
     */
    public String toString() {
        return name + " " + Arrays.toString(rank);
    }
}
