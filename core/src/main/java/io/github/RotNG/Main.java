package io.github.RotNG;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

    //sluzy do rysowania grafiki
    private SpriteBatch batch;

    //ustawianie widoku kameru
    private OrthographicCamera camera;

    //tekstura gracza i mapy
    private Texture mapTexture;
    private Player player;

    //lista drzew
    private List<Tree> trees;

    //pociski
    private List<Bullet> bullets;

    //zarzadzanie rozmiarem i skalowaniem widoku gry, przy zmianie rozdzielczosci okna
    private Viewport viewport;

    //lista przeciwników
    private List<Enemy> enemies;

    //krztałt (do healthbara)
    private ShapeRenderer shapeRenderer;

    //lista przeciwnikow do usuniecia po zabiciu ich
    List<Enemy> toRemove = new ArrayList<>();

    //lista pociskow do usunięcia
    List<Bullet> bulletsToRemove = new ArrayList<>();

    //muzyka
    private Music backgroundMusic;

    //tekstury i zmienne użyte do grafiki tytułowej
    private Texture titleTexture;
    private float titleAlpha = 1f;
    private float titleTimer = 0f;
    private boolean showTitle = true;

    //dźwięk po pokananiu wszystkich przeciwników
    private Sound victorySound;
    private boolean victorySoundPlayed = false;

    //metoda odpwoiedzialna za inicjalizowanie wszystkiego w create
    @Override
    public void create() {

        //tworzenie obiektu do rysowania grafiki
        batch = new SpriteBatch();

        //ustawienie kamery (orthographic bo 2D) i definicja położenia kamery
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false); //tak jest domyslnie
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        //zaladowanie tekstury
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

        //tworzenie tablicy pociskow
        bullets = new ArrayList<>();

        //tablica przeciwnikow
        enemies = new ArrayList<>();
        shapeRenderer = new ShapeRenderer();

        // dodawanie kilku przeciwnikow
        enemies.add(new Enemy(0, 200));
        enemies.add(new Enemy(-200, -200));
        enemies.add(new Enemy(300, 0));
        enemies.add(new Enemy(-600, -100));
        enemies.add(new Enemy(500, 400));
        enemies.add(new Enemy(-400, -100));
        enemies.add(new Enemy(-100, -500));
        enemies.add(new Enemy(0, 600));
        enemies.add(new Enemy(400, -400));

        //obsluga muzyki w grze
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        backgroundMusic.setLooping(true);     // zapętlenie
        backgroundMusic.setVolume(0.8f);      // głośność od 0.0 do 1.0
        backgroundMusic.play();               // start

        //tekstura tytulu gry pojawiającego się na poczatku
        titleTexture = new Texture("title2.png");

        //dzwiek po pokonaniu wszystkich przeciwnikow
        victorySound = Gdx.audio.newSound(Gdx.files.internal("victory.wav"));
    }

    //obsluga przeskalowywania okna
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render() {
        //ustawianie danego koloru na ekranie (czyszczenie)
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        //wywołanie metody aktualizacji gracza w każdej klatce gry i sprawdzanie kolizji z drzewami
        player.update(Gdx.graphics.getDeltaTime(), trees);

        //aktualizowanie kamery gracza
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        //obsluga strzelania pociskami w kierunku kliknięcia myszką
        if (Gdx.input.justTouched()) {
            Vector2 worldClick = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            Vector2 startPos = new Vector2(player.getX(), player.getY());
            bullets.add(new Bullet(startPos, worldClick));
        }

        //usuwanie i aktualizacja pociskow metodą update
        bullets.removeIf(bullet -> !bullet.update(Gdx.graphics.getDeltaTime()));

        //aktualizowanie wszystkich przeciwnikow w grze przechopdząc po każdym po koleii
        for (Enemy enemy : enemies) {enemy.update(Gdx.graphics.getDeltaTime());}

        //jeśli wszyscy przeciwnicy nie żyją to victory sound
        if(enemies.isEmpty()) {
            if (!victorySoundPlayed) {
                victorySound.play();
                victorySoundPlayed = true;
            }
        }

        //poczatek rysowania grafik
        batch.begin();

        //rysowanie i renderowanie drzew, pociskow i przeciwnikow oraz gracza
        drawTiledMap(); // powtarzana trawa
        for (Tree tree : trees) tree.render(batch);
        for (Bullet bullet : bullets) bullet.render(batch);
        for (Enemy enemy : enemies) enemy.render(batch);
        player.render(batch, camera);

        //rysowanie title screena
        if (showTitle) { //glowna flaga, czy tytul ma byc jeszcze pokazywany
            titleTimer += Gdx.graphics.getDeltaTime(); //delta time to czas w sekundach miedzy klatkami

            //napis jest w pełni widoczny przez 1 sekunde
            if (titleTimer > 1f && titleTimer <= 3f) {
                // Stopniowo zmniejsza alpha (od 1.0 do 0.0 przez 2 sekundy)
                titleAlpha = 1f - (titleTimer - 1f) / 2f;
            } else if (titleTimer > 3f) { // po trzech sekundach juz nie jest widoczy w cale
                showTitle = false;
                titleAlpha = 0f;
            }

            //ustawienie przezroczystosci
            batch.setColor(1f, 1f, 1f, titleAlpha);

            float x = (Gdx.graphics.getWidth() - titleTexture.getWidth()) / 2f;
            float y = (Gdx.graphics.getHeight() - titleTexture.getHeight()) / 2f;
            batch.draw(titleTexture, x - 300, y -220); //rysowanie grafiki

            batch.setColor(1f, 1f, 1f, 1f); // reset koloru na domyślny
        }

        batch.end();

        // wyrenderowanie healthbara poprzez rysowanie wypełnionych kształtów pod przeciwnikiem
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Enemy enemy : enemies) enemy.renderHealthBar(shapeRenderer);
        shapeRenderer.end();

        //tworzy nową listę przeciwników do usunięcia
        List<Enemy> toRemove = new ArrayList<>();

        //obsluga kolizji pocisków z przeciwnikami
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

        //usuwanie pociskow i przeciwnikow
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

    //zwolnienie zasobów
    @Override
    public void dispose() {
        batch.dispose();
        mapTexture.dispose();
        player.dispose();
        for (Tree tree : trees) tree.dispose();
        for (Bullet bullet : bullets) bullet.dispose();
        for (Enemy enemy : enemies) enemy.dispose();
        shapeRenderer.dispose();
        backgroundMusic.dispose();
        titleTexture.dispose();
        victorySound.dispose();
    }
}
