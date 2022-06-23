package net.tjalp.kelp.gamerule

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory.createBooleanRule
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory.createIntRule
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry.register
import net.minecraft.world.GameRules
import net.minecraft.world.GameRules.Category

object KelpGameRules {

    val ENABLE_END: GameRules.Key<GameRules.BooleanRule> = register("enableEnd", Category.MISC, createBooleanRule(true))
    val ENABLE_COMBAT_TAG: GameRules.Key<GameRules.BooleanRule> = register("enableCombatTag", Category.PLAYER, createBooleanRule(false))
    val COMBAT_TAG_TIME: GameRules.Key<GameRules.IntRule> = register("combatTagTime", Category.MISC, createIntRule(30))
}