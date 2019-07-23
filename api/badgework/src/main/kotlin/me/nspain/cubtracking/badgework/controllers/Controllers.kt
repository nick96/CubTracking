package me.nspain.cubtracking.badgework.controllers

import me.nspain.cubtracking.badgework.models.Completion
import me.nspain.cubtracking.badgework.models.Cub
import me.nspain.cubtracking.badgework.models.CubRequest
import me.nspain.cubtracking.badgework.models.CubResponse
import me.nspain.cubtracking.badgework.repositories.AchievementRepository
import me.nspain.cubtracking.badgework.repositories.CompletionRepository
import me.nspain.cubtracking.badgework.repositories.CubRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/cubs", produces = [MediaType.APPLICATION_JSON_VALUE])
class CubController(
        @Autowired private val cubRepository: CubRepository,
        @Autowired private val completionRepository: CompletionRepository,
        @Autowired private val achievementRepository: AchievementRepository
) {
    @GetMapping
    fun getCubs(@RequestParam(required = false) name: String?): List<CubResponse> {
        val cubs = listOf(
                CubResponse(1, "cub1", listOf()),
                CubResponse(2, "cub2", listOf())
        )
        if (name == null) {
            return cubs
        }

        return cubs.filter { cub -> cub.name == name }
    }

    @GetMapping("/{id}")
    fun getCub(@PathVariable id: Long): ResponseEntity<CubResponse> {
        val cub = cubRepository.findByIdOrNull(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val completions = completionRepository.findAll().filter { c -> c.cub.cubId == cub.cubId }
        val completedAchievements = completions.map { c -> c.achievement.achievementId }
        val resp = CubResponse(cub.cubId, cub.name, completedAchievements)
        return ResponseEntity.ok(resp)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createCub(@RequestBody cub: CubRequest): ResponseEntity<CubResponse> {
        val requestedAchievements = cub.achievements ?: listOf()
        val validAchievementIds = achievementRepository.findAll().map { a -> a.achievementId }
        if (!validAchievementIds.containsAll(requestedAchievements)) {
            return ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY)
        }

        val cub = cubRepository.save(Cub(0, cub.name))

        val approver = (SecurityContextHolder.getContext().authentication.principal as UserDetails).username
        completionRepository.saveAll(requestedAchievements.map {Completion(
                0,
                cub,
                achievementRepository.findByIdOrNull(it)!!,
                Date.from(Instant.now()),
                approver)})

        val resp = CubResponse(cub.cubId, cub.name, requestedAchievements)
        return ResponseEntity(resp, HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteCub(@PathVariable id: Long): ResponseEntity<CubResponse> {
        val cub = cubRepository.findByIdOrNull(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val completions = completionRepository.findAll().filter { c -> c.cub.cubId == cub.cubId }
        val completedAchievements = completions.map { c -> c.achievement.achievementId }
        val resp = CubResponse(cub.cubId, cub.name, completedAchievements)
        cubRepository.delete(cub)
        return ResponseEntity.ok(resp)
    }

    @PutMapping("/{id}")
    fun updateCub(@PathVariable id: Long, @RequestBody cubUpdate: CubRequest): ResponseEntity<CubResponse> {
        val existingCub = cubRepository.findByIdOrNull(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val updatedCub = Cub(existingCub.cubId, cubUpdate.name)

        val updatedAchievements = cubUpdate.achievements ?: listOf()

        // Ensure that all the given achievements IDs are valid
        val existingAchievements = achievementRepository.findAll().map { a -> a.achievementId }
        if (!existingAchievements.containsAll(updatedAchievements)) {
            return ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY)
        }

        val approver = (SecurityContextHolder.getContext().authentication.principal as UserDetails).username
        val existingCompletions = completionRepository.findAll().filter { c -> c.cub.cubId == id }.map { c -> c.achievement.achievementId }
        val newCompletions = updatedAchievements.filter { a -> !existingCompletions.contains(a) }

        cubRepository.save(updatedCub)
        completionRepository.saveAll(newCompletions.map { Completion(
                0,
                updatedCub,
                achievementRepository.findByIdOrNull(it)!!,
                Date.from(Instant.now()),
                approver)})

        val resp = CubResponse(id, cubUpdate.name, cubUpdate.achievements ?: listOf())
        return ResponseEntity.ok(resp)
    }
}

@RestController
@RequestMapping("/achievement")
class BadgeController(private val repository: AchievementRepository) {

}