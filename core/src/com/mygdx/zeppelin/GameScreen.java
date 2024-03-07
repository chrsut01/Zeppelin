package com.mygdx.zeppelin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;


import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.random;

public class GameScreen implements Screen {
    final Zepp game;

    Texture planeImage;
    Texture zeppelinImage;

    Texture scrollSkyImage1;
    Texture scrollSkyImage2;

    Sound planeFlyingSound;
    Sound planeCrashSound;
    Music zepEngineSound;
    OrthographicCamera camera;
    Rectangle zeppelin;
    Array<Plane> planes;
    long lastPlaneTime;
    int planesHit;
    final float backgroundVelocity = 1;
    private float backgroundX = 0;
    private float backgroundY = 0;

    int screenWidth = GameConfig.SCREEN_WIDTH;
    int screenHeight = GameConfig.SCREEN_HEIGHT;

    public GameScreen(final Zepp game) {
        this.game = game;

        // load the images for the plane and the zeppelin
        planeImage = new Texture(Gdx.files.internal("sopwith_camel_90x50_crop.png"));
        zeppelinImage = new Texture(Gdx.files.internal("zeppelin_image1.png"));


        scrollSkyImage1 = new Texture(Gdx.files.internal("scroll-Sky1.jpg"));
        scrollSkyImage2 = new Texture(Gdx.files.internal("scroll-Sky1.jpg"));

        planeFlyingSound = Gdx.audio.newSound(Gdx.files.internal("plane_flying.mp3"));
        planeCrashSound = Gdx.audio.newSound(Gdx.files.internal("plane_crash.mp3"));

        // load and loop zeppelin engine sound effect
        zepEngineSound = Gdx.audio.newMusic(Gdx.files.internal("ZeppelinEngine.mp3"));
        zepEngineSound.setLooping(true);


        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        // create a Rectangle to logically represent the zeppelin (original image: 965x195 pixels)
        zeppelin = new Rectangle();
        zeppelin.width = 723;
        zeppelin.height = 145;
        zeppelin.x = screenWidth / 2 - zeppelin.width / 2; // center the zeppelin horizontally
        zeppelin.y = screenHeight / 2 - zeppelin.height / 2;; // bottom left corner of the zeppelin is 20 pixels above
        // the bottom screen edge


        // create the planes array and spawn the first plane
        planes = new Array<>();
        spawnPlane();

    }

    private void spawnPlane() {
        float x = screenWidth;
        float y = random((screenHeight/4), screenHeight - (screenHeight/4)); // ensures planes enter screen at least 1/4 screen-height from top and bottom
        int yAngle = random.nextInt(120) - 60;

        Plane plane = new Plane(x, y, yAngle);
        planeFlyingSound.play();
        planes.add(plane);
        lastPlaneTime = TimeUtils.millis() + 4000; // sets the delay time to 4 seconds
    }

    @Override
    public void render(float delta) {
        this.update();
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch...
        game.batch.begin();

        // draw the background
        game.batch.draw(scrollSkyImage1,  backgroundX, backgroundY, 16384, 1856);
        game.batch.draw(scrollSkyImage2,  backgroundX + 16384, backgroundY, 16384, 1856);


        // draw the Drops Collected score
        game.font.draw(game.batch, "Planes hit: " + planesHit, 5, 475);
        // shows backgroundX
        game.font.draw(game.batch, "backgroundX: " + backgroundX, 5, 450);
        // draw the zeppelin and all drops
        game.batch.draw(zeppelinImage, zeppelin.x, zeppelin.y, zeppelin.width, zeppelin.height);
        for (Rectangle plane : planes) {
            game.batch.draw(planeImage, plane.x, plane.y);
        }

        game.batch.end();

        backgroundX -= backgroundVelocity;
        if (backgroundX <= -32768.0 + screenWidth){ //this number comes from 2 X 16384(image width) - screenWidth (screen width)
            backgroundX = 0;
        }



        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            zeppelin.x = touchPos.x - zeppelin.width / 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            zeppelin.x -= 30 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            zeppelin.x += 30 * Gdx.graphics.getDeltaTime();

        // make sure the zeppelin stays within the screen bounds right/left
        if (zeppelin.x < 0)
            zeppelin.x = 0;
        if (zeppelin.x > screenWidth - zeppelin.width)
            zeppelin.x = screenWidth - zeppelin.width;

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            zeppelin.y += 30 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            zeppelin.y -= 30 * Gdx.graphics.getDeltaTime();

        // make sure the zeppelin stays within the screen bounds up/down
        if (zeppelin.y < 0)
            zeppelin.y = 0;
        if (zeppelin.y > screenHeight - zeppelin.height)
            zeppelin.y = screenHeight - zeppelin.height;

        // check if we need to create a new plane
        if (TimeUtils.timeSinceMillis(lastPlaneTime) > 4000)
            spawnPlane();

        // move the planes, remove any that are beneath the bottom edge of
        // the screen or that hit the zeppelin. In the later case we increase the
        // value our drops counter and add a sound effect.
        Iterator<Plane> iter = planes.iterator();
        while (iter.hasNext()) {
            Plane plane = iter.next();
            plane.y -= plane.getyAngle() * Gdx.graphics.getDeltaTime();
            plane.x -= 150 * Gdx.graphics.getDeltaTime();
           /* if (plane.y + 50 < 0)
                iter.remove();*/
            if (plane.overlaps(zeppelin)) {
                planesHit++;
                planeCrashSound.play();
                // Delay the stopping of planeFlyingSound by 1 second
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        planeFlyingSound.stop();
                    }
                }, 0.25F); // 1 second delay
                iter.remove();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Adjust the viewport to the new screen size
        camera.setToOrtho(false, screenWidth, screenHeight);
    }

    @Override
    public void show() {
        // start the playback of the zeppelin engine sound effect when the screen is shown
        zepEngineSound.play();
        lastPlaneTime = TimeUtils.millis() + 5000; // 5-second delay before planes start coming

    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    public void update(){
    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        Gdx.app.exit();

    }

    @Override
    public void dispose() {
        planeImage.dispose();
        zeppelinImage.dispose();
        planeFlyingSound.dispose();
        planeCrashSound.dispose();
        zepEngineSound.dispose();
        scrollSkyImage1.dispose();
        scrollSkyImage2.dispose();
    }

}