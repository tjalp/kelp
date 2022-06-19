package net.tjalp.kelp.config

import net.minecraft.client.option.SimpleOption
import net.tjalp.kelp.Kelp
import net.tjalp.kelp.config.details.CombatDetails
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.writeText

/**
 * The main config
 */
class KelpConfig {

    /**
     * Whether to enable the fast loading screen or not
     * This option removes the built-in 2 seconds delay
     * before closing the 'Loading Terrain...' screen
     */
    var fastLoadingScreen: Boolean = true

    /**
     * Whether the end is enabled or not
     */
    var endEnabled: Boolean = true

    /**
     * All the combat settings
     */
    var combat: CombatDetails = CombatDetails()

    /**
     * Get all available options
     */
    fun asOptions(): Array<SimpleOption<*>?> {
        val options = ArrayList<SimpleOption<*>>()
//            for (field in KelpConfig::class.java.declaredFields) {
//                // TODO: Add options
//            }
        options.add(SimpleOption.ofBoolean("option.kelp.fast_loading_screen", this.fastLoadingScreen) { this.fastLoadingScreen = it })
        return options.toTypedArray()
    }

    companion object {

        /**
         * Read the config from the config file
         */
        fun read(path: Path): KelpConfig {
            if (!path.exists()) return KelpConfig()
            val json = path.inputStream().bufferedReader().use { it.readText() }
            return Kelp.GSON.fromJson(json, KelpConfig::class.java)
        }

        /**
         * Save the current config to the config file
         */
        fun save(path: Path, config: KelpConfig) {
            val json = Kelp.GSON_PRETTY.toJson(config)
            path.parent.createDirectories()
            path.writeText(json)
        }
    }
}