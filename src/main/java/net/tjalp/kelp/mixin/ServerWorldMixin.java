package net.tjalp.kelp.mixin;

import net.minecraft.server.world.ServerWorld;
import net.tjalp.kelp.gamerule.KelpGameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
abstract class ServerWorldMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;resetWeather()V"))
    private void resetWeather(ServerWorld world) {
        if (!world.getGameRules().getBoolean(KelpGameRules.INSTANCE.getEND_RAIN_ON_SLEEP())) return;

        ((ServerWorldAccessor) world).invokeResetWeather();
    }
}
