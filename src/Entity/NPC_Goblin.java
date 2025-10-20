package Entity;

import Main.EntityType;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class NPC_Goblin extends Entity{

    BufferedImage NPCSpritesheet;

    public Rectangle attackHitbox = new Rectangle(0, 0, 64, 64);
    public boolean attackHitApplied = false;
    public int chaseRange = 350;   // Distanta(fata de player) de la care incepe sa urmareasca player-ul
    public NPC_Goblin(GamePanel gp)
    {
        this.gp = gp;
        this.collision = true;
        this.entityType = EntityType.NPC;
        this.type = "Goblin";
        direction = "left";
        speed = 1;
        solidArea = new Rectangle(20, 0, 20, 55);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getNPCImageFromSpritesheet();
    }
    public void getNPCImageFromSpritesheet() {
        try {
            int tileSize = 64;
            InputStream is = getClass().getResourceAsStream("/res/npc/level3/spritesheet-nivel3-goblin.png");
            if (is == null) {
                System.err.println("Imaginea NU a fost găsită!");
            } else {
                NPCSpritesheet = ImageIO.read(is);
            }
            //attack left
            attack1goblin = getSprite(0, 2, tileSize);
            attack2goblin = getSprite(1, 2, tileSize);
            attack3goblin = getSprite(2, 2, tileSize);
            attack4goblin = getSprite(3, 2, tileSize);
            attack5goblin = getSprite(4, 2, tileSize);
            attack6goblin = getSprite(5, 2, tileSize);
            //attack right
            attack7goblin = getSprite(0, 0, tileSize);
            attack8goblin = getSprite(1, 0, tileSize);
            attack9goblin = getSprite(2, 0, tileSize);
            attack10goblin = getSprite(3, 0, tileSize);
            attack11goblin = getSprite(4, 0, tileSize);
            attack12goblin = getSprite(5, 0, tileSize);

            //dead left
            dead1Goblin = getSprite(4, 2, tileSize);
            dead2Goblin = getSprite(5, 2, tileSize);
            dead3Goblin = getSprite(6, 2, tileSize);
            dead4Goblin = getSprite(7, 2, tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private BufferedImage getSprite(int col, int row, int tileSize) {
        return NPCSpritesheet.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
    }
    //@Override
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
                if (spriteNum > 6) {
                    spriteNum = 1;
                    attackHitApplied = false;
                }
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
            if (spriteNum == 1) image = dead1Goblin;
            if (spriteNum == 2) image = dead2Goblin;
            if (spriteNum == 3) image = dead3Goblin;
            if (spriteNum == 4) image = dead4Goblin;
        } else {
            switch (direction) {
                case "left":
                    if (spriteNum == 1) image = attack1goblin;
                    if (spriteNum == 2) image = attack2goblin;
                    if (spriteNum == 3) image = attack3goblin;
                    if (spriteNum == 4) image = attack4goblin;
                    if (spriteNum == 5) image = attack5goblin;
                    if (spriteNum == 6) image = attack6goblin;
                    break;
                case "right":
                    if (spriteNum == 1) image = attack7goblin;
                    if (spriteNum == 2) image = attack8goblin;
                    if (spriteNum == 3) image = attack9goblin;
                    if (spriteNum == 4) image = attack10goblin;
                    if (spriteNum == 5) image = attack11goblin;
                    if (spriteNum == 6) image = attack12goblin;
                    break;
            }
        }

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        //HEALTH BAR
        int barWidth = gp.tileSize;       // width same as tile size
        int barHeight = 8;                // height of health bar
        int barX = screenX;               // align with NPC sprite left
        int barY = screenY - barHeight - 5; // 5 pixels above the sprite


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
        if ((spriteNum == 6 || spriteNum == 2 || spriteNum == 4) && !attackHitApplied) {
            if (gp.player.level == this.level) {
                Rectangle npcHitbox = new Rectangle(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height);


                attackHitbox.width = 64;
                attackHitbox.height = 64;


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
                    int damage = 10 + new Random().nextInt(11);
                    gp.player.hp -= damage;
                    if (gp.player.hp <= 0) {
                        gp.player.lives -= 1;
                        gp.gameState = gp.dyingState;
                    }
                    attackHitApplied = true;
                }
            }
        }
        //coliziuni orizontale
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

        // coliziuni verticale
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

        //ANIMATII
        spriteCounter++;
        if (spriteCounter > 16) {
            spriteNum = (spriteNum % 8) + 1;
            spriteCounter = 0;
        }


        velocityY += gravity;
        worldY += velocityY;

        //coliziuni verticale
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
