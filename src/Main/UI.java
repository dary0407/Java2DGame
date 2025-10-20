package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class UI {
    GamePanel gp;
    Font arial_40;
    Graphics2D g2;
    BufferedImage backgroundImage;
    BufferedImage menuImage;
    BufferedImage menuButtonsImage;
    BufferedImage menuBackButton;
    BufferedImage menuHelpButton;
    BufferedImage menuHomeButton;
    BufferedImage menuReplayButton;
    BufferedImage menuRestartButton;
    BufferedImage menuSaveButton;
    public int commandNum = 0;
    public String message = "";
    public boolean messageOn = false;
    public int messageCounter = 0;

    public UI(GamePanel gp)
    {
        this.gp=gp;
        arial_40=new Font("Arial", Font.PLAIN, 40);
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/res/menu/Fundal.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            menuImage = ImageIO.read(getClass().getResourceAsStream("/res/menu/MeniuFundal.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            InputStream is = getClass().getResourceAsStream("/res/menu/ButoaneMeniu.png");
            if (is == null) {
                System.out.println("Imaginea NU a fost găsită! Verifică calea și plasarea.");
            } else {
                menuButtonsImage = ImageIO.read(is);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            InputStream is = getClass().getResourceAsStream("/res/menu/ButoaneBack.png");
            if (is == null) {
                System.out.println("Imaginea NU a fost găsită! Verifică calea și plasarea.");
            } else {
                menuBackButton = ImageIO.read(is);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            InputStream is = getClass().getResourceAsStream("/res/menu/ButoaneRestart.png");
            if (is == null) {
                System.out.println("Imaginea NU a fost găsită! Verifică calea și plasarea.");
            } else {
                menuRestartButton = ImageIO.read(is);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            InputStream is = getClass().getResourceAsStream("/res/menu/ButoaneSave.png");
            if (is == null) {
                System.out.println("Imaginea NU a fost găsită! Verifică calea și plasarea.");
            } else {
                menuSaveButton = ImageIO.read(is);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
        messageCounter = 0;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gameState == gp.titleState) {
            drawTitleScreen(g2);
        }
        else if (gp.gameState == gp.playState) {
            drawHUD(g2);  // Draw health, crystals, lives HUD
        }
        else if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if (messageOn) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
            g2.setColor(Color.WHITE);
            g2.drawString(message, 50, 100);
            messageCounter++;
            if (messageCounter > 120) {
                messageOn = false;
                messageCounter = 0;
            }
        }
    }

    private void drawHUD(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();

        int baselineY = 40;
        int iconSize = 32;
        int spacing = 5;

        // LEFT SIDE HUD
        int x = 10;
        int iconY = baselineY - iconSize + 6;

        g2.drawImage(gp.healthIcon, x, iconY, iconSize, iconSize, null);
        x += iconSize + spacing;

        String healthText = String.valueOf(gp.player.hp);
        g2.drawString(healthText, x, baselineY);
        x += fm.stringWidth(healthText) + spacing;

        gp.drawCrystalsHUD(g2, gp.player, x, baselineY);

        // RIGHT SIDE LIVES
        int maxLives = 3;
        int heartSize = iconSize;
        int heartY = iconY;

        int totalLivesWidth = maxLives * heartSize + (maxLives - 1) * spacing;
        int livesX = gp.getWidth() - totalLivesWidth - 10;

        gp.drawLives(g2, gp.player.lives, maxLives, livesX, heartY, heartSize);

        gp.drawWeaponHUD(g2);


    }
    public void drawTitleScreen(Graphics2D g)
    {
        //BACKGROUND
        if(backgroundImage !=null)
        {
            g2.drawImage(backgroundImage, 0, 0,null);
        }
        //MENU BACKGROUND
        int menuWidth = (menuImage.getWidth() * 2);
        int menuHeight = (menuImage.getHeight() * 2);
        int menuX = gp.screenWidth / 2 - menuWidth / 2;
        if(menuImage !=null)
        {
            g2.drawImage(menuImage, menuX,60, menuWidth, menuHeight,null);
        }
        //MENU
        final int buttonWidth = menuButtonsImage.getWidth() / 2 ;
        final int buttonHeight = menuButtonsImage.getHeight() / 3;
        int newWidth = buttonWidth * 2;
        int newHeight = buttonHeight * 2;
        if (menuButtonsImage != null) {
            int x = (gp.screenWidth - newWidth) / 2;
            int y = gp.tileSize * 4;
            int loadY = y + newHeight + 10;
            int quitY = y + (newHeight + 10) * 2;

            // NEW GAME
            BufferedImage newGameButtonn1 = menuButtonsImage.getSubimage(0, 0, buttonWidth, buttonHeight);
            g2.drawImage(newGameButtonn1, x, y, newWidth, newHeight, null);
            if(commandNum == 0)
            {
                BufferedImage newGameButton2 = menuButtonsImage.getSubimage(buttonWidth, 0, buttonWidth, buttonHeight);
                g2.drawImage(newGameButton2, x, y, newWidth, newHeight, null);
            }

            // LOAD GAME
            BufferedImage loadGameButton1 = menuButtonsImage.getSubimage(0, buttonHeight, buttonWidth, buttonHeight);
            g2.drawImage(loadGameButton1, x, y + newHeight + 10, newWidth, newHeight, null);
            if(commandNum == 1)
            {
                BufferedImage loadGameButton2 = menuButtonsImage.getSubimage(buttonWidth, buttonHeight, buttonWidth, buttonHeight);
                g2.drawImage(loadGameButton2, x, loadY, newWidth, newHeight, null);
            }

            // QUIT
            BufferedImage quitButton1 = menuButtonsImage.getSubimage(0, buttonHeight * 2, buttonWidth, buttonHeight);
            g2.drawImage(quitButton1, x, y + (newHeight + 10) * 2, newWidth, newHeight,null);
            if(commandNum == 2)
            {
                BufferedImage quitButton2 = menuButtonsImage.getSubimage(buttonWidth, buttonHeight * 2, buttonWidth, buttonHeight);
                g2.drawImage(quitButton2, x, quitY, newWidth, newHeight, null);
            }
        }

    }
    public void drawPauseScreen()
    {
        if(backgroundImage !=null)
        {
            g2.drawImage(backgroundImage, 0, 0,null);
        }
        //MENU BACKGROUND
        int menuWidth = (menuImage.getWidth() * 2);
        int menuHeight = (menuImage.getHeight() * 2);
        int menuX = gp.screenWidth / 2 - menuWidth / 2;
        if(menuImage !=null)
        {
            g2.drawImage(menuImage, menuX,60, menuWidth, menuHeight,null);
        }
        //MENU
        final int buttonWidth = menuButtonsImage.getWidth() / 2 ;
        final int buttonHeight = menuButtonsImage.getHeight() / 3;
        int newWidth = buttonWidth * 2;
        int newHeight = buttonHeight * 2;
        if (menuButtonsImage != null) {
            int x = (gp.screenWidth - newWidth) / 2;
            int y = gp.tileSize * 3;

            int resumeY = y;
            int newGameY = resumeY + newHeight + 10;
            int loadY = newGameY + newHeight + 10;
            int quitY = loadY + newHeight + 10;

            // RESUME
            BufferedImage resumeButton1 = menuBackButton.getSubimage(0, 0, buttonWidth, buttonHeight);
            g2.drawImage(resumeButton1, x, resumeY, newWidth, newHeight, null);
            if (commandNum == 0) {
                BufferedImage resumeButton2 = menuBackButton.getSubimage(buttonWidth, 0, buttonWidth, buttonHeight);
                g2.drawImage(resumeButton2, x, resumeY, newWidth, newHeight, null);
            }

            // NEW GAME
            BufferedImage newGameButton1 = menuRestartButton.getSubimage(0, 0, buttonWidth, buttonHeight);
            g2.drawImage(newGameButton1, x, newGameY, newWidth, newHeight, null);
            if (commandNum == 1) {
                BufferedImage newGameButton2 = menuRestartButton.getSubimage(buttonWidth, 0, buttonWidth, buttonHeight);
                g2.drawImage(newGameButton2, x, newGameY, newWidth, newHeight, null);
            }

            // SAVE GAME
            BufferedImage saveGameButton1 = menuSaveButton.getSubimage(0, 0, buttonWidth, buttonHeight);
            g2.drawImage(saveGameButton1, x, loadY, newWidth, newHeight, null);
            if (commandNum == 2) {
                BufferedImage saveGameButton2 = menuSaveButton.getSubimage(buttonWidth, 0, buttonWidth, buttonHeight);
                g2.drawImage(saveGameButton2, x, loadY, newWidth, newHeight, null);
            }

            // QUIT
            BufferedImage quitButton1 = menuButtonsImage.getSubimage(0, buttonHeight * 2, buttonWidth, buttonHeight);
            g2.drawImage(quitButton1, x, quitY, newWidth, newHeight, null);
            if (commandNum == 3) {
                BufferedImage quitButton2 = menuButtonsImage.getSubimage(buttonWidth, buttonHeight * 2, buttonWidth, buttonHeight);
                g2.drawImage(quitButton2, x, quitY, newWidth, newHeight, null);
            }
        }
    }
    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x=gp.screenWidth/2 - length/2;
        return x;
    }
}
