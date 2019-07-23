package me.nspain.cubtracking.badgework.models

data class CubResponse(
        val id: Long,
        val name: String,
        val achievements: List<Long>
)