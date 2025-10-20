package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import Main.EntityType;
import Object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class Player extends Entity {


    GamePanel gp;
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public boolean attackChecked = false;

    BufferedImage playerSpritesheet;


    double jumpStrength = -10;

    int attackCounter = 0; //for sword attack
    public int lives = 3;
    public int hp = 100;
    public int score = 0;



    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.entityType = EntityType.PLAYER;

        screenX = gp.screenWidth / 2 - 7 * gp.tileSize; //de modificat
        screenY = gp.screenHeight / 2;

        solidArea = new Rectangle(50, 105, 15, 85);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;



        setDefaultValues();
        getPlayerImageFromSpritesheet();

    }

    public void setDefaultValues() {
        if (gp.level == 1) {
            worldX = gp.tileSize;
            worldY = gp.tileSize * 7;
        } else if (gp.level == 2) {
            worldX = gp.tileSize * 3;
            worldY = gp.tileSize * 6;
        } else if (gp.level == 3) {
            worldX = gp.tileSize;
            worldY = gp.tileSize;
        }
        speed = 4;
        direction = "down";
    }

    private BufferedImage getSprite(int col, int row, int tileSize) {
        return playerSpritesheet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
    }

    public void getPlayerImageFromSpritesheet() {
        try {
            int tileSize = 64; // your sprite size
            InputStream is = getClass().getResourceAsStream("/res/player/playerSpritesheetFinal.png");
            if (is == null) {
                System.err.println("Imaginea NU a fost găsită!");
            } else {
                playerSpritesheet = ImageIO.read(is);
            }
            //attack
            attack1 = getSprite(0, 0, tileSize);
            attack2 = getSprite(1, 0, tileSize);
            attack3 = getSprite(2, 0, tileSize);
            attack4 = getSprite(3, 0, tileSize);
            attack5 = getSprite(4, 0, tileSize);
            attack6 = getSprite(5, 0, tileSize);
            attack7 = getSprite(6, 0, tileSize);
            attack8 = getSprite(7, 0, tileSize);

            //run to right
            run1 = getSprite(0, 1, tileSize);
            run2 = getSprite(1, 1, tileSize);
            run3 = getSprite(2, 1, tileSize);
            run4 = getSprite(3, 1, tileSize);

            //run to left
            runleft1 = getSprite(0, 2, tileSize);
            runleft2 = getSprite(1, 2, tileSize);
            runleft3 = getSprite(2, 2, tileSize);
            runleft4 = getSprite(3, 2, tileSize);

            //jump to right
            sare1 = getSprite(0, 3, tileSize);
            sare2 = getSprite(1, 3, tileSize);
            sare3 = getSprite(2, 3, tileSize);

            //idle
            sta1 = getSprite(0, 4, tileSize);
            sta2 = getSprite(1, 4, tileSize);
            sta3 = getSprite(2, 4, tileSize);
            sta4 = getSprite(3, 4, tileSize);

            //jump to left
            sareleft1 = getSprite(0, 5, tileSize);
            sareleft2 = getSprite(1, 5, tileSize);
            sareleft3 = getSprite(2, 5, tileSize);

            //attack with pickaxe
            tarnacop1 = getSprite(0, 6, tileSize);
            tarnacop2 = getSprite(1, 6, tileSize);
            tarnacop3 = getSprite(2, 6, tileSize);

            //attack with bow&arrow
            arc1 = getSprite(0, 7, tileSize);
            arc2 = getSprite(1, 7, tileSize);
            arc3 = getSprite(2, 7, tileSize);
            arc4 = getSprite(3, 7, tileSize);
            arc5 = getSprite(4, 7, tileSize);
            arc6 = getSprite(5, 7, tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAttackOnNPC() {
        for (Entity npc : gp.npc) {
            if (npc != null && npc.alive) {
                Rectangle attackArea = new Rectangle();
                int attackWidth = 100;
                int attackHeight = 80;

                if (direction.equals("right")) {
                    attackArea.setBounds(worldX + solidArea.x + solidArea.width, worldY + solidArea.y, attackWidth, attackHeight);
                } else if (direction.equals("left")) {
                    attackArea.setBounds(worldX + solidArea.x - attackWidth, worldY + solidArea.y, attackWidth, attackHeight);
                } else if (direction.equals("up")) {
                    attackArea.setBounds(worldX + solidArea.x, worldY + solidArea.y - attackHeight, attackHeight, attackWidth);
                } else if (direction.equals("down")) {
                    attackArea.setBounds(worldX + solidArea.x, worldY + solidArea.y + solidArea.height, attackHeight, attackWidth);
                }

                Rectangle npcHitbox = new Rectangle(npc.worldX + npc.solidArea.x, npc.worldY + npc.solidArea.y, npc.solidArea.width, npc.solidArea.height);

                if (attackArea.intersects(npcHitbox)) {
                    if (gp.selectedWeapon == 1) {
                        int damage = 20 + new Random().nextInt(31); // 20 to 50 damage
                        npc.takeDamage(damage);
                    }
                }
            }
        }
    }

    public void checkAttackOnObject() {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                SuperObject obj = gp.obj[i];

                if (obj.level != gp.level) {
                    continue;  // skip objects not in the current level
                }

                Rectangle playerHitbox = new Rectangle(
                        worldX + solidArea.x,
                        worldY + solidArea.y,
                        solidArea.width,
                        solidArea.height + 1
                );

                Rectangle objectHitbox = new Rectangle(
                        obj.worldX + obj.solidArea.x,
                        obj.worldY + obj.solidArea.y,
                        obj.solidArea.width,
                        obj.solidArea.height
                );

                Rectangle attackArea = new Rectangle();
                int attackWidth = 40;
                int attackHeight = 80;

                if (direction.equals("right")) {
                    attackArea.setBounds(worldX + solidArea.x + solidArea.width, worldY + solidArea.y, attackWidth, attackHeight);
                } else if (direction.equals("left")) {
                    attackArea.setBounds(worldX + solidArea.x - attackWidth, worldY + solidArea.y, attackWidth, attackHeight);
                } else if (direction.equals("up")) {
                    attackArea.setBounds(worldX + solidArea.x, worldY + solidArea.y - attackHeight, attackHeight, attackWidth);
                } else if (direction.equals("down")) {
                    attackArea.setBounds(worldX + solidArea.x, worldY + solidArea.y + solidArea.height, attackHeight, attackWidth);
                }

                if (gp.selectedWeapon == 2 && (attackArea.intersects(objectHitbox) || playerHitbox.intersects(objectHitbox))) {
                    if (obj.breakable) {
                        obj.durability--;

                        if (obj.durability <= 0 && !obj.collected) {
                            if (obj.name.equals("Crystal_red")) {
                                System.out.println("Red crystal picked up!");
                                score += 3;
                                gp.crystalsRed++;
                            } else if (obj.name.equals("Crystal_blue")) {
                                System.out.println("Blue crystal picked up!");
                                score += 2;
                                gp.crystalsBlue++;
                            } else if (obj.name.equals("Crystal_pink")) {
                                System.out.println("Pink crystal picked up!");
                                score += 2;
                                gp.crystalsPink++;
                            } else if (obj.name.equals("Crystal_black")) {
                                System.out.println("Black crystal picked up!");
                                score += 1;
                                gp.crystalsBlack++;
                            }
                            obj.collected = true;
                        }
                    }
                }
            }
        }
    }

    public void checkPickupObject() {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                SuperObject obj = gp.obj[i];

                if (obj.level != gp.level || obj.collected) {
                    continue;  // skip objects not in the current level
                }

                if (obj.breakable) {
                    continue;  // skip breakable objects in pickup check
                }

                // Player's hitbox
                Rectangle playerHitbox = new Rectangle(
                        worldX + solidArea.x - 4, // expand left
                        worldY + solidArea.y - 4, // expand up
                        solidArea.width + 8,      // expand width (left + right)
                        solidArea.height + 8      // expand height (up + down)
                );

                // Object's hitbox
                Rectangle objectHitbox = new Rectangle(
                        obj.worldX + obj.solidArea.x,
                        obj.worldY + obj.solidArea.y,
                        obj.solidArea.width,
                        obj.solidArea.height
                );

                if (playerHitbox.intersects(objectHitbox)) {
                    if (obj.collected) continue; // redundant but safe

                    obj.collected = true;  // mark collected right away

                    switch (obj.name) {
                        case "Map":
                            System.out.println("Map picked up!");
                            score += 5;
                            gp.nextLevel = true;
                            gp.level = 2;
                            break;
                        case "Crystal_purple":
                            System.out.println("Purple crystal picked up!");
                            score += 3;
                            gp.crystalsPurple++;
                            break;
                        case "CrystalFinal":
                            System.out.println("Final Crystal picked up!");
                            score += 20;
                            gp.endGameScheduled = true;
                            gp.finalCrystalPicked = System.currentTimeMillis();
                            break;
                    }
                }
            }
        }
    }

    public void checkBowAttackOnNPC() {
        int bowRange = 300;  // Range distance in pixels
        int bowWidth = 200;  // Optional: width for vertical/horizontal tolerance

        for (Entity npc : gp.npc) {
            if (npc != null && npc.alive) {
                int dx = npc.worldX + npc.solidArea.x + npc.solidArea.width / 2 - (worldX + solidArea.x + solidArea.width / 2);
                int dy = npc.worldY + npc.solidArea.y + npc.solidArea.height / 2 - (worldY + solidArea.y + solidArea.height / 2);

                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= bowRange) {
                    // Check if NPC is roughly in the facing direction
                    boolean inRange = false;

                    if (direction.equals("right") && dx > 0 && Math.abs(dy) < bowWidth) {
                        inRange = true;
                    } else if (direction.equals("left") && dx < 0 && Math.abs(dy) < bowWidth) {
                        inRange = true;
                    } else if (direction.equals("up") && dy < 0 && Math.abs(dx) < bowWidth) {
                        inRange = true;
                    } else if (direction.equals("down") && dy > 0 && Math.abs(dx) < bowWidth) {
                        inRange = true;
                    }

                    if (inRange) {
                        int damage = 15 + new Random().nextInt(16); // 15 to 30 damage, for example
                        npc.takeDamage(damage);
                    }
                }
            }
        }
    }

    public void checkMeetSamuraiRange() {
        int range = 200;  // distance in pixels

        for (Entity npc : gp.npc) {
            if (npc != null && npc.alive) {
                if (npc instanceof NPC_Samurai) {
                    NPC_Samurai samurai = (NPC_Samurai) npc;

                    // Skip if dialogue already done
                    if (samurai.dialogueDone) {
                        continue;
                    }

                    // Calculate distance between player and the samurai NPC
                    int playerCenterX = worldX + solidArea.x + solidArea.width / 2;
                    int playerCenterY = worldY + solidArea.y + solidArea.height / 2;

                    int npcCenterX = samurai.worldX + samurai.solidArea.x + samurai.solidArea.width / 2;
                    int npcCenterY = samurai.worldY + samurai.solidArea.y + samurai.solidArea.height / 2;

                    int dx = playerCenterX - npcCenterX;
                    int dy = playerCenterY - npcCenterY;

                    double distance = Math.sqrt(dx * dx + dy * dy);
                    //SAMURAI
                    if (distance <= range && gp.cChecker.isOnGround(this) && gp.level == samurai.level) {
                        samurai.dialogueDone = true;

                        // Begin image-based dialogue
                        gp.dialogueIndex = 0;
                        gp.currentDialogueNPC = samurai;
                        gp.gameState = gp.dialogueState;
                        break;
                    }
                }
            }
        }
    }
    public void checkMeetSardonixRange() {
        int range = 200;  // distance in pixels

        for (Entity npc : gp.npc) {
            if (npc != null && npc.alive) {
                if (npc instanceof NPC_Sardonix) {
                    NPC_Sardonix sardonix = (NPC_Sardonix) npc;

                    // Skip if dialogue already done
                    if (sardonix.dialogueDone) {
                        continue;
                    }

                    // Calculate distance between player and the samurai NPC
                    int playerCenterX = worldX + solidArea.x + solidArea.width / 2;
                    int playerCenterY = worldY + solidArea.y + solidArea.height / 2;

                    int npcCenterX = sardonix.worldX + sardonix.solidArea.x + sardonix.solidArea.width / 2;
                    int npcCenterY = sardonix.worldY + sardonix.solidArea.y + sardonix.solidArea.height / 2;

                    int dx = playerCenterX - npcCenterX;
                    int dy = playerCenterY - npcCenterY;

                    double distance = Math.sqrt(dx * dx + dy * dy);
                    //SARDONIX
                    if (distance <= range && gp.cChecker.isOnGround(this) && gp.level == sardonix.level) {
                        sardonix.dialogueDone = true;

                        // Begin image-based dialogue
                        gp.dialogueIndex = 0;
                        gp.currentDialogueSardonix = sardonix;
                        gp.gameState = gp.dialogueState;
                        break;
                    }
                }
            }
        }
    }
    public void update() {
        if (gp.gameState != gp.playState) {
            return;
        }
        boolean moving = false;
        checkMeetSamuraiRange();
        checkMeetSardonixRange();
        if (gp.gameState == gp.dialogueState) {
            return;
        }
        checkPickupObject();


        int oldWorldX = worldX;
        int oldWorldY = worldY;
        // --- HORIZONTAL MOVEMENT ---
        horizontalCollision = false;

        if (keyH.leftPressed) {
            direction = "left";
            gp.cChecker.checkHorizontalTile(this);
            if (!horizontalCollision) {
                worldX -= speed;
                moving = true;
            }
        }
        if (keyH.rightPressed) {
            direction = "right";
            gp.cChecker.checkHorizontalTile(this);
            if (!horizontalCollision) {
                worldX += speed;
                moving = true;
            }
        }

        if (worldY > gp.screenHeight + 300) {
            lives--;

            if (lives <= 0) {
                gp.gameOver = true;
                gp.gameState = gp.gameOverState;
            } else {
                gp.gameState = gp.dyingState;
            }
        }

        // --- JUMPING ---
        if (keyH.upPressed && !jumping && gp.cChecker.isOnGround(this)) {
            velocityY = +jumpStrength;  // or jumpStrength, based on coordinate system
            jumping = true;
        }

        // --- GRAVITY ---
        velocityY += gravity;

        // --- VERTICAL MOVEMENT ---
        worldY += velocityY;
        verticalCollision = false;
        gp.cChecker.checkVerticalTile(this);


        if (velocityY > 0 && verticalCollision) { // falling and hit ground
            int tileRow = (worldY + solidArea.y + solidArea.height) / gp.tileSize;
            worldY = tileRow * gp.tileSize - solidArea.y - solidArea.height;
            velocityY = 0;
            jumping = false;
        }
        else if (velocityY < 0 && verticalCollision) { // jumping and hit ceiling
            int tileRow = (worldY + solidArea.y) / gp.tileSize;
            worldY = (tileRow + 1) * gp.tileSize - solidArea.y;
            velocityY = 0;
        }
        gp.cChecker.checkEntityCollision(this, gp.npc);
        if (this.entityCollision && this.contactEntity != null && this.contactEntity.entityType == EntityType.NPC) {

            int bounceBackSpeed = 5;

            if (direction.equals("left")) {
                worldX += bounceBackSpeed;
            } else if (direction.equals("right")) {
                worldX -= bounceBackSpeed;
            } else if (direction.equals("up")) {
                worldY += bounceBackSpeed;
            } else if (direction.equals("down")) {
                worldY -= bounceBackSpeed;
            }

        }
        if (keyH.attackPressed && !attacking) {
            attacking = true;
            attackCounter = 0;
            spriteNum = 1;
            attackChecked = false;
            keyH.attackPressed = false;
        }

        if (attacking) {
            attackCounter++;

            if (gp.selectedWeapon == 1) {
                // Sword attack
                if (attackCounter % 5 == 0) {
                    spriteNum++;
                    if (spriteNum == 4) {
                        checkAttackOnNPC();
                    }
                    if (spriteNum > 8) {
                        spriteNum = 1;
                        attacking = false;
                    }
                }

            } else if (gp.selectedWeapon == 2) {
                // Pickaxe attack
                if (!attackChecked) {
                    checkAttackOnObject();
                    attackChecked = true;
                }
                if (attackCounter % 7 == 0) {
                    spriteNum++;
                    if (spriteNum > 3) {
                        spriteNum = 1;
                        attacking = false;
                        attackChecked = false;
                    }
                }

            } else if (gp.selectedWeapon == 3) {
                if (attackCounter % 5 == 0) {
                    spriteNum++;
                    if (spriteNum == 3) {
                        checkBowAttackOnNPC();
                    }
                    if (spriteNum > 6) {
                        spriteNum = 1;
                        attacking = false;
                    }
                }
            }
        }

        if (moving || jumping) {
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum++;
                if (spriteNum > 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

    }
    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;


        if (attacking) {
            switch (gp.selectedWeapon) {
                case 1 -> { // Sword
                    switch (spriteNum) {
                        case 1 -> image = attack1;
                        case 2 -> image = attack2;
                        case 3 -> image = attack3;
                        case 4 -> image = attack4;
                        case 5 -> image = attack5;
                        case 6 -> image = attack6;
                        case 7 -> image = attack7;
                        case 8 -> image = attack8;
                    }
                }

                case 2 -> { // Pickaxe
                    switch (spriteNum) {
                        case 1 -> image = tarnacop1;
                        case 2 -> image = tarnacop2;
                        case 3 -> image = tarnacop3;
                    }
                }

                case 3 -> { // Bow
                    switch (spriteNum) {
                        case 1 -> image = arc1;
                        case 2 -> image = arc2;
                        case 3 -> image = arc3;
                        case 4 -> image = arc4;
                        case 5 -> image = arc5;
                        case 6 -> image = arc6;
                    }
                }
            }
        } else if (jumping) {
            if (direction.equals("right")) {
                if (spriteNum == 1) image = sare1;
                if (spriteNum == 2) image = sare2;
                if (spriteNum == 3) image = sare3;
            } else {
                if (spriteNum == 1) image = sareleft1;
                if (spriteNum == 2) image = sareleft2;
                if (spriteNum == 3) image = sareleft3;
            }
        } else {
            if (keyH.leftPressed || keyH.rightPressed) {
                switch (direction) {
                    case "left":
                        switch (spriteNum) {
                            case 1 -> image = runleft1;
                            case 2 -> image = runleft2;
                            case 3 -> image = runleft3;
                            case 4 -> image = runleft4;
                        }
                        break;
                    case "right":
                        switch (spriteNum) {
                            case 1 -> image = run1;
                            case 2 -> image = run2;
                            case 3 -> image = run3;
                            case 4 -> image = run4;
                        }
                        break;
                }
            } else {
                switch (spriteNum) {
                    case 1 -> image = sta1;
                    case 2 -> image = sta2;
                    case 3 -> image = sta3;
                    case 4 -> image = sta4;
                }
            }
        }

        g2.drawImage(image, screenX, screenY + gp.tileSize, gp.tileSize * 2, gp.tileSize * 2, null);
    }
}
