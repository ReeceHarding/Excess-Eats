package com.yourcompany.excesseats.data.repository;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \'2\u00020\u0001:\u0001\'B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J(\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0002J*\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u00112\u0006\u0010\u0012\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u0013\u0010\u0014J*\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00112\u0006\u0010\u0017\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u0018\u0010\u0019J*\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00060\u00112\u0006\u0010\u0017\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b\u001b\u0010\u0019J@\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u001d0\u00112\u0006\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020\u000bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b!\u0010\"J3\u0010#\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u001d0\u00110$2\u0006\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020\u000b\u00f8\u0001\u0002J*\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00060\u00112\u0006\u0010\u0012\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0002\u00a2\u0006\u0004\b&\u0010\u0014R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\n\u0002\b\u0019\u00a8\u0006("}, d2 = {"Lcom/yourcompany/excesseats/data/repository/ItemRepository;", "", "()V", "items", "", "", "Lcom/yourcompany/excesseats/data/model/Item;", "itemsFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "calculateDistance", "", "lat1", "lon1", "lat2", "lon2", "createItem", "Lkotlin/Result;", "item", "createItem-gIAlu-s", "(Lcom/yourcompany/excesseats/data/model/Item;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteItem", "", "itemId", "deleteItem-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getItem", "getItem-gIAlu-s", "getNearbyItems", "", "latitude", "longitude", "radiusInKm", "getNearbyItems-BWLJW6A", "(DDDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeNearbyItems", "Lkotlinx/coroutines/flow/Flow;", "updateItem", "updateItem-gIAlu-s", "Companion", "app_debug"})
public final class ItemRepository {
    private final java.util.Map<java.lang.String, com.yourcompany.excesseats.data.model.Item> items = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.String, com.yourcompany.excesseats.data.model.Item>> itemsFlow = null;
    @org.jetbrains.annotations.NotNull
    public static final com.yourcompany.excesseats.data.repository.ItemRepository.Companion Companion = null;
    @kotlin.jvm.Volatile
    private static volatile com.yourcompany.excesseats.data.repository.ItemRepository instance;
    
    private ItemRepository() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<kotlin.Result<java.util.List<com.yourcompany.excesseats.data.model.Item>>> observeNearbyItems(double latitude, double longitude, double radiusInKm) {
        return null;
    }
    
    private final double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return 0.0;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/yourcompany/excesseats/data/repository/ItemRepository$Companion;", "", "()V", "instance", "Lcom/yourcompany/excesseats/data/repository/ItemRepository;", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.yourcompany.excesseats.data.repository.ItemRepository getInstance() {
            return null;
        }
    }
}