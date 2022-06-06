package app;

import database.Database;
import database.DatabaseImplementation;
import database.MYSQLrepository;
import database.settings.Settings;
import database.settings.SettingsImplementation;
import gui.table.TableModel;
import lombok.Getter;
import lombok.Setter;
import observer.Notification;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;
import resource.implementation.InformationResource;
import rules.Checker;
import tree.Tree;
import tree.implementation.TreeImplementation;
import utils.Constants;

import javax.swing.tree.DefaultTreeModel;
import java.util.Stack;

@Getter
@Setter
public class AppCore extends PublisherImplementation {
    ////GLOBALNI MODEL
    private Database database;      //TREBA DA PRISTUPI BAZI PODATAKA
    private Settings settings;      //NASA KLASA KOJA NAM SLUZI DA PROSSLEDIMO IP ADRESA, IME BAZE, USERNAME, PASSWORD
    private TableModel tableModel;      //MODEL ZA TABELU
    private DefaultTreeModel defaultTreeModel;
    private Tree tree;

    private Checker checker;

    public AppCore() {
        this.settings = initSettings();
        ///KACIMO SE NA MYSQL BAZU SA SETTINGS PARAMETRIMA
        this.database = new DatabaseImplementation(new MYSQLrepository(this.settings));
        this.tableModel = new TableModel(); //PRAVIMO PRAZAN TABLE MODEL
        this.tree = new TreeImplementation();   //PRAVIMO PRAZNO STABLO
        this.checker = new Checker();
    }

    private Settings initSettings() {       //INICIJALIZUJE SETTINGS OBJEKAT
        Settings settingsImplementation = new SettingsImplementation();
        settingsImplementation.addParameter("mysql_ip", Constants.MYSQL_IP);
        settingsImplementation.addParameter("mysql_database", Constants.MYSQL_DATABASE);
        settingsImplementation.addParameter("mysql_username", Constants.MYSQL_USERNAME);
        settingsImplementation.addParameter("mysql_password", Constants.MYSQL_PASSWORD);
        return settingsImplementation;
    }


    public DefaultTreeModel loadResource(){
        InformationResource ir = (InformationResource) this.database.loadResource();    //UCITAVA RESURS IZ BAZE
        return this.tree.generateTree(ir);  //PRAVI PO 1 DEFAULT TREE NODE ZA SVAKO DETE OD IR
    }

    public void readDataFromTable(String query, String fromTable){

        //CITA PODATKE ZA DATU TABELU I SETUJE REDOVE U TABLE MODEL
        tableModel.setRows(this.database.readDataFromTable(query, fromTable));

        //Zasto ova linija moze da ostane zakomentarisana?
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }


    public void run(String query){
        Stack<String> stack = checker.check(query);
        if(stack.empty()){
            readDataFromTable(query, "employees");
            System.out.println("Upit dobar stack je prazan!!!!!!!!!!!!!!!!!!!!");
        }else{
            while (!stack.empty()){
                System.out.println(stack.pop());
            }
        }

    }


}
