package facePamphlet;

/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;

import java.awt.*;
import java.util.*;

@SuppressWarnings("serial")
public class FacePamphletCanvas extends GCanvas implements
        FacePamphletConstants {

    private GLabel message;

    /**
     * This method displays a message string near the bottom of the canvas.
     * Every time this method is called, the previously displayed message (if
     * any) is replaced by the new message text passed in.
     */
    public void showMessage(String msg) {
        if (message != null) {
            remove(message);
        }
        message = new GLabel(msg);
        message.setFont(MESSAGE_FONT);
        add(message, (getWidth() - message.getWidth()) / 2, getHeight()
                - BOTTOM_MESSAGE_MARGIN);
    }

    /**
     * This method displays the given profile on the canvas. The canvas is first
     * cleared of all existing items and then the given profile is displayed. The
     * profile display includes the name of the user from the profile, the
     * corresponding image (or an indication that an image does not exist), the
     * status of the user, and a list of the user's friends in the social
     * network.
     */
    public void displayProfile(FacePamphletProfile profile) {
        if (profile == null) {
            removeAll();
            return;
        }
        removeAll();
        createName(profile);
        createImageRect(profile);
        if (profile.getStatus() != null) {
            showStatus(profile);
        }
        addFriends(profile);
        showMessage("Displaying " + profile.getName());
    }

    /**
     * Adds a friend connection between two profiles. Updates both profiles
     * about the new connection.  Adds to the canvas the list of friends of this
     * profile.
     * 
     * @param profile, the profile to add the new friend to.
     */
    private void addFriends(FacePamphletProfile profile) {
        GLabel friends = new GLabel("Friends:");
        friends.setFont(PROFILE_FRIEND_LABEL_FONT);
        add(friends, getWidth() / 2, IMAGE_MARGIN + TOP_MARGIN + NAME_HEIGHT);
        Iterator<String> iterator = profile.getFriends();
        int numOfFriends = 0;
        while (iterator.hasNext()) {
            GLabel friend = new GLabel(iterator.next());
            friend.setFont(PROFILE_FRIEND_FONT);
            add(friend, getWidth() / 2, IMAGE_MARGIN + TOP_MARGIN + NAME_HEIGHT + friends.getHeight()
                            + (numOfFriends * friend.getHeight()));
            numOfFriends++;
        }
    }

    /**
     * Display all the information about this profile, including the name, 
     * status, friends, image.
     * @param profile, the profile to display the info. 
     */
    private void showStatus(FacePamphletProfile profile) {
        GLabel status = new GLabel(profile.getName() + " is " + profile.getStatus());
        status.setFont(PROFILE_STATUS_FONT);
        add(status, LEFT_MARGIN, STATUS_MARGIN + status.getHeight() + IMAGE_MARGIN + IMAGE_HEIGHT + TOP_MARGIN + NAME_HEIGHT);
    }

    /**
     * Prints out the name of the profile.
     * @param profile, the profile to display the name. 
     */
    private void createName(FacePamphletProfile profile) {
        GLabel name = new GLabel(profile.getName());
        name.setFont(PROFILE_NAME_FONT);
        name.setColor(Color.BLUE);
        add(name, LEFT_MARGIN, TOP_MARGIN + name.getHeight());
    }

    /**
     * Displays a default photo if the user hasn't uploaded one.  If the user
     * upload a photo, replace the default photo with it.  
     * @param profile, the profile of the photo. 
     */
    private void createImageRect(FacePamphletProfile profile) {
        if (profile.getImage() != null) {
            GImage image = profile.getImage();
            double widthScaleFactor = IMAGE_WIDTH / image.getWidth();
            double heightScaleFactor = IMAGE_HEIGHT / image.getHeight();
            image.scale(widthScaleFactor, heightScaleFactor);
            add(image, LEFT_MARGIN,
                    IMAGE_MARGIN + TOP_MARGIN + NAME_HEIGHT);
        } else {
            GImage defaultImage = new GImage(IMAGE);
            add(defaultImage, LEFT_MARGIN,
                    IMAGE_MARGIN + TOP_MARGIN + NAME_HEIGHT);
        }
    }
}
