package net.tjalp.kelp

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.tjalp.kelp.command.KelpClientCommand

object KelpClient : ClientModInitializer {

    override fun onInitializeClient() {
        registerEvents()
    }

    private fun registerEvents() {

        // Register the client commands
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            KelpClientCommand.register(dispatcher)
        }
    }
}