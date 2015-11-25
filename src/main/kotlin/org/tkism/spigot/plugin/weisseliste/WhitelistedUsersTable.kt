package org.tkism.spigot.plugin.weisseliste

import kotlin.sql.Table

/**
 * Created by Tki on 11/25/2015.
 */
class WhitelistedUsersTable(
        tablename: String,
        uuidfieldname: String,
        uuidfieldlength: Int = 250,
        deniedfieldname: String) : Table(tablename) {
    val uuid = varchar(uuidfieldname, length = uuidfieldlength).nullable()
    val denied = varchar(deniedfieldname, length = 1).default("N")
}