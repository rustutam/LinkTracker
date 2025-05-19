package backend.academy.bot.api.tg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageHandlerAnnotation {
    int priority() default 0;

    TgCommand[] commands() default {};

    States[] states() default {};
}
