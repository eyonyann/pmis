package com.example.lab7.di

import android.app.Application
import androidx.room.Room
import com.example.lab7.db.MainDb
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@androidx.test.espresso.core.internal.deps.dagger.Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @androidx.test.espresso.core.internal.deps.dagger.Provides
    @Singleton
    fun provideMainDb(App:Application): MainDb {
        return Room.databaseBuilder(
            App,
            MainDb::class.java,
            name = "info.db"
        ).createFromAsset("db/info.db").build()
    }
}
