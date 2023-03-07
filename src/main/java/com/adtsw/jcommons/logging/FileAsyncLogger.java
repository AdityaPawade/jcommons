package com.adtsw.jcommons.logging;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

public class FileAsyncLogger<E> extends AbstractAsyncLogger<E> {

    private final File logFile;

    public FileAsyncLogger(String filePath, int bufferSize, int flushThresholdPercentage) {
        super(bufferSize, flushThresholdPercentage);
        this.logFile = new File(filePath);
    }

    @Override
    protected void doFlush(List<E> events) {

        List<String> stringEvents = events.stream().map(new Function<E, String>() {
            @Override
            public String apply(E event) {
                return event.toString();
            }
        }).collect(Collectors.toList());

        try {
            FileUtils.writeLines(logFile, Charset.defaultCharset().toString(), stringEvents, true);
        } catch (IOException e) {
            throw new RuntimeException("IOException while writing to file " + logFile.getName(), e);
        }
    }
}
