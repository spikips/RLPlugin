package net.runelite.client.plugins.itemidcheck;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

public class ItemIdCheckOverlay extends Overlay
{
    private final Client client;
    private final ItemIdCheckConfig config;

    @Inject
    public ItemIdCheckOverlay(Client client, ItemIdCheckConfig config)
    {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (config.showInventoryIds())
        {
            Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY.getGroupId(), WidgetInfo.INVENTORY.getChildId());
            if (inventoryWidget != null)
            {
                List<WidgetItem> inventoryItems = inventoryWidget.getWidgetItems();
                for (WidgetItem item : inventoryItems)
                {
                    String text = String.valueOf(item.getId());
                    net.runelite.api.Point location = item.getCanvasLocation();
                    OverlayUtil.renderTextLocation(graphics, location, text, Color.WHITE);
                }
            }
        }

        if (config.showObjectIds())
        {
            Tile[][][] tiles = client.getScene().getTiles();
            int plane = client.getPlane();
            for (Tile[] tileArray : tiles[plane])
            {
                if (tileArray == null) continue;

                for (Tile tile : tileArray)
                {
                    if (tile == null) continue;

                    for (GameObject object : tile.getGameObjects())
                    {
                        if (object != null)
                        {
                            renderObjectId(graphics, object);
                        }
                    }

                    WallObject wallObject = tile.getWallObject();
                    if (wallObject != null)
                    {
                        renderObjectId(graphics, wallObject);
                    }

                    DecorativeObject decoObject = tile.getDecorativeObject();
                    if (decoObject != null)
                    {
                        renderObjectId(graphics, decoObject);
                    }

                    GroundObject groundObject = tile.getGroundObject();
                    if (groundObject != null)
                    {
                        renderObjectId(graphics, groundObject);
                    }
                }
            }
        }

        if (config.showCurrentPlaneTileLocations() || config.showAllPlaneTileLocations())
        {
            Tile[][][] tiles = client.getScene().getTiles();
            for (int p = 0; p < tiles.length; p++)
            {
                if (!config.showAllPlaneTileLocations() && p != client.getPlane())
                {
                    continue;
                }

                for (Tile[] tileArray : tiles[p])
                {
                    if (tileArray == null) continue;

                    for (Tile tile : tileArray)
                    {
                        if (tile != null)
                        {
                            renderTileLocation(graphics, tile);
                        }
                    }
                }
            }
        }

        return null;
    }

    private void renderObjectId(Graphics2D graphics, TileObject object)
    {
        net.runelite.api.Point textLocation = object.getCanvasLocation();
        if (textLocation != null)
        {
            String text = String.valueOf(object.getId());
            OverlayUtil.renderTextLocation(graphics, textLocation, text, Color.WHITE);
        }
    }

    private void renderTileLocation(Graphics2D graphics, Tile tile)
    {
        WorldPoint worldPoint = tile.getWorldLocation();
        LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);
        if (localPoint != null)
        {
            net.runelite.api.Point canvasPoint = Perspective.localToCanvas(client, localPoint, client.getPlane());
            if (canvasPoint != null)
            {
                String text = String.format("%d, %d, %d", worldPoint.getX(), worldPoint.getY(), worldPoint.getPlane());
                OverlayUtil.renderTextLocation(graphics, canvasPoint, text, Color.WHITE);
            }
        }
    }
}
