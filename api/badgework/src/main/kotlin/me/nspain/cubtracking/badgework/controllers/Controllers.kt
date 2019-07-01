package me.nspain.cubtracking.badgework.controllers

import me.nspain.cubtracking.badgework.models.Cub
import me.nspain.cubtracking.badgework.repositories.AchievementRepository
import me.nspain.cubtracking.badgework.repositories.CubRepository
import me.nspain.cubtracking.badgework.repositories.UserRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cubs")
class CubController(private val repository: CubRepository) {
    @GetMapping
    fun getCubs(@RequestParam name: String?): List<Cub> {
        return listOf(Cub(1, "cub1"))
    }

    @GetMapping("/{id}")
    fun getCub(@PathVariable id: Long): Cub {
        return Cub(id, "cub$id")
    }
}

@RestController
@RequestMapping("/badges")
class BadgeController(private val repository: AchievementRepository) {

}

@RestController
@RequestMapping("/boomerang")
class BoomerangController(private val repository: AchievementRepository) {}

@RestController
@RequestMapping("/greywolf")
class GreyWolfController(private val repository: AchievementRepository) {}

@RestController
@RequestMapping("/user")
class UserController(private val repository: UserRepository) {}