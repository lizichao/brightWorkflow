package cn.com.bright.workflow.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Saeid Mirzaei
 */

/**
 * A method level annotation to mark the method as being a JMX attribute.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//@Inherited
//@Documented
public @interface StartProcess {

  String processKey() default "";

}
