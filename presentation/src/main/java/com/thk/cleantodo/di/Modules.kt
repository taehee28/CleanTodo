package com.thk.cleantodo.di

import com.thk.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetTodoListUseCase(todoRepository: TodoRepository) =
        GetTodoListUseCase(todoRepository)

    @Provides
    fun provideAddNewTodoUseCase(todoRepository: TodoRepository) =
        AddNewTodoUseCase(todoRepository)

    @Provides
    fun provideSetCompletedUseCase(todoRepository: TodoRepository) =
        SetCompletedUseCase(todoRepository)

    @Provides
    fun provideDeleteTodoUseCase(todoRepository: TodoRepository) =
        DeleteTodoUseCase(todoRepository)

    @Provides
    fun provideUpdateTodoUseCase(todoRepository: TodoRepository) =
        UpdateTodoUseCase(todoRepository)
}