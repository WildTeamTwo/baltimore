package com.baltimore.subprogram;

/**
 * Created by paul on 26.10.18.
 */
public interface SubProgram {
    String displayName();

    void start() throws Exception;

    void releaseResources();

    void end();


}
