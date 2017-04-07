package view.gameobjects;

import controller.PhysicsController;
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
import model.ID;

/**
* Render a hero object to the screen.
*
* @author  SATAS
* @version 1.0
* @since   2017-02-18 
*/
public class EarthSmallEnemy extends RenderableObject
{
    
    private Image enemyImage;

    private PhysicsController pyc = new PhysicsController(this);
    boolean directionLeft = true;
    
    /**
    * Constructor 
    * 
    * @param loc
    * @param width
    * @param height
    */
    public EarthSmallEnemy(Point loc, ID id)
    {
        //call superclass constructor
        super(loc);
        this.id = id;
        //update bounding box for the object
        super.boundingBox = new Rectangle(loc.x, loc.y, 32, 83);
        
        enemyImage = null;
        try {
            enemyImage = ImageIO.read(getClass().getResource("/Images/zombie1_walk0.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open small enemy.png");
            System.exit(-1);
        }
    }
    
    public void translate(int dx, int dy){
        
        //Make sure we can actually move by creating a testrect object and trying it first
        
        //the location to move to
        Point newLoc = new Point(boundingBox.x+dx, boundingBox.y + dy); 
        
        //the location for testing, subject 1 from y so we dont count ground
        Point testLoc = new Point(boundingBox.x+dx, boundingBox.y + dy - 1);
        
        //build testRect
        Rectangle testRect = new Rectangle(boundingBox.width, boundingBox.height);
        testRect.setLocation(testLoc);
        
        //if test rect was successful its safe to move the real object
        if(!GameData.getInstance().checkCollision(testRect, this)){
            boundingBox.setLocation(newLoc);
        }
    }
    
    /**
    * {@inheritDoc}
    */
    @Override
    public void update()
    {
        if(!pyc.canMove(PhysicsController.DIRECTION.LEFT))
            directionLeft = false;
        if(!pyc.canMove(PhysicsController.DIRECTION.RIGHT))
            directionLeft = true;
        
        if(directionLeft)pyc.addForce(PhysicsController.DIRECTION.LEFT, 2);
        else pyc.addForce(PhysicsController.DIRECTION.RIGHT, 2);
        
        //update physics controller
        pyc.update();
       
        //get translation from physics controller
        Point.Double p = pyc.getNextTranslation();
        
        //translate object
        if(p.x >0){
            for(int i=0; i<p.x; i++){
                translate(1, 0);
            }
        }
        else if(p.x < 0){
            for(int i=0; i<-p.x; i++){
                translate(-1, 0);
            }
        }
        if(p.y >0){
            for(int i=0; i<p.y; i++){
                translate(0, 1);
            }
        }
        else if(p.y < 0){
            for(int i=0; i<-p.y; i++){
                translate(0, -1);
            }
        }
        super.collide();
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        boundingBox.x = boundingBox.x + 5;
    }
    
    /**
    * {@inheritDoc}
    */
    @Override
    public void render(Graphics2D g2, Rectangle viewport)
    {
        if(viewport.contains(boundingBox.getLocation()))
        {
            int translatedX =  (int)boundingBox.getX() - (int)viewport.getX();
            int translatedY =  (int)boundingBox.getY() - (int)viewport.getY();

            int border = 2;
            g2.drawImage(enemyImage, translatedX, translatedY, (int)boundingBox.getWidth()-border*2, (int)boundingBox.getHeight()-border*2, null); 
        }
        
    }

   
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
