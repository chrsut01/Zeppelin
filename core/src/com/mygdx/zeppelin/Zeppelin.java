package com.mygdx.zeppelin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Zeppelin extends Rectangle {
    private Texture zeppelinImage;
    private Sound engineSound;
    int screenWidth = GameConfig.SCREEN_WIDTH;
    int screenHeight = GameConfig.SCREEN_HEIGHT;

    public Zeppelin(float x, float y, float width, float height) {
        super(x, y, width, height);
        zeppelinImage = new Texture(Gdx.files.internal("zeppelin_image1.png"));
        engineSound = Gdx.audio.newSound(Gdx.files.internal("ZeppelinEngine.mp3"));
        playEngineSound(2.2f); // Set the initial volume (you can change this value)
    }

    public void update() {
        handleInput();
    }

    public Texture getZeppelinImage() {
        return zeppelinImage;
    }


    public void playEngineSound(float volume) {
        engineSound.loop(volume);
        // You can add more parameters for pitch, pan, etc., based on your requirements.
    }

    private void handleInput() {
        // Handle user input for zeppelin movement
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            // camera.unproject(touchPos); // If needed
            x = touchPos.x - width / 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            x -= 30 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            x += 30 * Gdx.graphics.getDeltaTime();

        // Ensure the zeppelin stays within the screen bounds
        if (x < 0)
            x = 0;
        if (x > screenWidth - width)
            x = screenWidth - width;

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            y += 30 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            y -= 30 * Gdx.graphics.getDeltaTime();

        // Ensure the zeppelin stays within the screen bounds
        if (y < 0)
            y = 0;
        if (y > screenHeight - height)
            y = screenHeight - height;
    }

    public void dispose() {
        zeppelinImage.dispose();
        engineSound.dispose();
    }
}
