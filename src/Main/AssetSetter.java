package Main;

import Entity.*;
import Object.OBJ_Crystals;
import Object.OBJ_Key;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    //pozitia cristalelor pe mapa
    public void setObject() {
        gp.obj[0] = new OBJ_Crystals("black");
        gp.obj[0].worldX = 8 * gp.tileSize;
        gp.obj[0].worldY = 8 * gp.tileSize;
        gp.obj[0].level = 1;

        gp.obj[1] = new OBJ_Crystals("blue");
        gp.obj[1].worldX = 11 * gp.tileSize;
        gp.obj[1].worldY = 6 * gp.tileSize;
        gp.obj[1].level = 1;

        gp.obj[2] = new OBJ_Crystals("pink");
        gp.obj[2].worldX = 9 * gp.tileSize;
        gp.obj[2].worldY = 4 * gp.tileSize;
        gp.obj[2].level = 1;

        gp.obj[3] = new OBJ_Crystals("pink");
        gp.obj[3].worldX = 8 * gp.tileSize;
        gp.obj[3].worldY = 3 * gp.tileSize;
        gp.obj[3].level = 1;

        gp.obj[4] = new OBJ_Crystals("red");
        gp.obj[4].worldX = 7 * gp.tileSize;
        gp.obj[4].worldY = 2 * gp.tileSize;
        gp.obj[4].level = 1;

        gp.obj[5] = new OBJ_Crystals("pink");
        gp.obj[5].worldX = 29 * gp.tileSize;
        gp.obj[5].worldY = 7 * gp.tileSize;
        gp.obj[5].level = 1;

        gp.obj[6] = new OBJ_Crystals("blue");
        gp.obj[6].worldX = 28 * gp.tileSize;
        gp.obj[6].worldY = 6 * gp.tileSize;
        gp.obj[6].level = 1;

        gp.obj[7] = new OBJ_Key("Map");
        gp.obj[7].worldX = 34 * gp.tileSize;
        gp.obj[7].worldY = 8 * gp.tileSize;
        gp.obj[7].level = 1;

        gp.obj[8] = new OBJ_Crystals("purple");
        gp.obj[8].worldX = 5 * gp.tileSize;
        gp.obj[8].worldY = gp.tileSize;
        gp.obj[8].level = 1;

        gp.obj[9] = new OBJ_Crystals("purple");
        gp.obj[9].worldX = 13 * gp.tileSize;
        gp.obj[9].worldY = 6 * gp.tileSize;
        gp.obj[9].level = 1;

        gp.obj[10] = new OBJ_Crystals("purple");
        gp.obj[10].worldX = 17 * gp.tileSize;
        gp.obj[10].worldY = 4 * gp.tileSize;
        gp.obj[10].level = 1;

        gp.obj[11] = new OBJ_Crystals("purple");
        gp.obj[11].worldX = 22 * gp.tileSize;
        gp.obj[11].worldY = 8 * gp.tileSize;
        gp.obj[11].level = 1;

        gp.obj[12] = new OBJ_Crystals("purple");
        gp.obj[12].worldX = 9 * gp.tileSize;
        gp.obj[12].worldY = 8 * gp.tileSize;
        gp.obj[12].level = 2;

        gp.obj[13] = new OBJ_Crystals("purple");
        gp.obj[13].worldX = 0;
        gp.obj[13].worldY = 6 * gp.tileSize;
        gp.obj[13].level = 2;

        gp.obj[14] = new OBJ_Crystals("purple");
        gp.obj[14].worldX = 11 * gp.tileSize;
        gp.obj[14].worldY = 4 * gp.tileSize;
        gp.obj[14].level = 2;

        gp.obj[15] = new OBJ_Crystals("purple");
        gp.obj[15].worldX = 18 * gp.tileSize;
        gp.obj[15].worldY = 8 * gp.tileSize;
        gp.obj[15].level = 2;

        gp.obj[16] = new OBJ_Crystals("purple");
        gp.obj[16].worldX = 25 * gp.tileSize;
        gp.obj[16].worldY = gp.tileSize;
        gp.obj[16].level = 2;

        gp.obj[17] = new OBJ_Crystals("purple");
        gp.obj[17].worldX = 17 * gp.tileSize;
        gp.obj[17].worldY = 2 * gp.tileSize;
        gp.obj[17].level = 2;

        gp.obj[18] = new OBJ_Crystals("purple");
        gp.obj[18].worldX = 47 * gp.tileSize;
        gp.obj[18].worldY = 6 * gp.tileSize;
        gp.obj[18].level = 2;

        //LEVEL 3
        gp.obj[19] = new OBJ_Crystals("purple");
        gp.obj[19].worldX = 13 * gp.tileSize;
        gp.obj[19].worldY = 5 * gp.tileSize;
        gp.obj[19].level = 3;

        gp.obj[20] = new OBJ_Crystals("purple");
        gp.obj[20].worldX = 15 * gp.tileSize;
        gp.obj[20].worldY = 7 * gp.tileSize;
        gp.obj[20].level = 3;

        gp.obj[21] = new OBJ_Crystals("purple");
        gp.obj[21].worldX = 31 * gp.tileSize;
        gp.obj[21].worldY = 2 * gp.tileSize;
        gp.obj[21].level = 3;

        gp.obj[22] = new OBJ_Crystals("purple");
        gp.obj[22].worldX = 42 * gp.tileSize;
        gp.obj[22].worldY = 5 * gp.tileSize;
        gp.obj[22].level = 3;

        gp.obj[23] = new OBJ_Key("CrystalFinal");
        gp.obj[23].worldX = 46 * gp.tileSize;
        gp.obj[23].worldY = 3 * gp.tileSize;
        gp.obj[23].level = 3;

    }

    public void setNPC()
    {
        gp.npc[0] = new NPC_Samurai(gp);
        gp.npc[0].worldX = 2260;
        gp.npc[0].worldY = 130;
        gp.npc[0].hp = 200;
        gp.npc[0].maxHp = 200;
        gp.npc[0].level = 2;
        gp.npc[0].alive = true;
        gp.npc[0].id = 0;

        gp.npc[1] = new NPC_FlyingEye(gp);
        gp.npc[1].worldX = gp.tileSize * 3;
        gp.npc[1].worldY = gp.tileSize * 5;
        gp.npc[1].hp = 200;
        gp.npc[1].maxHp = 200;
        gp.npc[1].level = 3;
        gp.npc[1].alive = true;
        gp.npc[1].id = 1;

        gp.npc[2] = new NPC_Goblin(gp);
        gp.npc[2].worldX = gp.tileSize * 29;
        gp.npc[2].worldY = gp.tileSize * 4;
        gp.npc[2].hp = 200;
        gp.npc[2].maxHp = 200;
        gp.npc[2].level = 3;
        gp.npc[2].alive = true;
        gp.npc[2].id = 2;

        gp.npc[3] = new NPC_Sardonix(gp);
        gp.npc[3].worldX = gp.tileSize * 45;
        gp.npc[3].worldY = 0;
        gp.npc[3].hp = 200;
        gp.npc[3].maxHp = 200;
        gp.npc[3].level = 3;
        gp.npc[3].alive = true;
        gp.npc[3].id = 3;

        gp.npc[4] = new NPC_Warrior(gp);
        gp.npc[4].worldX = gp.tileSize * 36;
        gp.npc[4].worldY = gp.tileSize * 3;;
        gp.npc[4].hp = 200;
        gp.npc[4].maxHp = 200;
        gp.npc[4].level = 3;
        gp.npc[4].alive = true;
        gp.npc[4].id = 4;

    }

}
