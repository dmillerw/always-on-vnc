package com.antlersoft.android.drawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.antlersoft.util.ObjectPool;
import com.antlersoft.util.SafeObjectPool;

public class OverlappingCopy {
    private static SafeObjectPool<Rect> ocRectPool = new SafeObjectPool<Rect>() {
        /* class com.antlersoft.android.drawing.OverlappingCopy.C00601 */

        /* access modifiers changed from: protected */
        @Override // com.antlersoft.util.ObjectPool
        public Rect itemForPool() {
            return new Rect();
        }
    };

    private static void transformRect(Rect source, Rect transformedSource, int deltaX, int deltaY) {
        transformedSource.set(deltaX < 0 ? source.right * -1 : source.left, deltaY < 0 ? source.bottom * -1 : source.top, deltaX < 0 ? source.left * -1 : source.right, deltaY < 0 ? source.top * -1 : source.bottom);
    }

    private static void copyTransformedRect(Rect stepSourceRect, Rect stepDestRect, int deltaX, int deltaY, Bitmap data, Canvas bitmapBackedCanvas, Paint paint) {
        transformRect(stepSourceRect, stepSourceRect, deltaX, deltaY);
        stepDestRect.set(stepSourceRect);
        stepDestRect.offset(deltaX, deltaY);
        bitmapBackedCanvas.drawBitmap(data, stepSourceRect, stepDestRect, paint);
    }

    public static void Copy(Bitmap data, Canvas bitmapBackedCanvas, Paint paint, Rect source, int destX, int destY) {
        Copy(data, bitmapBackedCanvas, paint, source, destX, destY, ocRectPool);
    }

    public static void Copy(Bitmap data, Canvas bitmapBackedCanvas, Paint paint, Rect source, int destX, int destY, ObjectPool<Rect> rectPool) {
        int absDeltaX;
        int absDeltaY;
        int xStepWidth;
        int yStepHeight;
        int deltaX = destX - source.left;
        int deltaY = destY - source.top;
        if (deltaX < 0) {
            absDeltaX = -deltaX;
        } else {
            absDeltaX = deltaX;
        }
        if (deltaY < 0) {
            absDeltaY = -deltaY;
        } else {
            absDeltaY = deltaY;
        }
        if (absDeltaX != 0 || absDeltaY != 0) {
            if (absDeltaX >= source.right - source.left || absDeltaY >= source.bottom - source.top) {
                ObjectPool.Entry<Rect> entry = rectPool.reserve();
                Rect dest = entry.get();
                dest.set(source.left + deltaX, source.top + deltaY, source.right + deltaX, source.bottom + deltaY);
                bitmapBackedCanvas.drawBitmap(data, source, dest, paint);
                rectPool.release(entry);
                return;
            }
            ObjectPool.Entry<Rect> transformedSourceEntry = rectPool.reserve();
            Rect transformedSource = transformedSourceEntry.get();
            transformRect(source, transformedSource, deltaX, deltaY);
            ObjectPool.Entry<Rect> transformedDestEntry = rectPool.reserve();
            Rect transformedDest = transformedDestEntry.get();
            transformedDest.set(transformedSource);
            transformedDest.offset(absDeltaX, absDeltaY);
            ObjectPool.Entry<Rect> intersectEntry = rectPool.reserve();
            Rect intersect = intersectEntry.get();
            intersect.setIntersect(transformedSource, transformedDest);
            boolean xStepDone = false;
            if (absDeltaX > absDeltaY) {
                xStepWidth = absDeltaX;
                yStepHeight = (source.bottom - source.top) - absDeltaY;
            } else {
                xStepWidth = (source.right - source.left) - absDeltaX;
                yStepHeight = absDeltaY;
            }
            ObjectPool.Entry<Rect> stepSourceEntry = rectPool.reserve();
            Rect stepSourceRect = stepSourceEntry.get();
            ObjectPool.Entry<Rect> stepDestEntry = rectPool.reserve();
            Rect stepDestRect = stepDestEntry.get();
            int xStep = 0;
            while (!xStepDone) {
                int stepRight = intersect.right - (xStep * xStepWidth);
                int stepLeft = stepRight - xStepWidth;
                if (stepLeft <= intersect.left) {
                    stepLeft = intersect.left;
                    xStepDone = true;
                }
                boolean yStepDone = false;
                int yStep = 0;
                while (!yStepDone) {
                    int stepBottom = intersect.bottom - (yStep * yStepHeight);
                    int stepTop = stepBottom - yStepHeight;
                    if (stepTop <= intersect.top) {
                        stepTop = intersect.top;
                        yStepDone = true;
                    }
                    stepSourceRect.set(stepLeft, stepTop, stepRight, stepBottom);
                    copyTransformedRect(stepSourceRect, stepDestRect, deltaX, deltaY, data, bitmapBackedCanvas, paint);
                    yStep++;
                }
                xStep++;
            }
            if (absDeltaX > 0) {
                stepSourceRect.set(transformedSource.left, transformedSource.top, intersect.left, transformedSource.bottom);
                copyTransformedRect(stepSourceRect, stepDestRect, deltaX, deltaY, data, bitmapBackedCanvas, paint);
            }
            if (absDeltaY > 0) {
                stepSourceRect.set(intersect.left, transformedSource.top, transformedSource.right, intersect.top);
                copyTransformedRect(stepSourceRect, stepDestRect, deltaX, deltaY, data, bitmapBackedCanvas, paint);
            }
            rectPool.release(stepDestEntry);
            rectPool.release(stepSourceEntry);
            rectPool.release(intersectEntry);
            rectPool.release(transformedDestEntry);
            rectPool.release(transformedSourceEntry);
        }
    }
}
