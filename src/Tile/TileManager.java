package Tile;

import Main.GamePanel;
import Object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    //BACKGROUND NIVEL 1
    Image[] backgroundsLevel1;
    //LAYERS NIVEL 1
    public int[][] layer1;
    public int[][] layer2;
    public int[][] layer3; //objects
    //BACKGROUND NIVEL 2
    Image[] backgroundsLevel2;
    int backgroundCount = 5;
    //LAYERS NIVEL 2
    public int[][] LayerWater;
    public int[][] LayerGround;
    public int[][] LayerGrass;
    public int[][] LayerObjects;

    //LAYERS & BACKGROUND NIVEL 3
    Image [] backgroundsLevel3;
    public int [][] LayerGroundLevel3;

    BufferedImage tileSpritesheetLevel1;
    BufferedImage tileSpritesheetLevel2;
    BufferedImage tileSpritesheetLevel3;

    public BufferedImage image;
    public boolean collision = false;

    public TileManager(GamePanel gp)
    {
        this.gp=gp;

        tile=new Tile[10];
        //LAYER NIVEL 1
        layer1 = new int[gp.maxWorldCol][gp.maxWorldRow];
        layer2 = new int[gp.maxWorldCol][gp.maxWorldRow];
        //BACKGROUND NIVEL 1
        backgroundsLevel1 = new Image[backgroundCount];
        for (int i = 0; i < backgroundCount; i++) {
            loadBackgroundLayer(i, "/res/backgrounds/level1/background" + (i + 1) + ".png", backgroundsLevel1);
        }
        //LAYER NIVEL 2
        LayerWater = new int [gp.worldWidth][gp.worldHeight];
        LayerGround = new int [gp.worldWidth][gp.worldHeight];
        LayerGrass= new int[gp.worldWidth][gp.worldHeight];
        LayerObjects = new int[gp.worldWidth][gp.worldHeight];
        //BACKGROUND NIVEL 2
        backgroundsLevel2 = new Image[backgroundCount];
        for (int i = 0; i < backgroundCount; i++) {
            loadBackgroundLayer(i, "/res/backgrounds/level2/background" + (i + 1) + ".png", backgroundsLevel2);
        }
        //LAYER NIVEL 3
        LayerGroundLevel3 = new int [gp.worldWidth][gp.worldHeight];
        //BACKGROUND NIVEL 3
        backgroundsLevel3 = new Image[backgroundCount];
        for (int i = 0; i < backgroundCount; i++) {
            loadBackgroundLayer(i, "/res/backgrounds/level3/background" + (i + 1) + ".png", backgroundsLevel3);
        }

        try {
            InputStream is1 = getClass().getResourceAsStream("/res/tiles/level1/tileSpritesheet.png");
            if (is1 == null) {
                System.err.println("Imaginea NU a fost găsită!");
            } else {
                tileSpritesheetLevel1 = ImageIO.read(is1);
            }
            InputStream is2 = getClass().getResourceAsStream("/res/tiles/level2/tiles-spritesheet.png");
            if (is2 == null) {
                System.err.println("Imaginea NU a fost găsită!");
            } else {
                tileSpritesheetLevel2 = ImageIO.read(is2);
            }
            InputStream is3 = getClass().getResourceAsStream("/res/tiles/level3/spritesheet-nivel-3.png");
            if (is3 == null) {
                System.err.println("Imaginea NU a fost găsită!");
            } else {
                tileSpritesheetLevel3 = ImageIO.read(is3);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadLevel(int level)
    {
        switch(level)
        {
            case 1:
                loadMap("/res/maps/level1/world01.txt", layer1);
                loadMap("/res/maps/level1/world01layer2.txt", layer2);
                break;
            case 2:
                loadMap("/res/maps/level2/layer-apa.txt",LayerWater);
                loadMap("/res/maps/level2/layer-ground.txt",LayerGround);
                loadMap("/res/maps/level2/layer-grass.txt",LayerGrass);
                loadMap("/res/maps/level2/layer-objects.txt",LayerObjects);
                break;
            case 3:
                loadMap("/res/maps/level3/layer-ground.txt", LayerGroundLevel3);
                break;
        }
    }
    public void loadTilesFromSpritesheetLevel2() {
        int tileWidth = 64;  // Lățimea unui tile (exemplu)
        int tileHeight = 64; // Înălțimea unui tile (exemplu)

        int rows = tileSpritesheetLevel2.getHeight() / tileHeight;
        int cols = tileSpritesheetLevel2.getWidth() / tileWidth;

        tile = new Tile[rows * cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int index = row * cols + col;
                Tile t = new Tile();
                t.image = tileSpritesheetLevel2.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                tile[index] = t;
                if ((index == 16) || (index == 17) || (index == 19)) {
                    t.isWater = true;
                    t.collision = false;
                } else if ((index == 11) || (index == 14) || (index == 15) || (index == 22)) {
                    t.isWater = false;
                    t.collision = true;
                }
                if ((index == 18)) {
                    t.isWall = true;
                }
            }
        }

    }
    public void loadTilesFromSpritesheetLevel1() {
        int tileWidth = 64;  // Lățimea unui tile (exemplu)
        int tileHeight = 64; // Înălțimea unui tile (exemplu)

        int rows = tileSpritesheetLevel1.getHeight() / tileHeight;
        int cols = tileSpritesheetLevel1.getWidth() / tileWidth;

        tile = new Tile[rows * cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int index = row * cols + col;
                Tile t = new Tile();
                t.image = tileSpritesheetLevel1.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight);

                tile[row * cols + col] = t;
                //marcam tiles care sunt solide
                if((index == 3) || (index == 12) || (index == 13) || (index == 14) || (index == 25) || (index == 26) || (index == 27)){
                    t.collision = true;
                } else {
                    t.collision = false;
                }

            }
        }
    }
    public void loadTilesFromSpritesheetLevel3() {
        int tileWidth = 64;  // Lățimea unui tile (exemplu)
        int tileHeight = 64; // Înălțimea unui tile (exemplu)

        int rows = tileSpritesheetLevel3.getHeight() / tileHeight;
        int cols = tileSpritesheetLevel3.getWidth() / tileWidth;

        tile = new Tile[rows * cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int index = row * cols + col;
                Tile t = new Tile();
                t.image = tileSpritesheetLevel3.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                tile[row*cols+col] = t;

                if((index == 11))
                {
                    t.isWater = true;
                    t.collision = false;
                }else if ((index == 0) || (index == 1) || (index == 2) || (index == 7) ||(index == 8) || (index == 9) || (index == 10) || (index == 14) ||(index == 16) || (index == 17) || (index == 22) || (index == 45) || (index == 46))
                {
                    t.isWater = false;
                    t.collision = true;
                } else if (index == 24) {
                    t.isWall = true;
                }
            }
        }
    }

    public void loadBackgroundLayer(int index, String path, Image[] backgrounds) {
        try {
            backgrounds[index] = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath, int[][] tileMatrix){
        try {
            for (int i = 0; i < gp.maxWorldCol; i++) {
                for (int j = 0; j < gp.maxWorldRow; j++) {
                    tileMatrix[i][j] = -1;
                }
            }
            InputStream is=getClass().getResourceAsStream(filePath);
            BufferedReader br=new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row =0;

            while(col < gp.maxWorldCol && row <gp.maxWorldRow)
            {
                String line = br.readLine();

                while(col< gp.maxWorldCol){
                    String [] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    tileMatrix[col][row]=num;
                    col++;
                }

                if(col == gp.maxWorldCol){
                    col=0;
                    row++;
                }
            }
            br.close();
        }catch(IOException e){

        }

    }
    public void drawLevel1(Graphics2D g2, SuperObject[] obj) {
        // BACKGROUND IMAGES
        for (int i = 0; i < backgroundCount; i++) {
            if (backgroundsLevel1[i] != null) {
                g2.drawImage(backgroundsLevel1[i], 0, 0, gp.screenWidth, gp.screenHeight, null);
            }
        }

        // TILE BASE (LAYER 1)
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = layer1[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (tileNum != -1) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        // Deseneaza objects intre layer 1 si 2
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null && obj[i].level == gp.level) {
                obj[i].draw(g2, gp);
            }
        }

        // Platforme
        worldCol = 0;
        worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = layer2[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (tileNum != -1) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
    public void drawLevel2(Graphics2D g2, SuperObject[] obj) {
        for (int i = 0; i < backgroundCount; i++) {
            if (backgroundsLevel2[i] != null) {
                g2.drawImage(backgroundsLevel2[i], 0, 0, 3 * gp.screenWidth, gp.screenHeight, null);
            }
        }

        //layer apa
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = LayerWater[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (tileNum != -1) {
                if (tileNum >= 0 && tileNum < tile.length) {
                    if (tile[tileNum] != null) {
                        g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    } else {
                        System.out.println("tile[" + tileNum + "] is null");
                    }
                } else {
                    System.out.println("tileNum out of bounds: " + tileNum);
                }
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        //layer ground
        worldCol = 0;
        worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = LayerGround[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (tileNum != -1) {
                if (tileNum >= 0 && tileNum < tile.length) {
                    if (tile[tileNum] != null) {
                        g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    } else {
                        System.out.println("tile[" + tileNum + "] is null");
                    }
                } else {
                    System.out.println("tileNum out of bounds: " + tileNum);
                }
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null && obj[i].level == gp.level && !obj[i].collected) {
                obj[i].draw(g2, gp);
            }
        }
        //layer grass
        worldCol = 0;
        worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = LayerGrass[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (tileNum != -1) {
                if (tileNum >= 0 && tileNum < tile.length) {
                    if (tile[tileNum] != null) {
                        g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    } else {
                        System.out.println("tile[" + tileNum + "] is null");
                    }
                } else {
                    System.out.println("tileNum out of bounds: " + tileNum);
                }
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
        //layer objects
        worldCol = 0;
        worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = LayerObjects[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY - gp.tileSize;

            if (tileNum != -1) {
                if (tileNum >= 0 && tileNum < tile.length) {
                    if (tile[tileNum] != null) {
                        g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    } else {
                        System.out.println("tile[" + tileNum + "] is null");
                    }
                } else {
                    System.out.println("tileNum out of bounds: " + tileNum);
                }
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
    public void drawLevel3(Graphics2D g2, SuperObject[] obj) {
        for (int i = 0; i < backgroundCount; i++) {
            if (backgroundsLevel3[i] != null) {
                g2.drawImage(backgroundsLevel3[i], 0, 0, 3 * gp.screenWidth, gp.screenHeight, null);
            }
        }
        int worldCol = 0;
        int worldRow = 0;

        int cameraX = gp.player.worldX - gp.player.screenX;
        int cameraY = gp.player.worldY - gp.player.screenY;

// Limitează camera la marginile mapei
        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > gp.worldWidth - gp.screenWidth)
            cameraX = gp.worldWidth - gp.screenWidth ;
        if (cameraY > gp.worldHeight - gp.screenHeight)
            cameraY = gp.worldHeight - gp.screenHeight;

        //layer ground
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = LayerGroundLevel3[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;


            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;



            if (tileNum != -1) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null && obj[i].level == gp.level && !obj[i].collected) {
                obj[i].draw(g2, gp);
            }
        }
    }
}
