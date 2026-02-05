package net.xuyifei.lolipickaxe.common.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigField {

    ConfigType[] type();

    String comment();

    ValueType valueType();

    int intDefaultValue() default 0;

    int intMinValue() default 0;

    int intMaxValue() default 0;

    String intMinValueField() default "";

    String intMaxValueField() default "";

    double doubleDefaultValue() default 0.0;

    double doubleMinValue() default 0;

    double doubleMaxValue() default 0;

    String doubleMinValueField() default "";

    String doubleMaxValueField() default "";

    boolean booleanDefaultValue() default false;

    String stringDefaultValue() default "";

    String[] listDefaultValue() default {};

    ValueType listType() default ValueType.STRING;

    String[] mapDefaultValue() default {};

    ValueType mapKeyType() default ValueType.STRING;

    ValueType mapValueType() default ValueType.INT;

    boolean warning() default false;

    String warningMethod() default "";

    enum ConfigType {
        NONE, CONFIG, COMMAND, GUI
    }

    enum ValueType {
        INT, DOUBLE, BOOLEAN, STRING, LIST, MAP
    }

}

