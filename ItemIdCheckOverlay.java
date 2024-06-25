package net.runelite.client.plugins.itemidcheck;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
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
            Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
            if (inventoryWidget != null)
            {
                List<WidgetItem> inventoryItems = inventoryWidget.getWidgetItems();
                for (WidgetItem item : inventoryItems)
                {
                    String text = String.valueOf(item.getId());
                    Point location = item.getCanvasLocation();
                    if (location != null)
                    {
                        renderText(graphics, new Point(location.getX(), location.getY()), text);
                    }
                }
            }
        }

        if (config.showObjectIds())
        {
            Tile[][][] tiles = client.getScene().getTiles();
            int plane = client.getPlane();
            for (Tile[] tileRow : tiles[plane])
            {
                if (tileRow == null) continue;

                for (Tile tile : tileRow)
                {
                    if (tile == null) continue;

                    for (GameObject object : tile.getGameObjects())
                    {
                        if (object != null)
                        {
                            renderObjectId(graphics, object);
                        }
                    }

                    if (tile.getWallObject() != null)
                    {
                        renderObjectId(graphics, tile.getWallObject());
                    }

                    if (tile.getDecorativeObject() != null)
                    {
                        renderObjectId(graphics, tile.getDecorativeObject());
                    }

                    if (tile.getGroundObject() != null)
                    {
                        renderObjectId(graphics, tile.getGroundObject());
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

                for (Tile[] tileRow : tiles[p])
                {
                    if (tileRow == null) continue;

                    for (Tile tile : tileRow)
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
        Point textLocation = object.getCanvasLocation();
        if (textLocation != null)
        {
            String text = String.valueOf(object.getId());
            renderText(graphics, textLocation, text);
        }
    }

    private void renderTileLocation(Graphics2D graphics, Tile tile)
    {
        WorldPoint worldPoint = tile.getWorldLocation();
        LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);
        if (localPoint != null)
        {
            Point canvasPoint = client.getCanvasLocation(localPoint, client.getPlane());
            if (canvasPoint != null)
            {
                String text = String.format("%d, %d, %d", worldPoint.getX(), worldPoint.getY(), worldPoint.getPlane());
                renderText(graphics, canvasPoint, text);
            }
        }
    }

    private void renderText(Graphics2D graphics, Point point, String text)
    {
        int fontSize = config.textSize();
        String fontName = config.textFont();
        boolean isBold = config.textBold();
        Font font = new Font(fontName, isBold ? Font.BOLD : Font.PLAIN, fontSize);
        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
        graphics.drawString(text, point.getX(), point.getY());
    }
}
