import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetropolisView extends JFrame {

    protected JTextField metropolisTF, continentTF, populationTF;
    protected static final int TEXT_FIELD_WIDTH = 15;
    protected JButton addBTN, searchBTN;
    protected static final int BUTTON_WIDTH = 8;

    protected JComboBox populationCB, matchCB;
    protected String[] populationOptions = {"Population larger than", "Population smaller than or equal to"};
    protected String[] matchOptions = {"Exact match", "Partial match"};

    protected JTable dataTable;
    protected JScrollPane dataContainer;

    public MetropolisView(MetropolisModel model) {
        super("Metropolis Viewer");

        setLayout(new BorderLayout(5, 5));

        // ###################################################################################### //
        JPanel topPanel = new JPanel();

        topPanel.add(Box.createHorizontalStrut(30));

        topPanel.add(new JLabel("Metropolis:"), BorderLayout.NORTH);
        metropolisTF = new JTextField("", TEXT_FIELD_WIDTH);
        topPanel.add(metropolisTF, BorderLayout.NORTH);

        topPanel.add(new JLabel("Continent:"), BorderLayout.NORTH);
        continentTF = new JTextField("", TEXT_FIELD_WIDTH);
        topPanel.add(continentTF, BorderLayout.NORTH);

        topPanel.add(new JLabel("Population:"), BorderLayout.NORTH);
        populationTF = new JTextField("", TEXT_FIELD_WIDTH);
        topPanel.add(populationTF, BorderLayout.NORTH);

        topPanel.add(Box.createHorizontalStrut(30));

        add(topPanel, BorderLayout.NORTH);

        // ##################################################################################### //

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

        addBTN = new JButton("Add");
        searchBTN = new JButton("Search");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(addBTN);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(searchBTN);
        buttonPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        rightPanel.add(buttonPanel);

        Box optionsBox = Box.createVerticalBox();
        optionsBox.setBorder(new TitledBorder("Search Options"));
        populationCB = new JComboBox(populationOptions);
        matchCB = new JComboBox(matchOptions);
        matchCB.setMaximumSize(new Dimension(matchCB.getMaximumSize().width , matchCB.getMinimumSize().height));
        populationCB.setMaximumSize(new Dimension(matchCB.getMaximumSize().width , matchCB.getMinimumSize().height));
        optionsBox.add(populationCB);
        optionsBox.add(matchCB);
        optionsBox.setAlignmentY(Component.TOP_ALIGNMENT);

        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(optionsBox);

        rightPanel.add(Box.createVerticalStrut(20));

        rightPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        add(rightPanel, BorderLayout.EAST);

        // ################################################################################# //

        dataTable = model.dataTable;
        dataContainer = new JScrollPane(dataTable);
        add(dataContainer, BorderLayout.CENTER);

        // ################################################################################# //
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * Adds a listener to this view.
     * The listener will be notified whenever the user wants to add or look up data
     *
     * @param listener object that will be notified
     * */
    public void addListener(ActionListener listener) {
        addBTN.addActionListener(listener);
        searchBTN.addActionListener(listener);
    }

}
