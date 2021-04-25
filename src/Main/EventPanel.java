package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

class EventPanel extends JPanel
{
    private JTextField filename = new JTextField(), dir = new JTextField();

    private JPanel contentPane;
    private JComboBox choiceBox;
    public JButton saveTeams, loadTeams;

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
        String[] teamColumnNames = { "Team #", "Team Name"};

        // Initializing the JTable

        JTable teamTable = new JTable(new DefaultTableModel(teamData, teamColumnNames));

        JScrollPane teamScrollPane = new JScrollPane(teamTable);

        //The control panel below the input stuff
        JButton addTeam = new JButton("Add Team");
        JButton removeTeam = new JButton("Remove Team");
        saveTeams = new JButton("Save Teams");
        loadTeams = new JButton("Load Teams");

        JPanel teamControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        teamControlPanel.add(addTeam);
        teamControlPanel.add(removeTeam);
        teamControlPanel.add(saveTeams);
        teamControlPanel.add(loadTeams);

        JLabel teamLabel = new JLabel("Teams");
        teamLabel.setHorizontalAlignment(SwingConstants.CENTER);

        filename = new JTextField();
        filename.setEditable(false);
        filename.setPreferredSize(new Dimension(200, 50));
        filename.setMinimumSize(filename.getPreferredSize());
        filename.setMaximumSize(filename.getPreferredSize());

        dir = new JTextField();
        dir.setEditable(false);
        dir.setPreferredSize(new Dimension(200, 50));
        dir.setMaximumSize(dir.getPreferredSize());
        dir.setMinimumSize(dir.getPreferredSize());

        JTable fileTable = new JTable();


        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BoxLayout(teamPanel, BoxLayout.PAGE_AXIS));
        teamPanel.add(teamLabel);
        teamPanel.add(teamScrollPane);
        teamPanel.add(teamControlPanel);
        teamPanel.add(filename);
        teamPanel.add(dir);

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
        eventSplitPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        eventSplitPane.setPreferredSize(new Dimension(1800, 950));
        eventSplitPane.setMaximumSize(eventSplitPane.getPreferredSize());
        eventSplitPane.setMinimumSize(eventSplitPane.getPreferredSize());
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

        saveTeams.addActionListener((e) -> {
            JFileChooser c = new JFileChooser();
            c.setPreferredSize(new Dimension(800, 600));
            c.setMaximumSize(c.getPreferredSize());
            c.setMinimumSize(c.getPreferredSize());
            // Demonstrate "Save" dialog:
            int rVal = c.showSaveDialog(EventPanel.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename.setText(c.getSelectedFile().getName());
                dir.setText(c.getCurrentDirectory().toString());
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                filename.setText("You pressed cancel");
                dir.setText("");
            }
        });
    }
}