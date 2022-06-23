package net.tjalp.kelp.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.LongArgumentType.getLong
import com.mojang.brigadier.arguments.LongArgumentType.longArg
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.minecraft.command.EntitySelector
import net.minecraft.command.argument.EntityArgumentType.getPlayers
import net.minecraft.command.argument.EntityArgumentType.players
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.tjalp.kelp.Kelp
import net.tjalp.kelp.gamerule.KelpGameRules
import net.tjalp.kelp.util.formatFull
import java.time.Duration

object KelpCommand {

    private val combat = Kelp.combat

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val node = literal<ServerCommandSource>("kelp")
            .then(literal<ServerCommandSource>("combat")
                .then(literal<ServerCommandSource>("set")
                    .then(argument<ServerCommandSource, EntitySelector>("targets", players())
                        .executes { executeCombatSet(it.source, getPlayers(it, "targets")) }
                        .then(argument<ServerCommandSource, Long>("seconds", longArg())
                            .executes { executeCombatSet(it.source, getPlayers(it, "targets"), getLong(it, "seconds")) })))
                .then(literal<ServerCommandSource>("remove")
                    .then(argument<ServerCommandSource, EntitySelector>("target", players())
                        .executes { executeCombatRemove(it.source, getPlayers(it, "target")) })))

        dispatcher.register(node)
    }

    private fun executeCombatSet(source: ServerCommandSource, targets: Collection<ServerPlayerEntity>, seconds: Long = source.world.gameRules.getInt(KelpGameRules.COMBAT_TAG_TIME).toLong()): Int {
        val duration = Duration.ofSeconds(seconds)
        val displayDuration = duration.formatFull()

        for (target in targets) this.combat.setInCombat(target, duration, true)

        if (targets.size == 1) {
            source.sendFeedback(
                Text.literal("Set ")
                    .append(targets.iterator().next().displayName)
                    .append(" into combat for $displayDuration "),
                true
            )
        } else {
            source.sendFeedback(
                Text.literal("Set ${ targets.size } players into combat for $displayDuration"),
                true
            )
        }

        return Command.SINGLE_SUCCESS
    }

    private fun executeCombatRemove(source: ServerCommandSource, targets: Collection<ServerPlayerEntity>): Int {
        for (target in targets) this.combat.setInCombat(target, false)

        if (targets.size == 1) {
            source.sendFeedback(
                Text.literal("Removed combat timer of ")
                    .append(targets.iterator().next().displayName),
                false
            )
        } else {
            source.sendFeedback(
                Text.literal("Removed combat timer of ${ targets.size } players"),
                true
            )
        }

        return Command.SINGLE_SUCCESS
    }
}