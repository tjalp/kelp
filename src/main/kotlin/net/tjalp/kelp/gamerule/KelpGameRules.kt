package net.tjalp.kelp.gamerule

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory.createBooleanRule
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory.createIntRule
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry.register
import net.minecraft.world.GameRules
import net.minecraft.world.GameRules.Category

object KelpGameRules {

    val ALLOW_SLEEPING_WHILE_THUNDERING: GameRules.Key<GameRules.BooleanRule> = register("allowSleepingWhileThundering", Category.UPDATES, createBooleanRule(true))
    val COMBAT_TAG_TIME: GameRules.Key<GameRules.IntRule> = register("combatTagTime", Category.MISC, createIntRule(30))
    val ENABLE_COMBAT_TAG: GameRules.Key<GameRules.BooleanRule> = register("enableCombatTag", Category.PLAYER, createBooleanRule(false))
    val ENABLE_END: GameRules.Key<GameRules.BooleanRule> = register("enableEnd", Category.MISC, createBooleanRule(true))
    val END_RAIN_ON_SLEEP: GameRules.Key<GameRules.BooleanRule> = register("endRainOnSleep", Category.UPDATES, createBooleanRule(true))
}