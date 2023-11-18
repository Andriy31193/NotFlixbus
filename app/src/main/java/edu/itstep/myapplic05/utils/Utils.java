package edu.itstep.myapplic05.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static String loadJSONFromAsset(Context context, String filename) {
        String json;
        try {
            InputStream inputStream = context.getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            int bytesRead = inputStream.read(buffer);

            if (bytesRead > 0)
                json = new String(buffer, StandardCharsets.UTF_8);
            else json = null;

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

}
