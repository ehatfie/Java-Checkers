import javax.swing.JFrame;
import java.awt.*;
import javax.swing.JPanel;
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
        getContentPane().add(new MyPanel(c));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt){ repaint(); }

}
