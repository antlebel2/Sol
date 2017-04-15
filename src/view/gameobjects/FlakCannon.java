/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.gameobjects;

import com.sun.javafx.scene.traversal.Direction;
import controller.AnimationController;
import controller.Main;
import controller.PhysicsController;
import controller.PhysicsController.DIRECTION;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import model.GameData;
import static view.gameobjects.GameFigure.STATE_ALIVE;

/**
 *
 * @author alebel
 */
public class FlakCannon extends Weapon {

    private PhysicsController pyc;
    private AnimationController animationController;
    private boolean alive = false;
    private int mouseX = 0;//mouseclick location x
    private int mouseY = 0;//mouseclick location y
    
    public FlakCannon(float sx, float sy, int mx, int my, RenderableObject owner) {
        super(sx, sy, owner);
        //All this mess and experimentation
        this.mouseX = mx; //get mouselocation passed from Hero when mouse clicked
        this.mouseY = my;
        int travelX = mx - (int)sx;
        int travelY = my - (int)sy;
     
        //TODO: this should be passed in via parameters im just overiding it here for quick debuging
        int x = GameData.getInstance().getHero().getBoundingBox().x;
        int y = GameData.getInstance().getHero().getBoundingBox().y;
        super.boundingBox = new Rectangle(x, y, 90, 60);
        
        pyc = new PhysicsController(this);
        
        animationController = new AnimationController(AnimationController.Mode.AUTO, "seeker");
        animationController.setFps(32);
        animationController.setSpriteSheet("seeker");
        
        if(travelY > 0)
            pyc.addForce(DIRECTION.DOWN, travelY * 2);
        else 
            pyc.addForce(DIRECTION.UP, -travelY - 50);
        if(travelX > 0)
            pyc.addForce(DIRECTION.RIGHT, travelX);
        else
            pyc.addForce(DIRECTION.LEFT, -travelX);
        System.out.println(travelX + " - " + travelY);
         
    }
    
    @Override
    public void render(Graphics2D g2, Rectangle viewport){
        int translatedX =  (int)boundingBox.getX() - (int)viewport.getX();
        int translatedY =  (int)boundingBox.getY() - (int)viewport.getY();
        Point xy = this.loc;
        int x = xy.x;
        int y = xy.y;
        Rectangle boundingBoxForRendering = new Rectangle(translatedX, translatedY, 80, 50);
        
        BufferedImage sprite = animationController.getFrame();
        g2.drawImage(sprite, translatedX, translatedY, 80, 50, null);
        animationController.update();
        
        if(Main.debug){
            g2.setColor(Color.red);
            g2.drawRect(boundingBoxForRendering.x, boundingBoxForRendering.y, boundingBoxForRendering.width, boundingBoxForRendering.height);
            g2.setColor(Color.yellow);
            g2.drawRect(translatedX, translatedY, boundingBox.width, boundingBox.height);
            
        }
        
        
    }
    
    @Override
    public void update() {
        super.update();
        //pyc.update(); 
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
        
        //stop when hits the ground
        if(!pyc.canMove(DIRECTION.DOWN)){
            pyc.clear();
        }
        
        
        //All of this below is testing
       //None of this worked obviously
   
        //System.out.print("The bounding box stretches from : " + boundingBox.getX() + " To " + (boundingBox.getX() + boundingBox.getWidth()) + " ");
        //System.out.print("And from : " + boundingBox.getY() + " To " + (boundingBox.getY() + boundingBox.getHeight()));
        //System.out.println("The mouse is located at : " + mouseX + "and " + mouseY);
        Point mouseloc = new Point(mouseX, mouseY);
        
        if(boundingBox.getLocation() == mouseloc)
            GameData.getInstance().removeGameObject(this);
        
        double deltaX = boundingBox.getX() - mouseX;
        double deltaY = boundingBox.getY() - mouseY;
        //if(deltaX > 25 || deltaX < 25 || deltaY > 25 || deltaY < 25){
        //    GameData.getInstance().removeGameObject(this);
        //    System.out.println("Removed!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //}
        
        
        
       
    }
    
    public void translate(int dx, int dy){
        //get Point of mouse click
        Point mouseClick = new Point(mouseX,mouseY);
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
   

    @Override
    public void mouseClicked(MouseEvent e) {
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

    
    
    public String getObjectType(){
        return "Grenade Weapon";
    }
    
    public boolean isAlive(){
        return this.alive;
    }
    
    public void setAlive(boolean a){
        alive = a;
    }
    
    
}