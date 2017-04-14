package controller;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import model.ImageLibrary;
import view.swingcomponents.MainWindow;




/**
* Main class, all shared objects are accessed through this class.
*
* @author  SATAS
* @version 1.0
* @since   2017-02-20 
*/
public class Main
{
    
    //static GraphicsDevice device = GraphicsEnvironment
    //    .getLocalGraphicsEnvironment().getScreenDevices()[0];
    
    public static int WIN_WIDTH = 700; //the width of the gui window
    public static int WIN_HEIGHT = 600; //the height of the gui window
    
    public static boolean debug = false;
   /**
   * This is the main entry point for the application
   * 
   * @param args Array of command line arguments
   */
    public static void main(String[] args)
    {
        
        //load images
        ImageLibrary.getInstance().loadSpriteSheet("gold_coin", "/Images/spinning-coin-spritesheet.png", 171, 171, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("hero_run_left", "/Images/hero_run_left.png", 166, 155, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("hero_run_right", "/Images/hero_run_right.png", 166, 155, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("hero_stand_left", "/Images/hero_stand_left.png", 166, 155, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("hero_stand_right", "/Images/hero_stand_right.png", 166, 155, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("hero_jump_left", "/Images/hero_jump_left.png", 166, 155, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("hero_jump_right", "/Images/hero_jump_right.png", 166, 155, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("primary1_left", "/Images/primary1_left.png", 51, 26, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("primary1_right", "/Images/primary1_right.png", 51, 26, 0, 0);        
        ImageLibrary.getInstance().loadSpriteSheet("grenade", "/Images/grenade.png", 144, 82, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("blueEnemyWalkingRight2", "/Images/blueEnemyWalkingRight2.png", 248, 180, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("blueEnemyWalkingLeft2", "/Images/blueEnemyWalkingLeft2.png", 248, 180, 0, 0);        
        ImageLibrary.getInstance().loadSpriteSheet("blueEnemyAttackLeft", "/Images/blueEnemyWalkingRight2.png", 248, 180, 0, 0);
        ImageLibrary.getInstance().loadSpriteSheet("blueEnemyAttackRight", "/Images/blueEnemyWalkingLeft2.png", 248,  180, 0, 0); 
        
        //build and display the gui
        MainWindow.getInstance().setVisible(true);

        //initially show the main menu screen
        GameController.getInstance().resetViewport();
        GameController.getInstance().showMainMenu();
        
        // Start rendering engine
        Animator.getInstance().start();
    }
}
