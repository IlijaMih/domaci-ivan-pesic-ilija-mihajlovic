package gui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PrettyListener implements ActionListener {

    private ArrayList<String> keyWords;

    public PrettyListener() {
        this.keyWords = new ArrayList<>();
        keyWords.add("select");
        keyWords.add("from");
        keyWords.add("where");
        keyWords.add("left_join");
        keyWords.add("right_join");
        keyWords.add("join");
        keyWords.add("on");
        keyWords.add("group_by");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JTextPane textPane = MainFrame.getInstance().getTextPane();
        String query = textPane.getText().replaceAll("\n", " ");

        textPane.setText("");

        String splitedText[] = query.split(" ");
        appendText(splitedText, textPane);


        System.out.println(textPane.getText());

    }



    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

    private void appendText(String s[], JTextPane textPane){
        for (int i = 0; i < s.length; i++) {
            if(keyWords.contains(s[i].toLowerCase())){
                if (i > 0){
                    appendToPane(textPane, "\n", Color.BLACK);
                }
                appendToPane(textPane, s[i].toUpperCase(), Color.BLUE);
            }else{
                appendToPane(textPane, s[i], Color.BLACK);
            }
            if(i+1<s.length) {
                appendToPane(textPane, " ", Color.BLACK);
            }
        }
    }
}
