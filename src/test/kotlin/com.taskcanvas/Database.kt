package com.taskcanvas

import java.sql.Connection
import java.sql.DriverManager

object Database {
    private val jdbcUrl: String = config.taskCanvas.db.jdbcUrl
    private val username: String = config.taskCanvas.db.username
    private val password: String = config.taskCanvas.db.password

    fun connection(): Connection = DriverManager.getConnection(jdbcUrl, username, password)

    fun truncateAll() {
        connection().use { conn ->
            conn.createStatement().use { stmt ->
                val tables = listOf(
                    "task_canvas.user",
                    "task_canvas.todo",
                    "task_canvas.user_todo",
                    "task_canvas.tag"
                )

                tables.joinToString(",").let { table ->
                    stmt.execute("TRUNCATE $table RESTART IDENTITY CASCADE;")
                }
            }
        }
    }
}