package gui;

import app.AppCore;
import lombok.Data;
import observer.Notification;
import observer.Subscriber;
import rules.Rule;
import rules.implementation.ForeignKeyRule;
import rules.implementation.SequenceStatementsRule;
import rules.implementation.TablesColumnsRule;
import tree.implementation.SelectionListener;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Data
public class MainFrame extends JFrame implements Subscriber {
    ///////GLOBALNI VIEW, SINGLETON, IMPLEMENTIRA SUBSCRIBER INTERFEJS
    private static MainFrame instance = null;

    private AppCore appCore;        //POKAZIVAC NA SVOJ MODEL
    private JTable jTable;     //TABEELA DESNO
    private JScrollPane jsp;    //SKROLOVANJE
    private JTree jTree;        //STABLO LEVO
    private JPanel left;        //LEVI PANEL ZA STABLO

    private JTextPane textPane;

    private JPanel right;

    private JPanel panelButton;
    private JButton runButton;
    private JButton prettyButton;
    private JButton importButton;
    private JButton exportButton;

    private DefaultTreeModel defaultTreeModel;



    private MainFrame() {

    }

    public static MainFrame getInstance(){
        if (instance==null){
            instance=new MainFrame();
            instance.initialise();
        }
        return instance;
    }


    private void initialise() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelButton = new JPanel(new GridLayout());
        importButton = new JButton("Bulk import");
        exportButton = new JButton("Result Set export");
        prettyButton = new JButton("Pretty");
        runButton = new JButton("Run");

        prettyButton.addActionListener(new PrettyListener());

        exportButton.addActionListener(new ExportListener());

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = textPane.getText();
                appCore.run(query);
            }
        });

        importButton.addActionListener(new BulkListener());

        panelButton.add(importButton);
        panelButton.add(exportButton);
        panelButton.add(prettyButton);
        panelButton.add(runButton);

        textPane = new JTextPane();
        right = new JPanel(new BorderLayout());
        right.add(textPane, BorderLayout.NORTH);

        jTable = new JTable();      //PRAVIMO PRAZNU TABELIU
        jTable.setPreferredScrollableViewportSize(new Dimension(500, 400));
        jTable.setFillsViewportHeight(true);

        right.add(new JScrollPane(jTable), BorderLayout.CENTER);

        this.add(panelButton, BorderLayout.NORTH);
        this.add(right,BorderLayout.CENTER);
        //this.add(new JScrollPane(jTable), BorderLayout.CENTER);  //DODAJEMO SCROLPANE NA KOJI SMESTAMO PRAZNU TABELU

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


    }

    public void setAppCore(AppCore appCore) {
        this.appCore = appCore;     //SETUJEMO APPCORE
        this.appCore.addSubscriber(this);       //VEZUJEMO OBSERVER
        this.jTable.setModel(appCore.getTableModel());  //TABELI SETUJEMO MODEL
        initialiseTree();   //INICIJALIZUJEMO STABLO
    }

    private void initialiseTree() {
        //VRACA MODEL ZA BAZU
        defaultTreeModel = appCore.loadResource();     //KONEKTOVANI SMO NA BAZU I UCITAVAMO PODATKE
        jTree = new JTree(defaultTreeModel);
        jTree.addTreeSelectionListener(new SelectionListener());
        jsp = new JScrollPane(jTree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        left = new JPanel(new BorderLayout());
        left.add(jsp, BorderLayout.CENTER);
        add(left, BorderLayout.WEST);
        pack();
    }


    @Override
    public void update(Notification notification) {


    }
}
