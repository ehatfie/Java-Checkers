import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.abs;

public class Sprite {

    private String jpgName;
    private int locationX;
    private int locationY;
    private Image image;
    private int[] block = new int[2];

    public Sprite(){
        jpgName = "VOID";
        locationX = 0;
        locationY = 0;
        block[0] = 0; // y
        block[1] = 0; // x
    }

    public Sprite(String picName) {
        setImage(picName);
        locationX = 0;
        locationY = 0;
    }

    public int getX(){  return (block[1]-1)*80+60;   }
    public int getY(){  return (8-block[0])*80+60;   }
    // return block coordinate for sprite
    public int[] getBlock(){  return block;   }
    // set block coordinate for sprite
    public void setBlock(int[] block){ this.block = block; }
    public void setX(int x){ locationX = x; }
    public void setY(int y){ locationY = y;  }
    public void setImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ioe) {
            System.out.println("Unable to load image file");
        }
    }
    public Image getImage(){ return image;  }

    public void update(Graphics g) {
        // moves the sprite
        g.drawImage(getImage(), getX(), getY(), 60,60, null);
        // picture, location for top left, width/height of the icon, idk what observer is

    }
}
/*
    blockl (1,1) is bottom left
 */

