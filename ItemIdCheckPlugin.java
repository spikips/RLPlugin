package net.runelite.client.plugins.itemidcheck;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
		name = ".asd"
)
public class ItemIdCheckPlugin extends Plugin {

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ItemIdCheckOverlay overlay;

	@Inject
	private ItemIdCheckConfig config;

	@Provides
	ItemIdCheckConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ItemIdCheckConfig.class);
	}

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
	}
}
