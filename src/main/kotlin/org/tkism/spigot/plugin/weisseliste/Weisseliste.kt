package org.tkism.spigot.plugin.weisseliste

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.logging.Logger
import kotlin.sql.Database

/**
 * Created by Tki on 11/25/2015.
 */
class Weisseliste : JavaPlugin() {
    val consoleLogger = Logger.getLogger("Minecraft")
    var listener: WeisselisteListener? = null
    var db: Database? = null

    override fun onEnable() {
        if (!(File(this.dataFolder, "config.yml")).exists()) this.saveDefaultConfig()
        db = Database.connect(
                this.config.getString("db.url"),
                driver = this.config.getString("db.driver"),
                user = this.config.getString("db.user"),
                password = this.config.getString("db.password")
        )
        listener = WeisselisteListener(this, db as Database)
    }

    override fun onDisable() {
        listener = null
        db = null
    }

    fun log (msg:String) = consoleLogger.info("[${description.name}] $msg")
}