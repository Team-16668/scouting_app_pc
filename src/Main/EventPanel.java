package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class EventPanel extends JPanel
{
    private JPanel contentPane;
    private JComboBox choiceBox;

    public EventPanel(JPanel panel)
    {
        contentPane = panel;
        setOpaque(true);
        setBackground(Color.RED.darker().darker());

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
        JButton saveTeams = new JButton("Save Teams");
        JButton loadTeams = new JButton("Load Teams");

        JPanel teamControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        teamControlPanel.add(addTeam);
        teamControlPanel.add(removeTeam);
        teamControlPanel.add(saveTeams);
        teamControlPanel.add(loadTeams);

        JLabel teamLabel = new JLabel("Teams");
        teamLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BorderLayout());
        teamPanel.add(teamLabel, BorderLayout.NORTH);
        teamPanel.add(teamScrollPane, BorderLayout.CENTER);
        teamPanel.add(teamControlPanel, BorderLayout.SOUTH);

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

        add(eventSplitPane);


    }

    @Override
    public Dimension getPreferredSize()
    {
        return (new Dimension(500, 500));
    }
}