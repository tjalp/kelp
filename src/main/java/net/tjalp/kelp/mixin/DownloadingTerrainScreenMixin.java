package net.tjalp.kelp.mixin;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenMixin {

    @Redirect(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen;loadStartTime:J",
                    opcode = Opcodes.GETFIELD
            )
    )
    private long redirectLoadStartTime(DownloadingTerrainScreen instance) {
        return 0;
    }
}
