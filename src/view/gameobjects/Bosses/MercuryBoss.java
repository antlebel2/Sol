package view.gameobjects.Bosses;

import controller.PhysicsController;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
* @author  SATAS
* @version 1.0
* @since   2017-02-18 
*/
public class MercuryBoss extends AbstractBoss {
    
    private PhysicsController.DIRECTION direction;
    private long lastJumpTime;
    
    public MercuryBoss(Point loc) {
        super(loc, "sprite_sheet_alian_6_walk_left_300x427", "sprite_sheet_alian_6_walk_right_300x427");
        direction = PhysicsController.DIRECTION.LEFT;
        lastJumpTime = 0;
    }
    
    /**
    * {@inheritDoc}
    */
    @Override
    public void update(){
        super.update();
        
        if(direction == PhysicsController.DIRECTION.LEFT){

            if(getPyc().canMove(PhysicsController.DIRECTION.LEFT)){
                getPyc().getLeftMovementForce().setForcePerSecond(.25);
                getPyc().getLeftMovementForce().setActive(true);
            }
            else{
                direction = PhysicsController.DIRECTION.RIGHT;
            }

            //if moving left make sure right movement has stopped
            getPyc().getRightMovementForce().setActive(false);
            getPyc().setForce(PhysicsController.DIRECTION.RIGHT, 0);
        }
        else{

            if(getPyc().canMove(PhysicsController.DIRECTION.RIGHT)){
                getPyc().getRightMovementForce().setForcePerSecond(.25);
                getPyc().getRightMovementForce().setActive(true);
            }
            else{
                direction = PhysicsController.DIRECTION.LEFT;
            }

            //if moving right make sure left movement has stopped
            getPyc().getLeftMovementForce().setActive(false);
            getPyc().setForce(PhysicsController.DIRECTION.LEFT, 0);
        }
    
        //jump
        
        if(System.currentTimeMillis() - lastJumpTime >= 2000){
            getPyc().setForce(PhysicsController.DIRECTION.UP, 2000);
            lastJumpTime = System.currentTimeMillis();
        }
        
        
    }
    
    /**
    * {@inheritDoc}
    */
    @Override
    public void render(Graphics2D g2,Rectangle viewport){
        super.render(g2, viewport);
    }
}
