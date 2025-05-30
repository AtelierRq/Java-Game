package io.github.RotNG;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.List;


public class Player {
    private static final float SPEED = 200;
    private static final float SCALE = 0.5f;

    private TextureRegion[][] animations;
    private TextureRegion currentFrame;
    private float animationTimer = 0f;
    private float frameDuration = 0.3f;
    private int currentFrameIndex = 0;

    private Rectangle bounds;

    private enum Direction { UP, DOWN, LEFT, RIGHT }
    private Direction currentDirection = Direction.DOWN;

    public Player() {

        animations = new TextureRegion[4][2]; // [kierunek][klatka]
        animations[0][0] = new TextureRegion(new Texture("wizard_up1.png"));
        animations[0][1] = new TextureRegion(new Texture("wizard_up2.png"));
        animations[1][0] = new TextureRegion(new Texture("wizard_down1.png"));
        animations[1][1] = new TextureRegion(new Texture("wizard_down2.png"));
        animations[2][0] = new TextureRegion(new Texture("wizard_left1.png"));
        animations[2][1] = new TextureRegion(new Texture("wizard_left2.png"));
        animations[3][0] = new TextureRegion(new Texture("wizard_right1.png"));
        animations[3][1] = new TextureRegion(new Texture("wizard_right2.png"));

        currentFrame = animations[1][0]; // domyślnie w dół

        float width = currentFrame.getRegionWidth() * SCALE;
        float height = currentFrame.getRegionHeight() * SCALE;
        bounds = new Rectangle(0, 0, width, height);
    }

    public void update(float deltaTime, List<Tree> obstacles) {
        float oldX = bounds.x;
        float oldY = bounds.y;

        boolean moved = false;


        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            bounds.y += SPEED * deltaTime;
            currentDirection = Direction.UP;
            moved = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            bounds.y -= SPEED * deltaTime;
            currentDirection = Direction.DOWN;
            moved = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            bounds.x -= SPEED * deltaTime;
            currentDirection = Direction.LEFT;
            moved = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            bounds.x += SPEED * deltaTime;
            currentDirection = Direction.RIGHT;
            moved = true;
        }

        // Sprawdzenie kolizji
        for (Tree tree : obstacles) {
            if (bounds.overlaps(tree.getBounds())) {
                bounds.x = oldX;
                bounds.y = oldY;
                break;
            }
        }

        // Animacja
        if (moved) {
            animationTimer += deltaTime;
            if (animationTimer >= frameDuration) {
                animationTimer = 0f;
                currentFrameIndex = (currentFrameIndex + 1) % 2;
            }
        } else {
            currentFrameIndex = 0; // jeśli stoi, to 1. klatka
        }

        int dirIndex;
        switch (currentDirection) {
            case UP:
                dirIndex = 0;
                break;
            case DOWN:
                dirIndex = 1;
                break;
            case LEFT:
                dirIndex = 2;
                break;
            case RIGHT:
                dirIndex = 3;
                break;
            default:
                dirIndex = 1; // fallback
                break;
        }

        currentFrame = animations[dirIndex][currentFrameIndex];
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width * SCALE, bounds.height * SCALE);
    }

    public float getX() { return bounds.x + bounds.width / 2; }
    public float getY() { return bounds.y + bounds.height / 2; }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        // Zakładamy, że każda klatka to osobna tekstura
        for (TextureRegion[] dir : animations)
            for (TextureRegion frame : dir)
                frame.getTexture().dispose();
    }
}
