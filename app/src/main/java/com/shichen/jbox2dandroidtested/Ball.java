package com.shichen.jbox2dandroidtested;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import static com.shichen.jbox2dandroidtested.Config.FRICTION_RATIO;
import static com.shichen.jbox2dandroidtested.Config.RESTITUTION_RATIO;

/**
 * Created by shichen on 2017/11/13.
 *
 * @author shichen 754314442@qq.com
 */

public class Ball extends AndroidBody {
    private float radius=defaultRadius;

    public static final float defaultRadius=40.0f;

    public Ball(World world, float xInWorld, float yInWorld, float radiusInWorld) {
        this.radius = radiusInWorld;
        body = setupBody(world, xInWorld, yInWorld);
    }

    @Override
    public Body setupBody(World mWorld, float xInWorld, float yInWorld) {
        final float density = 4.0f;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;

        bodyDef.position.set(xInWorld, yInWorld);
        Shape shape = createCircleBody();

        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.density = density;
        def.friction = FRICTION_RATIO;
        def.restitution = RESTITUTION_RATIO;

        Body ball = mWorld.createBody(bodyDef);
        ball.createFixture(def);
        return ball;
    }

    private Shape createCircleBody() {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        return circleShape;
    }

    @Override
    public void drawSelf(Canvas mCanvas, Paint paint) {
        Log.d("BallInfo", "x=" + body.getPosition().x * Config.PROPORTION + ";y=" + body.getPosition().y * Config.PROPORTION + ";r=" + radius * Config.PROPORTION);
        mCanvas.drawCircle(body.getPosition().x * Config.PROPORTION, body.getPosition().y * Config.PROPORTION, radius * Config.PROPORTION, paint);
    }
}
