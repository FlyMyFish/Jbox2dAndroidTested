package com.shichen.jbox2dandroidtested;

/**
 * Created by shichen on 2017/11/13.
 *
 * @author shichen 754314442@qq.com
 */

public class Config {
    /**
     * 物理世界与手机屏幕的比例
     */
    public static final int PROPORTION = 50;
    /**
     * 摩擦系数
     */
    public static final float FRICTION_RATIO = 0.8f;
    /**
     * 弹性系数
     */
    public static final float RESTITUTION_RATIO = 0.6f;

    /**
     * 刷新率
     */
    public static final float dt = 1f / 60f;
    /**
     * 速率约束控制系数
     */
    public static final int velocityIterations = 5;
    /**
     * 位置约束控制系数
     */
    public static final int positionIterations = 20;
}
