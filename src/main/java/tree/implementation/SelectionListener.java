package tree.implementation;

import gui.MainFrame;
import resource.implementation.Entity;
import tree.TreeItem;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class SelectionListener implements TreeSelectionListener {
    @Override               ///KADA PRITISNEMO NA CVOR U STABLU DA ZNAKOJU TABEELU DA VRATI
    public void valueChanged(TreeSelectionEvent e) {

        JTree tree = (JTree) e.getSource();
        TreeItem node = (TreeItem) tree.getLastSelectedPathComponent();
        /* if nothing is selected */
        if (node == null || !(node.getDbNode() instanceof Entity)) return;

        String query = "SELECT * FROM "+ node.getName();

        MainFrame.getInstance().getAppCore().readDataFromTable(query, node.getName());
        //KADA SMO KLIKNULI NA NEKI CVOR U STABLU, POZIVA SE readDataFromTable()
        // KOJA CITA PODATKE IZ TABELE SA DATIM IMENOM

    }
}
