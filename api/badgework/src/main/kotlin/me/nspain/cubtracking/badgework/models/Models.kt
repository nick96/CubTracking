package me.nspain.cubtracking.badgework.models

import java.util.*
import javax.persistence.*

/**
 * A Cub whose badgework we're tracking.
 *
 * This class represents a Cub who's badgework we are tracking. It is linked to the [Completion] table which contains
 * the data about what achievements the cub has completed.
 *
 * @param [id] Unique ID to reference the cub by.
 * @param [name] Name of the cub.
 */
@Entity
data class Cub(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        val name: String
)

/**
 * A user who can utilise the API.
 *
 * This class represents a user who utilise the API.
 *
 * @param [id] Unique ID to reference the user by.
 * @param [name] Name of the user.
 */
@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        val name: String
)

/**
 * An entity that links a Cub and achievements.
 *
 * This class represents the link between a Cub and their achievements. It contains the metadata around when it was
 * completed and who approved the completion.
 *
 * @param [id] Unique ID to represent the completion data.
 * @param [cub] [Cub] being mapped to an [Achievement].
 * @param [achievement] [Achievement] being mapped to a [Cub].
 * @param [completionDate] Date the achievement was completed on.
 * @param [approvedBy] [User] approving the completion of an [Achievement].
 */
@Entity
data class Completion(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        @OneToOne
        @JoinColumn(name = "id")
        val cub: Cub,
        @OneToOne
        @JoinColumn(name = "id")
        val achievement: Achievement,
        val completionDate: Date,
        @ManyToOne
        @JoinColumn(name = "id")
        val approvedBy: User
)

/**
 * An achievement item.
 *
 * This class represents all achievable items. They can have zero or more subachievements required to complete them.
 * [subachievements] is a collection of all the subachievements that are applicable to this achievement,
 * [requiredSubachievements] is a subset of [subachievements] that are required to complete the achievement. The
 * rest of elements of [subachievements] are there to be completed to fill the [numRequiredSubachievements] quota.
 *
 * @param [id] Unique ID for referencing an achievement item.
 * @param [name] Name of the achievement item.
 * @param [description] Description of the achievement item.
 * @param [type] Achievement type.
 * @param [numRequiredSubachievements] Number of subachievements required to complete this achievement.
 * @param [subachievements] Collection of subachivements that make up this achievement.
 * @param [requiredSubachievements] Collection of subachievments that must be required to complete this achievement.
 */
@Entity
data class Achievement (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        @Column(unique = true)
        val name: String,
        val description: String,
        @ManyToOne
        @JoinColumn(name = "id")
        val type: AchievementType,
        val numRequiredSubachievements: Long?,
        @OneToMany
        @JoinColumn(name = "id")
        val subachievements: List<Achievement>?,
        @OneToMany
        @JoinColumn(name = "id")
        val requiredSubachievements: List<Achievement>?
)

/**
 * Type of achievement.
 *
 * This class represents a type of achievement and contains the metadata describing it. The idea with this is to make
 * the approval process easily extensible so we could have different rolls that are allowed to approve different
 * type of achievements. It also allows us to extend the available achievement types, hopefully without having
 * to change the code.
 *
 * @param [id] Unique ID used to reference an achievement type.
 * @param [name] Name of the achievement type.
 * @param [description] Description of the achievement type.
 */
@Entity
data class AchievementType(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        @Column(unique = true)
        val name: String,
        val description: String
)