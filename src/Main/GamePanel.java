package Main;

import Entity.Entity;
import Entity.Player;
import Entity.NPC_Samurai;
import Entity.NPC_Goblin;
import Entity.NPC_Warrior;
import Entity.NPC_FlyingEye;
import Entity.NPC_Sardonix;
import Tile.TileManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import Object.OBJ_Crystals;
import Object.SuperObject;

public class GamePanel extends JPanel implements Runnable{
    public GameDatabase db;

    //screen settings

    public final int tileSize=64;//64x64 tiles(px)
    public final int maxScreenCol=16;
    public final int maxScreenRow=12;
    public final int screenWidth=tileSize*maxScreenCol; //1024 px
    public final int screenHeight=tileSize*maxScreenRow; //768 px

    //world settings
    public final int maxWorldCol=48;
    public final int maxWorldRow=12;
    public final int worldWidth=tileSize*maxWorldCol; //3*1024=3072
    public final int worldHeight=tileSize*maxWorldRow; //768

    //LEVEL
    public int level = 1;
    //int level = 2;
    //int level = 3;

    //FPS
    int FPS=60;
    //SYSTEM
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH=new KeyHandler(this);
    public UI ui=new UI(this);
    Thread gameThread;
    //ENTITY si OBJECT
    public AssetSetter aSetter = new AssetSetter(this);

    public Player player=new Player(this,keyH);
    public EntityType entityType;

    public SuperObject [] obj = new SuperObject[24];
    public Entity [] npc = new Entity[5];


    //GAME STATE
    public int gameState;
    public final int titleState=0;
    public final int playState = 1; //STATE
    public final int pauseState = 2; //STATE

    // game over
    public final int gameOverState = 3;
    public final int dyingState = 4;
    public final int dialogueState = 5;
    public final int gameFinished = 6;
    public int gameOverCounter = 0;
    public boolean gameOver = false;
    public boolean nextLevel = false;
    public CollisionChecker cChecker = new CollisionChecker(this);

    public int crystalsRed = 0;
    public int crystalsBlue = 0;
    public int crystalsPink = 0;
    public int crystalsBlack = 0;
    public int crystalsPurple = 0;
    public int selectedWeapon = 1;
    public boolean sardonixKilled = false;
    public long finalCrystalPicked = 0;
    public boolean endGameScheduled = false;


    public int dialogueTimer = 0;      // timer pentru dialog
    public int dialogueDelay = 300;
    public Entity currentDialogueNPC;  // NPC-ul care are dialog cu PLAYER
    public Entity currentDialogueSardonix;
    public int dialogueIndex;          // index pentru dialog

    public long samuraiKillTime = 0;
    public boolean samuraiKilled = false;
    public int highestScore = 0;

    public Sound music = new Sound();

    private final OBJ_Crystals purpleCrystalHUD = new OBJ_Crystals("purple");

