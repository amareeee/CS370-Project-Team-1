package All_GUI;

import javax.swing.*;
import java.awt.*;

public class Healthbar extends JPanel {

    private JProgressBar healthbar;
    private JLabel percentage;

    private int maxhealth = 100;
    private int current = 100; //base hp 100%

    //getter so other classes can read current hp
    public int getCurrentHealth() {
        return current;
    }

    public Healthbar() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        setOpaque(false);

        //health bar
        healthbar = new JProgressBar(0, maxhealth); //healthbar has min val 0 and max val 100
        healthbar.setValue(current); //sets current as current health % for example
        healthbar.setPreferredSize(new Dimension(250, 30));
        healthbar.setForeground(Color.GREEN);

        //percentage label
        percentage = new JLabel(current + "%");
        percentage.setFont(new Font("Arial", Font.BOLD, 15));

        add(healthbar);
        add(percentage);
    }

    //update health (will be used for later)
    public void setHealth(int newHealth) {
        current = Math.max(0, Math.min(newHealth, maxhealth)); //health val stays between 0 - 100
        healthbar.setValue(current); //updates JprogressBar to match current health
        percentage.setText(current + "%"); //updates current health percentage text

        if (current <= 50) {
            healthbar.setForeground(Color.RED); // healthbar turns red at 50%
        } else {
            healthbar.setForeground(Color.GREEN); //healthbar is green at above 50%
        }
    }
}