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
    boolean hasSprite, playerTurn, again;
    int index;

    Controller() throws IOException, Exception{
        model = new Model();
        view = new View(this);
        selectedSprite = new Sprite();
        new Timer(10, view).start();
        index = -1;
        playerTurn = true;
        again = false;
    }
    public void update(Graphics g){ model.update(g); }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)){
            //gets here if left mouse button was clicked
            boolean check = false;
            if(playerTurn) {
                // if there is no selected sprite
                if (hasSprite == false) {
                    // get index of the sprite in the array
                    index = model.getRedSpriteLocation(pixelToBlock(e.getX(), e.getY()));
                    // if the index is in the number of sprites
                    if (index >= 0 && index <= 12) {
                        System.out.println("Success");
                        selectedSprite = model.getRedSprite(index);// maybe dont need this, remove at end
                        selectedSprite.setImage("redSelected.png");
                        hasSprite = true; // set the boolean for having a sprite selected
                    }
                }
                // if a sprite is already selected
                else {
                    int[] moveTo = pixelToBlock(e.getX(), e.getY());
                    // if move is valid
                    if(checkMove(moveTo, selectedSprite)){
                        // removes the jumped checker, first is for up right, second up left
                        if(moveTo[1] == selectedSprite.getBlock()[1] + 2)
                            model.removeBlack(model.getBlackSpriteLocation(new int[] {moveTo[0] - 1, moveTo[1] - 1 }));
                        else if (moveTo[1] == selectedSprite.getBlock()[1] - 2)
                            model.removeBlack(model.getBlackSpriteLocation(new int[] {moveTo[0] - 1, moveTo[1] + 1 }));
                        // if it jumped
                        if(moveTo[1] == selectedSprite.getBlock()[1] + 2 || moveTo[1] == selectedSprite.getBlock()[1] - 2)
                            check = true;
                        model.moveRedSprite(index, moveTo);
                        if(check)
                            again = checkNext(moveTo);
                        if(!again) {
                            hasSprite = false;
                            selectedSprite.setImage("redTransparent.png");
                            playerTurn = false;
                        }
                    }
                    // if move is not valid
                    else{
                        System.out.println("Try another move");
                    }
                }
            }
            // black turn

            else{
                // if there is no selected sprite
                if (hasSprite == false) {
                    // get index of the sprite in the array
                    index = model.getBlackSpriteLocation(pixelToBlock(e.getX(), e.getY()));
                    // if the index is in the number of sprites
                    if (index >= 0 && index <= 12) {
                        System.out.println("Success");
                        selectedSprite = model.getBlackSprite(index);// get the sprite so it can be manipulated
                        selectedSprite.setImage("graySelected.png"); // change image to the selected image
                        hasSprite = true; // set the boolean for having a sprite selected
                    }
                }
                // if a sprite is already selected
                else {
                    int[] moveTo = pixelToBlock(e.getX(), e.getY());
                    // if move is valid
                    if(checkMove(moveTo, selectedSprite)){
                        if(moveTo[1] == selectedSprite.getBlock()[1] + 2)
                            model.removeRed(model.getRedSpriteLocation(new int[] {moveTo[0] + 1, moveTo[1] - 1 }));
                        else if (moveTo[1] == selectedSprite.getBlock()[1] - 2)
                            model.removeRed(model.getRedSpriteLocation(new int[] {moveTo[0] + 1, moveTo[1] + 1 }));
                        model.moveBlackSprite(index, moveTo);
                        hasSprite = false;
                        selectedSprite.setImage("grayTransparent.png");
                        playerTurn = true;
                    }
                    // if move is not valid
                    else {
                        System.out.println("Try another move");
                    }
                }

            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            // gets here if right mouse button was clicked
            if(playerTurn){
                hasSprite = false;
                selectedSprite.setImage("redTransparent.png");
                System.out.println("Deselected red checker");
            }
            else{
                hasSprite = false;
                selectedSprite.setImage("grayTransparent.png");
                System.out.println("Deselected gray checker");
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {  }
    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    // converts pixel coordinates to a block
    public int[] pixelToBlock(int x, int y){
        int[] block = new int[2];

        block[0] = block[0] = abs(16-(((y-40) / 80)+8));
        block[1] = ((x + 40) / 80);

        return block;
    }

    // checks if move is valid
    public boolean checkMove(int[] dest, Sprite sprite){
        // player turn
        // if -1 y and +1 or -1 x
        int[] loc = sprite.getBlock(); // current location of the checker
        // for red turn
        if(playerTurn) {
            // if there is a red sprite at the jump location
            if(model.getRedSpriteLocation(dest) >= 0)
                return false;
            else if (loc[0] == dest[0] - 1 && dest[0] > 0 && dest[1] > 0 && dest[1] < 9) {
                if (loc[1] == dest[1] + 1 || loc[1] == dest[1] - 1)
                    return true;
                return false;
            } else if (loc[0] == dest[0] - 2) { // for a jump
                int[] check = new int[2];
                check[0] = dest[0] -1;
                if(dest[1] > loc[1]){ // if the jump is up and right
                    check[1] = dest[1] - 1;
                    // if there is a sprite
                }
                else if(dest[1] < loc[1]){ // if jump is up and left
                    check[1] = dest[1] + 1;
                }
                if(model.getBlackSpriteLocation(check) >= 0)
                    return true;
                /*
                    might be some issues here at some point in the future
                 */
            }
        }
        // for black turn
        else{
            // for a normal move
            if(model.getBlackSpriteLocation(dest) >= 0)
                return false;
            if (loc[0] == dest[0] + 1 && dest[0] > 0 && dest[1] > 0 && dest[1] < 9) {
                if (loc[1] == dest[1] + 1 || loc[1] == dest[1] - 1)
                    return true;
                return false;
            } else if (loc[0] == dest[0] + 2){ // for a jump
                int[] check = new int[2];
                check[0] = dest[0] + 1;
                if(dest[1] > loc[1]){ // if the jump is down and right
                    check[1] = dest[1] - 1;
                    // if there is a sprite
                }
                else if(dest[1] < loc[1]){ // if jump is up and left
                    check[1] = dest[1] + 1;
                }
                if(model.getRedSpriteLocation(check) >= 0)
                    return true;
            }
        }

        return false;
    }
    public boolean checkNext(int[] previousBlock){
        int[] currentBlock = selectedSprite.getBlock();
        int[] temp = new int[2];
        boolean good;
        int loc = -1;
        // maybe 4 if statements for the values of i, each manipulates current block then check against previous
        // check in the 3 locations that are not previous block
        for(int i = 0; i < 2; i++) {
            if (i == 0) {
                temp[0] = currentBlock[0] + 1;
                temp[1] = currentBlock[1] + 1;
            } else if (i == 1) {
                temp[0] = currentBlock[0] + 1;
                temp[1] = currentBlock[1] - 1;
            }
            if (playerTurn) {
                loc = model.getBlackSpriteLocation(temp);
                if (loc >= 0)
                    if (checkNextt(loc, currentBlock))
                        return true;
            }
        }

        return false;
    }
    public boolean checkNextt(int loc, int[] previousBlock){
        Sprite sprite2;
        int[] temp3, curentBlock;
        int temp2 = -1;

        if(playerTurn) {
            sprite2 = model.getBlackSprite(loc);
            curentBlock = sprite2.getBlock();
            temp3 = curentBlock;
            // goes through 2 it can jump
            if(temp3[0] + 1 <=8) {
                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        temp2 = model.getBlackSpriteLocation(new int[]{temp3[0] + 1, temp3[1] + 1});
                    } else if (i == 1) {
                        temp2 = model.getBlackSpriteLocation(new int[]{temp3[0] + 1, temp3[1] - 1});
                    }
                    // of the block being checked isnt the one with the red checker
                    // look for a checker, if one is found good = true meaning the turn is not over
                    if (temp2 == -1) {
                        return true;
                    }

                }
            }
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
        intro menu
        ai
        multi-jump
        game over menu
        turning them into a queen if it makes it to the opposite side

        MOVING:
        -   if its a jump then remove the checker
        -   dont let the user move sideways

    IDEAS:
        -   each sprite has its (x,y) block number in it, can only move -1 to x block if it isnt jumping
        -   stack to track the moves
            + Each sprite stays, if its jumped some boolean changes to false
            + that way if you go back
    FEATURES:
        -   An option to show best move
        -   When you click a checker it lights up
        -   Maybe difficulty settings where when you click the checker you can see possible moves
        -   Timer


 */
