package com.adtsw.jcommons.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class FileUtil {
    
    public static void writeLinesToFile(List<String> lines, String fileName, boolean compress) throws IOException {
        OutputStream fileOutputStream = new FileOutputStream(fileName);
        OutputStream outputStream = compress ? 
            new GZIPOutputStream(new FileOutputStream(fileName)) : 
            fileOutputStream;
        try {
            Writer writer = new OutputStreamWriter(outputStream, Charset.defaultCharset());
            try {
                String lineEnding = System.lineSeparator();
                for (final Object line : lines) {
                    if (line != null) {
                        writer.write(line.toString());
                    }
                    writer.write(lineEnding);
                }
            } finally {
                writer.close();
            }
        } finally {
            fileOutputStream.close();
        }
    }
}
