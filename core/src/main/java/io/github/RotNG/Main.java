package io.github.RotNG;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private SpriteBatch batch;

    //Player
    private Player player;

    //MapTexture
    private Texture mapTexture;

    //Camera
    private OrthographicCamera camera;

    @Override
    public void create() {

        //Player
        batch = new SpriteBatch();
        player = new Player();

        //Załadowanie terenu
        mapTexture = new Texture("grass2.png");

        // Inicjalizacja kamery
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);

        //Obsługa fullscreena
        //Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        //Gdx.graphics.setWindowedMode(displayMode.width - 920, displayMode.height - 140);  //Gdx.graphics.setWindowedMode(displayMode.width - 40, displayMode.height - 100);

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        //Player i Map
        player.update(Gdx.graphics.getDeltaTime());

        //Kamera
        // Ustaw pozycję kamery — gracz jest 1/3 szerokości ekranu od lewej
        float offsetX = Gdx.graphics.getWidth() / 10f;
        camera.position.set(player.getX(), player.getY(), 0);  //  player.getX() + offsetX
        camera.update();

        //macierz kamery
        batch.setProjectionMatrix(camera.combined);

        //Player i map
        batch.begin();

        //batch.draw(mapTexture, 0, 0);

        //tworzenie mapy nieskonczonej (terenu)
        int tileSize = mapTexture.getWidth(); // zakładamy kwadrat
        int drawRadius = 10; // ile kafelków w każdą stronę

        int playerTileX = (int)(player.getX() / tileSize);
        int playerTileY = (int)(player.getY() / tileSize);

        for (int y = -drawRadius; y <= drawRadius; y++) {
            for (int x = -drawRadius; x <= drawRadius; x++) {
                int drawX = (playerTileX + x) * tileSize;
                int drawY = (playerTileY + y) * tileSize;
                batch.draw(mapTexture, drawX, drawY);
            }
        }

        player.render(batch);
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        mapTexture.dispose();
        player.dispose();
    }
}
