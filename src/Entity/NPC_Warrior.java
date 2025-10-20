package Entity;

import Main.EntityType;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class NPC_Warrior extends Entity{
    BufferedImage NPCSpritesheet;
    public Rectangle attackHitbox;
    public boolean attackHitApplied = false;
    public int chaseRange = 350;

    public NPC_Warrior(GamePanel gp)
    {
        this.gp = gp;
        this.collision = true;
        this.entityType = EntityType.NPC;
        this.attackHitbox = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        this.type = "Warrior";
        direction = "left";
        speed = 1;
        solidArea = new Rectangle(50, 50, 15, 85); // <-- initialize here, adjust size as needed
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getNPCImageFromSpritesheet();
    }
    public void getNPCImageFromSpritesheet() {
        try {
            int tileSize = 64;
            InputStream is = getClass().getResourceAsStream("/res/npc/level3/spritesheet-nivel3-warrior.png");
            if (is == null) {
                System.err.println("Imaginea NU a fost găsită!");
            } else {
                NPCSpritesheet = ImageIO.read(is);
            }
            //attack left
            attack1warrior = getSprite(0, 0, tileSize);
            attack2warrior = getSprite(1, 0, tileSize);
            attack3warrior = getSprite(2, 0, tileSize);
            attack4warrior = getSprite(3, 0, tileSize);
            attack5warrior = getSprite(4, 0, tileSize);
            attack6warrior = getSprite(5, 0, tileSize);
            attack7warrior = getSprite(6, 0, tileSize);
            //attackright
            attack8warrior = getSprite(0, 3, tileSize);
            attack9warrior = getSprite(1, 3, tileSize);
            attack10warrior = getSprite(2, 3, tileSize);
            attack11warrior = getSprite(3, 3, tileSize);
            attack12warrior = getSprite(4, 3, tileSize);
            attack13warrior = getSprite(5, 3, tileSize);
            attack14warrior = getSprite(6, 3, tileSize);

            //dead
            dead1Warrior = getSprite(0, 1, tileSize);
            dead2Warrior = getSprite(1, 1, tileSize);
            dead3Warrior = getSprite(2, 1, tileSize);
            dead4Warrior = getSprite(3, 1, tileSize);


        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private BufferedImage getSprite(int col, int row, int tileSize) {
        return NPCSpritesheet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
    }
    @Override
    public void setAction() {

        int distanceX = gp.player.worldX - this.worldX;


        int distanceY = gp.player.worldY - this.worldY;
        double distanceToPlayer = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distanceToPlayer <= chaseRange) {

            direction = (distanceX < 0) ? "left" : "right";
        } else {

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
            else {
                spriteNum = 4;
            }

        } else {
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum++;
                if (spriteNum > 7) {
                    spriteNum = 1;
                    attackHitApplied = false;
                }
                spriteCounter = 0;
            }
        }
    }
    @Override
    public void draw(Graphics2D g2) {
        if (!alive && !dying || (this.level != gp.level)) return;
        BufferedImage image = null;
        if (dying) {
            if (spriteNum == 1) image = dead1Warrior;
            if (spriteNum == 2) image = dead2Warrior;
            if (spriteNum == 3) image = dead3Warrior;
            if (spriteNum == 4) image = dead4Warrior;
        } else {
            switch (direction) {
                case "right":
                    if (spriteNum == 1) image = attack1warrior;
                    if (spriteNum == 2) image = attack2warrior;
                    if (spriteNum == 3) image = attack3warrior;
                    if (spriteNum == 4) image = attack4warrior;
                    if (spriteNum == 5) image = attack5warrior;
                    if (spriteNum == 6) image = attack6warrior;
                    if (spriteNum == 7) image = attack7warrior;
                    break;

                case "left":
                    if (spriteNum == 1) image = attack8warrior;
                    if (spriteNum == 2) image = attack9warrior;
                    if (spriteNum == 3) image = attack10warrior;
                    if (spriteNum == 4) image = attack11warrior;
                    if (spriteNum == 5) image = attack12warrior;
                    if (spriteNum == 6) image = attack13warrior;
                    if (spriteNum == 7) image = attack14warrior;
                    break;
            }
        }

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, gp.tileSize*2, gp.tileSize*2, null);


        int barWidth = gp.tileSize;
        int barHeight = 8;
        int barX = screenX + 15;
        int barY = screenY - barHeight - 5;


        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);


        double healthPercent = (double) hp / maxHp;
        int healthWidth = (int)(barWidth * healthPercent);


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
            return;
        }
        if (!alive) return;

        setAction();
        updateAnimation();
        if ((spriteNum == 1 || spriteNum == 3 || spriteNum == 5 || spriteNum == 7) && !attackHitApplied) {
            if (gp.player.level == this.level) {

                Rectangle npcHitbox = new Rectangle(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height);

                if (direction.equals("left")) {
                    attackHitbox.x = npcHitbox.x - attackHitbox.width;
                } else {
                    attackHitbox.x = npcHitbox.x + npcHitbox.width;
                }
                attackHitbox.y = npcHitbox.y;

                Rectangle playerHitbox = new Rectangle(gp.player.worldX + gp.player.solidArea.x,
                        gp.player.worldY + gp.player.solidArea.y,
                        gp.player.solidArea.width,
                        gp.player.solidArea.height);

                if (attackHitbox.intersects(playerHitbox)) {
                    int damage = 10 + new Random().nextInt(11); // (0 to 10) + 10 = 10 to 20
                    gp.player.hp -= damage;
                    if (gp.player.hp <= 0) {
                        gp.player.lives -= 1;
                        gp.gameState = gp.dyingState;
                    }
                    attackHitApplied = true;
                }
            }
        }

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


        verticalCollision = false;
        gp.cChecker.checkVerticalTile(this);

        if (velocityY > 0 && verticalCollision) {

            int tileRow = (worldY + solidArea.y + solidArea.height) / gp.tileSize;
            worldY = tileRow * gp.tileSize - solidArea.y - solidArea.height;
            velocityY = 0;
            jumping = false;
        } else if (velocityY < 0 && verticalCollision) {

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


        velocityY += gravity;
        worldY += velocityY;


        verticalCollision = false;
        gp.cChecker.checkVerticalTile(this);

        if (velocityY > 0 && verticalCollision) {

            int tileRow = (worldY + solidArea.y + solidArea.height) / gp.tileSize;
            worldY = tileRow * gp.tileSize - solidArea.y - solidArea.height;
            velocityY = 0;
            jumping = false;
        } else if (velocityY < 0 && verticalCollision) {

            int tileRow = (worldY + solidArea.y) / gp.tileSize;
            worldY = (tileRow + 1) * gp.tileSize - solidArea.y;
            velocityY = 0;
        }
    }
}