package net.runelite.client.plugins.itemidcheck;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("itemidcheck")
public interface ItemIdCheckConfig extends Config
{
    @ConfigItem(
            keyName = "showInventoryIds",
            name = "Show Inventory IDs",
            description = "Configures whether inventory item IDs are displayed"
    )
    default boolean showInventoryIds()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showObjectIds",
            name = "Show Object IDs",
            description = "Configures whether game object IDs are displayed"
    )
    default boolean showObjectIds()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showCurrentPlaneTileLocations",
            name = "Show Current Plane Tile Locations",
            description = "Configures whether tile locations on the current plane are displayed"
    )
    default boolean showCurrentPlaneTileLocations()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showAllPlaneTileLocations",
            name = "Show All Plane Tile Locations",
            description = "Configures whether tile locations on all planes are displayed"
    )
    default boolean showAllPlaneTileLocations()
    {
        return false;
    }
}
