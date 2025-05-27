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
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Texture mapTexture;
    private Player player;

    private List<Tree> trees;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);

        mapTexture = new Texture("grass2.png");
        player = new Player();

        // Tworzenie kilku drzew
        trees = new ArrayList<>();
        trees.add(new Tree(400, 400));
        trees.add(new Tree(600, 300));
        trees.add(new Tree(800, 600));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        player.update(Gdx.graphics.getDeltaTime(), trees);

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        drawTiledMap(); // powtarzana trawa
        for (Tree tree : trees) tree.render(batch);
        player.render(batch, camera);
        batch.end();
    }

    private void drawTiledMap() {
        int tileSize = mapTexture.getWidth();
        int tilesX = 20;
        int tilesY = 20;
        for (int y = -tilesY / 2; y < tilesY / 2; y++) {
            for (int x = -tilesX / 2; x < tilesX / 2; x++) {
                batch.draw(mapTexture, x * tileSize, y * tileSize);
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapTexture.dispose();
        player.dispose();
        for (Tree tree : trees) tree.dispose();
    }
}
