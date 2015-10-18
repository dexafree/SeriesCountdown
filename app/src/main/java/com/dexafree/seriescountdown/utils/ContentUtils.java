package com.dexafree.seriescountdown.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;


/**
 * Created by Carlos on 22/9/15.
 */
public class ContentUtils {

    public static String readContentFromStream(InputStream stream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(stream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            byte b = (byte)result;
            buf.write(b);
            result = bis.read();
        }
        stream.close();
        return buf.toString();
    }

    public static String cleanHTMLtags(String input){
        String linebreak = "<br ?/?>";
        String tag = "</?[a-zA-Z]+ ?>";

        return input.replaceAll(linebreak, "\n").replaceAll(tag, "");
    }

}
