package com.thk.domain.di

import com.thk.domain.AddNewTodoUseCase
import com.thk.domain.GetTodoListUseCase
import com.thk.domain.SetCompletedUseCase
import com.thk.domain.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetTodoListUseCase(todoRepository: TodoRepository) =
        GetTodoListUseCase(todoRepository)

    @Provides
    @Singleton
    fun provideAddNewTodoUseCase(todoRepository: TodoRepository) =
        AddNewTodoUseCase(todoRepository)

    @Provides
    @Singleton
    fun provideSetCompletedUseCase(todoRepository: TodoRepository) =
        SetCompletedUseCase(todoRepository)

}