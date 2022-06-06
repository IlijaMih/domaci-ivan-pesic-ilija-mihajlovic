package gui;

import resource.data.Row;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class ExportListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {


        String query = MainFrame.getInstance().getTextPane().getText();
        List<Row> rows = MainFrame.getInstance().getAppCore().getDatabase().readDataFromTable(query, "employees");

        File file = new File("/Users/ilijamihajlovic/Desktop/file8.csv");
        PrintWriter printWriter;
        try {
             printWriter = new PrintWriter(file);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        for (Row row:rows){
            printWriter.println(row.printMap());
        }

        printWriter.close();
    }
}
