package me.maximpestryakov.yamblzweather.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;

public class StringUtil {

    public static String getErrorMessage(Throwable throwable) {
        Context context = App.getContext();
        if (throwable instanceof NoInternetException) {
            return context.getString(R.string.error_no_internet);
        }
        return context.getString(R.string.error_unknown);
    }

    public static String readFromFile(String filename) throws IOException {
        Context context = App.getContext();
        try (InputStream inputStream = context.openFileInput(filename)) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append('\n');
                }
                return sb.toString();
            }
        }
    }

    public static void writeToFile(String filename, String content) throws IOException {
        Context context = App.getContext();
        try (OutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            outputStream.write(content.getBytes());
        }
    }
}
