package Entity;

import Main.EntityType;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class NPC_Samurai extends Entity{
    BufferedImage NPCSpritesheet;

    public Rectangle attackHitbox = new Rectangle(0, 0, 64, 64);
    public boolean attackHitApplied = false;
    public int chaseRange = 200;   // Distance in pixels to start chasing player
    public int attackRange = 64;
    public boolean dialogueDone = false;// Distance in pixels to start attacking
    public BufferedImage[] dialogueImages = new BufferedImage[4];

    public NPC_Samurai(GamePanel gp)
    {
        this.gp = gp;
        this.collision = true;
        this.entityType = EntityType.NPC;
        this.type = "Samurai";
        direction = "left";
        speed = 1;
        solidArea = new Rectangle(95, 160, 5, 85);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getNPCImageFromSpritesheet();
        loadDialogueImages();
    }
    public void getNPCImageFromSpritesheet() {
        try {
            int tileSize = 64;
            InputStream is = getClass().getResourceAsStream("/res/npc/level2/spritesheet-nivel2-samurai.png");
            if (is == null) {
                System.err.println("Imaginea NU a fost găsită!");
                return;
            }
            NPCSpritesheet = ImageIO.read(is);
            //run left
            runleft1samurai = getSprite(8, 6, tileSize);
            runleft2samurai = getSprite(7, 6, tileSize);
            runleft3samurai = getSprite(6, 6, tileSize);
            runleft4samurai = getSprite(5, 6, tileSize);
            runleft5samurai = getSprite(4, 6, tileSize);
            runleft6samurai = getSprite(3, 6, tileSize);
            runleft7samurai = getSprite(2, 6, tileSize);
            runleft8samurai = getSprite(1, 6, tileSize);
            attackleft1Samurai = getSprite(8, 7, tileSize);
            attackleft2Samurai = getSprite(7, 7, tileSize);
            attackleft3Samurai = getSprite(6, 7, tileSize);
            attackleft4Samurai = getSprite(5, 7, tileSize);
            attackleft5Samurai = getSprite(4, 7, tileSize);
            //run right
            runright1samurai = getSprite(0, 1, tileSize);
            runright2samurai = getSprite(1, 1, tileSize);
            runright3samurai = getSprite(2, 1, tileSize);
            runright4samurai = getSprite(3, 1, tileSize);
            runright5samurai = getSprite(4, 1, tileSize);
            runright6samurai = getSprite(5, 1, tileSize);
            runright7samurai = getSprite(6, 1, tileSize);
            runright8samurai = getSprite(7, 1, tileSize);
            attackright1Samurai = getSprite(0, 2, tileSize);
            attackright2Samurai = getSprite(1, 2, tileSize);
            attackright3Samurai = getSprite(2, 2, tileSize);
            attackright4Samurai = getSprite(3, 2, tileSize);
            attackright5Samurai = getSprite(4, 2, tileSize);

            //dead left
            deadleft1Samurai = getSprite(8, 9, tileSize);
            deadleft2Samurai = getSprite(7, 9, tileSize);
            deadleft3Samurai = getSprite(6, 9, tileSize);
            deadleft4Samurai = getSprite(5, 9, tileSize);
            deadleft5Samurai = getSprite(4, 9, tileSize);
            deadleft6Samurai = getSprite(3, 9, tileSize);

            //dead right
            deadright1Samurai = getSprite(0, 4, tileSize);
            deadright2Samurai = getSprite(1, 4, tileSize);
            deadright3Samurai = getSprite(2, 4, tileSize);
            deadright4Samurai = getSprite(3, 4, tileSize);
            deadright5Samurai = getSprite(4, 4, tileSize);
            deadright6Samurai = getSprite(5, 4, tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private BufferedImage getSprite(int col, int row, int tileSize) {
        return NPCSpritesheet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
    }
    private BufferedImage resizeImage(BufferedImage original, int targetWidth, int targetHeight) {
        Image tmp = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }
    public void loadDialogueImages() {
        dialogueImages = new BufferedImage[4];

        try {
            BufferedImage rawImage = ImageIO.read(getClass().getResourceAsStream("/res/dialogue/samurai/dialog1.png"));
            dialogueImages[0] = resizeImage(rawImage, 600, 340);
            rawImage = ImageIO.read(getClass().getResourceAsStream("/res/dialogue/samurai/dialog1npc.png"));
            dialogueImages[1] = resizeImage(rawImage, 600, 340);
            rawImage = ImageIO.read(getClass().getResourceAsStream("/res/dialogue/samurai/dialog2.png"));
            dialogueImages[2] = resizeImage(rawImage, 600, 340);
            rawImage = ImageIO.read(getClass().getResourceAsStream("/res/dialogue/samurai/dialog2npc.png"));
            dialogueImages[3] = resizeImage(rawImage, 600, 340);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //@Override
    @Override
    public void setAction() {
        // Calculate distance to player
        int distanceX = gp.player.worldX - this.worldX;
        int distanceY = gp.player.worldY - this.worldY;
        double distanceToPlayer = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distanceToPlayer <= attackRange) {
            // Player is in attack range: stop moving and attack
            direction = (distanceX < 0) ? "left" : "right";
            attacking = true;

        } else if (distanceToPlayer <= chaseRange) {
            // Player is within chase range but outside attack range: chase player
            attacking = false;
            direction = (distanceX < 0) ? "left" : "right";

        } else {
            // Player is out of chase range: wander randomly every 2 seconds (120 frames)
            attacking = false;
            actionLockCounter++;
            if (actionLockCounter == 120) {
                Random random = new Random();
                int moveChance = random.nextInt(100) + 1;
                direction = (moveChance <= 50) ? "left" : "right";
                actionLockCounter = 0;
            }
        }
    }

    public void updateAnimation() {
        if (dying) {
            deathCounter++;

            if (deathCounter <= 5) spriteNum = 1;
            else if (deathCounter <= 10) spriteNum = 2;
            else if (deathCounter <= 15) spriteNum = 3;
            else if (deathCounter <= 20) spriteNum = 4;
            else if (deathCounter <= 25) spriteNum = 5;
            else if (deathCounter <= 30) spriteNum = 6;
            else {
                spriteNum = 6; // stay on final frame
            }

            return; // skip walking/attack if dying
        }
        if (attacking) {
            attackCounter++;
            if (attackCounter <= 5) spriteNum = 9;
            else if (attackCounter <= 10) spriteNum = 10;
            else if (attackCounter <= 15) spriteNum = 11;
            else if (attackCounter <= 20) spriteNum = 12;
            else if (attackCounter <= 25) spriteNum = 13;
            else {
                attacking = false;
                attackCounter = 0;
                spriteNum = 1;
                attackHitApplied = false;
            }
        } else {
            // Walking animation
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum++;
                if (spriteNum > 8) spriteNum = 1;
                spriteCounter = 0;
            }
        }
    }
    @Override
    public void draw(Graphics2D g2) {

        if (!alive && !dying || (this.level != gp.level)) return;
        BufferedImage image = null;

        if (dying) {
            // Dying takes priority over direction or attacking
            if (direction.equals("left")) {
                if (spriteNum == 1) image = deadleft1Samurai;
                if (spriteNum == 2) image = deadleft2Samurai;
                if (spriteNum == 3) image = deadleft3Samurai;
                if (spriteNum == 4) image = deadleft4Samurai;
                if (spriteNum == 5) image = deadleft5Samurai;
                if (spriteNum == 6) image = deadleft6Samurai;
            } else {
                if (spriteNum == 1) image = deadright1Samurai;
                if (spriteNum == 2) image = deadright2Samurai;
                if (spriteNum == 3) image = deadright3Samurai;
                if (spriteNum == 4) image = deadright4Samurai;
                if (spriteNum == 5) image = deadright5Samurai;
                if (spriteNum == 6) image = deadright6Samurai;
            }
        } else {
            // Movement/attack logic
            switch(direction) {
                case "right":
                    if (spriteNum == 1) image = runright1samurai;
                    if (spriteNum == 2) image = runright2samurai;
                    if (spriteNum == 3) image = runright3samurai;
                    if (spriteNum == 4) image = runright4samurai;
                    if (spriteNum == 5) image = runright5samurai;
                    if (spriteNum == 6) image = runright6samurai;
                    if (spriteNum == 7) image = runright7samurai;
                    if (spriteNum == 8) image = runright8samurai;
                    if (spriteNum == 9) image = attackright1Samurai;
                    if (spriteNum == 10) image = attackright2Samurai;
                    if (spriteNum == 11) image = attackright3Samurai;
                    if (spriteNum == 12) image = attackright4Samurai;
                    if (spriteNum == 13) image = attackright5Samurai;
                    break;

                case "left":
                    if (spriteNum == 1) image = runleft1samurai;
                    if (spriteNum == 2) image = runleft2samurai;
                    if (spriteNum == 3) image = runleft3samurai;
                    if (spriteNum == 4) image = runleft4samurai;
                    if (spriteNum == 5) image = runleft5samurai;
                    if (spriteNum == 6) image = runleft6samurai;
                    if (spriteNum == 7) image = runleft7samurai;
                    if (spriteNum == 8) image = runleft8samurai;
                    if (spriteNum == 9) image = attackleft1Samurai;
                    if (spriteNum == 10) image = attackleft2Samurai;
                    if (spriteNum == 11) image = attackleft3Samurai;
                    if (spriteNum == 12) image = attackleft4Samurai;
                    if (spriteNum == 13) image = attackleft5Samurai;
                    break;
            }
        }

        // Get screen position relative to player
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY + gp.tileSize, gp.tileSize * 3, gp.tileSize * 3, null);

        // Draw health bar above NPC
        int barWidth = gp.tileSize;       // width same as tile size
        int barHeight = 8;                // height of health bar
        int barX = screenX + gp.tileSize;               // align with NPC sprite left
        int barY = (screenY + gp.tileSize) - barHeight + gp.tileSize;

        // Background (gray)
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);

        // Calculate health percentage
        double healthPercent = (double) hp / maxHp;
        int healthWidth = (int)(barWidth * healthPercent);

        // Health fill (green)
        g2.setColor(Color.GREEN);
        g2.fillRect(barX, barY, healthWidth, barHeight);

        // Black border
        g2.setColor(Color.BLACK);
        g2.drawRect(barX, barY, barWidth, barHeight);
    }
    @Override
    public void update() {
        if (dying) {
            attackHitbox.setBounds(0, 0, 0, 0);
            dyingAnimation();
            return;
        }
        if (!alive) return;

        setAction();
        updateAnimation();

        // Apply damage only once at the precise attack frame
        if (attacking && attackCounter == 11 && !attackHitApplied) {
            Rectangle npcHitbox = new Rectangle(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height);

            if (direction.equals("left")) {
                attackHitbox.setBounds(npcHitbox.x - attackHitbox.width, npcHitbox.y, attackHitbox.width, attackHitbox.height);
            } else {
                attackHitbox.setBounds(npcHitbox.x + npcHitbox.width, npcHitbox.y, attackHitbox.width, attackHitbox.height);
            }

            Rectangle playerHitbox = new Rectangle(gp.player.worldX + gp.player.solidArea.x,
                    gp.player.worldY + gp.player.solidArea.y,
                    gp.player.solidArea.width,
                    gp.player.solidArea.height);

            if (attackHitbox.intersects(playerHitbox)) {
                int damage = 10 + new Random().nextInt(11);
                gp.player.hp -= damage;
                if (gp.player.hp <= 0) {
                    gp.player.lives -= 1;
                    gp.gameState = gp.dyingState;
                }
            }
            attackHitApplied = true;  // prevent double damage this attack animation
        }

        // --- HORIZONTAL COLLISION ---
        horizontalCollision = false;
        gp.cChecker.checkHorizontalTile(this);

        if (!horizontalCollision) {
            if (direction.equals("left")) {
                worldX -= speed;
            } else if (direction.equals("right")) {
                worldX += speed;
            }
        } else {
            // If blocked by any collision (including water), reverse direction
            if (!jumping) {  // Prevent double jumps while already in air
                velocityY = -15;  // Negative velocity to move up (tune the jump strength)
                jumping = true;
            }
            actionLockCounter = 0; // force reevaluation of movement soon
        }
        if (wall) {
            if (direction.equals("left")) {
                direction = "right";
            } else {
                direction = "left";
            }
            wall = false;
            actionLockCounter = 0; // force reevaluation of movement soon
        }

        // --- VERTICAL COLLISION ---
        verticalCollision = false;
        gp.cChecker.checkVerticalTile(this);

        if (velocityY > 0 && verticalCollision) {
            // Falling and hit ground
            int tileRow = (worldY + solidArea.y + solidArea.height) / gp.tileSize;
            worldY = tileRow * gp.tileSize - solidArea.y - solidArea.height;
            velocityY = 0;
            jumping = false;
        } else if (velocityY < 0 && verticalCollision) {
            // Jumping and hit ceiling
            int tileRow = (worldY + solidArea.y) / gp.tileSize;
            worldY = (tileRow + 1) * gp.tileSize - solidArea.y;
            velocityY = 0;
        }

        // if verticalCollision caused by water, reverse vertical direction or change horizontal direction
        if (verticalCollision) {
            if (direction.equals("left")) {
                direction = "right";
            } else {
                direction = "left";
            }
            actionLockCounter = 0;
        }

        // --- GRAVITY ---
        velocityY += gravity;
        worldY += velocityY;

        // --- VERTICAL COLLISION ---
        verticalCollision = false;
        gp.cChecker.checkVerticalTile(this);

        if (velocityY > 0 && verticalCollision) {
            // Falling and hit ground
            int tileRow = (worldY + solidArea.y + solidArea.height) / gp.tileSize;
            worldY = tileRow * gp.tileSize - solidArea.y - solidArea.height;
            velocityY = 0;
            jumping = false;
        } else if (velocityY < 0 && verticalCollision) {
            // Jumping and hit ceiling
            int tileRow = (worldY + solidArea.y) / gp.tileSize;
            worldY = (tileRow + 1) * gp.tileSize - solidArea.y;
            velocityY = 0;
        }
    }
}
