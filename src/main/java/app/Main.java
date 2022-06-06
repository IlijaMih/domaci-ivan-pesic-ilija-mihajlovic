package app;

import gui.MainFrame;
import resource.data.Row;

import javax.swing.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AppCore appCore = new AppCore();            ///GLOBALNI MODEEL
        MainFrame mainFrame = MainFrame.getInstance();      //GLOBALNI VIEW
        mainFrame.setAppCore(appCore);

    }

}
