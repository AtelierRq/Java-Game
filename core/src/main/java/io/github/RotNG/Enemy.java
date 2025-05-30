package io.github.RotNG;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {

    //składowe
    private static final float SPEED = 30f;
    private static final int MAX_HP = 5;

    private Texture texture;
    private Rectangle bounds;
    private int hp;

    private float moveTimer = 0f;
    private Vector2 direction;

    //tekstury przeciwników
    private static final String[] TEXTURE_PATHS = {
        "enemy4.png",
        "enemy5.png",
        "enemy6.png",
        "enemy7.png"
    };

    //konstruktor
    public Enemy(float x, float y) {

        //ładowanie losowej tekstury do przeciwnikow (jednej z 4)
        String path = TEXTURE_PATHS[MathUtils.random(TEXTURE_PATHS.length - 1)];
        texture = new Texture(path);

        //hitbox przeciwników
        bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());

        //losowe poruszanie się postaci
        hp = MAX_HP;
        direction = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f)).nor();
    }

    //odpowiada za losowy ruch przeciwnika co 0.5 sekundy
    public void update(float deltaTime) {
        moveTimer += deltaTime;

        //ustalenie kierunku losowo
        if (moveTimer > 0.5f) {
            direction.set(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f)).nor();
            moveTimer = 0f;
        }

        //przesunięcie w ustalonym kierunku z ustaloną prędkością
        bounds.x += direction.x * SPEED * deltaTime;
        bounds.y += direction.y * SPEED * deltaTime;
    }

    //wyrenderowanie przeciwnika
    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x - 55, bounds.y - 55);
    }

    //wyrenderowanie health bara
    public void renderHealthBar(ShapeRenderer shapeRenderer) {
        float barWidth = bounds.width;
        float barHeight = 5;
        float healthRatio = (float) hp / MAX_HP;

        shapeRenderer.rect(bounds.x - 50, bounds.y - 65, barWidth, barHeight); // tło (czarne)
        shapeRenderer.setColor(1, 0, 0, 1); // czerwony pasek
        shapeRenderer.rect(bounds.x - 50, bounds.y - 65, barWidth * healthRatio, barHeight);
        shapeRenderer.setColor(0, 0, 0, 1); // przywróć czarny

        //podgląd hitboxow
        //shapeRenderer.setColor(1, 0, 0, 1); // czerwony
        //shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    //getter
    public Rectangle getBounds() {
        return bounds;
    }

    //sprawdzanie czy hitboxy pocisków i przeciwników na siebie nachodzą
    public boolean overlaps(Rectangle bulletHitbox) {
        return bounds.overlaps(bulletHitbox);
    }

    //sprawdzanie czy przeciwnik otrzymal obrazenia
    public void hit() {
        hp--;
    }

    //sprawdzanie czy przeciwnik jest martwy
    public boolean isDead() {
        return hp <= 0;
    }

    //czyszczenie zasobów
    public void dispose() {
        texture.dispose();
    }
}
