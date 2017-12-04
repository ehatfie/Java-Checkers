import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.abs;


public class Model {
    private Sprite sprite;
    private ArrayList<Sprite> black = new ArrayList<>();
    private ArrayList<Sprite> red = new ArrayList<>();
    private ArrayList<Sprite> sprites = new ArrayList();

    Model()throws IOException {
        for(int i = 0; i < 16; i++){
            red.add(new Sprite("redTransparent.png"));
            black.add(new Sprite("grayTransparent.png"));
        }
        placeRed(red);
        placeBlack(black);


    }

    public void update(Graphics g){
        for(int i = 0; i < black.size(); i++) {
            red.get(i).update(g);
            black.get(i).update(g);
        }
    }
    // initializes black sprites
    public void placeBlack(ArrayList<Sprite> black) {
        // j is the row, 0 is top row, 1 is second from top, 2 is third,
        // this helps the placement of the checkers
        for (int i = 0; i < black.size(); i++) {
            // for first and third row
            if (i < 4 || i >=8) {
                if(i < 4) // for the first set of checkers
                    black.get(i).setBlock(pixelToBlock((i % 4) * 160 + 140, 60));// sets block location
                else // for the second set of checkers
                    black.get(i).setBlock(pixelToBlock((i % 4) * 160 + 140, 220));
            }
            // for the second row
            else{
                black.get(i).setBlock(pixelToBlock((i % 4) * 160 + 50, 140));
            }
        }
    }
    // initializes red sprites
    public void placeRed(ArrayList<Sprite> red){
        for (int i = 0; i < red.size(); i++){
            // first and third row
            if( i < 4 || i >=8) {
                if (i < 4) // first row
                    red.get(i).setBlock(pixelToBlock((i % 4) * 160 + 50, 460));
                else // third row
                    red.get(i).setBlock(pixelToBlock((i % 4) * 160 + 50, 620));
            }
            else
                red.get(i).setBlock(pixelToBlock((i % 4) * 160 + 140, 540));

        }
    }

    // gets the location in the sprite array of the sprite at the x/y click location
    public int getRedSpriteLocation(int x, int y) {
        Sprite temp;
        int xLoc, yLoc;
        for(int i = 0; i < red.size(); i++){
            temp = red.get(i);
            xLoc = temp.getX();
            yLoc = temp.getY();
            if(x < xLoc + 80 && x > xLoc ){
                if(y < yLoc + 80 && y > yLoc){
                    return i;
                }
            }
        }
        // if there is no sprite at the click location return a -1
        return -1;
    }

    // returns the index of the sprite located at the coordinates
    public int getBlackSpriteLocation(int x, int y) {
        Sprite temp;
        int xLoc, yLoc;
        for(int i = 0; i < black.size(); i++){
            temp = black.get(i);
            xLoc = temp.getX();
            yLoc = temp.getY();
            if(x < xLoc + 80 && x > xLoc ){
                if(y < yLoc + 80 && y > yLoc){
                    return i;
                }
            }
        }
        // if there is no sprite at the click location return a -1
        return -1;
    }
    // returns red sprite object
    public Sprite getRedSprite(int loc){ return red.get(loc); }
    // returns black sprite object
    public Sprite getBlackSprite(int loc){ return black.get(loc); }
    // moves red checkers
    public void moveRedSprite(int index, int[] destination){

        red.get(index).setBlock(destination);

        System.out.println("Moved Red");
    }
    // moves black checkers
    public void moveBlackSprite(int index, int x, int y){
        int blockX = (x-50)/80;
        int blockY = (y-50)/80;

        black.get(index).setY(blockY*80 + 60);
        black.get(index).setX(blockX*80 + 60);
        System.out.println("Moved Black");
    }
    // turns pixel locations into block locations
    public int[] pixelToBlock(int x, int y){
        int[] block = new int[2];

        block[0] = block[0] = abs(16-((y / 80)+8));
        block[1] = ((x+50) / 80);

        return block;
    }
}
