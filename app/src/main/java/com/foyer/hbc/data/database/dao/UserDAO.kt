package com.foyer.hbc.data.database.dao

import androidx.room.*
import com.foyer.hbc.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: List<UserEntity>)

    @Query("SELECT * from UserEntity")
    fun getUsers(): Flow<List<UserEntity>>

    @Update
    fun updateUser(userEntity: UserEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUser(userEntity: UserEntity)

    @Query("SELECT * from UserEntity order by consumptionsPayed desc LIMIT :take")
    suspend fun getBestUsers(take: Int): List<UserEntity>
}
