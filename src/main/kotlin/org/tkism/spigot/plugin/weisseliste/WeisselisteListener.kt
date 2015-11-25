package org.tkism.spigot.plugin.weisseliste

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import kotlin.sql.Database
import kotlin.sql.Table
import kotlin.sql.count
import kotlin.sql.select

/**
 * Created by Tki on 11/25/2015.
 */
class WeisselisteListener(instance: Weisseliste, dbInstance: Database) : Listener {
    init { instance.server.pluginManager.registerEvents(this, instance) }
    val plugin = instance
    val db = dbInstance
    val WhitelistedUsers = WhitelistedUsersTable(
            plugin.config.getString("db.tablename"),
            plugin.config.getString("db.uuidfield.name"),
            plugin.config.getInt("db.uuidfield.length")
    )

    @EventHandler(priority = EventPriority.LOW) fun onPlayerLogin(event: PlayerLoginEvent) {
        var whitelisted = false
        db.withSession {
            WhitelistedUsers.slice(WhitelistedUsers.uuid.count()).select {
                WhitelistedUsers.uuid.eq(event.player.uniqueId.toString())
            } forEach {
                if (it[WhitelistedUsers.uuid.count()] > 0) whitelisted = true
            }
        }
        if (!whitelisted) {
            if (plugin.config.getBoolean("log_on_kick"))
                plugin.log(plugin.config.getString("messages.kick_log").replace("\${USERNAME}", "${event.player.name}"))
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, plugin.config.getString("messages.kick_message"))
        }
    }
}