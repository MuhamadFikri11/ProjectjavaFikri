import frame.BrandsepatuViewFrame;
import frame.SepatuViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        //Koneksi.getConnection();
        BrandsepatuViewFrame viewFrame = new BrandsepatuViewFrame();
//        SepatuViewFrame viewFrame = new SepatuViewFrame();
        viewFrame.setVisible(true);
    }
}
