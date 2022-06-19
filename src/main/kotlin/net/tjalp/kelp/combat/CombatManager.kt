package net.tjalp.kelp.combat

import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.server.network.ServerPlayerEntity
import net.tjalp.kelp.Kelp
import net.tjalp.kelp.config.details.CombatDetails
import java.time.Duration

/**
 * The combat manager, it keeps track of which players are in combat
 */
class CombatManager {

    private val details: CombatDetails
        get() = Kelp.config.combat

    init {
        EntityElytraEvents.ALLOW.register {
            if (!it.world.isClient) it is ServerPlayerEntity && !isInCombat(it)
            true
        }
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register { _, entity, killed ->
            if (entity !is LivingEntity || killed !is ServerPlayerEntity) return@register

            entity.addStatusEffect(StatusEffectInstance(StatusEffects.MINING_FATIGUE, 600, 0))
            entity.addStatusEffect(StatusEffectInstance(StatusEffects.GLOWING, 2400, 0))
            setInCombat(killed, false)
        }
        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            val player = handler.player

            if (!isInCombat(player)) return@register

            handler.player.kill()
        }
    }

    /**
     * Every player's combat timer
     */
    private val combatTimer: MutableMap<ServerPlayerEntity, Long> = mutableMapOf()

    /**
     * Set a player in combat
     *
     * @param player The target player
     * @param duration The duration to set the player in combat for
     * @param override Whether to override the timer if it is longer than the specified duration
     */
    fun setInCombat(player: ServerPlayerEntity, duration: Duration = Duration.ofSeconds(details.seconds), override: Boolean = false) {
        val calculatedTime = System.currentTimeMillis() + duration.toMillis()

        // If the time is less than the current time, and it won't be overridden, don't continue
        if (this.combatTimer.contains(player) && calculatedTime < this.combatTimer[player]!! && !override) return

        this.combatTimer[player] = calculatedTime
    }

    /**
     * Set a player in combat
     */
    fun setInCombat(player: ServerPlayerEntity, combat: Boolean) {
        if (combat) setInCombat(player)
        else this.combatTimer.remove(player)
    }

    /**
     * Whether a player is in combat or not
     *
     * @param player The target player
     * @return true if in combat, false otherwise
     */
    fun isInCombat(player: ServerPlayerEntity): Boolean {
        val timer = this.combatTimer[player]

        if (timer == null || timer <= System.currentTimeMillis()) {
            setInCombat(player, false)
            return false
        }

        return true
    }

    /**
     * Get the time left of a player's combat timer
     *
     * @param player The target player
     * @return The time left in [Duration], or [Duration.ZERO] if non-existent
     */
    fun getCombatTimeLeft(player: ServerPlayerEntity): Duration {
        return if (isInCombat(player)) {
            Duration.ofMillis(this.combatTimer[player]!! - System.currentTimeMillis())
        } else Duration.ZERO
    }
}