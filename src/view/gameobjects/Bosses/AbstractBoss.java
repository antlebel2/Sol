package view.gameobjects.Bosses;

import controller.AnimationController;
import controller.Main;
import controller.PhysicsController;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import model.GameData;
import view.gameobjects.Hero;
import view.gameobjects.PrimaryWeapon;
import view.gameobjects.RenderableObject;
import view.gameobjects.Weapon;
import view.swingcomponents.VictoryScreen;
import view.swingcomponents.MainWindow;

/**
* Render a hero object to the screen.
*
* @author  SATAS
* @version 1.0
* @since   2017-02-18 
*/
public abstract class AbstractBoss extends RenderableObject {
    
    private Rectangle viewportMain;
    private float health = 0;
    private float displayHealth =0;
    private float blueValue = 255;
    private int healthPacks =0;
    static boolean movingLeft = false;
    static boolean movingRight = false;
    static boolean movingUp = false;
    static boolean movingDown = false;
    static boolean facingRight;
    
    private boolean alive;
    
    private PhysicsController pyc;
    private AnimationController animationController;
    
    private boolean renderHealthBar;
    
    private String leftWalkSpriteSheet;
    private String rightWalkSpriteSheet;
    private String leftJumpSpriteSheet;
    private String rightJumpSpriteSheet;
    
    
    /**
    * Constructor 
    * 
    * @param loc
    */
    public AbstractBoss(Point loc, String leftWalkSpriteSheet, String rightWalkSpriteSheet) {
        //call superclass constructor
        super(loc);
        facingRight = true;
        
        this.leftWalkSpriteSheet = leftWalkSpriteSheet;
        this.rightWalkSpriteSheet = rightWalkSpriteSheet;
        this.leftJumpSpriteSheet = leftWalkSpriteSheet;
        this.rightJumpSpriteSheet = rightWalkSpriteSheet;
        
        
        animationController = new AnimationController(AnimationController.Mode.AUTO, leftWalkSpriteSheet);
        animationController.setFps(48);
        
        
        pyc = new PhysicsController(this);
        
        
        
        
        renderHealthBar = false;
        health = 100;
        displayHealth = 100;
        alive = true;
        
        //update bounding box for the object
        super.boundingBox = new Rectangle(loc.x, loc.y, animationController.getFrame().getWidth(), animationController.getFrame().getHeight());
    }
    
    public AnimationController getAnimationController(){
        return animationController;
    }
    
    
    public void die(){
        alive = false;
        AnimationController.explosionEffect(new Point((int)getBoundingBox().getCenterX(), (int)getBoundingBox().getCenterY()));
        GameData.getInstance().removeGameObject(this);
        
            //Thread.sleep(4000);
        VictoryScreen dialogMenu = new VictoryScreen(MainWindow.getInstance(), false);
        int parentX = MainWindow.getInstance().getX();
        int parentY = MainWindow.getInstance().getY();
        int parentWidth = MainWindow.getInstance().getWidth();
        int parentHeight = MainWindow.getInstance().getHeight();
        dialogMenu.setLocation(parentX + parentWidth / 2 - dialogMenu.getWidth() / 2, parentY + parentHeight / 2 - dialogMenu.getHeight() / 2);
        dialogMenu.getContentPane().setBackground(Color.BLACK);
        dialogMenu.setResizable(true);
        dialogMenu.setAlwaysOnTop(true);
        dialogMenu.setVisible(true);
        
    }
    
    /**
    * {@inheritDoc}
    */
    @Override
    public void update(){
        super.update();
        healthBound();     
      
        
        if(alive = true && health <= 0){
            die();
            return;
        }
      
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
        
        //check collisions
        ArrayList<RenderableObject> collisions = GameData.getInstance().getCollisions(this);
        for(int i=0; i< collisions.size(); i++){
            RenderableObject obj = collisions.get(i);
            if(obj instanceof Hero){
                ((Hero)obj).setShield(-1);
            }
            else if(obj instanceof PrimaryWeapon || obj instanceof Weapon){
                this.setShield(-10);
                //GameData.getInstance().removeGameObject(obj);
                System.out.println("hit!");
            }
        }
    }
    
