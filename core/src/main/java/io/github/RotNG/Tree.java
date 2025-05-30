package io.github.RotNG;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Tree {
    //skladowe
    private Texture texture;
    private Rectangle bounds;
    private static final float SCALE = 0.2f;

    //konstruktor
    public Tree(float x, float y) {
        //tekstura i hitbox
        texture = new Texture("tree2.png");
        bounds = new Rectangle(x, y, texture.getWidth() - 50, texture.getHeight() - 50);
    }

    //wyrenderowanie
    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x - 50, bounds.y - 50);
    }

    //getter
    public Rectangle getBounds() {
        return bounds;
    }

    //usuwanie zasobów
    public void dispose() {
        texture.dispose();
    }
}
