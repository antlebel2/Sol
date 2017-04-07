package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import model.GameData;
import model.ID;
import view.BufferedImageLoader;
import view.gameobjects.Laser;
import view.gameobjects.Background;
import view.gameobjects.EarthBoss;
import view.menus.GameButton;
import view.gameobjects.Goal;
import view.worldmap.Earth;
import view.gameobjects.EarthSmallEnemy;
import view.gameobjects.Gold;
import view.menus.Menu;
import view.worldmap.Mercury;
import view.worldmap.Moon;
import view.worldmap.Sun;
import view.gameobjects.Text;
import view.gameobjects.Tile;
import view.swingcomponents.MainWindow;
import view.worldmap.Venus;

/**
* The GameController class is responsible for making changes to the GameModel.
* This is where the logic goes for handling user interaction with the game.
* Any new objects are created here.
* Levels and menus are loaded here
* 
* @author  SATAS
* @version 1.0
* @since   2017-02-18 
*/
public class GameController implements ActionListener, KeyListener, ComponentListener{
  
    //singleton instance
    private static GameController instance;
    
    /**
     * Singleton class
     * 
     * @return the singleton instance of this class
     */
    public static GameController getInstance(){
        //initialize instance on first use
        if(instance == null){
            instance = new GameController();
        }
        //return the instance
        return instance;
    }
    
    
    //define any menu buttons here so we can reference them in the action events
    private GameButton mainMenuButton;
    private GameButton startButton;
    private GameButton scoresButton;
    private GameButton controlsButton;
    private GameButton quitButton;
    
    private static String screen; //keeping track of what screen is being shown. 
    
    private boolean fullscreen;
    
    Goal goal;
    Earth earth;
    Sun sun;
    Moon moon;
    Venus venus;
    Mercury mercury;
    //we can either create a new background obj each time we load a new menu
    //which will cause the animation to restart and look awkward
    //or we can define the background as a class var and reuse the same one that
    //way switching between menus is smooth.
    private Background menuBackground;
    private BufferedImage levelTest = BufferedImageLoader.getInstance().loadImage("/Images/level1_map_test.png");//loading the level
    
    /**
     * private constructor prevents bypassing singleton pattern
    */
    private GameController(){
        //Initialize the menu background.
        //note that other backgrounds can be created as needed. this is only here
        //because multiple screens use this background and it is animated
        menuBackground = new Background("/Images/corona_bk.png", Background.Stretch.VIEWPORT, true, true);
        //levelTest = BufferedImageLoader.getInstance().loadImage("/Images/level.png");//loading the level
        screen = "";
        
        fullscreen = false;
    }
    
   /**
   * This method removes all references to game objects and clears the obj list.
   * Each renderable object defines a clear method that takes care of action
   * listeners.
   * This method is called every time a new screen is loaded such as menus or levels
   */
    public void clear(){
        //call clear() on each game object
        for(int i=0; i<GameData.getInstance().gameObjects.size(); i++){
            GameData.getInstance().gameObjects.get(i).clear();
        }
        //clear the game object list
        GameData.getInstance().gameObjects.clear();
        //stop sounds
        SoundController.getInstance().stop();

        
        //reset viewport
        //GameData.getInstance().viewport.setLocation(new Point(0,0));
        //resetViewport();
    }
    
    public void resetViewport(){
        //GameData.getInstance().viewport = new Rectangle(0,GameData.getInstance().world.height-MainWindow.getInstance().getGamePanel().getHeight(),MainWindow.getInstance().getGamePanel().getWidth(),MainWindow.getInstance().getGamePanel().getHeight());
        GameData.getInstance().viewport = new Rectangle(0,0,MainWindow.getInstance().getGamePanel().getWidth(),MainWindow.getInstance().getGamePanel().getHeight());
        Animator.getInstance().init(GameData.getInstance().viewport.width,GameData.getInstance().viewport.height);
        
        System.out.println("Screen: "+screen);
        //reload menu screens because they need to be centered.
        if(screen.equals("MainMenu")){
            showMainMenu();
        }
        else if(screen.equals("World")){
            showWorld();
        }
        else if(screen.equals("Scores")){
            showScores();
        }
        else if(screen.equals("Controls")){
            showControls();
        }

    }
    
