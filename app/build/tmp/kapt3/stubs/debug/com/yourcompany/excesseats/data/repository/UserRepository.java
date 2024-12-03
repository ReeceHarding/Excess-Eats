package com.yourcompany.excesseats.data.repository;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\t\u0018\u0000 +2\u00020\u0001:\u0001+B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J*\u0010\f\u001a\b\u0012\u0004\u0012\u00020\n0\r2\u0006\u0010\u000e\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u000f\u0010\u0010J*\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\r2\u0006\u0010\u0013\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u0014\u0010\u0015J*\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\n0\r2\u0006\u0010\u0013\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u0017\u0010\u0015J*\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\r2\u0006\u0010\u0019\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u001a\u0010\u0015J*\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00060\r2\u0006\u0010\u0013\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u001c\u0010\u0015J\u001d\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\r0\u001e2\u0006\u0010\u0013\u001a\u00020\u0005\u00f8\u0001\u0002J*\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\n0\r2\u0006\u0010\u000e\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b \u0010\u0010JB\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00120\r2\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020#2\u0006\u0010%\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b&\u0010\'J*\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00060\r2\u0006\u0010\u0003\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b)\u0010*R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006,"}, d2 = {"Lcom/yourcompany/excesseats/data/repository/UserRepository;", "", "()V", "preferences", "", "", "Lcom/yourcompany/excesseats/data/model/Preference;", "userFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/yourcompany/excesseats/data/model/User;", "users", "createUser", "Lkotlin/Result;", "user", "createUser-gIAlu-s", "(Lcom/yourcompany/excesseats/data/model/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteUser", "", "userId", "deleteUser-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUser", "getUser-gIAlu-s", "getUserByEmail", "email", "getUserByEmail-gIAlu-s", "getUserPreferences", "getUserPreferences-gIAlu-s", "observeUser", "Lkotlinx/coroutines/flow/Flow;", "updateUser", "updateUser-gIAlu-s", "updateUserLocation", "latitude", "", "longitude", "locationString", "updateUserLocation-yxL6bBk", "(Ljava/lang/String;DDLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateUserPreferences", "updateUserPreferences-gIAlu-s", "(Lcom/yourcompany/excesseats/data/model/Preference;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class UserRepository {
    private final java.util.Map<java.lang.String, com.yourcompany.excesseats.data.model.User> users = null;
    private final java.util.Map<java.lang.String, com.yourcompany.excesseats.data.model.Preference> preferences = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.String, com.yourcompany.excesseats.data.model.User>> userFlow = null;
    @org.jetbrains.annotations.NotNull
    public static final com.yourcompany.excesseats.data.repository.UserRepository.Companion Companion = null;
    @kotlin.jvm.Volatile
    private static volatile com.yourcompany.excesseats.data.repository.UserRepository instance;
    
    private UserRepository() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<kotlin.Result<com.yourcompany.excesseats.data.model.User>> observeUser(@org.jetbrains.annotations.NotNull
    java.lang.String userId) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/yourcompany/excesseats/data/repository/UserRepository$Companion;", "", "()V", "instance", "Lcom/yourcompany/excesseats/data/repository/UserRepository;", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.yourcompany.excesseats.data.repository.UserRepository getInstance() {
            return null;
        }
    }
}