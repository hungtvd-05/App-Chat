package testing;

import com.app.swing.blurHash.BlurHash;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("D:\\Users\\Finn\\Downloads\\FPT-Polytechnic-DN-poster-marvel-4-1.png"));
        String blurhashStr = BlurHash.encode(image);
        System.out.println(blurhashStr);
    } 
}

// L68EoVt80JNdpyXBrpt7DNRjrps;