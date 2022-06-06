package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BulkListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = MainFrame.getInstance().getTextPane().getText();

        System.out.println(s);
        MainFrame.getInstance().getAppCore().getDatabase().importData(s);
    }
}
