package me.nspain.cubtracking.badgework.models

import java.util.*
import javax.persistence.*

/**
 * A Cub whose badgework we're tracking.
 *
 * This class represents a Cub who's badgework we are tracking. It is linked to the [Completion] table which contains
 * the data about what achievements the cub has completed.
 *
 * @param [cubId] Unique ID to reference the cub by.
 * @param [name] Name of the cub.
 */
@Table(name = "Cub")
@Entity
data class Cub(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val cubId: Long,
        val name: String
){
        constructor(): this(0, "")
}

/**
 * An entity that links a Cub and achievements.
 *
 * This class represents the link between a Cub and their achievements. It contains the metadata around when it was
 * completed and who approved the completion.
 *
 * @param [completionId] Unique ID to represent the completion data.
 * @param [cub] [Cub] being mapped to an [Achievement].
 * @param [achievement] [Achievement] being mapped to a [Cub].
 * @param [completionDate] Date the achievement was completed on.
 * @param [approvedBy] Person approving the completion of an [Achievement].
 */
@Table(name = "Completion")
@Entity
data class Completion(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val completionId: Long,
        // Many completion rows can map to a single cub
        @ManyToOne(optional = false)
        @JoinColumn(name = "cubId")
        val cub: Cub,
        // Many completion rows can map to a single achievement
        @ManyToOne(optional = false)
        @JoinColumn(name = "achievementId")
        val achievement: Achievement,
        val completionDate: Date,
        val approvedBy: String
)

/**
 * An achievement item.
 *
 * This class represents all achievable items. They can have zero or more subachievements required to complete them.
 * [subachievements] is a collection of all the subachievements that are applicable to this achievement,
 * [requiredSubachievements] is a subset of [subachievements] that are required to complete the achievement. The
 * rest of elements of [subachievements] are there to be completed to fill the [numRequiredSubachievements] quota.
 *
 * @param [achievementId] Unique ID for referencing an achievement item.
 * @param [name] Name of the achievement item.
 * @param [description] Description of the achievement item.
 * @param [type] Achievement type.
 * @param [numRequiredSubachievements] Number of subachievements required to complete this achievement.
 * @param [subachievements] Collection of subachivements that make up this achievement.
 * @param [requiredSubachievements] Collection of subachievments that must be required to complete this achievement.
 */
@Table(name = "Achievement")
@Entity
data class Achievement (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val achievementId: Long,
        @Column(unique = true)
        val name: String,
        val description: String,
        @ManyToOne
        @JoinColumn(name = "achievementTypeId")
        val type: AchievementType,
        val numRequiredSubachievements: Long?,
        @OneToMany
        @JoinColumn(name = "achievementId")
        val subachievements: List<Achievement>?,
        @OneToMany
        @JoinColumn(name = "achievementId")
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
 * @param [achievementTypeId] Unique ID used to reference an achievement type.
 * @param [name] Name of the achievement type.
 * @param [description] Description of the achievement type.
 */
@Table(name = "AchievementType")
@Entity
data class AchievementType(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val achievementTypeId: Long,
        @Column(unique = true)
        val name: String,
        val description: String
)