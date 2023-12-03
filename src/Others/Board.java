package Others;


import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private Simulation simulation;
    public Board(Simulation simulation)
    {
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.BLACK);
        this.simulation = simulation;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int size = 10;
        for (var person : this.simulation.personList)
        {
            g.setColor(person.getState().getColor());
            g.fillOval((int) (person.getX() * 16 - size / 2), (int) (person.getY() * 16 - size / 2), size, size);
        }
    }



}
