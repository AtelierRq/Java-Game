package io.github.RotNG;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;


public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Texture mapTexture;
    private Player player;

    private List<Tree> trees;

    private List<Bullet> bullets;

    private Viewport viewport;

    private List<Enemy> enemies;
    private ShapeRenderer shapeRenderer;

    List<Enemy> toRemove = new ArrayList<>();
    List<Bullet> bulletsToRemove = new ArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        mapTexture = new Texture("grass4.png");
        player = new Player();

        // Tworzenie kilku drzew
        trees = new ArrayList<>();
        trees.add(new Tree(-150, 80));
        trees.add(new Tree(180, 170));
        trees.add(new Tree(220, -140));
        trees.add(new Tree(-300, -250));
        trees.add(new Tree(-480, 250));
        trees.add(new Tree(-160, 450));
        trees.add(new Tree(-660, -100));
        trees.add(new Tree(0, -500));
        trees.add(new Tree(700, -330));
        trees.add(new Tree(560, 150));
        trees.add(new Tree(340, -540));
        trees.add(new Tree(-450, -650));
        trees.add(new Tree(300, 500));
        trees.add(new Tree(-780, -470));
        trees.add(new Tree(-570, 710));
        trees.add(new Tree(-910, 280));
        trees.add(new Tree(0, 800));
        trees.add(new Tree(760, 520));
        trees.add(new Tree(1050, 20));
        trees.add(new Tree(1100, 160));

        bullets = new ArrayList<>();

        enemies = new ArrayList<>();
        shapeRenderer = new ShapeRenderer();

        // dodawanie kilku przeciwnikow
        enemies.add(new Enemy(100, 100));
        enemies.add(new Enemy(-200, -200));
        enemies.add(new Enemy(300, 0));
        enemies.add(new Enemy(-600, -100));
        enemies.add(new Enemy(500, 400));
        enemies.add(new Enemy(-400, -100));
        enemies.add(new Enemy(-100, -500));
        enemies.add(new Enemy(0, 600));
        enemies.add(new Enemy(400, -400));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        player.update(Gdx.graphics.getDeltaTime(), trees);

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.justTouched()) {
            Vector2 worldClick = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            Vector2 startPos = new Vector2(player.getX(), player.getY());
            bullets.add(new Bullet(startPos, worldClick));
        }

        bullets.removeIf(bullet -> !bullet.update(Gdx.graphics.getDeltaTime()));

        for (Enemy enemy : enemies) {enemy.update(Gdx.graphics.getDeltaTime());}

        batch.begin();
        drawTiledMap(); // powtarzana trawa
        for (Tree tree : trees) tree.render(batch);
        for (Bullet bullet : bullets) bullet.render(batch);
        for (Enemy enemy : enemies) enemy.render(batch);
        player.render(batch, camera);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Enemy enemy : enemies) enemy.renderHealthBar(shapeRenderer);
        shapeRenderer.end();


        List<Enemy> toRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (enemy.overlaps(bullet.getBounds())) {
                    enemy.hit(); // odejmujemy 1 HP
                    bulletsToRemove.add(bullet);

                    if (enemy.isDead()) {
                        toRemove.add(enemy);
                    }
                    break;
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(toRemove);
    }

    //funkcja generująca trawe
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
        for (Bullet bullet : bullets) bullet.dispose();
        for (Enemy enemy : enemies) enemy.dispose();
        shapeRenderer.dispose();
    }
}
