package facePamphlet;

/* 
 * File: FacePamphlet.java
 * -----------------------
 * This program implements a basic social network management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;

import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;

@SuppressWarnings("serial")
public class FacePamphlet extends Program implements FacePamphletConstants {
    
    private JTextField name;
    private JTextField status;
    private JTextField picture;
    private JTextField friends;
    private FacePamphletProfile profile;
    private FacePamphletDatabase database = new FacePamphletDatabase();
    private FacePamphletCanvas canvas = new FacePamphletCanvas();

    /**
     * This method has the responsibility for initializing the interactors in
     * the application, and adding the canvas. 
     */
    public void init() {
        createButtons();
        add(canvas);
    }
    
    /**
     * Creates all the buttons and text fields with their listeners. 
     */
    private void createButtons() {
        // north border buttons
        name = new JTextField(TEXT_FIELD_SIZE);
        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton lookup = new JButton("Lookup");
        add(new JLabel("Name"), NORTH);
        add(name, NORTH);
        add(add, NORTH);
        add(delete, NORTH);
        add(lookup, NORTH);

        // west border buttons
        status = new JTextField(TEXT_FIELD_SIZE);
        picture = new JTextField(TEXT_FIELD_SIZE);
        friends = new JTextField(TEXT_FIELD_SIZE);
        JButton changeStatus = new JButton("Change Status");
        JButton changePicture = new JButton("Change Picture");
        JButton addFriends = new JButton("Add Friends");
        add(status, WEST);
        add(changeStatus, WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        add(picture, WEST);
        add(changePicture, WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        add(friends, WEST);
        add(addFriends, WEST);
        
        // listeners
        addActionListeners();
        name.addActionListener(this);
        status.addActionListener(this);
        picture.addActionListener(this);
        friends.addActionListener(this);
    }

    /**
     * This class is responsible for detecting when the buttons are clicked or
     * interactors are used and calls the appropriate method based on the 
     * user's behavior. 
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Add") && !(name.getText().isEmpty())) {
            if (database.containsProfile(name.getText())) {
                profile = new FacePamphletProfile(name.getText());
                database.addProfile(profile);
                canvas.showMessage(profile.getName() + " was already in the system.");
            } else {
                profile = new FacePamphletProfile(name.getText());
                database.addProfile(profile);
                canvas.showMessage("Added new user: " + profile.getName());
                canvas.displayProfile(profile);
            }
        }
        if (cmd.equals("Delete")) {
            if (database.containsProfile(name.getText())) {
                database.deleteProfile(name.getText());
                profile = null;
                canvas.displayProfile(profile);
                canvas.showMessage("Deleted user " + name.getText());
            } else {
                canvas.showMessage("Couldn't find user " + profile.getName());
            }
        }
        if (cmd.equals("Lookup")) {
            if (database.containsProfile(name.getText())) {
                profile = database.getProfile(name.getText());
                canvas.displayProfile(profile);
            } else {
                canvas.showMessage("The user " + name.getText() + " doesn't exist.");
            }
        }
        if (cmd.equals("Change Status") || e.getSource() == status && !(e.getActionCommand().isEmpty())) {
            if (profile == null) {
                canvas.showMessage("Choose a profile first.");
            } else {
                profile.setStatus(status.getText());
                canvas.displayProfile(profile);
            }
        }
        if (cmd.equals("Change Picture") || e.getSource() == picture && !(e.getActionCommand().isEmpty())) {
            if (profile == null) {
                System.out.println("choose a profile to update.");
            } else {
                GImage image = null;
                try {
                image = new GImage(picture.getText());
                } catch (ErrorException ex) {
                    canvas.showMessage("invalid photo. try again.");
                }
                if (image != null) {
                    profile.setImage(image);
                    canvas.displayProfile(profile);
                    canvas.showMessage("Image upload success.");
                }
            }
        }
        if (cmd.equals("Add Friends") || e.getSource() == friends && !(e.getActionCommand().isEmpty())) {
            if (profile.getName().equals(friends.getText())) {
                canvas.showMessage("You can't add yourself as a friend.");
            }
            else if (profile == null) {
                canvas.showMessage("choose a profile to update");
            } else {
                if (database.containsProfile(friends.getText())) {
                    if (isFriendAlready()) {
                        canvas.showMessage("already friends with " + friends.getText());
                    } else {
                        profile.addFriend(friends.getText());
                        FacePamphletProfile theFriend = database.getProfile(friends.getText());
                        theFriend.addFriend(profile.getName());
                        canvas.displayProfile(profile);
                        canvas.showMessage(profile.getName() + " and " + friends.getText() + " are friends");
                    }
                } else {
                    canvas.showMessage("this profile doesn't exist.");
                }
            }
        }
    }
    
    /**
     * Finds out if two profiles are already connected as friends. 
     * @return true if they are friends, false if not.
     */
    private boolean isFriendAlready() {
        if (!(profile.getFriends().hasNext())) {
            return false;
        }
        Iterator<String> it = profile.getFriends();
        while (it.hasNext()) {
            String friend = it.next();
            if (friend.equals(friends.getText())) {
                return true;
            }
        }
        return false;
    }
}


