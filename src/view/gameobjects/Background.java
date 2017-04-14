package view.gameobjects;

import controller.GameController;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import model.GameData;

/**
* This class renders a background image.
* 
* @author  SATAS
* @version 1.0
* @since   2017-02-18 
*/
public class Background extends RenderableObject {
    
    public enum Stretch {
        NONE, WORLD, VIEWPORT
    }
    
    private Image backgroundImage; //the image
    private double backgroundLocation; //for scroll animation, this is the current step
    private Stretch stretch; //is the images stretched?
    boolean scroll; //should the background be animated to scroll?
    boolean repeat;
    
    /**
    * Constructor for the Background object
    *
    * @param imagePath The relative path to the image file
    * @param stretch Should the image be stretched
    * @param scroll Should the image be animated
    */
    public Background(String imagePath, Stretch stretch, boolean repeat, boolean scroll) {
        super(new Point(0, 0)); //all backgrounds start at 0,0
        
        this.stretch = stretch; //set stretched
        this.scroll = scroll; //set scroll
        this.repeat = repeat;
        
        //initialize offset for animated backgrounds
        backgroundLocation = 0;
        
        //load the image file
        try{
            this.backgroundImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open background.png");
            System.exit(-1);
        }
    }
    
    /**
    * {@inheritDoc}
    */
    @Override
    public void update(){
        //if scroll is true update the location every frame
        if(scroll){
            if(Math.abs(this.backgroundLocation) >= 350) { 
                this.backgroundLocation = 0; 
            }
            else { 
                this.backgroundLocation = this.backgroundLocation - (.5); 
            }  
        }
    }
    
    /**
    * {@inheritDoc}
    */
    @Override
    public void render(Graphics2D g2, Rectangle viewport){
        //set size to the size of the image
        int width = backgroundImage.getWidth(null);
        int height = backgroundImage.getHeight(null);
        
        int x = 0;
        int y = 0;
        

        
        
        //if stretch set size to the size of the viewport
        if(stretch == Stretch.VIEWPORT){
            width = (int)viewport.getWidth();
            height = (int)viewport.getHeight();
        }
        else if(stretch == Stretch.WORLD){
            width = (int)GameData.getInstance().world.getWidth()+50;
            height = (int)GameData.getInstance().world.getHeight()+50;
            //draw in relation to the viewport
            x =  (int)boundingBox.getX() - (int)viewport.getX() - 50;
            y =  (int)boundingBox.getY() - (int)viewport.getY();
        }
        else if(stretch == Stretch.NONE){
            
        }
        
        if(scroll){
            width+=350;
            height+=350;
            x += backgroundLocation;
        }

        //draw the image
        //g2.drawImage(backgroundImage, (int)this.backgroundLocation, 0, width, height, null);
        if(repeat){
            
            int m = 0;
            while(viewport.getX()+viewport.getWidth() > x + width*m){
               g2.drawImage(backgroundImage, x + width*m, y, width, height, null); 
               m++;
            }
            
//            
//            while(m == 1 || viewport.getX()+viewport.getWidth() > x*m + width){
//               g2.drawImage(backgroundImage, x*m, y, width, height, null); 
//               m++;
//               System.out.println("test: "+m);
//            }
        }
        else{
            g2.drawImage(backgroundImage, x, y, width, height, null);
        }
        
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void keyPressed(KeyEvent e) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
}
