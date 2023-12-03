package Others;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bar extends JPanel {
    private Simulation simulation;
    private JComboBox<Integer> secondsComboBox;

    public Bar(Simulation simulation) {
        this.simulation = simulation;
    }

    public void createButtons() {
        JCheckBox jCheckBox = new JCheckBox("Immune start");
        JButton jButtonStart = new JButton("Start");
        JButton jButtonStop = new JButton("Stop");
        JButton jButtonSave = new JButton("Save");
        JButton jButtonLoad = new JButton("Load");
        secondsComboBox = new JComboBox<>(new Integer[]{});

        secondsComboBox.setEnabled(false);
        jButtonStop.setEnabled(false);
        jButtonSave.setEnabled(false);
        jButtonLoad.setEnabled(false);

        jButtonStart.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButtonStart.setEnabled(false);
                jButtonStop.setEnabled(true);
                secondsComboBox.setEnabled(false);
                jCheckBox.setEnabled(false);
                jButtonLoad.setEnabled(false);

                SwingWorker<Void, Void> swingWorker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
                        if(simulation.getSeconds() == 0)
                        {
                            simulation.createPops(jCheckBox.isSelected());
                        }
                        simulation.run(jCheckBox.isSelected());
                        return null;
                    }

                    @Override
                    protected void done() {
                        super.done();
                        jButtonStart.setEnabled(true);
                    }
                };
                swingWorker.execute();
            }
        });

        jButtonStop.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButtonStop.setEnabled(false);
                jButtonStart.setEnabled(true);
                secondsComboBox.setEnabled(true);
                jButtonLoad.setEnabled(true);
                simulation.stop();
            }
        });


        jButtonLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulation.restore((Integer) secondsComboBox.getSelectedItem());
            }
        });

        add(jCheckBox);
        add(jButtonStart);
        add(jButtonStop);
        add(jButtonLoad);
        add(secondsComboBox);
    }

    public void refreshComboBox()
    {
        Integer[] seconds = simulation.getSaveMap().keySet().toArray(new Integer[0]);
        secondsComboBox.setModel(new DefaultComboBoxModel<>(seconds));
        validate();
    }
}