    /**
    * This method will move the object
    * 
    * @param dx the change in the X location of the object
    * @param dy the change in the Y location of the object
    */
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
            if(dx == -1){
                this.movingLeft = true;
            }
            else if(dx == 1){
                this.movingRight = true;
            }
            if(dy == -1){
                this.movingUp = true;
            }
            else if(dy == 1){
                this.movingDown = true;
            }
            boundingBox.setLocation(newLoc);
        }
    }
    
    
    /**
    * {@inheritDoc}
    */
    @Override
    public void render(Graphics2D g2,Rectangle viewport)
    {
        if(viewport.intersects(this.getBoundingBox())){
            
        
            //draw in relation to the viewport
            int translatedX =  (int)boundingBox.getX() - (int)viewport.getX();
            int translatedY =  (int)boundingBox.getY() - (int)viewport.getY();
            
            Rectangle boundingBoxForRendering = new Rectangle(translatedX-166/3, translatedY, boundingBox.width, boundingBox.height);
            
            if(movingUp){
                if(facingRight){
                    animationController.setSpriteSheet(rightJumpSpriteSheet);
                    
                    if(animationController.getIndex() == 4){
                        BufferedImage sprite = animationController.getFrame();
                        g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);                 
                        movingUp = false;                              
                    }
                    else{
                        BufferedImage sprite = animationController.getFrame();
                        g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);                 
                        movingUp = false;                
                        animationController.update();                           
                    }                  
                }
                else{
                    animationController.setSpriteSheet(leftJumpSpriteSheet);
                    
                    if(animationController.getIndex() == 4){
                        BufferedImage sprite = animationController.getFrame();
                        g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);                 
                        movingUp = false;                              
                    }
                    else{
                        BufferedImage sprite = animationController.getFrame();
                        g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);                 
                        movingUp = false;                
                        animationController.update();                           
                    }                     
                }
            }
            else if(movingDown){
                if(facingRight){
                    animationController.setSpriteSheet(rightJumpSpriteSheet);
                    
                    if(animationController.getIndex() == 23){
                        BufferedImage sprite = animationController.getFrame();
                        g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);                 
                        movingDown = false;                              
                    }
                    else{
                        BufferedImage sprite = animationController.getFrame();
                        g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);                 
                        movingDown = false;                
                        animationController.update();                           
                    }                    
                }
                else{
                    animationController.setSpriteSheet(leftJumpSpriteSheet);
                    
                    if(animationController.getIndex() == 23){
                        BufferedImage sprite = animationController.getFrame();
                        g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);                 
                        movingDown = false;                              
                    }
                    else{
                        BufferedImage sprite = animationController.getFrame();
                        g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);                 
                        movingDown = false;                
                        animationController.update();                           
                    }                    
                }                
            }
            else if(movingRight){
                facingRight = true;
                
                animationController.setSpriteSheet(rightWalkSpriteSheet);
                BufferedImage sprite = animationController.getFrame();
                g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null); 
                
                movingRight = false;
                
                animationController.update();
            }
            else if(movingLeft){
                facingRight = false;

                animationController.setSpriteSheet(leftWalkSpriteSheet);
                BufferedImage sprite = animationController.getFrame();
                
                g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null); 
                movingLeft = false;
                
                animationController.update();
            }            
            else{
                animationController.setFrame(0);
                
                if(facingRight){
                    animationController.setSpriteSheet(rightWalkSpriteSheet);
                    BufferedImage sprite = animationController.getFrame();
                    g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);  
                }
                else{
                    animationController.setSpriteSheet(leftWalkSpriteSheet);
                    BufferedImage sprite = animationController.getFrame();
                    g2.drawImage(sprite, boundingBoxForRendering.x, boundingBoxForRendering.y, (int)boundingBoxForRendering.getWidth(), (int)boundingBoxForRendering.getHeight(), null);
                }                      
            }
            
            if(Main.debug){
                g2.setColor(Color.red);
                g2.drawRect(boundingBoxForRendering.x, boundingBoxForRendering.y, boundingBoxForRendering.width, boundingBoxForRendering.height);

                g2.setColor(Color.yellow);
                g2.drawRect(translatedX, translatedY, boundingBox.width, boundingBox.height);
            }

            
        renderHealthBar = true;    
        
//        for(int i=0; i<healthPacks-1; i++){
//         //g2.setColor(Color.red);
//         g2.setColor(new Color(10,50,(int)blueValue)); //the rectangles below the health bar have same color as health bar
//         g2.fillRect(1, 22, 10, 15);
//         if(healthPacks > 1 && i>0){
//             g2.fillRect((2*i)*7, 22, 10, 15);
//         }
//        }

        }
        
        
        if(renderHealthBar){
            float tempHealth = displayHealth;
            if(tempHealth > 100) tempHealth =100;

            int healthBarX = (int)(viewport.width-2-(100 * 2.5));
            g2.setColor(Color.darkGray);
            g2.fillRect(healthBarX,5,(int)(100 * 2.5), 15);
            g2.setColor(new Color(10,50,(int)blueValue));
            g2.fillRect(healthBarX, 5, (int) (tempHealth * 2.5), 15);
            g2.setColor(Color.white);
            g2.drawRect(healthBarX,5,(int)(100 *2.5), 15);
        }
    }

    private void healthBound() {
        //the health is depleated constatntly but just as a demo. will be changed when there are enemies in the game
        if(displayHealth > 100){
            displayHealth = 100;
        }
        if(displayHealth <=0 && health>0){
            health -= 100;
            displayHealth = health;
        }
        blueValue = displayHealth*5;
        if(health <=0){
            health =0;
        }
        if(blueValue > 255){
            blueValue = 255;
        }
        if(blueValue < 75){
            blueValue =75;
        }
        
        healthPacks = (int)health/100;
    }
    
    public float getShield(){
        return displayHealth;
    }
    public void setShield(float powerUp){
        this.displayHealth += powerUp;
        this.health += powerUp;
    }

    public PhysicsController getPyc() {
        return pyc;
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

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
