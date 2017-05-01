package com.dajia.util;

/**
 *@author fucheng
 *@date 2012-7-30
 *@comment
 */
public class SharedPreferenceException extends Exception {
    
    public SharedPreferenceException() {
        super();
    }
    
    public SharedPreferenceException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
    
    public SharedPreferenceException(String detailMessage) {
        super(detailMessage);
    }
    
    public SharedPreferenceException(Throwable throwable) {
        super(throwable);
    }
    
}
