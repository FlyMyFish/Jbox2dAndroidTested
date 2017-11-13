package com.shichen.jbox2dandroidtested;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.shichen.jbox2dandroidtested.Ball.defaultRadius;
import static com.shichen.jbox2dandroidtested.Config.FRICTION_RATIO;
import static com.shichen.jbox2dandroidtested.Config.RESTITUTION_RATIO;
import static com.shichen.jbox2dandroidtested.Config.dt;
import static com.shichen.jbox2dandroidtested.Config.positionIterations;
import static com.shichen.jbox2dandroidtested.Config.velocityIterations;

/**
 * Created by shichen on 2017/11/10.
 *
 * @author shichen 754314442@qq.com
 */

public class SimulateWorld extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private World mWorld;
    private int mWorldWidth, mWorldHeight;
    private SurfaceHolder mHolder;
    private boolean mIsDrawing;
    private Canvas mCanvas;
    private float mDensity = 0.6f;


    private Paint mPaint;

    public SimulateWorld(Context context) {
        this(context, null);
    }

    public SimulateWorld(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDensity = context.getResources().getDisplayMetrics().density;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(0xFFFF0000);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
    }

    private ExecutorService singleThreadPool;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mIsDrawing = true;
        this.mWorldWidth = getWidth();
        this.mWorldHeight = getHeight();
        createWorld();
        createWorldChild();
        ThreadFactory weatherThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("WeatherViewUpdate").build();
        singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), weatherThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(this);
    }

    /**
     * 创建物理世界
     */
    private void createWorld() {
        if (mWorld == null) {
            mWorld = new World(new Vec2(0, 10.0f));
            //构建物理世界：参数含义x方向加速度0，y方向加速度10.0f
            updateTopAndBottomBounds();
            updateLeftAndRightBounds();
        }
    }

    private void createWorldChild() {
        createBody();
    }

    private List<AndroidBody> bodyList = new ArrayList<>();

    private void createBody() {
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 6; i++) {
                bodyList.add(new Ball(mWorld, 0 + i * 2 * (defaultRadius) / Config.PROPORTION, (1 + j) * (defaultRadius) / Config.PROPORTION, (defaultRadius / 2.0f) / Config.PROPORTION));
            }
        }
    }

    private void updateTopAndBottomBounds() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        PolygonShape shape = new PolygonShape();
        float hx = mappingView2Body(mWorldWidth);
        float hy = mappingView2Body(Config.PROPORTION);
        shape.setAsBox(hx, hy);

        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.density = mDensity;
        def.friction = FRICTION_RATIO;
        def.restitution = RESTITUTION_RATIO;

        bodyDef.position.set(0, -hy);
        Body topBody = mWorld.createBody(bodyDef);
        topBody.createFixture(def);

        bodyDef.position.set(0, mappingView2Body(mWorldHeight) + hy);
        Body bottomBody = mWorld.createBody(bodyDef);
        bottomBody.createFixture(def);
    }

    private void updateLeftAndRightBounds() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        PolygonShape shape = new PolygonShape();
        float hx = mappingView2Body(Config.PROPORTION);
        float hy = mappingView2Body(mWorldHeight);
        shape.setAsBox(hx, hy);

        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.density = mDensity;
        def.friction = FRICTION_RATIO;
        def.restitution = RESTITUTION_RATIO;

        bodyDef.position.set(-hx, hy);
        Body leftBody = mWorld.createBody(bodyDef);
        leftBody.createFixture(def);

        bodyDef.position.set(mappingView2Body(mWorldWidth) + hx, 0);
        Body rightBody = mWorld.createBody(bodyDef);
        rightBody.createFixture(def);
    }

    private float mappingView2Body(float view) {
        return view / Config.PROPORTION;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mIsDrawing = false;
        singleThreadPool.shutdown();
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            draw();
        }
    }

    private void draw() {
        try {
            if (mWorld != null) {
                mWorld.step(dt, velocityIterations, positionIterations);
                for (int i = 0; i < destoryBodyList.size(); i++) {
                    mWorld.destroyBody(destoryBodyList.get(i));
                }
                for (int i = 0; i < newBodyCount; i++) {
                    bodyList.add(new Ball(mWorld, mappingView2Body(getWidth() / 2), (defaultRadius / 2.0f) / Config.PROPORTION, (defaultRadius / 2.0f) / Config.PROPORTION));
                }
                destoryBodyList.clear();
                newBodyCount = 0;
            }
            mCanvas = mHolder.lockCanvas();
            // SurfaceView背景
            mCanvas.drawColor(Color.parseColor("#3CA0D0"));
            for (int i = 0; i < bodyList.size(); i++) {
                bodyList.get(i).drawSelf(mCanvas, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private List<Body> destoryBodyList = new ArrayList<>();

    public void removeOne() {
        if (bodyList.size() > 0) {
            destoryBodyList.add(bodyList.get(0).body);
            bodyList.remove(0);
        }
    }

    private int newBodyCount = 0;

    public void addNewOne() {
        newBodyCount++;
    }
}
