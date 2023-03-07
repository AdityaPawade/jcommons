package com.adtsw.jcommons.logging;

public interface Logger<E> {
    
    void log(E message);

    void flush();
    
    void flush(boolean async);

    void shutdown();
}
