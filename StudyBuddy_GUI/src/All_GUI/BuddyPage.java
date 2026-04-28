package All_GUI;
//import health bar

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BuddyPage extends JPanel{
    private JPanel healthBarSpot; //buddy uses these
    private JLabel creatureImage;  //buddy uses these
    private Healthbar healthbar;
    private TaskManager taskManager;
    private ImageIcon healthyImg;
    private ImageIcon lowHPImg;
    public BuddyPage() {
        setLayout(new BorderLayout());
        setOpaque(false);

        createHealthBarSpot();
        createCreatureArea();

        //TaskManager and healthbar are linked
        //any task completion directly affects buddy's hp
        taskManager = new TaskManager(healthbar, this);

        add(creatureImage, BorderLayout.CENTER);
        add(healthBarSpot, BorderLayout.SOUTH);
    }

    private void createHealthBarSpot() {
        healthBarSpot = new JPanel();
        healthBarSpot.setOpaque(false);
        healthBarSpot.setLayout(new BorderLayout());
        healthBarSpot.setPreferredSize(new Dimension(0, 40));

        //add health bar component here
        healthbar = new Healthbar();
        healthBarSpot.add(healthbar, BorderLayout.CENTER);
    }
    private void createCreatureArea() {
        creatureImage = new JLabel();
        creatureImage.setHorizontalAlignment(SwingConstants.CENTER);
        creatureImage.setVerticalAlignment(SwingConstants.CENTER);

        //insert creature image here
        healthyImg = new ImageIcon(getClass().getResource("/resources/creature.png"));
        lowHPImg = new ImageIcon(getClass().getResource("/resources/deadpou.png"));
        creatureImage.setIcon(healthyImg);
    }

    public void updateImg(int currentHealth) {
        if (currentHealth <= 50) {
            creatureImage.setIcon(lowHPImg); //switches to deadpou at 50% hp or below
        } else {
            creatureImage.setIcon(healthyImg); //switches to healthy img at above 50% hp
        }
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
}
