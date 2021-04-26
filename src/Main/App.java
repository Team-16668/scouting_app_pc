package Main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by jb on 4/24/21.
 */
public class App {
    private String version = "1.0 - beta";

    private String info = "FTC Scouting App created by Team 16668 \n" +
            "Note: \n" +
            "Currently in Beta \n";

    private boolean writeTeams = true;

    public App() {
        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setText(info);
        infoTextArea.setBackground(new Color(241,241,241));
        infoTextArea.setEditable(false);
        infoTextArea.setMargin(new Insets(5, 5, 5,5));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(infoTextArea, BorderLayout.CENTER);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new CardLayout());

        EventPanel eventPanel = new EventPanel(contentPane);
        eventPanel.setPreferredSize(new Dimension(600, 100));
        eventPanel.setMaximumSize(eventPanel.getPreferredSize());
        eventPanel.setMinimumSize(eventPanel.getPreferredSize());
        ResultsPanel resultsPanel = new ResultsPanel(contentPane);

        contentPane.add(eventPanel, "event-manager");
        contentPane.add(resultsPanel, "results-page");

        contentPane.setSize(new Dimension(1920, 1080));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(contentPane, BorderLayout.CENTER);

        JButton save = new JButton("save");
        JButton load = new JButton("load");
        JButton resultsManager = new JButton("Results Manager");
        JButton eventManager = new JButton("Event Manager");

        JToolBar toolBar = new JToolBar("Tools");

        toolBar.add(save);
        toolBar.add(load);
        toolBar.add(resultsManager);
        toolBar.add(eventManager);

        JFrame frame = new JFrame();
        frame.setTitle("SCOUT - v " + version);
        frame.setLayout(new BorderLayout());
        frame.add(contentPane, BorderLayout.CENTER);
        frame.add(toolBar, BorderLayout.NORTH);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);

        JTextArea warning = new JTextArea();
        warning.setAlignmentX(Component.CENTER_ALIGNMENT);
        warning.setText("There is already a file for teams in this directory. Do you want to overwrite it?");
        warning.setEditable(false);
        warning.setLineWrap(true);
        warning.setWrapStyleWord(true);
        warning.setBackground(new Color(241,241,241));

        JPanel warningPanel = new JPanel();
        warningPanel.add(warning);

        JButton yes = new JButton();
        yes.setText("Yes");

        JButton no = new JButton();
        no.setText("No");

        JPanel warningControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        warningControlPanel.add(yes);
        warningControlPanel.add(no);

        JFrame warningFrame = new JFrame();
        warningFrame.setTitle("Warning!");
        warningFrame.setLayout(new BorderLayout());
        warningFrame.add(warningControlPanel, BorderLayout.SOUTH);
        warningFrame.add(warning, BorderLayout.NORTH);
        warningFrame.setPreferredSize(new Dimension(400,
                150
        ));
        warningFrame.setMinimumSize(warningFrame.getPreferredSize());
        warningFrame.setMaximumSize(warningFrame.getPreferredSize());
        warningFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        warningFrame.setLocationRelativeTo(null);
        warningFrame.setResizable(false);
        warningFrame.setVisible(false);
        warningFrame.setEnabled(false);

        resultsPanel.homeSplitPane.setDividerLocation(frame.getWidth() / 2);

        eventManager.addActionListener((e) -> {
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.show(contentPane, "event-manager");
        });

        resultsManager.addActionListener((e) -> {
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.show(contentPane, "results-page");
        });

        save.addActionListener((e) -> {
            DefaultTableModel inputModel = (DefaultTableModel) resultsPanel.inputTable.getModel();
            for(int i = 0; i < inputModel.getRowCount(); i++) {
                try {
                    File myObj = new File(inputModel.getValueAt(i, 0) + ".txt");
                    if (myObj.createNewFile()) {
                        System.out.println("File created: " + myObj.getName());
                    } else {
                        System.out.println("File already exists.");
                    }
                } catch (IOException a) {
                    System.out.println("An error occurred.");
                    a.printStackTrace();
                }

                try {
                    BufferedWriter outputWriter = null;
                    outputWriter = new BufferedWriter(new FileWriter(inputModel.getValueAt(i, 0) + ".txt"));
                    for(int o=0; o< inputModel.getColumnCount(); o++) {
                        outputWriter.write((String) inputModel.getValueAt(i, o));
                        outputWriter.newLine();
                    }
                    outputWriter.flush();
                    outputWriter.close();
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException a) {
                    System.out.println("An error occurred.");
                    a.printStackTrace();
                }
            }
        });

        eventPanel.saveTeams.addActionListener((e) -> {
            if(eventPanel.dir.getText() != "No File Location Selected") {
                try {
                    File myObj = new File(eventPanel.dir.getText() + "\\teams.txt");
                    if (myObj.createNewFile()) {
                        System.out.println("File created: " + myObj.getName());
                    } else {
                        warningFrame.setVisible(true);
                        warningFrame.setEnabled(true);
                        frame.setEnabled(false);
                    }
                } catch (IOException a) {
                    System.out.println("An error occurred.");
                    a.printStackTrace();
                }
                if (writeTeams) {
                    try {
                        BufferedWriter outputWriter = null;
                        outputWriter = new BufferedWriter(new FileWriter(eventPanel.dir.getText() + "\\teams.txt"));
                        for (int i = 0; i < eventPanel.teamTable.getRowCount(); i++) {
                            outputWriter.write(((String) eventPanel.teamTable.getValueAt(i, 0)) + "-" +
                                    ((String) eventPanel.teamTable.getValueAt(i, 1)));
                            if(i != eventPanel.teamTable.getRowCount() - 1) {
                                outputWriter.newLine();
                            }
                        }
                        outputWriter.flush();
                        outputWriter.close();
                    } catch (IOException a) {
                        System.out.println("An error occurred.");
                        a.printStackTrace();
                    }
                }
            }
        });

        yes.addActionListener((e) -> {
            writeTeams = true;
            warningFrame.setVisible(false);
            warningFrame.setEnabled(false);
            frame.setEnabled(true);
        });

        no.addActionListener((e) -> {
            writeTeams = false;
            warningFrame.setVisible(false);
            warningFrame.setEnabled(false);
            frame.setEnabled(true);
        });



    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new App();
        });
    }

}
