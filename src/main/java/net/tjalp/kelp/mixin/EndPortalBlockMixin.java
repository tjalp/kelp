package net.tjalp.kelp.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tjalp.kelp.Kelp;
import net.tjalp.kelp.config.KelpConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {

    Kelp kelp = Kelp.INSTANCE;
    KelpConfig config = kelp.getConfig();

    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getServer()Lnet/minecraft/server/MinecraftServer;"), cancellable = true)
    private void disableEnd(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (config.getEndEnabled()) return;

        if (entity instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) entity).networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal("End Portals are currently disabled!").formatted(Formatting.RED)));
        }
        ci.cancel();
    }
}
