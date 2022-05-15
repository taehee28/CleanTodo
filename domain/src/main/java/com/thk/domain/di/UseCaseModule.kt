package com.thk.domain.di

import com.thk.domain.*
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

    @Provides
    @Singleton
    fun provideDeleteTodoUseCase(todoRepository: TodoRepository) =
        DeleteTodoUseCase(todoRepository)

}