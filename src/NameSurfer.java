/*
 * File: NameSurfer.java
 * ---------------------------
 * The goal of this program is to draw graphs which show name rank popularity 
 * for 11 decades.  The user can enter a name to search.  If the name appears 
 * in the database as being in the top 1,000 popular names for any decade a
 * graph of the popularity will be displayed. 
 */

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import acm.program.Program;

@SuppressWarnings("serial")
public class NameSurfer extends Program implements NameSurferConstants {
    
    private JTextField name;
    private JButton graph;
    private JButton clear;
    private NameSurferGraph nameGraph;
    private NameSurferDataBase records;

    /**
     * Initializes the program by creating the basic display with buttons, text 
     * field and listeners.  
     */
    public void init() {
        add(new JLabel("Name"), SOUTH);
        name = new JTextField(10);
        graph = new JButton("Graph");
        clear = new JButton("Clear");
        add(name, SOUTH);
        add(graph, SOUTH);
        add(clear, SOUTH);
        addActionListeners();
        name.addActionListener(this);
        nameGraph = new NameSurferGraph();
        add(nameGraph);
        records = new NameSurferDataBase("names-data.txt");
    }
    
    /**
     * When the Graph button is clicked, if the name entered in the text field
     * exists in the data base, calls the addEntry method to print out the 
     * graph.  When the clear button is clicked calls the method to remove all
     * graphs from disply.  
     */
    public void actionPerformed(ActionEvent e)  {
        String cmd = e.getActionCommand();
        String text = name.getText().toUpperCase();
        if (cmd.equals("Graph")) {
            NameSurferEntry entry = records.findEntry(text);
            if (entry != null) {
                nameGraph.addEntry(entry);
            }
        }
        if (cmd.equals("Clear")) {
            nameGraph.clear();
        }
    }
}
