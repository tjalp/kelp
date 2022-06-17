package net.tjalp.kelp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.tjalp.kelp.command.KelpCommand
import net.tjalp.kelp.config.KelpConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Path

@Suppress("UNUSED")
object Kelp : ModInitializer {
    const val MOD_ID: String = "kelp"
    val GSON: Gson = GsonBuilder().create()
    val GSON_PRETTY: Gson = GsonBuilder().setPrettyPrinting().create()
    val logger: Logger = LogManager.getLogger();
    val modDirectory: Path = FabricLoaderImpl.INSTANCE.configDir.resolve(MOD_ID)
    val isDev: Boolean = FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment
    val configPath: Path = this.modDirectory.resolve("config.json")

    lateinit var config: KelpConfig; private set

    override fun onInitialize() {
        this.config = KelpConfig.read(this.modDirectory.resolve("config.json"))

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            KelpCommand.register(dispatcher)
        }
    }
}