package net.tjalp.kelp.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.tjalp.kelp.gamerule.KelpGameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketItem.class)
abstract class FireworkRocketItemMixin {

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isFallFlying()Z"))
    private boolean isFallFlying(PlayerEntity instance) {
        return instance.world.getGameRules().getBoolean(KelpGameRules.INSTANCE.getALLOW_FIREWORK_ROCKET_BOOSTING())
                && instance.isFallFlying();
    }
}
