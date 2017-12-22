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
                            again = checkNext();
                        if(!again) {
                            hasSprite = false;
                            if(selectedSprite.isKing())
                                selectedSprite.setImage("redKing.png");
                            else
                                selectedSprite.setImage("redTransparent.png");
                            playerTurn = false;
                            again = false;
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
                        if(moveTo[0] == selectedSprite.getBlock()[0] - 2){
                            if(moveTo[1] == selectedSprite.getBlock()[1] + 2)
                                model.removeRed(model.getRedSpriteLocation(new int[] {moveTo[0] + 1, moveTo[1] - 1 }));
                            else if (moveTo[1] == selectedSprite.getBlock()[1] - 2)
                                model.removeRed(model.getRedSpriteLocation(new int[] {moveTo[0] + 1, moveTo[1] + 1 }));
                        }
                        else if(moveTo[0] == selectedSprite.getBlock()[0] + 2){
                            if(moveTo[1] == selectedSprite.getBlock()[1] + 2)
                                model.removeRed(model.getRedSpriteLocation(new int[] {moveTo[0] - 1, moveTo[1] - 1 }));
                            else if (moveTo[1] == selectedSprite.getBlock()[1] - 2)
                                model.removeRed(model.getRedSpriteLocation(new int[] {moveTo[0] - 1, moveTo[1] + 1 }));
                        }
                        if(moveTo[1] == selectedSprite.getBlock()[1] + 2 || moveTo[1] == selectedSprite.getBlock()[1] - 2)
                            check = true;
                        model.moveBlackSprite(index, moveTo);
                        if(check)
                            again = checkNext();
                        if(!again) {
                            hasSprite = false;
                            if(selectedSprite.isKing())
                                selectedSprite.setImage("grayKing.png");
                            else
                                selectedSprite.setImage("grayTransparent.png");
                            playerTurn = true;
                            again = false;
                        }
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
                if(selectedSprite.isKing())
                    selectedSprite.setImage("redKing.png");
                else
                    selectedSprite.setImage("redTransparent.png");
                System.out.println("Deselected red checker");
            }
            else{
                hasSprite = false;
                if(selectedSprite.isKing())
                    selectedSprite.setImage("grayKing.png");
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
            if (model.getRedSpriteLocation(dest) >= 0 || model.getBlackSpriteLocation(dest) >= 0)
                return false;
            if (dest[0] > 0 && dest[0] < 9 && dest[1] > 0 && dest[1] < 9) { // if movement is on the board
                if (loc[0] == dest[0] - 1 ||(sprite.isKing() && loc[0] == dest[0] + 1)) {
                    if (loc[1] == dest[1] + 1 || loc[1] == dest[1] - 1)
                        return true;
                    return false;
                } else if (sprite.isKing() && loc[0] == dest[0] + 1) {
                    if (loc[1] == dest[1] + 1 || loc[1] == dest[1] - 1)
                        return true;
                    return false;
                } else if (loc[0] == dest[0] - 2 || (sprite.isKing() && loc[0] == dest[0] + 2)) { // for a jump
                    int checkY, checkX = 0;
                    if(loc[0] == dest[0] -2)
                        checkY = dest[0] - 1;
                    else
                        checkY = dest[0] +1;

                    if (dest[1] > loc[1]) // if the jump is to the right
                        checkX = dest[1] - 1;
                    else if (dest[1] < loc[1])  // if jump is to the left
                        checkX = dest[1] + 1;

                    int[] check = new int[]{checkY, checkX};
                    if (model.getBlackSpriteLocation(check) >= 0)
                        return true;
                /*
                    might be some issues here at some point in the future
                 */
                }
            }
        }
        // for black turn
        else {
            // for a normal move
            if (model.getBlackSpriteLocation(dest) >= 0 || model.getRedSpriteLocation(dest) >= 0)
                return false;
            if (dest[0] > 0 && dest[0] < 9 && dest[1] > 0 && dest[1] < 9) { //if jump to location is on the board
                if (loc[0] == dest[0] + 1 || (sprite.isKing() && loc[0] == dest[0] - 1)) {
                    if (loc[1] == dest[1] + 1 || loc[1] == dest[1] - 1)
                        return true;
                    return false;
                } else if (loc[0] == dest[0] + 2 || (sprite.isKing() && loc[0] == dest[0] - 2)) { // for a jump
                    int checkX = 0, checkY;
                    if(loc[0] == dest[0] + 2)
                        checkY = dest[0] + 1;
                    else
                        checkY = dest[0] - 1;

                    if (dest[1] > loc[1])  // if the jump is down and right
                        checkX = dest[1] - 1;
                     else if (dest[1] < loc[1])  // if jump is up and left
                        checkX = dest[1] + 1;
                    int[] check = new int[]{checkY, checkX};
                    if (model.getRedSpriteLocation(check) >= 0)
                        return true;
                }
            }
        }

        return false;
    }
    public boolean checkNext(){
        int[] currentBlock = selectedSprite.getBlock();
        int[] temp = new int[2];
        int checkX = 0, checkY = 0;
        int loc;
        // maybe 4 if statements for the values of i, each manipulates current block then check against previous
        // check in the 3 locations that are not previous block
        for(int i = 0; i < 2; i++) {
            if (i == 0) {
                if(playerTurn) // if player turn then temp[0] is one up from currentBlock[0], other way for black turn
                    checkY = currentBlock[0] + 1;
                else
                    checkY = currentBlock[0] - 1;
                checkX = currentBlock[1] + 1;
            } else if (i == 1) {
                if(playerTurn)
                    checkY = currentBlock[0] + 1;
                else
                    checkY = currentBlock[0] - 1;
                checkX = currentBlock[1] - 1;
            }
            temp[0] = checkY;
            temp[1] = checkX;
            if (playerTurn)
                loc = model.getBlackSpriteLocation(temp);
            else
                loc = model.getRedSpriteLocation(temp);
            if (loc >= 0)
                if (checkNext(loc))
                    return true;
        }
        if(selectedSprite.isKing()){
            for(int i = 0; i < 2; i++){
                if(i == 0) {
                    if (playerTurn)
                        checkY = currentBlock[0] - 1;
                    else
                        checkY = currentBlock[0] + 1;
                }
                else{
                    if(playerTurn)
                        checkY = currentBlock[0] + 1;
                    else
                        checkY = currentBlock[1] + 1;
                }
                temp[0] = checkY;
                temp[1] = checkX;
                if(playerTurn)
                    loc = model.getBlackSpriteLocation(temp);
                else
                    loc = model.getRedSpriteLocation(temp);
                if(loc >=0)
                    if(checkNext(loc))
                        return true;
            }
        }

        return false;
    }
    public boolean checkNext(int loc){
        Sprite sprite2;
        int[] temp3, currentBlock;
        int temp2 = -1;

        if(playerTurn) {
            sprite2 = model.getBlackSprite(loc); // get the sprite that were going to use to check for jumps
            currentBlock = sprite2.getBlock(); // gets current block
            temp3 = new int[] {currentBlock[0], currentBlock[1]}; // puts it in a temporary variable, have been having issues otherwise
            // goes through 2 it can jump
            if(temp3[0] + 1 <= 8) { // if the block is on the board
                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        temp3[0] += 1;
                        temp3[1] += 1;
                    } else if (i == 1) {
                        temp3[0] += 1;
                        temp3[1] -= 1;
                    }
                    if (temp3[1] > 8 || temp3[1] < 1)// if out of bounds
                        return false;
                    if (model.getBlackSpriteLocation(temp3) == -1)
                        return true;
                }

            }
        }
        else{
            sprite2 = model.getRedSprite(loc);
            currentBlock = sprite2.getBlock();
            temp3 = new int[]{currentBlock[0], currentBlock[1]};
            if(temp3[0] - 1 >= 1){
                for(int i = 0; i < 2; i++) {
                    if (i == 0) {
                        temp3[0] -= 1;
                        temp3[1] += 1;
                    } else if (i == 1) {
                        temp3[0] -= 1;
                        temp3[1] -= 1;
                    }
                    if (temp3[1] > 8 || temp3[1] < 1)// if out of bounds
                        return false;
                    if (model.getRedSpriteLocation(temp3) == -1)
                        return true;
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
        implement the king movement

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

        -   Maybe difficulty settings where when you click the checker you can see possible moves
        -   Timer


 */
