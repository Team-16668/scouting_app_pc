package Main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.TableView;
import java.awt.*;
import java.io.*;

class EventPanel extends JPanel
{
    public JTextField dir = new JTextField();

    private JPanel contentPane;
    private JComboBox choiceBox;
    public JButton saveTeams, loadTeams, saveMatches, loadMatches;
    public JTable teamTable;
    public String[] teamColumnNames;
    public TableCellEditor originalEditor;
    public JPanel fileControlPanel;

    public EventPanel(JPanel panel)
    {
        contentPane = panel;
        setOpaque(true);
        setBackground(new Color(245, 127, 38));

        //Create the Match Table
        String[][] teamData = {
                { "16668", "4-Gear Clovers"},
                { "11115", "Gluten Free"},
                { "8393", "The Giant Diencephalic BRAINSTEM"},
        };

        // Column Names
        teamColumnNames = new String[]{"Team #", "Team Name"};

        // Initializing the JTable

        teamTable = new JTable(new DefaultTableModel(teamData, teamColumnNames));

        JScrollPane teamScrollPane = new JScrollPane(teamTable);

        //The control panel below the input stuff
        JButton addTeam = new JButton("Add Team");
        JButton removeTeam = new JButton("Remove Team");
        JButton chooseLocation = new JButton("Select File Location");
        saveTeams = new JButton("Save Teams");
        loadTeams = new JButton("Load Teams");

        dir = new JTextField();
        dir.setEditable(false);
        dir.setText("No File Location Selected");
        dir.setPreferredSize(new Dimension(200, 50));
        dir.setMaximumSize(dir.getPreferredSize());
        dir.setMinimumSize(dir.getPreferredSize());

        JPanel teamControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        teamControlPanel.add(addTeam);
        teamControlPanel.add(removeTeam);

        fileControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fileControlPanel.add(chooseLocation);
        fileControlPanel.add(dir);
        fileControlPanel.add(saveTeams);
        fileControlPanel.add(loadTeams);

        JLabel teamLabel = new JLabel("Teams");
        teamLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BorderLayout());
        teamPanel.add(teamLabel, BorderLayout.NORTH);
        teamPanel.add(teamScrollPane, BorderLayout.CENTER);
        teamPanel.add(teamControlPanel, BorderLayout.SOUTH);

        //Match Data

        DefaultTableModel ml = new DefaultTableModel(1,5);
        JTable matchTable = new JTable( ml );

        String[] matchColumnNames = { "Team #", "TeleOp Score", "Endgame Score"};

        matchTable.getColumnModel().getColumn(0).setHeaderValue("Match");
        matchTable.getColumnModel().getColumn(1).setHeaderValue("Red 1");
        matchTable.getColumnModel().getColumn(2).setHeaderValue("Red 2");
        matchTable.getColumnModel().getColumn(3).setHeaderValue("Blue 1");
        matchTable.getColumnModel().getColumn(4).setHeaderValue("Blue 2");

        JComboBox teams = new JComboBox(new Object[]{"team 1", "team 2", "team 3"});
        teams.setEditable(false);
        teams.removeAllItems();
        for(int i=0; i< teamTable.getModel().getRowCount(); i++) {
            teams.addItem(teamTable.getModel().getValueAt(i, 0));
        }

        matchTable.getModel().setValueAt("Match 1", 0, 0);
        originalEditor = matchTable.getColumnModel().getColumn(1).getCellEditor();
        matchTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(teams));
        matchTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(teams));
        matchTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(teams));
        matchTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(teams));

        JScrollPane matchScrollPane = new JScrollPane(matchTable);

        //The control panel below the input stuff
        JButton addMatch = new JButton("Add Match");
        JButton removeMatch = new JButton("Remove Match");
        saveMatches = new JButton("Save Matches");
        loadMatches = new JButton("Load Matches");

        JPanel matchControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        matchControlPanel.add(addMatch);
        matchControlPanel.add(removeMatch);
        matchControlPanel.add(saveMatches);
        matchControlPanel.add(loadMatches);

        JLabel matchLabel = new JLabel("Matches");
        matchLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel matchPanel = new JPanel();
        matchPanel.setLayout(new BorderLayout());
        matchPanel.add(matchLabel, BorderLayout.NORTH);
        matchPanel.add(matchScrollPane, BorderLayout.CENTER);
        matchPanel.add(matchControlPanel, BorderLayout.SOUTH);

        JSplitPane eventSplitPane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, teamPanel, matchPanel);
        eventSplitPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        eventSplitPane.setResizeWeight(0.5d);

        setLayout(new BorderLayout());

        add(eventSplitPane, BorderLayout.CENTER);
        add(fileControlPanel, BorderLayout.SOUTH);

        addTeam.addActionListener((e) -> {
            DefaultTableModel teamModel = (DefaultTableModel) teamTable.getModel();
            teamModel.addRow(new String[]{"", ""});
        });

        removeTeam.addActionListener((e) -> {
            DefaultTableModel teamModel = (DefaultTableModel) teamTable.getModel();
            teamModel.removeRow(teamModel.getRowCount()-1);
        });

        chooseLocation.addActionListener((e) -> {
            JFileChooser c = new JFileChooser();
            c.setPreferredSize(new Dimension(800, 600));
            c.setMaximumSize(c.getPreferredSize());
            c.setMinimumSize(c.getPreferredSize());
            // Demonstrate "Save" dialog:
            int rVal = c.showSaveDialog(EventPanel.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                dir.setText(c.getCurrentDirectory().toString());
                System.out.println(dir.getText());
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {

            }
        });

        loadTeams.addActionListener((e) -> {
            BufferedReader reader;
            try {
                DefaultTableModel teamModel = (DefaultTableModel) teamTable.getModel();
                reader = new BufferedReader(new FileReader(
                        dir.getText() + "\\teams.txt"
                ));
                int lines = 0;
                String line = reader.readLine();
                while(line != null) {
                    String name = line.substring(line.indexOf("-")+1);
                    if(teamModel.getRowCount() > lines)  {
                        teamModel.setValueAt(line.substring(0, line.indexOf("-")), lines, 0);
                        teamModel.setValueAt(name, lines, 1);
                    } else {
                        teamModel.addRow(new String[]{line.substring(0, line.indexOf("-")),
                                name});
                    }
                    
                    System.out.println(line);
                    line = reader.readLine();
                    lines++;
                }
                
                reader.close();
                teamTable = new JTable(teamModel);
                //setTeamTable(teamModel);
            } catch(IOException o) {
                o.printStackTrace();
            }
            teams.removeAllItems();
            for(int i=0; i< teamTable.getModel().getRowCount(); i++) {
                teams.addItem(teamTable.getModel().getValueAt(i, 0));
            }
        });

        addMatch.addActionListener((e) -> {

            DefaultTableModel matchModel = (DefaultTableModel) matchTable.getModel();
            String newMatch = "Match " + ((matchModel.getRowCount()) + 1);
            System.out.println(newMatch);


            matchTable.getColumnModel().getColumn(1).setCellEditor(originalEditor);
            matchTable.getColumnModel().getColumn(2).setCellEditor(originalEditor);
            matchTable.getColumnModel().getColumn(3).setCellEditor(originalEditor);
            matchTable.getColumnModel().getColumn(4).setCellEditor(originalEditor);

            matchModel.addRow(new String[]{newMatch, "", "", "", ""});

            matchTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(teams));
            matchTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(teams));
            matchTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(teams));
            matchTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(teams));


        });

        saveMatches.addActionListener((e) -> {

        });
    }

    public void setTeamTable(DefaultTableModel model) {teamTable = new JTable(model); }

}