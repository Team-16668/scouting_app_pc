package Main;

import javax.swing.*;
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
        //Create the Input Table
        String[][] inputData = {
                { "16668", "250", "45" },
                { "11115", "2000", "95" },
                { "8393", "350", "120" },
        };

        // Column Names
        String[] inputColumnNames = { "Team #", "TeleOp Score", "Endgame Score"};

        // Initializing the JTable

        JTable inputTable = new JTable(new DefaultTableModel(inputData, inputColumnNames));

        JScrollPane inputScrollPane = new JScrollPane(inputTable);

        //The control panel below the input stuff
        JButton addRow = new JButton("Add Team Entry");
        JButton removeRow = new JButton("Remove Team Entry");

        JPanel inputControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputControlPanel.add(addRow);
        inputControlPanel.add(removeRow);

        JLabel inputLabel = new JLabel("Input Data");
        inputLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        inputPanel.add(inputControlPanel, BorderLayout.SOUTH);

        //Create the Output Table with 2 Teams
        String[][] outputData = new String[inputData.length][3];
        for(int i = 0; i < outputData.length; i++) {
            outputData[i][0] = String.valueOf(i+1);
        }

        // Column Names
        String[] outputColumnNames = { "Rank", "Team #", "Score"};

        // Initializing the JTable
        JTable outputTable = new JTable(new DefaultTableModel(outputData, outputColumnNames){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        });

        JScrollPane outputScrollPane = new JScrollPane(outputTable);

        //The control panel below the input stuff
        JButton calculate = new JButton("Calculate!");

        Object[] rankingCategories = {"Teleop", "Endgame"};

        JComboBox outputRankings = new JComboBox(rankingCategories);
        outputRankings.setEditable(false);

        JPanel outputControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outputControlPanel.add(outputRankings);
        outputControlPanel.add(calculate);

        JLabel outputLabel = new JLabel("Rankings");
        outputLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        outputPanel.add(outputControlPanel, BorderLayout.SOUTH);

        JSplitPane splitPane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel);
        splitPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setText(info);
        infoTextArea.setBackground(new Color(241,241,241));
        infoTextArea.setEditable(false);
        infoTextArea.setMargin(new Insets(5, 5, 5,5));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(infoTextArea, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        JButton save = new JButton("save");
        JButton load = new JButton("load");

        JToolBar toolBar = new JToolBar("Tools");

        toolBar.add(save);
        toolBar.add(load);


        addRow.addActionListener((e) -> {
            DefaultTableModel inputModel = (DefaultTableModel) inputTable.getModel();
            inputModel.addRow(new Object[]{"", "", ""});
            DefaultTableModel outputModel = (DefaultTableModel) outputTable.getModel();
            int rank = outputTable.getRowCount()+1;
            outputModel.addRow(new Object[]{rank, "", ""});
            outputLabel.setText("Rankings - Uncalculated");
        });

        removeRow.addActionListener((e) -> {
            DefaultTableModel inputModel = (DefaultTableModel) inputTable.getModel();
            inputModel.removeRow(inputTable.getRowCount() - 1);

            DefaultTableModel outputModel = (DefaultTableModel) outputTable.getModel();
            outputModel.removeRow(outputTable.getRowCount() - 1);

            outputLabel.setText("Rankings - Uncalculated");
        });

        calculate.addActionListener((e) -> {
            DefaultTableModel inputModel = (DefaultTableModel) inputTable.getModel();
            int arrayIndex = outputRankings.getSelectedIndex()+1;
            String[][] inputArray = new String[inputModel.getRowCount()][2];
            for(int i = 0; i < inputModel.getRowCount(); i++) {
                inputArray[i][0] = (String) inputModel.getValueAt(i,0);
                inputArray[i][1] = (String) inputModel.getValueAt(i,arrayIndex);
            }
            inputArray = sortHighestToLowest(inputArray);

            DefaultTableModel outputModel = (DefaultTableModel) outputTable.getModel();
            for(int i = 0; i < inputArray.length; i++) {
                outputModel.setValueAt(inputArray[i][0], i, 1);
                outputModel.setValueAt(inputArray[i][1], i, 2);
            }

            outputLabel.setText("Rankings - " + outputRankings.getSelectedItem());
        });

        save.addActionListener((e) -> {
            DefaultTableModel inputModel = (DefaultTableModel) inputTable.getModel();
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

        outputRankings.addItemListener((e) -> {
            outputLabel.setText("Rankings - Uncalculated");
        });

        inputTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                outputLabel.setText("Rankings - Uncalculated");
            }
        });


        JFrame frame = new JFrame();
        frame.setTitle("SCOUT - v " + version);
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(toolBar, BorderLayout.NORTH);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);

        splitPane.setDividerLocation(frame.getWidth() / 2);
    }

    public String[][] sortHighestToLowest(String[][] array) {
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for(int i = 1; i< array.length; i++) {
                if(Integer.parseInt(array[i-1][1]) < Integer.parseInt(array[i][1])) {
                    String[] tempString = array[i-1];
                    array[i-1] = array[i];
                    array[i] = tempString;
                    sorted = false;
                }
            }
        }

        return array;
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
