package lk.ac.mrt.cse.medipal.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lakshan on 10/21/17.
 */
public class ImageUtil {
    public static Logger LOGGER = org.apache.log4j.Logger.getLogger(ImageUtil.class);
    public static String imagePath = "/home/medipal/images/";
    public static String stringtoImage(String encodedImageStr, String fileName, String extension) {
        String fileDirectory = "profile/";
        String filePath = null;
        try {
            fileName = fileName + "." + extension;
            filePath = imagePath + fileDirectory + fileName;
            // Decode String using Base64 Class
            byte[] imageByteArray = Base64.decodeBase64(encodedImageStr);

            // Write Image into File system - Make sure you update the path
            FileOutputStream imageOutFile = new FileOutputStream(filePath);
            imageOutFile.write(imageByteArray);
            imageOutFile.close();
        } catch (FileNotFoundException ex) {
            LOGGER.error("Image Path not found", ex);
        } catch (IOException ex) {
            LOGGER.error("Exception while converting the Image " , ex);
        }
        return filePath;
    }
}
