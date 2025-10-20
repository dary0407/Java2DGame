package Entity;

import Main.EntityType;
import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    GamePanel gp;

    public EntityType entityType;
    public int worldX, worldY;
    public int speed;

    public boolean jumping = false; //variabila pentru sarit
    public double gravity = 0.5; //gravitatea
    public boolean wall = false;
    public boolean collision = false; //initial nu exista coliziuni
    public int solidAreaDefaultX;   //area "solida" a entitatilor
    public int solidAreaDefaultY;
    public boolean entityCollision = false;
    public Entity contactEntity = null;
    public boolean alive = true;
    public boolean dying = false;
    public int hp;
    public int maxHp; //viata maxima a unei entitati
    public int level;
    public String type; //tipul pentru entity
    public int id;


    //variabile pentru animatiile entitatii player
    public BufferedImage sta1, sta2, sta3, sta4, attack1, attack2, attack3, attack4, attack5, attack6, attack7, attack8, sare1, sare2, sare3, sareleft1, sareleft2, sareleft3, runleft1, runleft2, runleft3, runleft4, sareleft;
    public BufferedImage run1, run2, run3, run4;
    public BufferedImage tarnacop1, tarnacop2, tarnacop3;
    public BufferedImage arc1, arc2, arc3, arc4, arc5, arc6;

    public String direction; //directia de miscare
    public Rectangle solidArea;
    public boolean horizontalCollision = false;
    public boolean verticalCollision = false;

    public double velocityY = 0;

    public int spriteCounter = 0;

    public int spriteNum = 1;
    public int actionLockCounter = 0; //schimba random animatiile
    public boolean attacking = false;
    public int attackCounter = 0; //
    public int deathCounter = 0;
    public int attackCooldown = 0;

    // SAMURAI
    public BufferedImage runleft1samurai, runleft2samurai, runleft3samurai, runleft4samurai, runleft5samurai, runleft6samurai, runleft7samurai, runleft8samurai;
    public BufferedImage runright1samurai, runright2samurai, runright3samurai, runright4samurai, runright5samurai, runright6samurai, runright7samurai, runright8samurai;
    public BufferedImage attackleft1Samurai, attackleft2Samurai, attackleft3Samurai, attackleft4Samurai, attackleft5Samurai;
    public BufferedImage attackright1Samurai, attackright2Samurai, attackright3Samurai, attackright4Samurai, attackright5Samurai;
    public BufferedImage deadleft1Samurai, deadleft2Samurai, deadleft3Samurai, deadleft4Samurai, deadleft5Samurai, deadleft6Samurai;
    public BufferedImage deadright1Samurai, deadright2Samurai, deadright3Samurai, deadright4Samurai, deadright5Samurai, deadright6Samurai;

    //FLYING EYE
    public BufferedImage runleft1npc, runleft2npc, runleft3npc, runleft4npc, runleft5npc, runleft6npc;
    public BufferedImage runright1npc, runright2npc, runright3npc, runright4npc, runright5npc, runright6npc;
    public BufferedImage dead1FlyingEye, dead2FlyingEye, dead3FlyingEye, dead4FlyingEye, dead5FlyingEye, dead6FlyingEye, dead7FlyingEye, dead8FlyingEye;

    //GOBLIN
    public BufferedImage attack1goblin, attack2goblin, attack3goblin, attack4goblin, attack5goblin, attack6goblin, attack7goblin, attack8goblin, attack9goblin, attack10goblin, attack11goblin, attack12goblin;
    public BufferedImage dead1Goblin, dead2Goblin, dead3Goblin, dead4Goblin;

    //SARDONIX
    public BufferedImage idle1, idle2, idle3, idle4, idle5, idle6;
    public BufferedImage attack1Sardonix, attack2Sardonix, attack3Sardonix, attack4Sardonix, attack5Sardonix, attack6Sardonix;

    //WARRIOR
    public BufferedImage attack1warrior, attack2warrior, attack3warrior, attack4warrior, attack5warrior, attack6warrior, attack7warrior, attack8warrior, attack9warrior, attack10warrior, attack11warrior, attack12warrior, attack13warrior, attack14warrior;
    public BufferedImage dead1Warrior, dead2Warrior, dead3Warrior, dead4Warrior;

    public void update() {
    }

    public void draw(Graphics2D g2) {

    }

    public void setAction() {
    }

    public void takeDamage(int damage) {
        if (!dying && alive) {
            hp -= damage;
            if (hp <= 0) {
                hp = 0;
                dying = true;
                spriteNum = 1;
                deathCounter = 0;
                // You can add any additional death logic here or override in subclasses
            }
        }
    }

    public void dyingAnimation() {
        deathCounter++;

        // Se schimba sprite-ul o data la 10 "ticks"
        if (deathCounter % 10 == 0) {
            spriteNum++;
            if (spriteNum > 6) {  //6 frames
                dying = false;    //animatia pentru "moarte" se termina
                alive = false;   //devine mort
            }
        }
    }
}
