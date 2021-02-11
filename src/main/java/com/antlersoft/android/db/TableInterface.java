package com.antlersoft.android.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
/* renamed from: com.antlersoft.android.db.TableInterface */
public @interface TableInterface {
    String ImplementingClassName() default "";

    boolean ImplementingIsAbstract() default true;

    boolean ImplementingIsPublic() default true;

    String TableName() default "";
}
