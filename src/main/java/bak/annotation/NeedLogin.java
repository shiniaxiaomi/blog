package bak.annotation;

import java.lang.annotation.*;

/**
 * 标记注解,标记该注解的方法需要登入才能访问
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NeedLogin {
}
