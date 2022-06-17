package net.tjalp.kelp.config

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.option.GameOptionsScreen
import net.minecraft.client.gui.widget.ButtonListWidget
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.tjalp.kelp.Kelp

class KelpConfigScreen(parent: Screen?) : GameOptionsScreen(
    parent,
    MinecraftClient.getInstance().options,
    Text.translatable("title.kelp.config"),
) {

    private lateinit var list: ButtonListWidget

    override fun init() {
        this.list = ButtonListWidget(client, width, height, 32, height - 32, 25)
        this.list.addAll(Kelp.config.asOptions())
        addSelectableChild(this.list)
        addDrawableChild(ButtonWidget(width / 2 - 100, height - 27, 200, 20, ScreenTexts.DONE) {
            KelpConfig.save(Kelp.configPath, Kelp.config)
            client?.setScreen(this.parent)
        })
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(matrices)
        this.list.render(matrices, mouseX, mouseY, delta)
        drawCenteredText(matrices, textRenderer, title, width / 2, 15, 16777215)
        super.render(matrices, mouseX, mouseY, delta)
    }

    override fun removed() {
        KelpConfig.save(Kelp.configPath, Kelp.config)
    }
}