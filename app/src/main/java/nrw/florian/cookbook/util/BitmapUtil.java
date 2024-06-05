package nrw.florian.cookbook.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * @author Florian J. Kleine-Vorholt
 */
public class BitmapUtil {

    /**
     * Converts byte array to bitmap.
     * @param bytes byte array to be converted
     * @return Bitmap image from the byte array
     */
    public static Bitmap byteToBitmap(byte[] bytes)
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
    public static byte[] bitmapToByte(Bitmap bitmap)
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


    /**
     * Compresses a bitmap to a specified size.
     * @param bitmap bitmap to be compressed
     * @param newHeight height of the compressed bitmap
     * @param newQuality quality of the compressed bitmap
     * @return compressed bitmap
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int newHeight, int newQuality)
    {
        return compressBitmap(bitmap, newHeight, (newHeight*bitmap.getWidth()/bitmap.getHeight()), newQuality);
    }


    /**
     * Compresses a bitmap to a specified size.
     * @param bitmap bitmap to be compressed
     * @param newHeight height of the compressed bitmap
     * @param newWidth width of the compressed bitmap
     * @param newQuality quality of the compressed bitmap
     * @return compressed bitmap
     */
    private static Bitmap compressBitmap(final Bitmap bitmap, final int newHeight, final int newWidth, final int newQuality)
    {
        // RESCALE THE BITMAP
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        // Compress
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, newQuality, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}