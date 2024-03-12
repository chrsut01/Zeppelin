package com.mygdx.zeppelin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Boot game;

    OrthographicCamera camera;

    int screenWidth = GameConfig.SCREEN_WIDTH;
    int screenHeight = GameConfig.SCREEN_HEIGHT;
    public MainMenuScreen(final Boot game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();


        game.font.draw(game.batch, "Welcome to Arika-Schiff!!! ", screenWidth / 2 -200, screenHeight / 2 + 25);
        game.font.draw(game.batch, "Hit the spacebar to begin!", screenWidth / 2 - 200, screenHeight / 2 - 50);
        // Set the font size
        game.font.getData().setScale(2f); // Adjust 2f to your desired scale
        // Set the font color (optional)
        game.font.setColor(Color.WHITE); // Adjust Color.WHITE to your desired color
        game.batch.end();

      /*  if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }*/

        // initiates game with spacebar
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}