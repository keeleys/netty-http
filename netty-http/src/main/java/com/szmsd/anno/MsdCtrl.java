package com.szmsd.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author TianJun
 * @param url : /user
 * @param method : execute
 * @Description controller注解,url /开头
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface MsdCtrl {
	String url();
	String method() default "execute";
}
