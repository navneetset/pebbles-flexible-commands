package tech.sethi.pebbles.flexiblecommands

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer
import org.slf4j.LoggerFactory
import tech.sethi.pebbles.flexiblecommands.command.CommandRegistry
import tech.sethi.pebbles.flexiblecommands.util.CooldownManager

object FlexibleCommands : ModInitializer {
    private val logger = LoggerFactory.getLogger("pebbles-flexible-commands")
	var server: MinecraftServer? = null

	override fun onInitialize() {
		logger.info("Initializing Pebbles Flexible Commands")

		CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
			CommandRegistry.registerAliases(dispatcher)
			CommandRegistry.registerInfoCommands(dispatcher)
		}

		ServerLifecycleEvents.SERVER_STARTED.register { server ->
			this.server = server

			val dispatcher = server.commandSource.dispatcher
			CommandRegistry.registerAliases(dispatcher)
			CommandRegistry.registerInfoCommands(dispatcher)
		}
		
		// Register server stopping event to save cooldowns
		ServerLifecycleEvents.SERVER_STOPPING.register { 
			logger.info("Shutting down cooldown manager...")
			CooldownManager.shutdown()
		}
	}
}