   /**
   * Clear everything and load the main menu. 
   */
    public void showMainMenu(){
        screen = "MainMenu";
        
        //clear all game objects
        clear();
        
        //add background
        GameData.getInstance().gameObjects.add(menuBackground);
        
        //play background music
        SoundController.getInstance().menuBGM();
        
        //add game title
        Font font = new Font("TimesRoman", Font.BOLD, 75); 
        Text text = new Text(new Point(GameData.getInstance().viewport.width/2, 50),"SOL", font, true);
        GameData.getInstance().addGameObject(text);
        
        //create and add a menu object
        int width = 120; //width of the buttons
        int height = 30; //height of the buttons
        int x = GameData.getInstance().viewport.width/2 - width/2; //center buttons
        int y = 150; //y position of first button
        int gap = 50; //the verticle gap between buttons
        
        //create a new menu object, this is a container that will handle highlighting
        //and adding of buttons to the object list
        Menu mainMenu = new Menu(new Point(x,y));
        GameData.getInstance().addGameObject(mainMenu);
        
        //add start button to the menu
        startButton = new GameButton(new Point(x,y+gap),width,height, "Start");
        startButton.addActionListener(this);
        mainMenu.addButton(startButton);
        
        //add score button to the menu
        scoresButton = new GameButton(new Point(x,y+gap*2),width,height, "High Score");
        scoresButton.addActionListener(this);
        mainMenu.addButton(scoresButton);
        
        //add controls button to the menu
        controlsButton = new GameButton(new Point(x,y+gap*3),width,height, "Controls");
        controlsButton.addActionListener(this);
        mainMenu.addButton(controlsButton);
        
        //add quit button to the menu
        quitButton = new GameButton(new Point(x,y+gap*4),width,height, "Quit");
        quitButton.addActionListener(this);
        mainMenu.addButton(quitButton);
    }


   /**
   * Clear everything and load the world map.
   * This is the screen where players select their level.
   */
    public void showWorld(){
        screen = "World";
        
        //clear all game objects
        clear();
        
        //add background
        GameData.getInstance().gameObjects.add(new Background("/Images/world_map_1.png", Background.Stretch.VIEWPORT, false, false));
        
        //play background music
        SoundController.getInstance().openWorldBGM();
        
        //Add informational text at the top of the map
        Font font = new Font("TimesRoman", Font.BOLD, 30);
        String gameNameString = "SYSTEM MAP - Select assault point";
        Text text = new Text(new Point(GameData.getInstance().viewport.width/2, 10),gameNameString, font, true);
        text.setColor(Color.WHITE);
        GameData.getInstance().addGameObject(text);
        
        //add the shooter object
        GameData.getInstance().getShooter();
        GameData.getInstance().addGameObject(GameData.getInstance().getShooter());
        //draw rect arround planets
        //Earth on world map
        earth = new Earth(new Point(800,Main.WIN_HEIGHT-400),50,50);
        GameData.getInstance().addGameObject(earth);
        earth.addActionListener(this);
        //Sun on world map
        sun = new Sun(new Point(800,Main.WIN_HEIGHT-400),50,50);
        GameData.getInstance().addGameObject(sun);
        sun.addActionListener(this); 
        //Moon on world map
        moon = new Moon(new Point(800,Main.WIN_HEIGHT-400),50,50);
        GameData.getInstance().addGameObject(moon);
        moon.addActionListener(this);
        //Venus on world map
        venus = new Venus(new Point(800,Main.WIN_HEIGHT-400),50,50);
        GameData.getInstance().addGameObject(venus);
        venus.addActionListener(this);
        //Mercury on world map
        mercury = new Mercury(new Point(800,Main.WIN_HEIGHT-400),50,50);
        GameData.getInstance().addGameObject(mercury);
        mercury.addActionListener(this);
        
        
        //GameData.getInstance().addGameObject(new Shooter(new Point(100, 100)));
    }
    
