package net.runelite.client.plugins.itemidcheck;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("itemidcheck")
public interface ItemIdCheckConfig extends Config {

    @ConfigItem(
            keyName = "fontSize",
            name = "Font Size",
            description = "Configures the font size of the text overlay"
    )
    default int fontSize() {
        return 12;
    }

    @ConfigItem(
            keyName = "fontStyle",
            name = "Font Style",
            description = "Configures the font style of the text overlay (PLAIN, BOLD, ITALIC)"
    )
    default String fontStyle() {
        return "PLAIN";
    }

    @ConfigItem(
            keyName = "fontType",
            name = "Font Type",
            description = "Configures the font type of the text overlay"
    )
    default String fontType() {
        return "Arial";
    }

    @ConfigItem(
            keyName = "enableGameObjectIds",
            name = "Enable Game Object IDs",
            description = "Toggle displaying IDs of game objects around the world"
    )
    default boolean enableGameObjectIds() {
        return false;
    }

    @ConfigItem(
            keyName = "showTileLocations",
            name = "Show Tile Locations",
            description = "Toggle displaying tile locations (NONE, CURRENT_PLANE, ALL_PLANES)"
    )
    default TileDisplayOption showTileLocations() {
        return TileDisplayOption.NONE;
    }
}
