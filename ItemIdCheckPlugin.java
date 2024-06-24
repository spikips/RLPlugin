package net.runelite.client.plugins.itemidcheck;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
		name = ".asd"
)
public class ItemIdCheckPlugin extends Plugin
{
	private static final Logger logger = LoggerFactory.getLogger(ItemIdCheckPlugin.class);

	@Inject
	private Client client;

	@Inject
	private ItemIdCheckConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ItemIdCheckOverlay overlay;

	@Provides
	ItemIdCheckConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemIdCheckConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}
}
