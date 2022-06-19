package net.tjalp.kelp.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.LongArgumentType.getLong
import com.mojang.brigadier.arguments.LongArgumentType.longArg
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import net.minecraft.command.EntitySelector
import net.minecraft.command.argument.EntityArgumentType.getPlayer
import net.minecraft.command.argument.EntityArgumentType.player
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.tjalp.kelp.Kelp
import java.time.Duration

object KelpCommand {

    private val combat = Kelp.combat

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val node = literal<ServerCommandSource>("kelp")
            .then(literal<ServerCommandSource>("combat")
                .then(literal<ServerCommandSource?>("set")
                    .then(argument<ServerCommandSource, EntitySelector>("target", player())
                        .executes { executeCombatSet(it.source, getPlayer(it, "target")) }
                        .then(argument<ServerCommandSource, Long>("seconds", longArg())
                            .executes { executeCombatSet(it.source, getPlayer(it, "target"), getLong(it, "seconds")) })))
                .then(literal<ServerCommandSource?>("remove")
                    .then(argument<ServerCommandSource, EntitySelector>("target", player())
                        .executes { executeCombatRemove(it.source, getPlayer(it, "target")) })))

        dispatcher.register(node)
    }

    private fun executeCombatSet(source: ServerCommandSource, target: ServerPlayerEntity, duration: Long? = null): Int {
        if (duration == null) combat.setInCombat(player = target, override = true)
        else this.combat.setInCombat(target, Duration.ofSeconds(duration), true)

        source.sendFeedback(Text.literal("Player ${ target.playerListName } now has a combat timer"), true)

        return Command.SINGLE_SUCCESS
    }

    private fun executeCombatRemove(source: ServerCommandSource, target: ServerPlayerEntity): Int {
        this.combat.setInCombat(target, false)

        source.sendFeedback(Text.literal("Player ${ target.playerListName } is no longer in combat"), false)

        return Command.SINGLE_SUCCESS
    }
}