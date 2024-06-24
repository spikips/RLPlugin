package net.runelite.client.plugins.itemidcheck;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.api.Perspective;

import javax.inject.Inject;
import java.awt.*;

public class ItemIdCheckOverlay extends Overlay {

    private static final int INVENTORY_WIDGET_ID = WidgetInfo.INVENTORY.getId();

    private final Client client;
    private final ItemIdCheckConfig config;

    @Inject
    public ItemIdCheckOverlay(Client client, ItemIdCheckConfig config) {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        renderInventoryItems(graphics);
        if (config.enableGameObjectIds()) {
            renderGameObjects(graphics);
        }
        if (config.showTileLocations() != TileDisplayOption.NONE) {
            renderTileLocations(graphics);
        }
        return null;
    }

    private void renderInventoryItems(Graphics2D graphics) {
        Widget inventoryWidget = client.getWidget(INVENTORY_WIDGET_ID);

        if (inventoryWidget == null || inventoryWidget.isHidden()) {
            return;
        }

        // Retrieve font configuration
        int fontSize = config.fontSize();
        String fontStyleString = config.fontStyle();
        String fontType = config.fontType();

        int fontStyle;
        switch (fontStyleString.toUpperCase()) {
            case "BOLD":
                fontStyle = Font.BOLD;
                break;
            case "ITALIC":
                fontStyle = Font.ITALIC;
                break;
            case "PLAIN":
            default:
                fontStyle = Font.PLAIN;
                break;
        }

        Font font = new Font(fontType, fontStyle, fontSize);
        graphics.setFont(font);

        for (Widget child : inventoryWidget.getDynamicChildren()) {
            if (child.getItemId() > 0) {
                String idText = String.valueOf(child.getItemId());
                net.runelite.api.Point canvasLocation = child.getCanvasLocation();

                if (canvasLocation != null && canvasLocation.getX() != -1 && canvasLocation.getY() != -1) {
                    OverlayUtil.renderTextLocation(graphics, canvasLocation, idText, Color.WHITE);
                }
            }
        }
    }

    private void renderGameObjects(Graphics2D graphics) {
        int playerPlane = client.getPlane();
        int fontSize = config.fontSize();
        String fontStyleString = config.fontStyle();
        String fontType = config.fontType();

        int fontStyle;
        switch (fontStyleString.toUpperCase()) {
            case "BOLD":
                fontStyle = Font.BOLD;
                break;
            case "ITALIC":
                fontStyle = Font.ITALIC;
                break;
            case "PLAIN":
            default:
                fontStyle = Font.PLAIN;
                break;
        }

        Font font = new Font(fontType, fontStyle, fontSize);
        graphics.setFont(font);

        for (Tile[][] planeTiles : client.getScene().getTiles()) {
            if (planeTiles == null) continue;

            for (Tile[] rowTiles : planeTiles) {
                if (rowTiles == null) continue;

                for (Tile tile : rowTiles) {
                    if (tile == null) continue;

                    for (GameObject gameObject : tile.getGameObjects()) {
                        if (gameObject == null) continue;

                        if (gameObject.getWorldLocation().getPlane() != playerPlane) {
                            continue;
                        }

                        String idText = String.valueOf(gameObject.getId());
                        WorldPoint worldLocation = gameObject.getWorldLocation();
                        LocalPoint localPoint = LocalPoint.fromWorld(client, worldLocation);

                        if (localPoint != null) {
                            Point canvasLocation = Perspective.localToCanvas(client, localPoint, client.getPlane());
                            if (canvasLocation != null) {
                                OverlayUtil.renderTextLocation(graphics, new net.runelite.api.Point(canvasLocation.getX(), canvasLocation.getY()), idText, Color.WHITE);
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderTileLocations(Graphics2D graphics) {
        int playerPlane = client.getPlane();
        int fontSize = config.fontSize();
        String fontStyleString = config.fontStyle();
        String fontType = config.fontType();

        int fontStyle;
        switch (fontStyleString.toUpperCase()) {
            case "BOLD":
                fontStyle = Font.BOLD;
                break;
            case "ITALIC":
                fontStyle = Font.ITALIC;
                break;
            case "PLAIN":
            default:
                fontStyle = Font.PLAIN;
                break;
        }

        Font font = new Font(fontType, fontStyle, fontSize);
        graphics.setFont(font);

        for (Tile[][] planeTiles : client.getScene().getTiles()) {
            if (planeTiles == null) continue;

            for (Tile[] rowTiles : planeTiles) {
                if (rowTiles == null) continue;

                for (Tile tile : rowTiles) {
                    if (tile == null) continue;

                    if (config.showTileLocations() == TileDisplayOption.CURRENT_PLANE && tile.getPlane() != playerPlane) {
                        continue;
                    }

                    WorldPoint worldLocation = tile.getWorldLocation();
                    LocalPoint localPoint = LocalPoint.fromWorld(client, worldLocation);

                    if (localPoint != null) {
                        Point canvasLocation = Perspective.localToCanvas(client, localPoint, client.getPlane());
                        if (canvasLocation != null) {
                            String locationText = worldLocation.getX() + ", " + worldLocation.getY();
                            OverlayUtil.renderTextLocation(graphics, new net.runelite.api.Point(canvasLocation.getX(), canvasLocation.getY()), locationText, Color.YELLOW);
                        }
                    }
                }
            }
        }
    }
}
