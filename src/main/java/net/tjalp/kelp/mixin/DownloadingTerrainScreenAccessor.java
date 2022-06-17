package net.tjalp.kelp.mixin;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DownloadingTerrainScreen.class)
public interface DownloadingTerrainScreenAccessor {

    @Accessor
    long getLoadStartTime();
}
