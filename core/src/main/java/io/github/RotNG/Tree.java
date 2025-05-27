package io.github.RotNG;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Tree {
    private Texture texture;
    private Rectangle bounds;
    private static final float SCALE = 0.2f;

    public Tree(float x, float y) {
        texture = new Texture("tree2.png");
        bounds = new Rectangle(x, y, texture.getWidth() - 50, texture.getHeight() - 50);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x - 50, bounds.y - 50);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
    }
}
