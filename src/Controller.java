/*
    Started 11/25/2017
    Written by: Erik Hatfield
    email: ehhatfield@gmail.com
 */

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

import static java.lang.Math.abs;


public class Controller implements MouseListener {
    Model model;
    View view;
    Sprite selectedSprite;
    boolean hasSprite, playerTurn;
    int index;

    Controller() throws IOException, Exception{
        model = new Model();
        view = new View(this);
        selectedSprite = new Sprite();
        new Timer(10, view).start();
        index = -1;
        playerTurn = true;
    }
    public void update(Graphics g){ model.update(g); }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)){
            //gets here if left mouse button was clicked
            if(playerTurn) {
                // if there is no selected sprite
                if (hasSprite == false) {
                    // get index of the sprite in the array
                    index = model.getRedSpriteLocation(e.getX(), e.getY());
                    // if the index is in the number of sprites
                    if (index >= 0 && index <= 16) {
                        System.out.println("Success");
                        selectedSprite = model.getRedSprite(index);// maybe dont need this
                        hasSprite = true; // set the boolean for having a sprite selected
                    }

                }
                // if a sprite is already selected
                else {
                    int[] moveTo = pixelToBlock(e.getX(), e.getY());

                    if(checkMove(moveTo, selectedSprite)){
                        model.moveRedSprite(index, moveTo);
                        hasSprite = false;
                        playerTurn = false;
                    }
                   //
                }
            }
            // black turn
            else{
                // if there is no selected sprite
                if (hasSprite == false) {
                    // get index of the sprite in the array
                    index = model.getBlackSpriteLocation(e.getX(), e.getY());
                    // if the index is in the number of sprites
                    if (index >= 0 && index <= 16) {
                        System.out.println("Success");
                        selectedSprite = model.getBlackSprite(index);// maybe dont need this
                        hasSprite = true; // set the boolean for having a sprite selected
                    }
                }
                // if a sprite is already selected
                else {
                    // only lets the checker move one spot in front, need to add something for if its jumping enemy
                    if (e.getY() > model.getBlackSprite(index).getY() + 80
                            && e.getY() < model.getBlackSprite(index).getY()+160) {
                        model.moveBlackSprite(index, e.getX(), e.getY()); // move the sprite to the new location
                        hasSprite = false;
                        playerTurn = true;
                    }
                }

            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            // gets here if right mouse button was clicked
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {   }
    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    // converts pixel coordinates to a block
    public int[] pixelToBlock(int x, int y){
        int[] block = new int[2];

        block[0] = block[0] = abs(16-(((y-40) / 80)+8));
        block[1] = (x / 80);

        return block;
    }

    // checks if move is valid
    public boolean checkMove(int[] dest, Sprite sprite){
        // player turn
        // if -1 y and +1 or -1 x
        int[] loc = sprite.getBlock();
        if(loc[0] == dest[0] - 1 && dest[0] > 0 && dest[1] > 0 && dest[1] < 9) {
            if(loc[1] == dest[1]+ 1 || loc[1] == dest[1] -1)
                return true;

                return false;
        }
        else if (loc[0] == dest[0] +2)
        {

        }
        return false;
    }
    public static void main(String[] args) throws Exception{

        Controller test = new Controller();
        //new Controller();

        System.out.println("");
    }
}
/*
    TODO:

        MOVING:
        -   if its a jump then remove the checker
        -   dont let the user move sideways


    IDEAS:
        -   each sprite has its (x,y) block number in it, can only move -1 to x block if it isnt jumping
        -   stack to track the moves
    FEATURES:
        -   An option to show best move
        -   When you click a checker it lights up
        -   Maybe difficulty settings where when you click the checker you can see possible moves
        -   Timer


 */
