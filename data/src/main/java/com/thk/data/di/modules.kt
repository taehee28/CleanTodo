package com.thk.data.di

import android.content.Context
import androidx.room.Room
import com.thk.data.TodoDataSource
import com.thk.data.TodoDataSourceImpl
import com.thk.data.TodoRepositoryImpl
import com.thk.data.database.TodoDao
import com.thk.data.database.TodoDatabase
import com.thk.domain.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTodoRepository(todoDataSource: TodoDataSource): TodoRepository {
        return TodoRepositoryImpl(todoDataSource)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideTodoDataSource(todoDao: TodoDao): TodoDataSource {
        return TodoDataSourceImpl(todoDao)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): TodoDatabase {
        return Room.databaseBuilder(
            appContext,
            TodoDatabase::class.java,
            "todo_db"
        )
            .build()
    }
}