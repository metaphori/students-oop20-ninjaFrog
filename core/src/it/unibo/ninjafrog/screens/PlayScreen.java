package it.unibo.ninjafrog.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

import it.unibo.ninjafrog.hud.Hud;
import it.unibo.ninjafrog.utilities.Pair;
/**
 * Definition of the PlayScreen interface.
 */
public interface PlayScreen {
    /**
     * Method to be called when a new Melon object has to be created.
     * @param position A pair containing X and Y values where the new object will be created.
     */
    void spawnMelon(Pair<Float, Float> position);
    /**
     * Method to be called when a new Orange object has to be created.
     * @param position A pair containing X and Y values where the new object will be created.
     */
    void spawnOrange(Pair<Float, Float> position);
    /**
     * Method to be called when a new Cherries object has to be created.
     * @param position A pair containing X and Y values where the new object will be created.
     */
    void spawnCherries(Pair<Float, Float> position);
    /**
     * Setter of the double jump ability.
     * @param b True if you want to enable it, false otherwise.
     */
    void setDoubleJumpAbility(boolean b);
    /**
     * Add a new life to the player.
     */
    void addLife();
    /**
     * Set the WinScreen.
     */
    void setWinScreen();
    /**
     * Set the GameOverScreen.
     */
    void setGameOverScreen();
    /**
     * Getter of the TiledMap.
     * @return The TiledMap used by the PlayScreen.
     */
    TiledMap getMap();
    /**
     * Getter of the World.
     * @return The World used by the PlayScreen.
     */
    World getWorld();
    /**
     * Getter of the TextureAtlas.
     * @return The TextureAtlas used by the PlayScreen.
     */
    TextureAtlas getAtlas();
    /**
     * Getter of the Hud.
     * @return The Hud used by the PlayScreen.
     */
    Hud getHud();
}