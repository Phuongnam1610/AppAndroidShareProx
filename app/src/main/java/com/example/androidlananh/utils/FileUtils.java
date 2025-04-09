package com.example.androidlananh.utils;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.firestore.v1.Cursor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {
    public static File uriToFile( Context context,Uri uri) {
        InputStream inputStream = null;
        File tempFile = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            tempFile = File.createTempFile("temp", ".jpg");
            FileOutputStream fileOut = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOut.write(buffer, 0, length);
            }
            fileOut.close();
            tempFile.deleteOnExit();
            return tempFile;
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
