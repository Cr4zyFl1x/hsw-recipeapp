package nrw.florian.cookbook.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * @author Florian J. Kleine-Vorholt
 */
public class ByteStreamUtil {

    /**
     * Converts byte array to bitmap.
     * @param bytes byte array to be converted
     * @return Bitmap image from the byte array
     */
    public static Bitmap byte2Bitmap(byte[] bytes)
    {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * Converts a bitmap to a byte array.
     * @param bitmap bitmap to be converted
     * @return byte array from the bitmap
     */
    public static byte[] bitmap2Byte(Bitmap bitmap)
    {
        if (bitmap == null) {
            return null;
        }
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}