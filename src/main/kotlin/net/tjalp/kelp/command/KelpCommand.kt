package net.tjalp.kelp.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.tjalp.kelp.Kelp
import net.tjalp.kelp.config.KelpConfigScreen


object KelpCommand {

    private var openConfigScreen = false

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        val node = literal("kelp")
            .then(literal("config")
                .requires { Kelp.isDev }
                .executes { this.executeConfig(it.source) })

        dispatcher.register(node)

        ClientTickEvents.END_CLIENT_TICK.register {
            if (openConfigScreen) {
                this.openConfigScreen = false
                it.setScreen(KelpConfigScreen(null))
            }
        }
    }

    private fun executeConfig(source: FabricClientCommandSource): Int {
        this.openConfigScreen = true
        return Command.SINGLE_SUCCESS
    }
}