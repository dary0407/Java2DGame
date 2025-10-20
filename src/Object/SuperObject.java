package Object;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collected = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 64, 64);
    public int durability = 1;  // durabilitatea default, se modifica in functie de cristal
    public boolean breakable = false;
    public int level;
    public boolean active = false;

    public void draw(Graphics2D g2, GamePanel gp) {
        if (collected || this.level != gp.level) {
            return;
        }

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (image != null) {
            int scaledSize = (int)(gp.tileSize * 0.75);
            int offset = (gp.tileSize - scaledSize) / 2;

            int verticalShift = 10;
            g2.drawImage(image, screenX + offset, screenY + offset - verticalShift, scaledSize, scaledSize, null);
        }

    }

    public void update() {
    }
}