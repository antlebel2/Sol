package controller;

import java.io.File;
import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundController 
{
    private Clip clip;
    private Clip playerWalkClip;
    private Clip smallEnemyWalkClip;
    private Clip mediumEnemyWalkClip;
    private Clip largeEnemyWalkClip;
    private boolean paused = false;
    
    public SoundController(){
        try {
            clip = AudioSystem.getClip();
            playerWalkClip = AudioSystem.getClip();
            smallEnemyWalkClip = AudioSystem.getClip();
            mediumEnemyWalkClip = AudioSystem.getClip();
            largeEnemyWalkClip = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
        }
    }
    
    /**
    * Method for music toggle - Tyrel Tachibana
    * updated by Cameron Droke
    */
    public void pauseSound()
    {
        if(paused)
        {
            clip.start();
            playerWalkClip.start();
            smallEnemyWalkClip.start();
            mediumEnemyWalkClip.start();
            largeEnemyWalkClip.start();
        }
        else
        {
            clip.stop();
            playerWalkClip.stop();
            smallEnemyWalkClip.stop();
            mediumEnemyWalkClip.stop();
            largeEnemyWalkClip.stop();
        }
        this.paused = !this.paused;
    }
    
    public void stop(){
        clip.stop();
        playerWalkClip.stop();
        smallEnemyWalkClip.stop();
        mediumEnemyWalkClip.stop();
        largeEnemyWalkClip.stop();
        
        clip.close();
        playerWalkClip.close();
        smallEnemyWalkClip.close();
        mediumEnemyWalkClip.close();
        largeEnemyWalkClip.close();
    }
    
    public void menuBGM()
    {
        try
        {
            File file = new File("src/Sounds/sol-menu2.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            if(paused)clip.stop();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void selectConfirm()
    {
        try
        {
            File file = new File("src/Sounds/sol-confirm.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            Clip clipNew = AudioSystem.getClip();
            if(!paused)
            {
                clipNew.open(audioIn);
                clipNew.start();
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void selectDeny()
    {
        try
        {
            File file = new File("src/Sounds/sol-deny.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            Clip clipNew = AudioSystem.getClip();
            if(!paused)
            {
                clipNew.open(audioIn);
                clipNew.start();
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void openWorldBGM()
    {
        try
        {
            File file = new File("src/Sounds/sol-worldmap.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            if(paused)clip.stop();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void earthBGM()
    {
        try
        {
            File file = new File("src/Sounds/sol-earthBGM.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            if(paused)clip.stop();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void moonBGM()
    {
        
    }
    
    public void venusBGM()
    {
        
    }
    
    public void mercuryBGM()
    {
        
    }
    
    public void sunBGM()
    {
        
    }
    
    public void bossBGM()
    {
        try
        {
            File file = new File("src/Sounds/sol-bossBGM.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            if(paused)clip.stop();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void primaryWeaponFire()
    {
        try
        {
            File file = new File("src/Sounds/sol-laser.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            Clip clipNew = AudioSystem.getClip();
            if(!paused)
            {
                clipNew.open(audioIn);
                clipNew.start();
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void grenadeLauncherFire()
    {
        
    }
    
    public void seekerMissileFire()
    {
        
    }
    
    public void flakeCannonFire()
    {
        
    }
    
    public void forceBlasterFire()
    {
        
    }
    
    public void weaponPickUp()
    {
        
    }
    
    public void coinPickUp()
    {
        try
        {
            File file = new File("src/Sounds/sol-coin.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            Clip clipNew = AudioSystem.getClip();
            if(!paused)
            {
                clipNew.open(audioIn);
                clipNew.start();
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void repurposedFleshMove()
    {
        try
        {
            File file = new File("src/Sounds/sol-repurposedFlesh.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            if(!paused)
            {
                // If clip is not already running and has not been opened yet,
                // open the clip and start play. Else reset the clip to play
                // from the beginning then play.
                if(!smallEnemyWalkClip.isRunning())
                {
                    if(!smallEnemyWalkClip.isOpen())
                    {
                        smallEnemyWalkClip.open(audioIn);
                        smallEnemyWalkClip.start();
                    }
                    else
                    {
                        smallEnemyWalkClip.setFramePosition(0);
                        smallEnemyWalkClip.start();
                    }
                }
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void harvesterDroneMove()
    {
        try
        {
            File file = new File("src/Sounds/sol-harvesterDrone.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            if(!paused)
            {
                // If clip is not already running and has not been opened yet,
                // open the clip and start play. Else reset the clip to play
                // from the beginning then play.
                if(!mediumEnemyWalkClip.isRunning())
                {
                    if(!mediumEnemyWalkClip.isOpen())
                    {
                        mediumEnemyWalkClip.open(audioIn);
                        mediumEnemyWalkClip.start();
                    }
                    else
                    {
                        mediumEnemyWalkClip.setFramePosition(0);
                        mediumEnemyWalkClip.start();
                    }
                }
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void assaultCommanderMove()
    {
        try
        {
            File file = new File("src/Sounds/sol-assaultCommander.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            if(!paused)
            {
                // If clip is not already running and has not been opened yet,
                // open the clip and start play. Else reset the clip to play
                // from the beginning then play.
                if(!largeEnemyWalkClip.isRunning())
                {
                    if(!largeEnemyWalkClip.isOpen())
                    {
                        largeEnemyWalkClip.open(audioIn);
                        largeEnemyWalkClip.start();
                    }
                    else
                    {
                        largeEnemyWalkClip.setFramePosition(0);
                        largeEnemyWalkClip.start();
                    }
                }
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
    
    public void playerMove()
    {
        try
        {
            File file = new File("src/Sounds/sol-player-ver2.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            if(!paused)
            {
                // If clip is not already running and has not been opened yet,
                // open the clip and start play. Else reset the clip to play
                // from the beginning then play.
                if(!playerWalkClip.isRunning())
                {
                    if(!playerWalkClip.isOpen())
                    {
                        playerWalkClip.open(audioIn);
                        playerWalkClip.start();
                    }
                    else
                    {
                        playerWalkClip.setFramePosition(0);
                        playerWalkClip.start();
                    }
                }
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
        {
            System.out.printf("Error: %s \n", e.toString());
        }
    }
}