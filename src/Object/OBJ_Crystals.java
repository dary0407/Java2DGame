package Object;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Crystals extends SuperObject {

    private BufferedImage[] animationFrames;
    private int currentFrame = 0;
    private int frameCount = 4; // nr frames in animatii
    private int frameWidth = 64; //
    private int frameHeight = 64;
    private int animationTimer = 0;//
    public boolean isAnimated = false;

    public OBJ_Crystals(String color) {
        name = "Crystal_" + color;
        try {
            if (color.equals("purple")) {
                isAnimated = true;
                BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/res/objects/spritesheetMov.png"));

                animationFrames = new BufferedImage[frameCount];

                animationFrames[0] = spritesheet.getSubimage(0, 0, frameWidth, frameHeight); // top-left
                animationFrames[1] = spritesheet.getSubimage(frameWidth, 0, frameWidth, frameHeight); // top-right
                animationFrames[2] = spritesheet.getSubimage(0, frameHeight, frameWidth, frameHeight); // bottom-left
                animationFrames[3] = spritesheet.getSubimage(frameWidth, frameHeight, frameWidth, frameHeight); // bottom-right

                image = animationFrames[0];

                breakable = false;
                durability = 0;

            } else {
                try {
                    image = ImageIO.read(getClass().getResourceAsStream("/res/objects/" + color + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                breakable = true;

                switch (color) {
                    case "black":
                        durability = 1;
                        break;
                    case "red":
                        durability = 3;
                        break;
                    case "blue":
                        durability = 2;
                        break;
                    case "pink":
                        durability = 2;
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update() {
        if (isAnimated && animationFrames != null) {
            animationTimer++;
            if (animationTimer > 10) {
                currentFrame = (currentFrame + 1) % animationFrames.length;
                image = animationFrames[currentFrame];
                animationTimer = 0;
            }
        }
    }
}