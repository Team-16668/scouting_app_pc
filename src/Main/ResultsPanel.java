package Main;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ResultsPanel extends JPanel
{
    private JButton jcomp4;
    private JPanel contentPane;
    private JComboBox choiceBox;
    public JSplitPane homeSplitPane;
    public JTable inputTable;

    public ResultsPanel(JPanel panel)
    {
        contentPane = panel;
        setOpaque(true);
        setBackground(Color.RED.darker().darker());

        //Create the Input Table
        String[][] inputData = {
                { "16668", "250", "45" },
                { "11115", "2000", "95" },
                { "8393", "350", "120" },
        };

        // Column Names
        String[] inputColumnNames = { "Team #", "TeleOp Score", "Endgame Score"};

        // Initializing the JTable

        inputTable = new JTable(new DefaultTableModel(inputData, inputColumnNames));

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

        homeSplitPane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel);
        homeSplitPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add(homeSplitPane);

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



        outputRankings.addItemListener((e) -> {
            outputLabel.setText("Rankings - Uncalculated");
        });

        inputTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                outputLabel.setText("Rankings - Uncalculated");
            }
        });
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

    @Override
    public Dimension getPreferredSize()
    {
        return (new Dimension(500, 500));
    }
}