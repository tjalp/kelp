package net.tjalp.kelp.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.tjalp.kelp.Kelp;
import net.tjalp.kelp.combat.CombatManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Duration;
import java.util.HashSet;
import java.util.UUID;

import static net.tjalp.kelp.util.DurationKt.formatFull;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin {

    private final Kelp kelp = Kelp.INSTANCE;
    private final CombatManager combat = kelp.getCombat();
    private final HashSet<UUID> wasInCombat = new HashSet<>();

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isScaledWithDifficulty()Z"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!kelp.getConfig().getCombat().getEnabled()) return;

        PlayerEntity thisPlayer = (PlayerEntity) (Object) this;

        if (!(thisPlayer instanceof ServerPlayerEntity)) return;

        if (source instanceof EntityDamageSource) {
            Entity attacker = source.getAttacker();

            if (attacker instanceof ServerPlayerEntity
                || (attacker instanceof ProjectileEntity && ((ProjectileEntity) attacker).getOwner() instanceof ServerPlayerEntity)) {
                this.combat.setInCombat((ServerPlayerEntity) thisPlayer, Duration.ofSeconds(kelp.getConfig().getCombat().getSeconds()), false);
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity thisPlayer = (PlayerEntity) (Object) this;

        if (!(thisPlayer instanceof ServerPlayerEntity thisServerPlayer)) return;

        if (!this.combat.isInCombat(thisServerPlayer) && wasInCombat.contains(thisServerPlayer.getUuid())) {
            wasInCombat.remove(thisServerPlayer.getUuid());

            OverlayMessageS2CPacket packet = new OverlayMessageS2CPacket(
                    Text.literal("You are no longer in combat!").setStyle(Style.EMPTY.withColor(TextColor.parse("#00FF00")))
            );
            thisServerPlayer.networkHandler.sendPacket(packet);
            return;
        }

        if (!this.combat.isInCombat(thisServerPlayer)) return;

        Duration duration = this.combat.getCombatTimeLeft(thisServerPlayer).plus(Duration.ofSeconds(1));

        OverlayMessageS2CPacket packet = new OverlayMessageS2CPacket(
                Text.literal(formatFull(duration)).setStyle(Style.EMPTY.withColor(TextColor.parse("#F8C8DC")))
        );
        thisServerPlayer.networkHandler.sendPacket(packet);

        wasInCombat.add(thisServerPlayer.getUuid());
    }
}
