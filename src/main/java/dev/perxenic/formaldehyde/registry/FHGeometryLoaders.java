package dev.perxenic.formaldehyde.registry;

import dev.perxenic.formaldehyde.impl.LargeTextureGeometryLoader;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber
public class FHGeometryLoaders {
    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(LargeTextureGeometryLoader.ID, LargeTextureGeometryLoader.INSTANCE);
    }
}
