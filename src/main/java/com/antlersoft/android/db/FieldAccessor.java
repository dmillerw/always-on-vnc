package com.antlersoft.android.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
/* renamed from: com.antlersoft.android.db.FieldAccessor */
public @interface FieldAccessor {
    boolean Both() default true;

    String DefaultValue() default "";

    String Name() default "";

    boolean Nullable() default true;

    FieldType Type() default FieldType.DEFAULT;

    FieldVisibility Visibility() default FieldVisibility.PRIVATE;
}
