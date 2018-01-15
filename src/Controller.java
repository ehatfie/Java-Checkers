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
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

import static java.awt.SystemColor.menu;
import static java.lang.Math.abs;


public class Controller implements MouseListener {
    Model model;
    View view;
    Sprite selectedSprite;
    boolean hasSprite, playerTurn, again, twoPlayer, moved;
    int index;

    Controller() throws IOException, Exception{
        model = new Model();
        view = new View(this);
        selectedSprite = new Sprite();
        new Timer(10, view).start();
        index = -1;
        playerTurn = true;
        again = false;
        twoPlayer = false;
        moved = false;
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
                    if (checkMove(moveTo, selectedSprite)) {
                        // removes the jumped checker, first is for up right, second up left
                        if (moveTo[1] == selectedSprite.getBlock()[1] + 2)
                            model.removeBlack(model.getBlackSpriteLocation(new int[]{moveTo[0] - 1, moveTo[1] - 1}));
                        else if (moveTo[1] == selectedSprite.getBlock()[1] - 2)
                            model.removeBlack(model.getBlackSpriteLocation(new int[]{moveTo[0] - 1, moveTo[1] + 1}));
                        // if it jumped
                        if (moveTo[1] == selectedSprite.getBlock()[1] + 2 || moveTo[1] == selectedSprite.getBlock()[1] - 2)
                            check = true;
                        model.moveRedSprite(index, moveTo);
                        moved = true;
                        if (check) {
                            again = checkNext();
                            check = false;
                        }
                        if (!again) {
                            hasSprite = false;
                            if (selectedSprite.isKing())
                                selectedSprite.setImage("redKing.png");
                            else
                                selectedSprite.setImage("redTransparent.png");
                            playerTurn = false;
                            again = false;
                            moved = false;
                        }
                    }
                    // if move is not valid
                    else {
                        System.out.println("Try another move");
                        if(checkNext() ==  false && moved == true) {
                            hasSprite = false;
                            if (selectedSprite.isKing())
                                selectedSprite.setImage("redKing.png");
                            else
                                selectedSprite.setImage("redTransparent.png");
                            playerTurn = false;
                            again = false;
                            moved = false;
                        }
                    }
                }
            }
            // black turn
            else{
                if(twoPlayer) {
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
                        if (checkMove(moveTo, selectedSprite)) {
                            if (moveTo[0] == selectedSprite.getBlock()[0] - 2) {
                                if (moveTo[1] == selectedSprite.getBlock()[1] + 2)
                                    model.removeRed(model.getRedSpriteLocation(new int[]{moveTo[0] + 1, moveTo[1] - 1}));
                                else if (moveTo[1] == selectedSprite.getBlock()[1] - 2)
                                    model.removeRed(model.getRedSpriteLocation(new int[]{moveTo[0] + 1, moveTo[1] + 1}));
                            } else if (moveTo[0] == selectedSprite.getBlock()[0] + 2) {
                                if (moveTo[1] == selectedSprite.getBlock()[1] + 2)
                                    model.removeRed(model.getRedSpriteLocation(new int[]{moveTo[0] - 1, moveTo[1] - 1}));
                                else if (moveTo[1] == selectedSprite.getBlock()[1] - 2)
                                    model.removeRed(model.getRedSpriteLocation(new int[]{moveTo[0] - 1, moveTo[1] + 1}));
                            }
                            if (moveTo[1] == selectedSprite.getBlock()[1] + 2 || moveTo[1] == selectedSprite.getBlock()[1] - 2)
                                check = true;
                            model.moveBlackSprite(index, moveTo);
                            moved = true;
                            if (check)
                                again = checkNext();
                            if (!again) {
                                hasSprite = false;
                                if (selectedSprite.isKing())
                                    selectedSprite.setImage("grayKing.png");
                                else
                                    selectedSprite.setImage("grayTransparent.png");
                                playerTurn = true;
                                again = false;
                                moved = false;
                            }
                        }
                        // if move is not valid
                        else {
                            System.out.println("Try another move");
                        }
                    }
                }
                else {
                    aiPlayer();
                    playerTurn = true;
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
                if(model.getRedSpriteLocation(selectedSprite.getBlock()) == 0) {
                    if (selectedSprite.isKing())
                        selectedSprite.setImage("grayKing.png");
                    selectedSprite.setImage("grayTransparent.png");
                    System.out.println("Deselected gray checker");
                }
                else{
                    if(selectedSprite.isKing())
                        selectedSprite.setImage("redKing.png");
                    selectedSprite.setImage("redTransparent.png");
                }
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
        if(playerTurn) // if player turn then temp[0] is one up from currentBlock[0], other way for black turn
            checkY = currentBlock[0] + 1;
        else
            checkY = currentBlock[0] - 1;
        for(int i = 0; i < 2; i++) {
            if (i == 0) {
                checkX = currentBlock[1] + 1; // to the right
            } else if (i == 1) {
                checkX = currentBlock[1] - 1; // to the left
            }
            if(checkX > 0 && checkX < 9) {
                temp[0] = checkY;
                temp[1] = checkX;
                if (playerTurn)
                    loc = model.getBlackSpriteLocation(temp);
                else
                    loc = model.getRedSpriteLocation(temp);
                if (loc >= 0) // if there is an opposing checker to the right or left of it
                    if (checkNext(loc, currentBlock))
                        return true;
            }
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
                    if(checkNext(loc, currentBlock))
                        return true;
            }
        }

        return false;
    }
    public boolean checkNext(int loc, int[] previousLoc){
        Sprite sprite2;
        int[] temp3, currentloc;
        int temp = -1, checkX, checkY;

        if(playerTurn) {
            sprite2 = model.getBlackSprite(loc); // get the sprite that were going to use to check for jumps
            currentloc = sprite2.getBlock(); // gets current block
            temp3 = new int[] {currentloc[0], currentloc[1]}; // puts it in a temporary variable, have been having issues otherwise
            checkY = currentloc[0] + 1;
            // goes through 2 it can jump
            if(checkY <= 8) { // if the block is on the board
                // same direction
                temp = previousLoc[1] - currentloc[1];
                checkX = currentloc[1] + temp;
                    if (checkX < 1 || checkX > 8)// if out of bounds
                        return false;
                    if (model.getBlackSpriteLocation(new int[]{checkY, checkX}) == -1)
                        return true;
                }
            }

        else{
            sprite2 = model.getRedSprite(loc); // sprite being jumped
            currentloc = sprite2.getBlock(); // location of the sprite being jumped
            temp3 = new int[]{currentloc[0], currentloc[1]}; // modifiable array locations
            temp = currentloc[1] - previousLoc[1];
            checkY = currentloc[0] - 1;
            checkX = currentloc[1] + temp;
            if(checkY - 1 >= 1){
                if (checkX > 8 || checkX < 1)// if out of bounds
                    return false;
                if (model.getRedSpriteLocation(new int[]{checkY, checkX}) == -1 )
                    return true;

            }
        }
        return false;
    }
    public boolean checkNext(int[] currentLoc){
        int checkX = 0, checkY = 0, loc;
        checkY = currentLoc[0] - 1;
            if(checkX > 0 && checkX < 9) {
                loc = model.getRedSpriteLocation(new int[]{checkY, checkX});
                if (loc >= 0)
                    if (checkX > 8 || checkX < 1)// if out of bounds
                        return false;
                    if (checkNext(loc, currentLoc))
                        return true;
            }
        return false;
    }
    public void aiPlayer(){
        int loc, checkX, checkY, temp;
        Sprite selected;
        int[] currentLoc;
        ArrayList <int[]> moves = new ArrayList<>();
        for(int i = 0; i < 12; i++) { // goes through all the checkers
            selectedSprite = model.getBlackSprite(i); // grabs one
            if (selectedSprite.isAlive()) { // if the sprite is alive
                currentLoc = new int[]{selectedSprite.getBlock()[0], selectedSprite.getBlock()[1]}; // gets the current location of the sprite
                for (int o = 0; o < 2; o++) {// goes through the possible jumps
                    checkY = currentLoc[0] - 1;
                    if (o == 0)
                        checkX = currentLoc[1] - 1;// to the left
                    else
                        checkX = currentLoc[1] + 1;// to the right
                    if (checkX > 0 && checkX < 9) {// if its on the board
                        if (model.getRedSpriteLocation(new int[]{checkY, checkX}) >= 0 ) { // if there is a red sprite
                            //maybe check if it can jump
                            if (checkNext()) {
                                temp = checkX - currentLoc[1];
                                moves.addAll(findNext(1, currentLoc, new int[]{checkY - 1, checkX + temp} ));
                            }

                        } else if (checkMove(new int[]{checkY, checkX}, selectedSprite) && model.getBlackSpriteLocation(new int[]{checkY, checkX}) == -1) { // if the move is valid
                            // maybe put them all in a list then see which is best
                            moves.add(new int[]{i, checkY, checkX});
                        }
                    }

                }
            }
        }
        int[] moveTo = findBestMove(moves);
        selectedSprite = model.getBlackSprite(moveTo[0]);
        if (moveTo[1] == selectedSprite.getBlock()[0] - 2) {
            if (moveTo[2] == selectedSprite.getBlock()[1] + 2)
                model.removeRed(model.getRedSpriteLocation(new int[]{moveTo[1] + 1, moveTo[2] - 1}));
            else if (moveTo[2] == selectedSprite.getBlock()[1] - 2)
                model.removeRed(model.getRedSpriteLocation(new int[]{moveTo[1] + 1, moveTo[2] + 1}));
        } else if (moveTo[1] == selectedSprite.getBlock()[0] + 2) {
            if (moveTo[2] == selectedSprite.getBlock()[1] + 2)
                model.removeRed(model.getRedSpriteLocation(new int[]{moveTo[1] - 1, moveTo[2] - 1}));
            else if (moveTo[2] == selectedSprite.getBlock()[1] - 2)
                model.removeRed(model.getRedSpriteLocation(new int[]{moveTo[1] - 1, moveTo[2] + 1}));
        }

        model.moveBlackSprite(moveTo[0], new int[]{moveTo[1], moveTo[2]});
        System.out.println();
    }
    public int[] findBestMove(ArrayList<int[]> moves){
        int[] nextMove, currentPos, bestMove = new int[3];
        ArrayList<int[]> bestMoves = new ArrayList<>();
        int distance, maxDistance = 0, temp;
        Sprite sprite;
        for(int i = 0; i < moves.size(); i++) { // maybe a problem here
            sprite = model.getBlackSprite(moves.get(i)[0]); // get the sprite that the move applies to
            nextMove = new int[]{moves.get(i)[1],moves.get(i)[2]};// get the move that it wants to make
            currentPos = sprite.getBlock();// get the current position
            temp = currentPos[1] - nextMove[1];
            if(model.getBlackSpriteLocation(new int[] {nextMove[0], nextMove[1]}) == -1) { // if there isnt a black checker there
                // if its a jump, checking if its jumping over a black checker
                if(temp == 2 || temp == -2){
                    System.out.println();
                    if(model.getBlackSpriteLocation(new int[] {nextMove[0] + 1, nextMove[1] + (temp/2)}) == -1){
                        distance = currentPos[0] - nextMove[0];
                    }
                    else
                        distance = 0;
                }
                else
                    distance = currentPos[0] - nextMove[0];

                if (distance > maxDistance) { // if there is a new "best" move, best move is based on how many jumps atm
                    maxDistance = distance;
                    bestMove = moves.get(i);
                    bestMoves.clear();// empties the old best moves
                } else if (distance == maxDistance) { // if there is a move that has the same best move
                    // check if bestMoves list does not contain the current best move
                    if (!bestMoves.contains(bestMove))
                        bestMoves.add(bestMove);
                    bestMoves.add(moves.get(i));// add best move of equal distance
                    // this is where the min would be found, maybe after its all collected
                }
            }
        }
        if(bestMoves.size() > 0)// if there is multiple equal best moves, currently going to pick a random one
            bestMove = bestMoves.get(new Random().nextInt(bestMoves.size()));

        return bestMove;

       // figure out how to assign some sort of value to the moves
       // maybe how far from the initial spot it is, how many red checkers it will remove.
       // ideally implement it so it sets up jumps but thats hard
    }
    public int maxValue(){
        int max = 0;
        /*
        if terminal(s)
            return utility(best min)
        v = -infinity
        for a,s in successors(state) do
            v = max(v, maxValue(s)) // v = the maximum of current v and the v returned from MaxValue

         return v;
            */

        return max;
    }

    public int minValue(){
        int min = 0;

        /*
        if terminal(s)
            return utility(best min)
         v = +infinity
         for a,s in successors(state) do
            v = min(v, maxValue(s) //v = the minimum of current v and the v returned from minValue
         return v;
         */
        return min;
    }
    public ArrayList<int[]> findNext(int depth, int[] currentLoc, int[] nextLoc) {
        // current loc is the current location of the checker, nextLoc is the position it is jumping to
        ArrayList<int[]> moves = new ArrayList<>();
        int temp = 0;
        int checkY, checkX;
        temp = nextLoc[1] - currentLoc[1]; // temp has the direction from the currentLoc to the jumpLoc
        checkY = nextLoc[0] -1;
        if(checkY > 1) { // if there is still a row that the checker can jump into
            for (int i = 0; i < 2; i++) {
                // check based on the next location, which is where this checker is jumping to. if there is a checker to the left then call checkNext and if it can jump then call findNext again if it cant, add to moves
                if(i == 0)
                    checkX = nextLoc[1] - 1;
                else
                    checkX = nextLoc[1] + 1;
                if(checkX > 0 && checkX < 9) { // if the spot being checked is on the board
                    if(model.getRedSpriteLocation(new int[]{checkY, checkX}) > 0) // if there is a red checker there
                        if(checkNext(nextLoc))
                            findNext(depth++, nextLoc, new int[]{checkY, checkX});
                        else
                            moves.add(new int[]{model.getIndex(selectedSprite), nextLoc[0], nextLoc[1]});
                }
            }
        }
        return moves;
    }
    public static void main(String[] args) throws Exception{
        Menu temp = new Menu();
        Controller test = new Controller();

    }

}
/*
    TODO:
        ai
        game over menu
        King cant jump backwards over something


    IDEAS:
        -   each sprite has its (x,y) block number in it, can only move -1 to x block if it isnt jumping
        -   stack to track the moves
            + Each sprite stays, if its jumped some boolean changes to false
            + that way if you go back
    FEATURES:
        -   An option to show best move
        -   Maybe difficulty settings where when you click the checker you can see possible moves
        -   Timer

    AI Implementation:
        IDEAS
            only have it check the best current move not best possible move
            first have it return best options
            random to select if they are all even
            for the points if it can make it a king then thats the best one
        TODO:
            add ai for king
            int checkY, checkX;
        ArrayList<int[]> moves = new ArrayList<>();
        checkY = currentLocation[0] - depth - 1;

        for (int i = 0; i < 2; i++) {
            if (i == 0)
                checkX = currentLocation[1] - depth - 1;
            else
                checkX = currentLocation[1] - depth + 1;
            if (checkX > 0 && checkX < 9) {
                if (model.getRedSpriteLocation(new int[]{checkY, checkX}) >= 0 && selectedSprite.isAlive() && checkX != currentLocation[1]) {
                    if (checkNext()) {
                        findNext(depth + 1, new int[]{checkY, checkX});
                    }
                } else if(checkX != selectedSprite.getBlock()[1])
                    moves.add(new int[]{model.getIndex(selectedSprite), checkY, checkX});
            }
        }

        return moves;
    }


 */
