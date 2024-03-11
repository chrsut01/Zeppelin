package com.mygdx.zeppelin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;


import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.random;

public class GameScreen implements Screen {
    final Boot game;
    private TileMapHelper tileMapHelper;
    Texture planeImage;
    //Texture zeppelinImage;

    //Texture scrollSkyImage1;
    //Texture scrollSkyImage2;

    Sound planeFlyingSound;
    Sound planeCrashSound;
    //Music zepEngineSound;
    OrthographicCamera camera;
    SpriteBatch batch;
    //Rectangle zeppelin;
    Array<Plane> planes;

    Zeppelin zeppelin;
    long lastPlaneTime;
    int planesHit;
    final float backgroundVelocity = 1;
    private float backgroundX = 0;
    private float backgroundY = 0;

    int screenWidth = GameConfig.SCREEN_WIDTH;
    int screenHeight = GameConfig.SCREEN_HEIGHT;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;  // e2

    public GameScreen(final Boot game) {
        this.game = game;
        this.batch = new SpriteBatch();

        this.tileMapHelper = new TileMapHelper(this);  // e2, e3: parameter
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap(); // e2

        // load the images for the plane
        planeImage = new Texture(Gdx.files.internal("sopwith_camel_small.png"));
        //zeppelinImage = new Texture(Gdx.files.internal("zeppelin_image1.png"));


        //scrollSkyImage1 = new Texture(Gdx.files.internal("scroll-Sky1.jpg"));
        //scrollSkyImage2 = new Texture(Gdx.files.internal("scroll-Sky1.jpg"));

        planeFlyingSound = Gdx.audio.newSound(Gdx.files.internal("plane_flying.mp3"));
        planeCrashSound = Gdx.audio.newSound(Gdx.files.internal("plane_crash.mp3"));

        // load and loop zeppelin engine sound effect
       // zepEngineSound = Gdx.audio.newMusic(Gdx.files.internal("ZeppelinEngine.mp3"));
       // zepEngineSound.setLooping(true);


        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        //zeppelin = new Zeppelin(screenWidth / 2 - 723 / 2, screenHeight / 2 - 145 / 2);
        zeppelin = new Zeppelin();
       // zeppelin.setScreenDimensions(screenWidth, screenHeight);
        //zeppelin.playEngineSound();

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
        //ScreenUtils.clear(0, 0, 0.2f, 1);  // makes screen very dark blue, almost black
        ScreenUtils.clear(0.5f, 0.7f, 0.9f, 0.5f); // makes screen light blue

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();
        // begin a new batch...
        game.batch.begin();

        // draw the background
        //game.batch.draw(scrollSkyImage1,  backgroundX, backgroundY, 16384, 1856);
        //game.batch.draw(scrollSkyImage2,  backgroundX + 16384, backgroundY, 16384, 1856);


        // draw the Drops Collected score
        game.font.draw(game.batch, "Planes hit: " + planesHit, 5, 475);
        // shows backgroundX
        game.font.draw(game.batch, "backgroundX: " + backgroundX, 5, 450);

        //zeppelin.render(batch);
        // draw the zeppelin and all drops
        game.batch.draw(zeppelin.getZeppelinImage(), zeppelin.x, zeppelin.y, zeppelin.getWidth(), zeppelin.getHeight());
        for (Rectangle plane : planes) {
            game.batch.draw(planeImage, plane.x, plane.y);
        }

        game.batch.end();

        backgroundX -= backgroundVelocity;
        if (backgroundX <= -32768.0 + screenWidth){ //this number comes from 2 X 16384(image width) - screenWidth (screen width)
            backgroundX = 0;
        }


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
        //zepEngineSound.play();
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
        // closes game if escape key is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();

        zeppelin.update();

    }

    @Override
    public void dispose() {
        planeImage.dispose();
        zeppelin.dispose();
        planeFlyingSound.dispose();
        planeCrashSound.dispose();
                                        // zepEngineSound.dispose();
        //scrollSkyImage1.dispose();
        //scrollSkyImage2.dispose();
        batch.dispose();

    }

}
