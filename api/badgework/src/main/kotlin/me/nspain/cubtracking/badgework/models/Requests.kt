package me.nspain.cubtracking.badgework.models

data class CubRequest(
        val name: String,
        val achievements: List<Long>? = null
)