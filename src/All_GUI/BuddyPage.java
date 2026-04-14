package All_GUI;
//import health bar

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BuddyPage extends JPanel{
    private JPanel healthBarSpot; //amari uses these
    private JLabel creatureImage;  //amari uses these
    public BuddyPage() {
        setLayout(new BorderLayout());
        setOpaque(false);

        createHealthBarSpot();
        createCreatureArea();

        add(healthBarSpot, BorderLayout.NORTH);
        add(creatureImage, BorderLayout.CENTER);
    }

    private void createHealthBarSpot() {
        healthBarSpot = new JPanel();
        healthBarSpot.setOpaque(false);
        healthBarSpot.setLayout(new BorderLayout());
        healthBarSpot.setPreferredSize(new Dimension(0, 40));

        //add health bar component here
    }
    private void createCreatureArea() {
        creatureImage = new JLabel();
        creatureImage.setHorizontalAlignment(SwingConstants.CENTER);
        creatureImage.setVerticalAlignment(SwingConstants.CENTER);

        //insert creature image here

    }
}
