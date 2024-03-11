package com.mygdx.zeppelin;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;

public class TileMapHelper {

    private GameScreen gameScreen;
    private TiledMap tiledMap;

    public TileMapHelper(GameScreen gameScreen) {   // e3: parametre
        this.gameScreen = gameScreen;
    }


    public OrthogonalTiledMapRenderer setupMap() {
        //tiledMap = new TmxMapLoader().load("maps/map0.tmx");
        tiledMap = new TmxMapLoader().load("maps/ZepMap1.tmx");

        parseMapObjects(tiledMap.getLayers().get("objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    private void parseMapObjects(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {
            // You can handle other map objects if needed
            if (mapObject instanceof PolygonMapObject) {
                // Process polygon map objects if necessary
             //   createStaticBody((PolygonMapObject) mapObject);

            }

            if (mapObject instanceof RectangleMapObject) {
                RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
               // Rectangle rectangle = rectangleMapObject.getRectangle();
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();  // version from testPlatformGame2
                String rectangleName = mapObject.getName(); // from testPlatformGame2

                // Handle rectangle map objects (e.g., ground, mountains)
                // You may want to store these objects or perform specific actions
                // based on their properties.
            }
        /*    private void createStaticBody(PolygonMapObject polygonMapObject) {
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                Body body = gameScreen.getWorld().createBody(bodyDef);
                Shape shape = createPolygonShape(polygonMapObject);
                body.createFixture(shape, 1000);
                shape.dispose();
            }*/
        }
    }
}
