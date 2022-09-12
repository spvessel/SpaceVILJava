package sandbox.View.Decorations;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageResources {
    private ImageResources() {
    }

    public static void loadImages() {
        try {
            InputStream stream = ImageResources.class.getResourceAsStream("/images/art.jpg");
            Art = ImageIO.read(stream);
            stream = ImageResources.class.getResourceAsStream("/images/spacevil_logo.png");
            SpaveVILLogo = ImageIO.read(stream);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage Art = null;
    public static BufferedImage SpaveVILLogo = null;
}
