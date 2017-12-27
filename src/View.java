import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {

    private class MyPanel extends JPanel {
        Controller controller;

        MyPanel(Controller c) {
            controller = c;
            addMouseListener(c);
        }

        public void paintComponent(Graphics g) {
            int x;
            int y;

            for(int row = 0; row < 8; row++)
            {
                for(int col = 0; col < 8; col++){
                    x = col*80+50;// x location of each square
                    y = row*80+50; // y location of each square
                    // alternates white/black
                    if((row %2) == (col%2))
                        g.setColor(Color.WHITE);
                    else
                        g.setColor(Color.BLACK);

                    g.fillRect(x, y, 80, 80);// fill the square at (x,y) with dimension width/height
                }
            }
            revalidate();
            controller.update(g);
        }
    }

    public View(Controller c) throws Exception{
        setTitle("Checkers!");
        setSize(800,1000); //subject to change

        // creates menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // Define and add two drop down menu to the menubar
        JMenu fileMenu = new JMenu("Options");
        menuBar.add(fileMenu);

        // Create and add simple menu item to one of the drop down menu
        JMenuItem newAction = new JMenuItem("New Game");

        // Create and add Radio Buttons as simple menu items to one of the drop
        // down menu
        JRadioButtonMenuItem radioAction1 = new JRadioButtonMenuItem(
                "Radio Button1");
        JRadioButtonMenuItem radioAction2 = new JRadioButtonMenuItem(
                "Radio Button2");
        // Create a ButtonGroup and add both radio Button to it. Only one radio
        // button in a ButtonGroup can be selected at a time.
        ButtonGroup bg = new ButtonGroup();
        bg.add(radioAction1);
        bg.add(radioAction2);
        fileMenu.add(newAction);

        getContentPane().add(new MyPanel(c));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }


    public void actionPerformed(ActionEvent evt){ repaint(); }

}