   /**
   * Clear everything and load the score screen. 
   */
    public void showScores(){
        screen = "Scores";
        
        //clear all game objects
        clear();
        
        //add background
        GameData.getInstance().gameObjects.add(menuBackground);
        
        //play background music
        SoundController.getInstance().menuBGM();
        
        //add game title
        Font font = new Font("TimesRoman", Font.BOLD, 75); 
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/2, 50),"SOL", font, true));

        //screen title
        font = new Font("TimesRoman", Font.PLAIN, 15); 
        String levelString = "High Scores";
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/2, 175),levelString, font, true));

        // Score board
        String scoreString01 = "1. Tyrel Tachibana 4526 1/27/2017";
        String scoreString02 = "2. Sam Sopp 4515 1/28/2017";
        String scoreString03 = "3. N/A";
        String scoreString04 = "4. N/A";
        String scoreString05 = "5. N/A";
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/3, 225),scoreString01, font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/3, 250),scoreString02, font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/3, 275),scoreString03, font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/3, 300),scoreString04, font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/3, 325),scoreString05, font, false));
        
        //create a new menu object, this is a container that will handle highlighting
        //and adding of buttons to the object list
        Menu scoresMenu = new Menu(new Point(0,0));
        GameData.getInstance().addGameObject(scoresMenu);
        
        //add button for returning to main menu
        mainMenuButton = new GameButton(new Point(GameData.getInstance().viewport.width/2 - 120/2,500),120,30, "Main Menu");
        mainMenuButton.addActionListener(this);
        scoresMenu.addButton(mainMenuButton);
    }

   /**
   * Clear everything and load the controls screen. 
   */
    public void showControls(){
        screen = "Controls";
        
        //clear all game objects
        clear();
        
        //add background
        GameData.getInstance().gameObjects.add(menuBackground);
        
        //play background music
        SoundController.getInstance().menuBGM();
        
        //add game title
        Font font = new Font("TimesRoman", Font.BOLD, 75); 
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/2, 50),"SOL", font, true));
        
        //add controls text
        font = new Font("TimesRoman", Font.BOLD, 22); 
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/16, 175),"Controls:", font, false));    
            
        font = new Font("TimesRoman", Font.PLAIN, 18); 

        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/16, 225),"Move Left:", font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/16, 250),"Move Right:", font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/16, 275),"Look Up:", font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/16, 300),"Crouch:", font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/16, 325),"Jump:", font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/16, 350),"Boost:", font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/16, 375),"Fire Primary Wepon:", font, false));
        GameData.getInstance().addGameObject(new Text(new Point(GameData.getInstance().viewport.width/16, 400),"Fire Secondary Weapon:", font, false));

        GameData.getInstance().addGameObject(new Text(new Point((GameData.getInstance().viewport.width/16)*12, 225),"Left Arrow / A", font, false));
        GameData.getInstance().addGameObject(new Text(new Point((GameData.getInstance().viewport.width/16)*12, 250),"Right Arrow / D", font, false));
        GameData.getInstance().addGameObject(new Text(new Point((GameData.getInstance().viewport.width/16)*12, 275),"Up Arrow / W", font, false));
        GameData.getInstance().addGameObject(new Text(new Point((GameData.getInstance().viewport.width/16)*12, 300),"Down Arrow / S", font, false));
        GameData.getInstance().addGameObject(new Text(new Point((GameData.getInstance().viewport.width/16)*12, 325),"Space", font, false));
        GameData.getInstance().addGameObject(new Text(new Point((GameData.getInstance().viewport.width/16)*12, 350),"Space (while midair)", font, false));
        GameData.getInstance().addGameObject(new Text(new Point((GameData.getInstance().viewport.width/16)*12, 375),"Left Mouse", font, false));
        GameData.getInstance().addGameObject(new Text(new Point((GameData.getInstance().viewport.width/16)*12, 400),"Right Mouse", font, false));
        
        //create a new menu object, this is a container that will handle highlighting
        //and adding of buttons to the object list
        Menu scoresMenu = new Menu(new Point(0,0));
        GameData.getInstance().addGameObject(scoresMenu);
        
        //add button for returning to main menu
        mainMenuButton = new GameButton(new Point(GameData.getInstance().viewport.width/2 - 120/2,500),120,30, "Main Menu");
        mainMenuButton.addActionListener(this);
        scoresMenu.addButton(mainMenuButton);
    }
    
    /**
   * Clear everything and load the first level
   */
    public void showLevel1(){
        screen = "Level1";
        
        //clear all game objects
        clear();
        
        //add background
        GameData.getInstance().gameObjects.add(new Background("/Images/BG Apocalyptic 2.jpg", Background.Stretch.WORLD, true, false));
        
        //play background music
        SoundController.getInstance().earthBGM();
        
        //loading the level
        BufferedImage levelMap = BufferedImageLoader.getInstance().loadImage("/Images/level1_map.png");
        
        loadImageLevel(levelMap);
        
    }
    public void showLevel2(){
        screen = "Level2";
        
        //clear all game objects
        clear();       

        //add background
        GameData.getInstance().gameObjects.add(new Background("/Images/moonBG.jpg", Background.Stretch.WORLD, true, false));
        
        
        //play background music
        SoundController.getInstance().moonBGM();
        
        //loading the level
        BufferedImage levelMap = BufferedImageLoader.getInstance().loadImage("/Images/level2_map.png");
        
        loadImageLevel(levelMap);
        
//        System.out.println("Level 2 under construction");
    }
    public void showLevel3(){
        screen = "Level3";
        
        //clear all game objects
        clear();
        
        //add background
        GameData.getInstance().gameObjects.add(new Background("/Images/venusBG.jpg", Background.Stretch.WORLD, true, false));

        
        //play background music
        SoundController.getInstance().venusBGM();        
        
        //loading the level
        BufferedImage levelMap = BufferedImageLoader.getInstance().loadImage("/Images/level.png");
        
        loadImageLevel(levelMap);
        
        System.out.println("Level 3 under construction");
    }
    public void showLevel4(){
        screen = "Level4";
        
        //clear all game objects
        clear();
        
        //add background
        GameData.getInstance().gameObjects.add(new Background("/Images/mercuryBG.jpg", Background.Stretch.WORLD, true, false));        

        //play background music
        SoundController.getInstance().mercuryBGM();
        
        //loading the level
        BufferedImage levelMap = BufferedImageLoader.getInstance().loadImage("/Images/level4_map.png");
        
        loadImageLevel(levelMap);
        
        System.out.println("Level 4 under construction");
    }
    public void showLevel5(){
        screen = "Level5";
        
        //clear all game objects
        clear();        
        
        //add background
        GameData.getInstance().gameObjects.add(new Background("/Images/sunBG.jpg", Background.Stretch.NONE, true, false));
        
        //play background music
        SoundController.getInstance().sunBGM();
        
        //loading the level
        BufferedImage levelMap = BufferedImageLoader.getInstance().loadImage("/Images/level5_map.png");
        
        loadImageLevel(levelMap);
        
        System.out.println("Level 5 under construction");
    }
    
    
    /**
    * {@inheritDoc}
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        //handle select events from all game buttons
        if(e.getSource() == startButton){
            showWorld();
        }
        else if(e.getSource() == scoresButton){
            showScores();
        }
        else if(e.getSource() == mainMenuButton){
            showMainMenu();
        }
        else if(e.getSource() == controlsButton){
            showControls();
        }
        else if(e.getSource() == quitButton){
            System.exit(0);
        }
        else if(e.getSource() == goal){
            showMainMenu();
        }
        
        
    }

   /**
   * A laser to the game object list
   * 
   * @param x The starting x position
   * @param y The starting y position
   * @param color The color
   * @param direction laser fired left or right
   */
    public void addLaser(float x, float y, Color color, boolean direction) {
        //add object
        Laser l = new Laser(x, y, color, direction); 
        GameData.getInstance().addGameObject(l);
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
        //if(){}
        if(e.getKeyCode() == KeyEvent.VK_1){
            showLevel1();
        }
        else if(e.getKeyCode() == KeyEvent.VK_2){
            showLevel2();
        }
        else if(e.getKeyCode() == KeyEvent.VK_0){
            showMainMenu();
        }
        else if(e.getKeyCode() == KeyEvent.VK_F2){
            Main.debug = !Main.debug;
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){SoundController.getInstance().pauseSound();}
        else if(e.getKeyCode() == KeyEvent.VK_F11){
            fullscreen = !fullscreen;
            if(fullscreen){
                //GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
                //device.setFullScreenWindow(Main.game);
                
                MainWindow.getInstance().setExtendedState(JFrame.MAXIMIZED_BOTH); 
                MainWindow.getInstance().dispose();
                MainWindow.getInstance().setUndecorated(true);
                
                MainWindow.getInstance().setVisible(true);
            }
            else{
                 
                
                MainWindow.getInstance().dispose();
                MainWindow.getInstance().setSize(700, 600);
                MainWindow.getInstance().setExtendedState(JFrame.NORMAL);
                MainWindow.getInstance().setUndecorated(false);
                MainWindow.getInstance().setVisible(true);
            }
        }
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        resetViewport();
        //GameData.getInstance().viewport = new Rectangle(0, 600-MainWindow.getInstance().getGamePanel().getHeight(), MainWindow.getInstance().getGamePanel().getWidth(),MainWindow.getInstance().getGamePanel().getHeight());
        //Main.animator.init();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
    
    private void loadImageLevel(BufferedImage image){
        int w = image.getWidth();
        int h = image.getHeight();
        
        //loop over every pixal from left to right, top to bottom
        for(int x =0; x< w; x++){
            for(int y=0; y<h; y++){
                
                //use bit-operator to get pixel color
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff; 
                int green=(pixel >> 8) & 0xff;
                int blue =(pixel)& 0xff;
                
                //the translated location
                Point tileLoc = new Point(x*Tile.TILESIZE, y*Tile.TILESIZE);
                
                
                //position the hero on the blue pixal
                if(red == 0 && green == 0 && blue == 255){
                    GameData.getInstance().getHero().setLocation(tileLoc);
                    GameData.getInstance().addGameObject(GameData.getInstance().getHero());
                    //hero starts with 100 sheild points
                    GameData.getInstance().getHero().setShield(100);
                }
                
                //position goal on green pixal
                //TODO: this goes away after we implement the boss right?
                else if(red == 0 && green == 255 && blue == 0){
                    goal = new Goal(tileLoc, 20, 200);
                    goal.addActionListener(this);
                    GameData.getInstance().addGameObject(goal);
                }

                //border
                //white pixal
                else if(red == 255 && green == 255 && blue == 255){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    tile.setSprite(3, 0); //brown block looks like dirt
                    tile.setSolid(true);
                }
                
                //platform
                //Rose color pixal
                if(red == 255 && green == 128 && blue == 128){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    tile.setSprite(7, 8); //green platform center
                    tile.setTrim(1); //over size the tile to hide the gap
                    tile.setSolid(true);
                }

                //platform left end
                //Rose color pixal
                if(red == 254 && green == 128 && blue == 128){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    
                    tile.setSprite(8, 9); //green platform left
                    tile.setTrim(1); //over size the tile to hide the gap
                    tile.setSolid(true);
                }
                
                //platform right end
                //Rose color pixal
                if(red == 253 && green == 128 && blue == 128){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    tile.setSprite(8, 7); //green platform right
                    tile.setTrim(1); //over size the tile to hide the gap
                    tile.setSolid(true);
                }
                
                //platform middle lvl-2
                //light green color pixal
                if(red == 128 && green == 255 && blue == 128){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    tile.setSprite(11, 2); //green platform right
                    tile.setTrim(1); //over size the tile to hide the gap
                    tile.setSolid(true);
                }
                
                //platform left end lvl-2
                //light green color pixal
                if(red == 128 && green == 254 && blue == 128){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    tile.setSprite(11, 3); //green platform right
                    tile.setTrim(1); //over size the tile to hide the gap
                    tile.setSolid(true);
                }
                
                //platform right end lvl-2
                //light green color pixal
                if(red == 128 && green == 253 && blue == 128){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    tile.setSprite(11, 1); //green platform right
                    tile.setTrim(1); //over size the tile to hide the gap
                    tile.setSolid(true);
                }
                
                //platform middle lvl-3
                //light purple color pixal
                if(red == 128 && green == 128 && blue == 255){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    tile.setSprite(4, 8); //green platform right
                    tile.setTrim(1); //over size the tile to hide the gap
                    tile.setSolid(true);
                }
                
                //platform left end lvl-3
                //light purple color pixal
                if(red == 128 && green == 128 && blue == 254){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    tile.setSprite(5, 9); //green platform right
                    tile.setTrim(1); //over size the tile to hide the gap
                    tile.setSolid(true);
                }
                
                //platform right end lvl-3
                //light purple color pixal
                if(red == 128 && green == 128 && blue == 253){
                    Tile tile = new Tile(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array
                    tile.setSprite(5, 7); //green platform right
                    tile.setTrim(1); //over size the tile to hide the gap
                    tile.setSolid(true);
                }
                
                //gold coin
                //yellow color pixal
                if(red == 255 && green == 242 && blue == 0){
                    Tile tile = new Gold(tileLoc); //Create tile object
                    GameData.getInstance().addGameObject(tile); //Add tile to game object array

                }
                
		// Small enemy type
                // Pinkish color pixal
                if(red == 249 && green == 45 && blue == 227)
                {
                    EarthSmallEnemy smallEnemy = new EarthSmallEnemy(tileLoc, ID.SmallEnemy); //Create tile object
                    GameData.getInstance().addGameObject(smallEnemy); //Add tile to game object array
                }
                
                // Small enemy type
                // Pinkish color pixal
                if(red == 255 && green == 0 && blue == 0){
                    EarthBoss boss = new EarthBoss(tileLoc); //create object
                    GameData.getInstance().addGameObject(boss); //Add object
                }
               
            }
        }
    }
    
    
    public static String getScreen(){
        return screen;
    }
    
}
