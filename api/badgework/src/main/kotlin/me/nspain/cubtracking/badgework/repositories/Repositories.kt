package me.nspain.cubtracking.badgework.repositories

import me.nspain.cubtracking.badgework.models.*
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for interacting with the [Achievement] entity.
 *
 * This allows us to perform operations on the achievements and the relationships between them.
 */
interface AchievementRepository: JpaRepository<Achievement, Long>

/**
 * Repository for interacting with the [Cub] entity.
 *
 * This allows us to perform operations on cubs.
 */
interface CubRepository: JpaRepository<Cub, Long>

/**
 * Repository for interacting with the [Completion] entity.
 *
 * This allows us interact with the mapping between [Cub]s and their [Achievement]s.
 */
interface CompletionRepository: JpaRepository<Completion, Long>

