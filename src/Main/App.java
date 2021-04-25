package Main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        ResultsPanel resultsPanel = new ResultsPanel(contentPane);

        contentPane.add(eventPanel, "event-manager");
        contentPane.add(resultsPanel, "results-page");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(contentPane, BorderLayout.CENTER);

        JButton save = new JButton("save");
        JButton load = new JButton("load");
        JButton eventManager = new JButton("Event Manager");
        JButton resultsManager = new JButton("Results Manager");

        JToolBar toolBar = new JToolBar("Tools");

        toolBar.add(save);
        toolBar.add(load);
        toolBar.add(eventManager);
        toolBar.add(resultsManager);

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
