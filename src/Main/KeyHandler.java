package Main;

import Entity.Entity;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed,downPressed,leftPressed,rightPressed,attackPressed;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code=e.getKeyCode();//returneaza un numar in functie de ce tasta a fost apasata
        //TITLE STATE
        if(gp.gameState == gp.titleState) {
            if(code==KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0)
                {
                    gp.ui.commandNum = 2;
                }
            }
            if(code==KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2)
                {
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER)
            {
                //PLAYING
                if(gp.ui.commandNum == 0)
                {
                    gp.restartGame();
                    gp.gameState = gp.playState;
                }
                //LOAD
                if(gp.ui.commandNum == 1)
                {
                    //LOAD GAME
                    gp.loadGame();
                    gp.gameState = gp.playState;
                    gp.ui.showMessage("Game Loaded!");
                }
                if(gp.ui.commandNum == 2)
                {
                    gp.closeGame();
                    gp.stopMusic();
                    System.exit(0);
                }
            }
        } else if (gp.gameState == gp.pauseState) {
            if(code==KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0)
                {
                    gp.ui.commandNum = 3;
                }
            }
            if(code==KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 3)
                {
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER)
            {
                //RESUME
                if(gp.ui.commandNum == 0)
                {
                    gp.gameState = gp.playState;
                }
                //REPLAY
                if (gp.ui.commandNum == 1) {
                    gp.restartGame();
                    gp.ui.commandNum = 0;
                    gp.gameState = gp.playState;
                }//SAVE
                if(gp.ui.commandNum == 2)
                {
                    gp.saveGame();
                    gp.ui.showMessage("Game Saved!");
                    gp.gameState = gp.playState;

                }//EXIT
                if(gp.ui.commandNum == 3)
                {
                    gp.closeGame();
                    gp.stopMusic();
                    System.exit(0);
                }
            }
        } else if(gp.gameState == gp.gameFinished) {
            if(code == KeyEvent.VK_ENTER)
            {
                gp.gameState = gp.titleState;
            }
        }
        //PLAY STATE
        if(code==KeyEvent.VK_1){
            gp.selectedWeapon = 1;
        }
        if(code==KeyEvent.VK_2){
            gp.selectedWeapon = 2;
        }
        if(code==KeyEvent.VK_3){
            gp.selectedWeapon = 3;
        }
        if(code==KeyEvent.VK_W){
            upPressed=true;
        }
        if(code==KeyEvent.VK_S){
            downPressed=true;
        }
        if(code==KeyEvent.VK_A){
            leftPressed=true;
        }
        if(code==KeyEvent.VK_D){
            rightPressed=true;
        }
        if (code == KeyEvent.VK_Z) {
            gp.nextLevel = true;
            if(gp.level == 1) {
                gp.level = 2;
            } else if (gp.level == 2) {
                gp.level = 3;
            } else if (gp.level == 3) {
                gp.level = 1;
            }
        }
        if(code == KeyEvent.VK_P){

            if(gp.gameState == gp.playState){
                gp.ui.commandNum = 0;
                gp.gameState = gp.pauseState;
            }
            else if(gp.gameState == gp.pauseState){
                gp.gameState = gp.playState;
            }
        }
        if(code == KeyEvent.VK_Q) {
            attackPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code=e.getKeyCode();
        if(code==KeyEvent.VK_W){
            upPressed=false;
        }
        if(code==KeyEvent.VK_S){
            downPressed=false;
        }
        if(code==KeyEvent.VK_A){
            leftPressed=false;
        }
        if(code==KeyEvent.VK_D){
            rightPressed=false;
        }
        if(code == KeyEvent.VK_Q) {
            attackPressed = false;
        }
    }
}
