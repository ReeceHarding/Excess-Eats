package com.yourcompany.excesseats.data.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000 #2\u00020\u0001:\u0001#J!\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J;\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00032\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\u000e\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ5\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\u0012\u001a\u00020\u00062\b\b\u0001\u0010\u0013\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0014J!\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00032\b\b\u0001\u0010\u0017\u001a\u00020\u0018H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019J+\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u001b\u001a\u00020\u001c2\b\b\u0001\u0010\u001d\u001a\u00020\u001cH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001eJ!\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0001\u0010 \u001a\u00020!H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006$"}, d2 = {"Lcom/yourcompany/excesseats/data/network/ApiService;", "", "forwardGeocode", "Lretrofit2/Response;", "Lcom/yourcompany/excesseats/data/network/GeocodingResponse;", "address", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserNotifications", "", "Lcom/yourcompany/excesseats/data/network/NotificationResponse;", "userId", "page", "", "size", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserStats", "Lcom/yourcompany/excesseats/data/network/UserStats;", "startDate", "endDate", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logAnalyticsEvent", "", "event", "Lcom/yourcompany/excesseats/data/network/AnalyticsEvent;", "(Lcom/yourcompany/excesseats/data/network/AnalyticsEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reverseGeocode", "latitude", "", "longitude", "(DDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendNotification", "notification", "Lcom/yourcompany/excesseats/data/network/NotificationRequest;", "(Lcom/yourcompany/excesseats/data/network/NotificationRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public abstract interface ApiService {
    @org.jetbrains.annotations.NotNull
    public static final com.yourcompany.excesseats.data.network.ApiService.Companion Companion = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String GEOCODING_API = "https://api.yourcompany.com/v1/geocoding/";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String NOTIFICATIONS_API = "https://api.yourcompany.com/v1/notifications/";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ANALYTICS_API = "https://api.yourcompany.com/v1/analytics/";
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "https://api.yourcompany.com/v1/geocoding/reverse")
    public abstract java.lang.Object reverseGeocode(@retrofit2.http.Query(value = "lat")
    double latitude, @retrofit2.http.Query(value = "lon")
    double longitude, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.yourcompany.excesseats.data.network.GeocodingResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "https://api.yourcompany.com/v1/geocoding/forward")
    public abstract java.lang.Object forwardGeocode(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "address")
    java.lang.String address, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.yourcompany.excesseats.data.network.GeocodingResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.POST(value = "https://api.yourcompany.com/v1/notifications/send")
    public abstract java.lang.Object sendNotification(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.yourcompany.excesseats.data.network.NotificationRequest notification, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.yourcompany.excesseats.data.network.NotificationResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "https://api.yourcompany.com/v1/notifications/user/{userId}")
    public abstract java.lang.Object getUserNotifications(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Path(value = "userId")
    java.lang.String userId, @retrofit2.http.Query(value = "page")
    int page, @retrofit2.http.Query(value = "size")
    int size, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.yourcompany.excesseats.data.network.NotificationResponse>>> continuation);
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.POST(value = "https://api.yourcompany.com/v1/analytics/event")
    public abstract java.lang.Object logAnalyticsEvent(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Body
    com.yourcompany.excesseats.data.network.AnalyticsEvent event, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> continuation);
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "https://api.yourcompany.com/v1/analytics/user/{userId}/stats")
    public abstract java.lang.Object getUserStats(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Path(value = "userId")
    java.lang.String userId, @org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "startDate")
    java.lang.String startDate, @org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "endDate")
    java.lang.String endDate, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.yourcompany.excesseats.data.network.UserStats>> continuation);
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/yourcompany/excesseats/data/network/ApiService$Companion;", "", "()V", "ANALYTICS_API", "", "BASE_URL", "GEOCODING_API", "NOTIFICATIONS_API", "app_debug"})
    public static final class Companion {
        private static final java.lang.String BASE_URL = "https://api.yourcompany.com/v1/";
        @org.jetbrains.annotations.NotNull
        public static final java.lang.String GEOCODING_API = "https://api.yourcompany.com/v1/geocoding/";
        @org.jetbrains.annotations.NotNull
        public static final java.lang.String NOTIFICATIONS_API = "https://api.yourcompany.com/v1/notifications/";
        @org.jetbrains.annotations.NotNull
        public static final java.lang.String ANALYTICS_API = "https://api.yourcompany.com/v1/analytics/";
        
        private Companion() {
            super();
        }
    }
}