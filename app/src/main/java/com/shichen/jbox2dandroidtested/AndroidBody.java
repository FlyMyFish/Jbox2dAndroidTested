package com.shichen.jbox2dandroidtested;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/**
 * Created by shichen on 2017/11/13.
 *
 * @author shichen 754314442@qq.com
 */

public abstract class AndroidBody {
    protected Body body;

    public abstract Body setupBody(World world, float xInWorld, float yInWorld);

    public abstract void drawSelf(Canvas mCanvas, Paint paint);
}