    public BufferedImage heartFull, heartEmpty, healthIcon, swordIcon, pickaxeIcon, bowIcon;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.loadHud();
    }

    public void setupGame() {
        db = new GameDatabase();
        db.connect();
        GameDatabase.createTablesIfNotExist();

        if (db.isWorldObjectsTableEmpty()) {
            Arrays.fill(obj, null);
            aSetter.setObject();
            db.saveWorldObjects(obj);
        }
        if (db.isNPCsTableEmpty()) {
            Arrays.fill(npc, null);
            aSetter.setNPC();
            db.saveWorldNPCs(npc);
        }
        loadLevel();
        gameState = titleState;
    }
    public void closeGame() {
        if (db != null) {
            db.disconnect();
        }
        stopMusic();
    }

    public void saveGame() {
        db.saveNewPlayer(player, level);
        db.saveWorldNPCs(npc);
        db.saveInventory(this);
        db.saveWorldObjects(obj);
    }

    public void loadGame() {
        this.level = db.loadPlayerData(player);
        db.loadAllWorldNPCs(this, npc);
        db.loadInventory(this);
        db.loadAllWorldObjects(obj);
        loadLevel();
    }

    public void startGameThread()
    {
        music.setFile("src/res/music/game-music-loop-5-144569.wav");
        music.setVolume(0.7f); // Set volume
        music.play();
        gameThread=new Thread(this);
        gameThread.start();
    }

    public void stopMusic() {
        music.stop();
    }

    public void loadHud() {
        try {
            heartFull = ImageIO.read(getClass().getResourceAsStream("/res/ui/Heart1.png"));
            heartEmpty = ImageIO.read(getClass().getResourceAsStream("/res/ui/Heart2.png"));
            healthIcon = ImageIO.read(getClass().getResourceAsStream("/res/ui/health.png"));
            swordIcon = ImageIO.read(getClass().getResourceAsStream("/res/ui/sword.png"));
            pickaxeIcon = ImageIO.read(getClass().getResourceAsStream("/res/ui/pickaxe.png"));
            bowIcon = ImageIO.read(getClass().getResourceAsStream("/res/ui/bow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawLives(Graphics2D g2, int lives, int maxLives, int startX, int y, int size) {
        int x = startX;
        int spacing = 5;

        for (int i = 0; i < maxLives; i++) {
            if (i < lives) {
                g2.drawImage(heartFull, x, y, size, size, null);
            } else {
                g2.drawImage(heartEmpty, x, y, size, size, null);
            }
            x += size + spacing;
        }
    }
    public void drawWeaponHUD(Graphics2D g2) {
        int weaponIconSize = 40;
        int weaponSpacing = 20;
        int totalWeaponWidth = 3 * weaponIconSize + 2 * weaponSpacing;

        int weaponX = (getWidth() - totalWeaponWidth) / 2;
        int weaponY = getHeight() - weaponIconSize - 10;

        for (int i = 1; i <= 3; i++) {
            BufferedImage icon = switch (i) {
                case 1 -> swordIcon;
                case 2 -> pickaxeIcon;
                case 3 -> bowIcon;
                default -> null;
            };

            if (icon != null) {
                g2.setColor(Color.BLACK);
                g2.fillRect(weaponX - 2, weaponY - 2, weaponIconSize + 4, weaponIconSize + 4);

                if (selectedWeapon == i) {
                    g2.setColor(Color.YELLOW);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRect(weaponX - 2, weaponY - 2, weaponIconSize + 4, weaponIconSize + 4);
                }


                g2.drawImage(icon, weaponX, weaponY, weaponIconSize, weaponIconSize, null);

                String keyLabel = String.valueOf(i);
                FontMetrics fm = g2.getFontMetrics();
                int labelX = weaponX + (weaponIconSize - fm.stringWidth(keyLabel)) / 2;
                int labelY = weaponY + weaponIconSize + fm.getHeight();
                g2.setColor(Color.WHITE);
                g2.drawString(keyLabel, labelX, labelY);
            }

            weaponX += weaponIconSize + weaponSpacing;
        }
    }
    public void drawCrystalsHUD(Graphics2D g2, Player player, int startX, int baselineY) {
        int iconSize = 32;
        int spacing = 10;


        OBJ_Crystals redCrystalHUD = new OBJ_Crystals("red");
        OBJ_Crystals blueCrystalHUD = new OBJ_Crystals("blue");
        OBJ_Crystals pinkCrystalHUD = new OBJ_Crystals("pink");
        OBJ_Crystals blackCrystalHUD = new OBJ_Crystals("black");

        int x = startX;
        int y = baselineY - iconSize + 6;

        Font originalFont = g2.getFont();
        Font smallFont = originalFont.deriveFont(originalFont.getSize() * 0.7f);

        java.util.function.BiFunction<Integer, Integer, Integer> drawCount = (count, posX) -> {
            g2.setFont(smallFont);
            g2.drawString("x", posX, baselineY);
            int smallXWidth = g2.getFontMetrics().stringWidth("x");
            posX += smallXWidth + 2;

            g2.setFont(originalFont);
            String countStr = String.valueOf(count);
            g2.drawString(countStr, posX, baselineY);
            posX += g2.getFontMetrics().stringWidth(countStr) + spacing;

            return posX;
        };
        // Deseneaza cristal rosu
        g2.drawImage(redCrystalHUD.image, x, y, iconSize, iconSize, null);
        x += iconSize + 5;
        x = drawCount.apply(crystalsRed, x);

        // Deseneaza cristal albastru
        g2.drawImage(blueCrystalHUD.image, x, y, iconSize, iconSize, null);
        x += iconSize + 5;
        x = drawCount.apply(crystalsBlue, x);

        // Deseneaza cristal roz
        g2.drawImage(pinkCrystalHUD.image, x, y, iconSize, iconSize, null);
        x += iconSize + 5;
        x = drawCount.apply(crystalsPink, x);

        // Deseneaza cristal negru
        g2.drawImage(blackCrystalHUD.image, x, y, iconSize, iconSize, null);
        x += iconSize + 5;
        x = drawCount.apply(crystalsBlack, x);

        // Deseneaza cristal mov
        g2.drawImage(purpleCrystalHUD.image, x, y, iconSize, iconSize, null);
        x += iconSize + 5;
        x = drawCount.apply(crystalsPurple, x);
    }
    @Override
    public void run() {

        double drawInterval = 1000000000.0 / FPS; //0.01666 sec
        double nextDrawTime = System.nanoTime() + drawInterval;


        while(gameThread !=null)
        {
            update();

            repaint();

            try {
                double remainingTime =nextDrawTime-System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime<0)
                {
                    remainingTime=0;
                }
                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void restartGame() {
        player.lives = 3;
        player.hp = 100;
        player.score = 0;
        level = 1;
        player.setDefaultValues();
        gameOver = false;
        player.velocityY = 0;
        player.jumping = false;
        crystalsRed = 0;
        crystalsBlue = 0;
        crystalsPink = 0;
        crystalsBlack = 0;
        crystalsPurple = 0;
        aSetter.setNPC();
        aSetter.setObject();
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].collected = false;
            }
        }
        db.saveWorldObjects(obj);
        db.loadWorldObjects(obj, level);
        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) {
                npc[i].alive = true;
            }
        }
        db.saveWorldNPCs(npc);
        db.loadWorldNPCs(this, npc, level);
        loadLevel();
    }

    public void loadLevel() {
        tileM.loadLevel(level);
        if(level == 1) {
            tileM.loadTilesFromSpritesheetLevel1();
        } else if (level == 2) {
            tileM.loadTilesFromSpritesheetLevel2();
        } else if (level == 3) {
            tileM.loadTilesFromSpritesheetLevel3();
        }
        if(nextLevel) player.setDefaultValues();
    }

    public void drawGameFinishScreen(Graphics2D g2) {
        if(ui.backgroundImage !=null)
        {
            g2.drawImage(ui.backgroundImage, 0, 0,null);
        }
        int menuWidth = (ui.menuImage.getWidth() * 2);
        int menuHeight = (ui.menuImage.getHeight() * 2);
        int menuX = 1024 / 2 - menuWidth / 2;
        if(ui.menuImage !=null)
        {
            g2.drawImage(ui.menuImage, menuX,60, menuWidth, menuHeight,null);
        }
        //g2.setColor(Color.BLACK);
        //g2.fillRect(0, 0, screenWidth, screenHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        String text = "Game Over";
        int textX = getCenteredTextX(g2, text);
        g2.drawString(text, textX, screenHeight / 2 - 100);

        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        String scoreText = "Score: " + player.score;
        textX = getCenteredTextX(g2, scoreText);
        g2.drawString(scoreText, textX, screenHeight / 2 - 40);

        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        String bestScoreText = "Highest Score: " + highestScore;
        textX = getCenteredTextX(g2, bestScoreText);
        g2.drawString(bestScoreText, textX, screenHeight / 2);

        String continueText = "Press ENTER to return to Main Menu";
        textX = getCenteredTextX(g2, continueText);
        g2.drawString(continueText, textX, screenHeight / 2 + 60);
    }

    public int getCenteredTextX(Graphics2D g2, String text) {
        FontMetrics fm = g2.getFontMetrics();
        return (screenWidth - fm.stringWidth(text)) / 2;
    }
    public void update()
    {
        if (gameState == gameOverState) {
            gameOverCounter++;

            if (gameOverCounter > 180) {
                gameOverCounter = 0;
                gameState = titleState;
                restartGame();
            }
        } else if (gameState == dyingState) {
            gameOverCounter++;
            if (gameOverCounter > 180) {
                gameOverCounter = 0;
                gameState = playState;
                player.setDefaultValues();
                player.velocityY = 0;
                player.jumping = false;
                player.hp = 100;
            }
        }

        if(gameState == playState){
            player.update();
            purpleCrystalHUD.update();

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null && npc[i].alive) {
                    npc[i].update();
                    if (!npc[i].alive && !npc[i].dying) {
                        if (npc[i] instanceof NPC_Samurai && !samuraiKilled) {
                            System.out.println("Samurai KILLED!");
                            player.score += 10;
                            samuraiKilled = true;
                            samuraiKillTime = System.currentTimeMillis();
                            System.out.println("Samurai defeated! Advancing to next level in 3 seconds...");
                        }
                        if (npc[i] instanceof NPC_Goblin) {
                            System.out.println("Goblin KILLED!");
                            player.score += 5;
                        }
                        if (npc[i] instanceof NPC_Warrior) {
                            System.out.println("Warrior KILLED!");
                            player.score += 10;
                        }
                        if (npc[i] instanceof NPC_FlyingEye) {
                            System.out.println("FlyingEye KILLED!");
                            player.score += 5;
                        }
                        if (npc[i] instanceof NPC_Sardonix && !sardonixKilled) {
                            System.out.println("Sardonix KILLED!");
                            player.score += 20;
                            sardonixKilled = true;
                        }
                    }
                }
            }
            if (samuraiKilled) {
                long now = System.currentTimeMillis();
                if (now - samuraiKillTime >= 3000) {
                    level++;
                    nextLevel = true;
                    samuraiKilled = false;
                }
            }

            if (endGameScheduled && System.currentTimeMillis() - finalCrystalPicked >= 3000) {
                saveGame();
                highestScore = db.getHighestScore();
                endGameScheduled = false;
                sardonixKilled = false;
                gameState = gameFinished;
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null && !obj[i].collected) {
                    obj[i].update();
                }
            }

            if(nextLevel){
                loadLevel();
                db.loadWorldNPCs(this, npc, level);

                nextLevel = false;
            }
        } else if (gameState == dialogueState) {
            dialogueTimer++;
            if (dialogueTimer >= dialogueDelay) {
                dialogueTimer = 0;
                dialogueIndex++;

                if (currentDialogueNPC instanceof NPC_Samurai) {
                    NPC_Samurai samurai = (NPC_Samurai) currentDialogueNPC;

                    if (dialogueIndex >= samurai.dialogueImages.length) {
                        // Se termina dialogul
                        gameState = playState;
                        currentDialogueNPC = null;
                        dialogueIndex = 0;
                    }
                }
                if (currentDialogueSardonix instanceof NPC_Sardonix) {
                    NPC_Sardonix sardonix = (NPC_Sardonix) currentDialogueSardonix;

                    if (dialogueIndex >= sardonix.dialogueImages.length) {
                        // Se termina dialogul
                        gameState = playState;
                        currentDialogueSardonix = null;
                        dialogueIndex = 0;
                    }
                }
            }
        }
        if(gameState == pauseState)
        {

        }
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2=(Graphics2D)g;
        //TITLE SCREEN
        if(gameState == titleState)
        {
            ui.draw(g2);
        }
        else if (gameState == gameOverState) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);

            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 64));
            g2.drawString("GAME OVER", screenWidth / 2 - 180, screenHeight / 2);
        } else if (gameState == dyingState) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);

            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 64));
            g2.drawString("YOU'RE DEAD!!", screenWidth / 2 - 180, screenHeight / 2);
        } else if (gameState == gameFinished) {
            drawGameFinishScreen(g2);
        } else if (gameState == playState || gameState == pauseState || gameState == dialogueState) {
            //TILE
            if(level == 1)
            {
                tileM.drawLevel1(g2, obj);
            } else if (level == 2)
            {
                tileM.drawLevel2(g2, obj);
                for (int i = 0; i < npc.length; i++) {
                    if (npc[i] != null && npc[i].alive && npc[i].level == 2) {
                        npc[i].draw(g2);
                    }
                }
            } else if (level == 3) {
                tileM.drawLevel3(g2, obj);
                for (int i = 0; i < npc.length; i++) {
                    if (npc[i] != null && npc[i].alive && npc[i].level == 3) {
                        npc[i].draw(g2);
                    }
                }

                if(sardonixKilled) {
                    for (int i = 0; i < obj.length; i++) {
                        if (obj[i] != null && obj[i].level == level && !obj[i].collected && obj[i].name.equals("CrystalFinal")) {
                            obj[i].draw(g2, this);
                        }
                    }
                }
            }
            //PLAYER
            player.draw(g2);
            //UI
            ui.draw(g2);
            if (gameState == dialogueState && currentDialogueNPC instanceof NPC_Samurai) {
                NPC_Samurai samurai = (NPC_Samurai) currentDialogueNPC;

                if (samurai.dialogueImages != null && dialogueIndex < samurai.dialogueImages.length) {
                    BufferedImage image = samurai.dialogueImages[dialogueIndex];
                    if (image != null) {
                        int x = (screenWidth - image.getWidth()) / 2;
                        int y = 50;

                        g2.drawImage(image, x, y, null);
                    }
                }
            }
            if (gameState == dialogueState && currentDialogueSardonix instanceof NPC_Sardonix) {
                NPC_Sardonix sardonix = (NPC_Sardonix) currentDialogueSardonix;

                if (sardonix.dialogueImages != null && dialogueIndex < sardonix.dialogueImages.length) {
                    BufferedImage image = sardonix.dialogueImages[dialogueIndex];
                    if (image != null) {
                        int x = (screenWidth - image.getWidth()) / 2;
                        int y = 50;

                        g2.drawImage(image, x, y, null);
                    }
                }
            }
        }
        g2.dispose();
    }
}
