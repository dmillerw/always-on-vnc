package com.antlersoft.android.bc;

import android.content.Context;
import android.os.Build;

/* renamed from: com.antlersoft.android.bc.BCFactory */
public class BCFactory {
    private static BCFactory _theInstance = new BCFactory();
    private static Class[] scaleDetectorConstructorArgs = {Context.class, OnScaleGestureListener.class};
    private IBCActivityManager bcActivityManager;
    private IBCGestureDetector bcGestureDetector;
    private IBCHaptic bcHaptic;
    private IBCMotionEvent bcMotionEvent;
    private IBCStorageContext bcStorageContext;
    private IBCSystemUiVisibility bcSystemUiVisibility;

    /* access modifiers changed from: package-private */
    public int getSdkVersion() {
        try {
            return Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public IBCActivityManager getBCActivityManager() {
        if (this.bcActivityManager == null) {
            synchronized (this) {
                if (this.bcActivityManager == null) {
                    if (getSdkVersion() >= 5) {
                        try {
                            this.bcActivityManager = (IBCActivityManager) getClass().getClassLoader().loadClass("com.antlersoft.android.bc.BCActivityManagerV5").newInstance();
                        } catch (Exception ie) {
                            this.bcActivityManager = new BCActivityManagerDefault();
//                            throw new RuntimeException("Error instantiating", ie);
                        }
                    } else {
                        this.bcActivityManager = new BCActivityManagerDefault();
                    }
                }
            }
        }
        return this.bcActivityManager;
    }

    public IBCGestureDetector getBCGestureDetector() {
        if (this.bcGestureDetector == null) {
            synchronized (this) {
                if (this.bcGestureDetector == null) {
                    try {
                        this.bcGestureDetector = (IBCGestureDetector) getClass().getClassLoader().loadClass("com.antlersoft.android.bc.BCGestureDetectorDefault").newInstance();
                    } catch (Exception ie) {
                        throw new RuntimeException("Error instantiating", ie);
                    }
                }
            }
        }
        return this.bcGestureDetector;
    }

    public IBCHaptic getBCHaptic() {
        if (this.bcHaptic == null) {
            synchronized (this) {
                if (this.bcHaptic == null) {
                    try {
                        this.bcHaptic = (IBCHaptic) getClass().getClassLoader().loadClass("com.antlersoft.android.bc.BCHapticDefault").newInstance();
                    } catch (Exception ie) {
                        throw new RuntimeException("Error instantiating", ie);
                    }
                }
            }
        }
        return this.bcHaptic;
    }

    public IBCMotionEvent getBCMotionEvent() {
        if (this.bcMotionEvent == null) {
            synchronized (this) {
                if (this.bcMotionEvent == null) {
                    if (getSdkVersion() >= 5) {
                        try {
                            this.bcMotionEvent = (IBCMotionEvent) getClass().getClassLoader().loadClass("com.antlersoft.android.bc.BCMotionEvent5").newInstance();
                        } catch (Exception ie) {
                            throw new RuntimeException("Error instantiating", ie);
                        }
                    } else {
                        try {
                            this.bcMotionEvent = (IBCMotionEvent) getClass().getClassLoader().loadClass("com.antlersoft.android.bc.BCMotionEvent4").newInstance();
                        } catch (Exception ie2) {
                            throw new RuntimeException("Error instantiating", ie2);
                        }
                    }
                }
            }
        }
        return this.bcMotionEvent;
    }

    public IBCScaleGestureDetector getScaleGestureDetector(Context context, OnScaleGestureListener listener) {
        if (getSdkVersion() < 5) {
            return new DummyScaleGestureDetector();
        }
        try {
            return (IBCScaleGestureDetector) getClass().getClassLoader().loadClass("com.antlersoft.android.bc.ScaleGestureDetector").getConstructor(scaleDetectorConstructorArgs).newInstance(context, listener);
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating ScaleGestureDetector", e);
        }
    }

    public IBCStorageContext getStorageContext() {
        if (this.bcStorageContext == null) {
            synchronized (this) {
                if (this.bcStorageContext == null) {
                    if (getSdkVersion() >= 8) {
                        try {
                            this.bcStorageContext = (IBCStorageContext) getClass().getClassLoader().loadClass("com.antlersoft.android.bc.BCStorageContext8").newInstance();
                        } catch (Exception ie) {
                            throw new RuntimeException("Error instantiating", ie);
                        }
                    } else {
                        try {
                            this.bcStorageContext = (IBCStorageContext) getClass().getClassLoader().loadClass("com.antlersoft.android.bc.BCStorageContext7").newInstance();
                        } catch (Exception ie2) {
                            throw new RuntimeException("Error instantiating", ie2);
                        }
                    }
                }
            }
        }
        return this.bcStorageContext;
    }

    public IBCSystemUiVisibility getSystemUiVisibility() {
        if (this.bcSystemUiVisibility == null) {
            synchronized (this) {
                if (this.bcSystemUiVisibility == null) {
                    if (getSdkVersion() >= 19) {
                        try {
                            this.bcSystemUiVisibility = (IBCSystemUiVisibility) getClass().getClassLoader().loadClass("com.antlersoft.android.bc.BCSystemUiVisibility19").newInstance();
                        } catch (Exception ie) {
                            throw new RuntimeException("Error instantiating", ie);
                        }
                    } else {
                        this.bcSystemUiVisibility = new DefaultSystemUiVisibility();
                    }
                }
            }
        }
        return this.bcSystemUiVisibility;
    }

    public static BCFactory getInstance() {
        return _theInstance;
    }
}
