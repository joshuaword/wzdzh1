package com.dajia.util;


/**
 *@author fucheng
 *@date 2012-6-13
 *@comment
 */
public abstract class SharedPreferencesDTO<T>  {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1594330378580200935L;
    
    public abstract boolean isSame(T o);
}
