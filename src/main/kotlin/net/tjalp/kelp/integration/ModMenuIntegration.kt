package net.tjalp.kelp.integration

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.tjalp.kelp.config.KelpConfigScreen

class ModMenuIntegration : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            return@ConfigScreenFactory KelpConfigScreen(parent)
        }
    }
}