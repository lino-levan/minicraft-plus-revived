package minicraft.item;

import minicraft.core.Game;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.Color;
import minicraft.gfx.Sprite;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;

import java.util.ArrayList;
import java.util.Random;

public class FishingRodItem extends Item {

    protected static ArrayList<Item> getAllInstances() {
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            items.add(new FishingRodItem(i));
        }

        return items;
    }
    private int uses = 0; // the more uses, the higher the chance of breaking
    public int level; // the higher the level the higher the chance of breaking

    private Random random = new Random(System.nanoTime());

    public static final int[] COLORS = {
            Color.get(-1, 210, 321, 555),
            Color.get(-1, 333, 444, 555),
            Color.get(-1, 321, 440, 555),
            Color.get(-1, 321, 55, 555)
    };

    private static final int[][] LEVEL_CHANCES = {
            {39, 9, 4, 0},
            {49, 14, 4, 0},
            {59, 29, 9, 0},
            {79, 59, 39, 0}
    };

    private static final String[] LEVEL_NAMES = {
            "Wood",
            "Iron",
            "Gold",
            "Gem"
    };

    public FishingRodItem(int level) {
        super(LEVEL_NAMES[level] + " Fishing rod", new Sprite(6, 5, COLORS[level]));
        this.level = level;
    }

    public static int getChance(int idx, int level) {
        return LEVEL_CHANCES[level][idx];
    }

    @Override
    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, Direction attackDir) {
        if (tile.equals(Tiles.get("water")) && !player.isSwimming()) { // make sure not to use it if swimming
            uses++;
            player.isFishing = true;
            player.fishingLevel = this.level;
            return true;
        }

        return false;
    }

    @Override
    public boolean canAttack() { return false; }

    @Override
    public boolean isDepleted() {
        if (random.nextInt(100) > 120 - uses - level * 6) { // breaking is random
            Game.notifications.add("Your Fishing rod broke.");
            return true;
        }
        return false;
    }

    @Override
    public Item clone() {
        FishingRodItem item = new FishingRodItem(this.level);
        item.uses = this.uses;
        return item;
    }
}
