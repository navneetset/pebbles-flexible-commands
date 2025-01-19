package tech.sethi.pebbles.flexiblecommands.util

import com.mojang.brigadier.ParseResults
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registries
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import tech.sethi.pebbles.flexiblecommands.FlexibleCommands

object PM {

    fun parseMessageWithStyles(text: String, placeholder: String, style: Boolean = true): Component {
        val mm = if (style) {
            MiniMessage.miniMessage()
        } else {
            MiniMessage.builder().tags(TagResolver.empty()).build()
        }

        return mm.deserialize(text.replace("{placeholder}", placeholder)).decoration(TextDecoration.ITALIC, false)
    }

    fun returnStyledText(text: String, style: Boolean = true): MutableText {
        val component = parseMessageWithStyles(text, "placeholder", style)
        val gson = GsonComponentSerializer.gson()
        val json = gson.serialize(component)
        return Text.Serialization.fromJson(json, DynamicRegistryManager.EMPTY) as MutableText
    }

    fun returnStyledJson(text: String): String {
        val component = parseMessageWithStyles(text, "placeholder")
        val gson = GsonComponentSerializer.gson()
        val json = gson.serialize(component)
        return json
    }

    fun sendText(player: PlayerEntity, text: String) {
        val component = returnStyledText(text)
        player.sendMessage(component, false)
    }


    fun runCommand(command: String) {
        try {
            val parseResults: ParseResults<ServerCommandSource> =
                FlexibleCommands.server!!.commandManager.dispatcher.parse(command, FlexibleCommands.server!!.commandSource)
            FlexibleCommands.server!!.commandManager.dispatcher.execute(parseResults)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun runCommandSilent(command: String) {
        try {
            val parseResults: ParseResults<ServerCommandSource> =
                FlexibleCommands.server!!.commandManager.dispatcher.parse(command, FlexibleCommands.server!!.commandSource.withSilent())
            FlexibleCommands.server!!.commandManager.dispatcher.execute(parseResults)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun runCommandSilentAt(command: String, x: Double, y: Double, z: Double, world: ServerWorld) {
        try {
            val parseResults: ParseResults<ServerCommandSource> = FlexibleCommands.server!!.commandManager.dispatcher.parse(
                command, FlexibleCommands.server!!.commandSource.withPosition(Vec3d(x, y, z)).withWorld(world).withSilent()
            )
            FlexibleCommands.server!!.commandManager.dispatcher.execute(parseResults)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun runCommandSilentAtEntity(command: String, x: Double, y: Double, z: Double, world: ServerWorld, player: PlayerEntity? = null) {
        try {
            val rotation = player?.rotationClient ?: Vec2f(0.0f, 0.0f)
            val parseResults: ParseResults<ServerCommandSource> = FlexibleCommands.server!!.commandManager.dispatcher.parse(
                command, FlexibleCommands.server!!.commandSource.withSilent().withEntity(player).withPosition(Vec3d(x, y, z)).withWorld(world).withRotation(rotation)
            )
            FlexibleCommands.server!!.commandManager.dispatcher.execute(parseResults)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun runCommandAsPlayer(player: PlayerEntity, command: String) {
        try {
            val parseResults: ParseResults<ServerCommandSource> =
                FlexibleCommands.server!!.commandManager.dispatcher.parse(command, player.commandSource)
            FlexibleCommands.server!!.commandManager.dispatcher.execute(parseResults)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLocaleText(key: String): String {
        return Text.translatable(key).string
    }

}