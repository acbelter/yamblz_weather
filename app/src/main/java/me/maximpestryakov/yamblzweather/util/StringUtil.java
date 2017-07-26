package me.maximpestryakov.yamblzweather.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StringUtil {

    private final Context context;

    public StringUtil(Context context) {
        this.context = context;
    }

    public String readFromFile(String filename) throws IOException {
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

    public void writeToFile(String filename, String content) throws IOException {
        try (OutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            outputStream.write(content.getBytes());
        }
    }
}
