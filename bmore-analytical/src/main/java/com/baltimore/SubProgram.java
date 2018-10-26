package com.baltimore;

/**
 * Created by paul on 26.10.18.
 */
public interface SubProgram  {
    String displayName();
    void start() throws Exception;
    void destroy();
    void end();


}
