package tech.sethi.pebbles.flexiblecommands.util

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.minecraft.server.network.ServerPlayerEntity
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object CooldownManager {
    private val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    private val cooldownFile = File("config/pebbles-flexible-commands/cooldowns.json")
    private val cooldowns = ConcurrentHashMap<String, Long>() // Maps "playerId:commandAlias" to Unix timestamp of expiration
    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    init {
        // Load cooldowns from file
        loadCooldowns()
        
        // Schedule periodic saving (every 5 minutes)
        scheduler.scheduleAtFixedRate({ saveCooldowns() }, 5, 5, TimeUnit.MINUTES)
    }

    private fun loadCooldowns() {
        if (!cooldownFile.exists()) return
        
        try {
            val type = object : TypeToken<Map<String, Long>>() {}.type
            val loadedCooldowns: Map<String, Long> = gson.fromJson(cooldownFile.readText(), type)
            
            // Only load cooldowns that haven't expired yet
            val currentTime = System.currentTimeMillis()
            loadedCooldowns.forEach { (key, expiration) ->
                if (expiration > currentTime) {
                    cooldowns[key] = expiration
                }
            }
            
            println("Loaded ${cooldowns.size} active cooldowns")
        } catch (e: Exception) {
            println("Failed to load cooldowns: ${e.message}")
        }
    }

    private fun saveCooldowns() {
        try {
            // Create directories if they don't exist
            cooldownFile.parentFile.mkdirs()
            
            // Clean expired cooldowns before saving
            cleanupExpiredCooldowns()
            
            // Save to file
            cooldownFile.writeText(gson.toJson(cooldowns))
            println("Saved ${cooldowns.size} active cooldowns")
        } catch (e: Exception) {
            println("Failed to save cooldowns: ${e.message}")
        }
    }

    private fun cleanupExpiredCooldowns() {
        val currentTime = System.currentTimeMillis()
        val expiredKeys = cooldowns.entries
            .filter { it.value <= currentTime }
            .map { it.key }
        
        expiredKeys.forEach { cooldowns.remove(it) }
    }

    fun setCooldown(player: ServerPlayerEntity, commandAlias: String, durationSeconds: Int) {
        if (durationSeconds <= 0) return
        
        val key = "${player.uuidAsString}:$commandAlias"
        val expirationTime = System.currentTimeMillis() + (durationSeconds * 1000L)
        cooldowns[key] = expirationTime
    }

    fun isOnCooldown(player: ServerPlayerEntity, commandAlias: String): Boolean {
        val key = "${player.uuidAsString}:$commandAlias"
        val expiration = cooldowns[key] ?: return false
        return System.currentTimeMillis() < expiration
    }

    fun getRemainingCooldown(player: ServerPlayerEntity, commandAlias: String): Int {
        val key = "${player.uuidAsString}:$commandAlias"
        val expiration = cooldowns[key] ?: return 0
        val remaining = expiration - System.currentTimeMillis()
        return if (remaining > 0) (remaining / 1000).toInt() else 0
    }

    fun shutdown() {
        saveCooldowns()
        scheduler.shutdown()
    }
} 