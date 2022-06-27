package net.tjalp.kelp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.minecraft.util.ActionResult
import net.tjalp.kelp.combat.CombatManager
import net.tjalp.kelp.command.KelpCommand
import net.tjalp.kelp.config.KelpConfig
import net.tjalp.kelp.gamerule.KelpGameRules
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

    val config: KelpConfig = KelpConfig.read(this.modDirectory.resolve("config.json"))

    lateinit var combat: CombatManager; private set

    override fun onInitialize() {
        this.combat = CombatManager()

        // Initialize gamerules by loading the class
        KelpGameRules.toString()

        registerEvents()
    }

    private fun registerEvents() {

        // Register the commands
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            KelpCommand.register(dispatcher)
        }

        EntitySleepEvents.ALLOW_SLEEP_TIME.register { entity, _, _ ->
            val world = entity.world
            if (!world.gameRules.getBoolean(KelpGameRules.ALLOW_SLEEPING_WHILE_THUNDERING)
                && entity.world.isThundering
                && world.isDay) return@register ActionResult.FAIL
            ActionResult.PASS
        }

        EntityElytraEvents.ALLOW.register {
            it.world.gameRules.getBoolean(KelpGameRules.ALLOW_ELYTRAS)
        }
    }
}