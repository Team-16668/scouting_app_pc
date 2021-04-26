package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

class EventPanel extends JPanel
{
    public JTextField dir = new JTextField();

    private JPanel contentPane;
    private JComboBox choiceBox;
    public JButton saveTeams, loadTeams;
    public JTable teamTable;
    public String[] teamColumnNames;

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
        teamControlPanel.add(chooseLocation);

        JPanel teamControlPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        teamControlPanel2.add(dir);
        teamControlPanel2.add(saveTeams);
        teamControlPanel2.add(loadTeams);

        JLabel teamLabel = new JLabel("Teams");
        teamLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BoxLayout(teamPanel, BoxLayout.PAGE_AXIS));
        teamPanel.add(teamLabel);
        teamPanel.add(teamScrollPane);
        teamPanel.add(teamControlPanel);
        teamPanel.add(teamControlPanel2);

        //Create the Match Table
        String[][] matchData = {
                { "16668", "250", "45" },
                { "11115", "2000", "95" },
                { "8393", "350", "120" },
        };

        // Column Names
        String[] matchColumnNames = { "Team #", "TeleOp Score", "Endgame Score"};

        // Initializing the JTable

        JTable matchTable = new JTable(new DefaultTableModel(matchData, matchColumnNames));

        JScrollPane matchScrollPane = new JScrollPane(matchTable);

        //The control panel below the input stuff
        JButton addMatch = new JButton("Add Match");
        JButton removeMatch = new JButton("Remove Match");

        JPanel matchControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        matchControlPanel.add(addMatch);
        matchControlPanel.add(removeMatch);

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

        add(eventSplitPane);

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
        });
    }

    public void setTeamTable(DefaultTableModel model) {teamTable = new JTable(model); }

}