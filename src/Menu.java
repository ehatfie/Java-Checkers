import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
public class Menu extends JFrame implements ActionListener {
    private class MyPanel extends JPanel{

    }
    public Menu(){
        setTitle("test");
        setSize(150,150);
        Object[] options = {"One-Player", "Two-Player"};
        JFrame frame = new JFrame();
        // creates menu bar
        int n = JOptionPane.showOptionDialog(frame, "Which mode would you like to play?", "menu",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

    }
    public void actionPerformed(ActionEvent evt){
        repaint();
    }
}
