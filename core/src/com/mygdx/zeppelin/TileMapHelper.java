package com.mygdx.zeppelin;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

public class TileMapHelper {

    private GameScreen gameScreen;
    private TiledMap tiledMap;

    public TileMapHelper(GameScreen gameScreen) {   // e3: parametre
        this.gameScreen = gameScreen;
    }


    public OrthogonalTiledMapRenderer setupMap() {
        tiledMap = new TmxMapLoader().load("maps/map0.tmx");
        parseMapObjects(tiledMap.getLayers().get("objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    private void parseMapObjects(MapObjects mapObjects) {
        for (MapObject mapObject : mapObjects) {
            // You can handle other map objects if needed
            if (mapObject instanceof PolygonMapObject) {
                // Process polygon map objects if necessary
            }

            if (mapObject instanceof RectangleMapObject) {
                RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
                Rectangle rectangle = rectangleMapObject.getRectangle();

                // Handle rectangle map objects (e.g., ground, mountains)
                // You may want to store these objects or perform specific actions
                // based on their properties.
            }
        }
    }
}
