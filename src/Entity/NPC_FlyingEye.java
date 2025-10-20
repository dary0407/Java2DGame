package Entity;

import Main.EntityType;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class NPC_FlyingEye extends Entity{
    BufferedImage NPCSpritesheet;

    public Rectangle attackHitbox = new Rectangle(0, 0, 64, 64);
    public int chaseRange = 350;
    public NPC_FlyingEye(GamePanel gp)
    {
        this.gp = gp;
        this.collision = true;
        this.entityType = EntityType.NPC;
        this.type = "FlyingEye";
        direction = "left";
        speed = 1;
        solidArea = new Rectangle(95, 160, 5, 85);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getNPCImageFromSpritesheet();
    }
    public void getNPCImageFromSpritesheet() {
        try {
            int tileSize = 64; // your sprite size
            InputStream is = getClass().getResourceAsStream("/res/npc/level3/spritesheet-nivel3-flyingeye.png");
            if (is == null) {
                System.err.println("Imaginea NU a fost găsită!");
                return;
            }
            NPCSpritesheet = ImageIO.read(is);

            //run left
            runleft1npc = getSprite(0, 0, tileSize);
            runleft2npc = getSprite(1, 0, tileSize);
            runleft3npc = getSprite(2, 0, tileSize);
            runleft4npc = getSprite(3, 0, tileSize);
            runleft5npc = getSprite(4, 0, tileSize);
            runleft6npc = getSprite(5, 0, tileSize);

            //runright
            runright1npc = getSprite(0, 1, tileSize);
            runright2npc = getSprite(1, 1, tileSize);
            runright3npc = getSprite(2, 1, tileSize);
            runright4npc = getSprite(3, 1, tileSize);
            runright5npc = getSprite(4, 1, tileSize);
            runright6npc = getSprite(5, 1, tileSize);

            //dead
            dead1FlyingEye = getSprite(0,2,tileSize);
            dead2FlyingEye = getSprite(1,2,tileSize);
            dead3FlyingEye = getSprite(2,2,tileSize);
            dead4FlyingEye = getSprite(3,2,tileSize);
            dead5FlyingEye = getSprite(4,2,tileSize);
            dead6FlyingEye = getSprite(5,2,tileSize);
            dead7FlyingEye = getSprite(0,3,tileSize);
            dead8FlyingEye = getSprite(1,3,tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private BufferedImage getSprite(int col, int row, int tileSize) {
        return NPCSpritesheet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
    }
    //@Override
    public void setAction() {
        //Calculam distanta orizontala spre player
        int distanceX = gp.player.worldX - this.worldX;

        // Distanta totala spre player
        int distanceY = gp.player.worldY - this.worldY;
        double distanceToPlayer = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distanceToPlayer <= chaseRange) {
            // Daca player-ul este in range
            direction = (distanceX < 0) ? "left" : "right";
        } else {
            // Daca player nu mai este in range, se misca random
            actionLockCounter++;
            if (actionLockCounter >= 120) {
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
                spriteNum = 6;
            }
        } else {
            // Animatie de miscare
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum++;
                if (spriteNum > 8) spriteNum = 1;
                spriteCounter = 0;
            }
        }
    }
    @Override
    public void draw(Graphics2D g2)
    {

        if (!alive && !dying || (this.level != gp.level)) return;
        BufferedImage image = null;

        if (dying) {
            if (spriteNum == 1) image = dead1FlyingEye;
            if (spriteNum == 2) image = dead2FlyingEye;
            if (spriteNum == 3) image = dead3FlyingEye;
            if (spriteNum == 4) image = dead4FlyingEye;
            if (spriteNum == 5) image = dead5FlyingEye;
            if (spriteNum == 6) image = dead6FlyingEye;
            if (spriteNum == 7) image = dead7FlyingEye;
            if (spriteNum == 8) image = dead8FlyingEye;
        } else {
            switch(direction) {
                case "left":
                    if (spriteNum == 1) image = runleft1npc;
                    if (spriteNum == 2) image = runleft2npc;
                    if (spriteNum == 3) image = runleft3npc;
                    if (spriteNum == 4) image = runleft4npc;
                    if (spriteNum == 5) image = runleft5npc;
                    if (spriteNum == 6) image = runleft6npc;
                    break;
                case "right":
                    if (spriteNum == 1) image = runright1npc;
                    if (spriteNum == 2) image = runright2npc;
                    if (spriteNum == 3) image = runright3npc;
                    if (spriteNum == 4) image = runright4npc;
                    if (spriteNum == 5) image = runright5npc;
                    if (spriteNum == 6) image = runright6npc;
                    break;
            }
        }
        // Pozitia relativa player
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY + gp.tileSize, gp.tileSize * 3, gp.tileSize * 3, null);

        // Pentru a desena "health bar" deasupra NPC-ului
        int barWidth = gp.tileSize;
        int barHeight = 8;
        int barX = screenX + gp.tileSize;
        int barY = (screenY + gp.tileSize) - barHeight + gp.tileSize - 25;

        // Background
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);

        // HP procent
        double healthPercent = (double) hp / maxHp;
        int healthWidth = (int)(barWidth * healthPercent);

        // FILL = verde
        g2.setColor(Color.GREEN);
        g2.fillRect(barX, barY, healthWidth, barHeight);

        g2.setColor(Color.BLACK);
        g2.drawRect(barX, barY, barWidth, barHeight);
    }
    @Override
    public void update() {
        if (dying) {
            attackHitbox.setBounds(0, 0, 0, 0);
            dyingAnimation();
            return; // Nu mai realizeaza alte animatii
        }
        if (!alive) return;
        setAction(); // Se schimba directia random
        updateAnimation();

        //COLIZIUNI ORIZONTALE
        horizontalCollision = false;
        gp.cChecker.checkHorizontalTile(this);

        if (!horizontalCollision) {
            if (direction.equals("left")) {
                worldX -= speed;
            } else if (direction.equals("right")) {
                worldX += speed;
            }
        } else {
            if (!jumping) {
                velocityY = -15;
                jumping = true;
            }
            actionLockCounter = 0;
        }
        if (wall) {
            if (direction.equals("left")) {
                direction = "right";
            } else {
                direction = "left";
            }
            wall = false;
            actionLockCounter = 0;
        }

        //COLIZIUNI VERTICALE
        verticalCollision = false;
        gp.cChecker.checkVerticalTile(this);

        if (velocityY > 0 && verticalCollision) {
            // cade
            int tileRow = (worldY + solidArea.y + solidArea.height) / gp.tileSize;
            worldY = tileRow * gp.tileSize - solidArea.y - solidArea.height;
            velocityY = 0;
            jumping = false;
        } else if (velocityY < 0 && verticalCollision) {
            // sare
            int tileRow = (worldY + solidArea.y) / gp.tileSize;
            worldY = (tileRow + 1) * gp.tileSize - solidArea.y;
            velocityY = 0;
        }

        if (verticalCollision) {
            if (direction.equals("left")) {
                direction = "right";
            } else {
                direction = "left";
            }
            actionLockCounter = 0;
        }

        //ANIMATII
        spriteCounter++;
        if (spriteCounter > 16) {
            spriteNum = (spriteNum % 8) + 1;
            spriteCounter = 0;
        }

        //GRAVITATE
        velocityY += gravity;
        worldY += velocityY;

        //COLIZIUNI VERTICALE
        verticalCollision = false;
        gp.cChecker.checkVerticalTile(this);

        if (velocityY > 0 && verticalCollision) {
            //cade
            int tileRow = (worldY + solidArea.y + solidArea.height) / gp.tileSize;
            worldY = tileRow * gp.tileSize - solidArea.y - solidArea.height;
            velocityY = 0;
            jumping = false;
        } else if (velocityY < 0 && verticalCollision) {
            // sare
            int tileRow = (worldY + solidArea.y) / gp.tileSize;
            worldY = (tileRow + 1) * gp.tileSize - solidArea.y;
            velocityY = 0;
        }
    }
}