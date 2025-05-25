package io.github.RotNG;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Player {
    private Texture texture;
    private float x, y;

    private static final float SCALE = 0.2f; // pomniejszony

    public Player() {
        texture = new Texture("Wizard.png");
        x = 0;
        y = 0;
    }

    public void update(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) y += 200 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) y -= 200 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) x -= 200 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) x += 200 * deltaTime;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, texture.getWidth() * SCALE, texture.getHeight() * SCALE);
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void dispose() {
        texture.dispose();
    }
}
