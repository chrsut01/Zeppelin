package com.mygdx.zeppelin;

import com.badlogic.gdx.math.Rectangle;

public class Plane extends Rectangle{
    private int yAngle;

    public Plane(float x, float y, int yAngle) {
        super(x, y, 90, 50);
        this.yAngle = yAngle;
    }

    public int getyAngle() {
        return yAngle;
    }
/* public void updatePosition(float deltaTime) {
        y -= yAngle * deltaTime;
        x -= 200 * deltaTime;
    }*/
}

