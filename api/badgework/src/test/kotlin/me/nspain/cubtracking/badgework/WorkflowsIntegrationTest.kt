package me.nspain.cubtracking.badgework

import me.nspain.cubtracking.badgework.models.Cub
import me.nspain.cubtracking.badgework.models.CubRequest
import me.nspain.cubtracking.badgework.models.CubResponse
import me.nspain.cubtracking.badgework.repositories.AchievementRepository
import me.nspain.cubtracking.badgework.repositories.CompletionRepository
import me.nspain.cubtracking.badgework.repositories.CubRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Integration tests for the common workflows. This allows us to
 *   1) Ensure the workflows work as expected
 *   2) Check the ergonomics of the API
 */
@SpringBootTest(classes = [BadgeworkApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@AutoConfigureWebTestClient
class WorkflowsIntegrationTest(@Autowired val webClient: WebTestClient) {

    @Autowired
    lateinit var cubRepository: CubRepository

    @Autowired
    lateinit var achievementRepository: AchievementRepository

    @Autowired
    lateinit var completionRepository: CompletionRepository


    @BeforeEach
    fun cleanRepositories() {
        cubRepository.deleteAll()
        achievementRepository.deleteAll()
        completionRepository.deleteAll()
    }


    /**
     * Test the workflow for adding a cub.
     *
     * At the end of this workflow a cub should exist with the expected name and no completed achievemnts
     * associated with them.
     */
    @Test
    fun `add cub workflow`() {
        val newCub = CubRequest("cubName")
        webClient.post().uri("/cubs").syncBody(newCub)
                .exchange()
                .expectStatus().isCreated
                .expectBody()
                .jsonPath("$.id").isNumber
                .jsonPath("$.name").isEqualTo("cubName")

        // As we start with empty repositories before every test, I can assume that the only cub in the DB is the one
        // we just put there.
        val cubs = cubRepository.findAll()
        assertEquals(1, cubs.size)
        assertEquals("cubName", cubs[0].name)
    }

    /**
     * Test the workflow for deleting a cub.
     *
     * At the end of this workflow the cub should no long exist.
     */
    @Test
    fun `remove cub workflow`() {
        cubRepository.save(Cub(1, "cubName"))
        webClient.delete().uri("/cubs/1").exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("cubName")

        assertNull(cubRepository.findByIdOrNull(1))
    }

    /**
     * Test the workflow for updating a cub.
     *
     -* At the end of this workflow the cub's details should have been update. It should still be associated with the
     * same achievements.
     */
    @Test
    fun `update cub workflow`() {
        val updatedCub = CubRequest("newCubName")
        cubRepository.save(Cub(1, "cubName"))
        val resp = webClient.post().uri("/cubs/1").syncBody(updatedCub).exchange()
                .expectStatus().isOk
                .expectBody(CubResponse::class.java)
                .returnResult().responseBody
        assertNotNull(resp)
        assertEquals("newCubName", resp.name)

        val cub = cubRepository.findByIdOrNull(1)
        assertNotNull(cub)
        assertEquals("newCubName", cub.name)
    }

    /**
     * Test the workflow for approving the completion of an achievement.
     *
     * At the end of this workflow the cub should have a completed achievement associated with them.
     */
    @Test
    fun `approve achievement completion workflow`() {
        cubRepository.save(Cub(1, "cubName"))
        val getCubResp = webClient.get().uri("/cubs/1").exchange()
                .expectBody(CubResponse::class.java)
                .returnResult().responseBody
        assertNotNull(getCubResp)
        assertEquals(getCubResp.name, "cubName")

        val cubRequest = CubRequest(getCubResp.name, getCubResp.achievements + 1)
        val updateCubResp = webClient.put().uri("/cubs/1").syncBody(cubRequest).exchange()
                .expectStatus().isOk
                .expectBody(CubResponse::class.java)
                .returnResult().responseBody
        assertNotNull(updateCubResp)
        assertEquals(listOf<Long>(1), updateCubResp.achievements)

        val completed = completionRepository.findAll()
                .filter { c -> c.cub.cubId == (1).toLong() }
                .map { c -> c.achievement.achievementId }
        assertEquals(listOf<Long>(1), completed)
    }

    /**
     * Test the workflow for adding achievements.
     *
     * At the end of this workflow a new achievement should exist and no cubs should be associated with it.
     */
    @Test
    fun `add achievement workflow`() {

    }

    /**
     * Test the workflow for removing an achievement.
     *
     * A the end of this workflow the achievement should no long exist and there should be no cubs associated with it.
     */
    @Test
    fun `remove archivement workflow`() {

    }
}