package org.xy.passportScanner.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfUtil {

    public static List<BufferedImage> pdfToImage(File file){
        List<BufferedImage> result = null;
        try {
            PDDocument doc = Loader.loadPDF(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            if (pageCount > 0){
                result = new ArrayList<>();
            }
            for(int i=0;i<pageCount;i++){
                BufferedImage image = renderer.renderImageWithDPI(i, 150);
                result.add(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
