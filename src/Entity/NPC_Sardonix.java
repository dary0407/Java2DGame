package Entity;

import Main.EntityType;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class NPC_Sardonix extends Entity{
    BufferedImage idleSpritesheet;
    BufferedImage attackSpritesheet;

    public Rectangle attackHitbox = new Rectangle(0, 0, 64, 64);
    public boolean attackHitApplied = false;
    public boolean dialogueDone = false;
    public BufferedImage[] dialogueImages = new BufferedImage[4];
    public NPC_Sardonix(GamePanel gp)
    {
        this.gp = gp;
        this.collision = true;
        this.entityType = EntityType.NPC;
        this.type = "Sardonix";
        direction = "idle";
        speed = 0;
        solidArea = new Rectangle(95, 160, 5, 85);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getNPCImageFromSpritesheet();
        loadDialogueImages();
    }
    public void getNPCImageFromSpritesheet() {
        try {
            int tileSize = 64;
            InputStream isIdle = getClass().getResourceAsStream("/res/npc/level3/spritesheet-nivel3-sardonix.png");
            if (isIdle == null) {
                System.err.println("Idle spritesheet not found!");
            } else {
                idleSpritesheet = ImageIO.read(isIdle);
            }

            InputStream isAttack = getClass().getResourceAsStream("/res/npc/level3/sardonix_spritesheet_attack.png");
            if (isAttack == null) {
                System.err.println("Attack spritesheet not found!");
            } else {
                attackSpritesheet = ImageIO.read(isAttack);
            }
            //idle
            idle1 = getSprite(idleSpritesheet, 0, 0, tileSize);
            idle2 = getSprite(idleSpritesheet, 1, 0, tileSize);
            idle3 = getSprite(idleSpritesheet, 2, 0, tileSize);
            idle4 = getSprite(idleSpritesheet, 3, 0, tileSize);
            idle5 = getSprite(idleSpritesheet, 4, 0, tileSize);
            idle6 = getSprite(idleSpritesheet, 5, 0, tileSize);

            // attack
            attack1Sardonix = getSprite(attackSpritesheet, 0, 0, tileSize);
            attack2Sardonix = getSprite(attackSpritesheet, 1, 0, tileSize);
            attack3Sardonix = getSprite(attackSpritesheet, 2, 0, tileSize);
            attack4Sardonix = getSprite(attackSpritesheet, 3, 0, tileSize);
            attack5Sardonix = getSprite(attackSpritesheet, 4, 0, tileSize);
            attack6Sardonix = getSprite(attackSpritesheet, 5, 0, tileSize);


        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private BufferedImage getSprite(BufferedImage sheet, int col, int row, int size) {
        return sheet.getSubimage(col * size, row * size, size, size);
    }
    @Override
    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter >= 120) {

            int dx = gp.player.worldX - this.worldX;
            int dy = gp.player.worldY - this.worldY;
            double distanceToPlayer = Math.sqrt(dx * dx + dy * dy);

            double attackRange = 200;

            Random random = new Random();

            if (distanceToPlayer <= attackRange) {

                int attackChance = random.nextInt(100) + 1;
                if (attackChance <= 40) {
                    attacking = true;
                    direction = "attack";
                    spriteNum = 1;
                } else {
                    attacking = false;
                    direction = "idle";
                    spriteNum = 1;
                }
            } else {

                attacking = false;
                direction = "idle";
                spriteNum = 1;
            }

            actionLockCounter = 0;
        }
    }
    public void draw(Graphics2D g2)
    {
        if(!alive) return;
        BufferedImage image = null;

        switch (direction) {
            case "idle":
                if (spriteNum == 1) image = idle1;
                else if (spriteNum == 2) image = idle2;
                else if (spriteNum == 3) image = idle3;
                else if (spriteNum == 4) image = idle4;
                else if (spriteNum == 5) image = idle5;
                else if (spriteNum == 6) image = idle6;
                break;

            case "attack":
                if (spriteNum == 1) image = attack1Sardonix;
                else if (spriteNum == 2) image = attack2Sardonix;
                else if (spriteNum == 3) image = attack3Sardonix;
                else if (spriteNum == 4) image = attack4Sardonix;
                else if (spriteNum == 5) image = attack5Sardonix;
                else if (spriteNum == 6) image = attack6Sardonix;
                break;
        }

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY + gp.tileSize, gp.tileSize * 3, gp.tileSize * 3, null);


        int barWidth = gp.tileSize;
        int barHeight = 8;
        int barX = screenX + gp.tileSize;
        int barY = (screenY + gp.tileSize) - barHeight;


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
            deathCounter++;


            if (deathCounter >= 60) {
                dying = false;
                alive = false;
            }
        }

        if (!alive) return;

        setAction();

        if (attacking) {

            if (spriteNum == 6 && !attackHitApplied) {
                int attackWidth = 128;
                int attackHeight = 64;

                attackHitbox.width = attackWidth;
                attackHitbox.height = attackHeight;

                Rectangle npcHitbox = new Rectangle(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height);

                if (gp.player.worldX < this.worldX) {

                    attackHitbox.x = npcHitbox.x - attackHitbox.width;
                    attackHitbox.y = npcHitbox.y + (npcHitbox.height - attackHitbox.height) / 2;
                } else {
                    attackHitbox.x = npcHitbox.x + npcHitbox.width;
                    attackHitbox.y = npcHitbox.y + (npcHitbox.height - attackHitbox.height) / 2;
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
                    attackHitApplied = true;
                }
            }
        }


        if (!attacking) {
            attackHitApplied = false;
        }


        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum % 6) + 1; // cycle through idle1 to idle6
            spriteCounter = 0;
        }
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
            BufferedImage rawImage = ImageIO.read(getClass().getResourceAsStream("/res/dialogue/sardonix/dialog1.png"));
            dialogueImages[0] = resizeImage(rawImage, 600, 340);
            rawImage = ImageIO.read(getClass().getResourceAsStream("/res/dialogue/sardonix/dialog1npc.png"));
            dialogueImages[1] = resizeImage(rawImage, 600, 340);
            rawImage = ImageIO.read(getClass().getResourceAsStream("/res/dialogue/sardonix/dialog2.png"));
            dialogueImages[2] = resizeImage(rawImage, 600, 340);
            rawImage = ImageIO.read(getClass().getResourceAsStream("/res/dialogue/sardonix/dialog2npc.png"));
            dialogueImages[3] = resizeImage(rawImage, 600, 340);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}