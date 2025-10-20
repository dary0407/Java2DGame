package Object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends SuperObject{

    public OBJ_Key(String type) {
        switch (type) {
            case "Map":
                name = "Map";
                try {
                    image = ImageIO.read(getClass().getResourceAsStream("/res/objects/mapa.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "CrystalFinal":
                name = "CrystalFinal";
                try {
                    image = ImageIO.read(getClass().getResourceAsStream("/res/objects/cristalFinal.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                System.out.println("Unknown type: " + type);
        }
    }
}
