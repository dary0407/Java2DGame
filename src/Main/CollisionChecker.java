package Main;


import Entity.Entity;

import java.awt.*;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkHorizontalTile(Entity entity) {
        int middleY = entity.worldY + entity.solidArea.y + entity.solidArea.height / 2;
        int row = middleY / gp.tileSize;

        int tileNum = -1;
        int tileNumObj = -1;
        int waterTileNum = -1;
        int col = switch (entity.direction) {
            case "left" -> (entity.worldX + entity.solidArea.x - entity.speed) / gp.tileSize;
            case "right" -> (entity.worldX + entity.solidArea.x + entity.solidArea.width + entity.speed) / gp.tileSize;
            case "idle" ->  gp.tileSize * 45;
            default -> 0;
        };

        if (isInBounds(col, row)) {
            if (gp.level == 1) {
                tileNum = gp.tileM.layer2[col][row];
            } else if (gp.level == 2) {
                tileNum = gp.tileM.LayerGrass[col][row];
                tileNumObj = gp.tileM.LayerObjects[col][row];
                waterTileNum = gp.tileM.LayerWater[col][row];

                if (entity.entityType == EntityType.NPC && waterTileNum != -1 && gp.tileM.tile[waterTileNum].isWater) {
                    entity.horizontalCollision = true;
                    return;
                }
                if (entity.entityType == EntityType.NPC && tileNum != -1 && gp.tileM.tile[tileNum].isWall) {
                    entity.wall = true;
                    return;
                }
            } else if (gp.level == 3)
            {
                tileNum = gp.tileM.LayerGroundLevel3[col][row];
                if (entity.entityType == EntityType.NPC && tileNum != -1 && gp.tileM.tile[tileNum].isWall) {
                    entity.wall = true;
                    return;
                }
            }
        }

        if ((tileNum != -1 && gp.tileM.tile[tileNum].collision) ||
                (tileNumObj != -1 && gp.tileM.tile[tileNumObj].collision)) {
            entity.horizontalCollision = true;
        }
    }

    public void checkVerticalTile(Entity entity) {
        int middleX = entity.worldX + entity.solidArea.x + entity.solidArea.width / 2;
        int col = middleX / gp.tileSize;

        int tileNum1 = -1, tileNum2 = -1;
        int topRow = (entity.worldY + entity.solidArea.y + (int)entity.velocityY) / gp.tileSize;
        int bottomRow = (entity.worldY + entity.solidArea.y + entity.solidArea.height + (int)entity.velocityY) / gp.tileSize;

        if (entity.velocityY < 0) {  // sare
            if (isInBounds(col, topRow)) {
                if (gp.level == 1) {
                    tileNum1 = gp.tileM.layer2[col][topRow];
                } else if(gp.level == 2) {
                    tileNum1 = gp.tileM.LayerGrass[col][topRow];
                } else if(gp.level == 3)
                {
                    tileNum1 = gp.tileM.LayerGroundLevel3[col][topRow];
                }
            }
        } else if (entity.velocityY > 0) {  // cade
            if (isInBounds(col, bottomRow)) {
                if (gp.level == 1) {
                    tileNum2 = gp.tileM.layer2[col][bottomRow];
                } else if(gp.level == 2) {
                    tileNum2 = gp.tileM.LayerGrass[col][bottomRow];
                } else if(gp.level == 3)
                {
                    tileNum2 = gp.tileM.LayerGroundLevel3[col][bottomRow];
                }
            }
        }

        // Pentru water tile
        if (gp.level == 2 && entity.entityType == EntityType.NPC) {
            if (isInBounds(col, bottomRow)) {
                int waterTileNum = gp.tileM.LayerWater[col][bottomRow];
                if (waterTileNum != -1 && gp.tileM.tile[waterTileNum].isWater) {
                    entity.verticalCollision = true;  // NPC treats water as solid ground
                    return;
                }
            }
        }

        if ((tileNum1 != -1 && gp.tileM.tile[tileNum1].collision) ||
                (tileNum2 != -1 && gp.tileM.tile[tileNum2].collision)) {
            entity.verticalCollision = true;
        }
    }

    private boolean isInBounds(int col, int row) {
        return col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow;
    }

    public void checkEntityCollision(Entity entity, Entity[] targets) {
        Rectangle entityBounds = new Rectangle(
                entity.worldX + entity.solidArea.x,
                entity.worldY + entity.solidArea.y,
                entity.solidArea.width,
                entity.solidArea.height
        );

        for (Entity target : targets) {
            if (target == null || target == entity || !target.alive) continue;

            if (target.level != gp.level) continue;

            Rectangle targetBounds = new Rectangle(
                    target.worldX + target.solidArea.x,
                    target.worldY + target.solidArea.y,
                    target.solidArea.width,
                    target.solidArea.height
            );

            if (entityBounds.intersects(targetBounds)) {
                entity.entityCollision = true;
                entity.contactEntity = target;
                return;
            }

        }
        entity.entityCollision = false;
        entity.contactEntity = null;
    }

    public boolean isOnGround(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int leftCol = entityLeftWorldX / gp.tileSize;
        int rightCol = entityRightWorldX / gp.tileSize;
        int bottomRow = (entityBottomWorldY + 1) / gp.tileSize;

        //daca nu este in bounds, cade
        if (!isInBounds(leftCol, bottomRow) && !isInBounds(rightCol, bottomRow)) {
            return false;
        }

        boolean leftTileSolid = false;
        boolean rightTileSolid = false;

        int rightTile = 0;
        int leftTile = 0;

        if (isInBounds(leftCol, bottomRow)) {

            if (gp.level == 1) {
                leftTile = gp.tileM.layer2[leftCol][bottomRow];
            } else if(gp.level == 2){
                leftTile = gp.tileM.LayerGrass[leftCol][bottomRow];
            } else if(gp.level == 3)
            {
                leftTile = gp.tileM.LayerGroundLevel3[leftCol][bottomRow];
            }
            leftTileSolid = leftTile != -1 && gp.tileM.tile[leftTile].collision;
        }
        if (isInBounds(rightCol, bottomRow)) {

            if (gp.level == 1) {
                rightTile = gp.tileM.layer2[rightCol][bottomRow];
            } else if (gp.level == 2){
                rightTile = gp.tileM.LayerGrass[rightCol][bottomRow];
            }else if (gp.level == 3)
            {
                rightTile = gp.tileM.LayerGroundLevel3[rightCol][bottomRow];
            }

            rightTileSolid = rightTile != -1 && gp.tileM.tile[rightTile].collision;
        }
        return leftTileSolid || rightTileSolid;
    }
}