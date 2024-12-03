package com.yourcompany.excesseats.data.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\bK\b\u0086\b\u0018\u00002\u00020\u0001B\u0093\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\r\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0018\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u001b\u0012\u000e\b\u0002\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00030\u001d\u0012\u000e\b\u0002\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00030\u001d\u0012\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u0018\u0012\b\b\u0002\u0010!\u001a\u00020\u0018\u0012\b\b\u0002\u0010\"\u001a\u00020\u0018\u0012\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u0018\u00a2\u0006\u0002\u0010$J\t\u0010I\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010J\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010K\u001a\u00020\u0003H\u00c6\u0003J\t\u0010L\u001a\u00020\u0003H\u00c6\u0003J\t\u0010M\u001a\u00020\u0013H\u00c6\u0003J\t\u0010N\u001a\u00020\u0015H\u00c6\u0003J\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\t\u0010P\u001a\u00020\u0018H\u00c6\u0003J\t\u0010Q\u001a\u00020\u0018H\u00c6\u0003J\t\u0010R\u001a\u00020\u001bH\u00c6\u0003J\u000f\u0010S\u001a\b\u0012\u0004\u0012\u00020\u00030\u001dH\u00c6\u0003J\t\u0010T\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010U\u001a\b\u0012\u0004\u0012\u00020\u00030\u001dH\u00c6\u0003J\u000b\u0010V\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010W\u001a\u0004\u0018\u00010\u0018H\u00c6\u0003\u00a2\u0006\u0002\u0010(J\t\u0010X\u001a\u00020\u0018H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0018H\u00c6\u0003J\u0010\u0010Z\u001a\u0004\u0018\u00010\u0018H\u00c6\u0003\u00a2\u0006\u0002\u0010(J\t\u0010[\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\\\u001a\u00020\u0007H\u00c6\u0003J\t\u0010]\u001a\u00020\u0003H\u00c6\u0003J\t\u0010^\u001a\u00020\nH\u00c6\u0003J\t\u0010_\u001a\u00020\u0003H\u00c6\u0003J\t\u0010`\u001a\u00020\rH\u00c6\u0003J\t\u0010a\u001a\u00020\rH\u00c6\u0003J\u009c\u0002\u0010b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\r2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u00182\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\u000e\b\u0002\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00030\u001d2\u000e\b\u0002\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00030\u001d2\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u00182\b\b\u0002\u0010!\u001a\u00020\u00182\b\b\u0002\u0010\"\u001a\u00020\u00182\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u0018H\u00c6\u0001\u00a2\u0006\u0002\u0010cJ\u0013\u0010d\u001a\u00020\u001b2\b\u0010e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010f\u001a\u00020\nH\u00d6\u0001J\t\u0010g\u001a\u00020\u0003H\u00d6\u0001R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00030\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0015\u0010 \u001a\u0004\u0018\u00010\u0018\u00a2\u0006\n\n\u0002\u0010)\u001a\u0004\b\'\u0010(R\u0013\u0010\u001f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0011\u0010\u001a\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010-R\u0011\u0010!\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010+R\u0017\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00030\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010&R\u0015\u0010#\u001a\u0004\u0018\u00010\u0018\u00a2\u0006\n\n\u0002\u0010)\u001a\u0004\b2\u0010(R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010+R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010+R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010+R\u0011\u0010\u000e\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u00108R\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010+R\u0011\u0010\u0019\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010/R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010/R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010+R\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010+R\u0011\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010AR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010+R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010DR\u0011\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010FR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010+R\u0011\u0010\"\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010/\u00a8\u0006h"}, d2 = {"Lcom/yourcompany/excesseats/data/model/Item;", "", "id", "", "title", "description", "foodType", "Lcom/yourcompany/excesseats/data/model/FoodType;", "quantity", "servings", "", "location", "latitude", "", "longitude", "imageUrl", "providerId", "providerName", "providerRating", "", "status", "Lcom/yourcompany/excesseats/data/model/ItemStatus;", "pickupInstructions", "pickupTimeStart", "", "pickupTimeEnd", "containerAvailable", "", "allergenInfo", "", "dietaryInfo", "claimedBy", "claimedAt", "createdAt", "updatedAt", "expiresAt", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/yourcompany/excesseats/data/model/FoodType;Ljava/lang/String;ILjava/lang/String;DDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;FLcom/yourcompany/excesseats/data/model/ItemStatus;Ljava/lang/String;JJZLjava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/Long;JJLjava/lang/Long;)V", "getAllergenInfo", "()Ljava/util/List;", "getClaimedAt", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getClaimedBy", "()Ljava/lang/String;", "getContainerAvailable", "()Z", "getCreatedAt", "()J", "getDescription", "getDietaryInfo", "getExpiresAt", "getFoodType", "()Lcom/yourcompany/excesseats/data/model/FoodType;", "getId", "getImageUrl", "getLatitude", "()D", "getLocation", "getLongitude", "getPickupInstructions", "getPickupTimeEnd", "getPickupTimeStart", "getProviderId", "getProviderName", "getProviderRating", "()F", "getQuantity", "getServings", "()I", "getStatus", "()Lcom/yourcompany/excesseats/data/model/ItemStatus;", "getTitle", "getUpdatedAt", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/yourcompany/excesseats/data/model/FoodType;Ljava/lang/String;ILjava/lang/String;DDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;FLcom/yourcompany/excesseats/data/model/ItemStatus;Ljava/lang/String;JJZLjava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/Long;JJLjava/lang/Long;)Lcom/yourcompany/excesseats/data/model/Item;", "equals", "other", "hashCode", "toString", "app_release"})
public final class Item {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String description = null;
    @org.jetbrains.annotations.NotNull
    private final com.yourcompany.excesseats.data.model.FoodType foodType = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String quantity = null;
    private final int servings = 0;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String location = null;
    private final double latitude = 0.0;
    private final double longitude = 0.0;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String imageUrl = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String providerId = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String providerName = null;
    private final float providerRating = 0.0F;
    @org.jetbrains.annotations.NotNull
    private final com.yourcompany.excesseats.data.model.ItemStatus status = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String pickupInstructions = null;
    private final long pickupTimeStart = 0L;
    private final long pickupTimeEnd = 0L;
    private final boolean containerAvailable = false;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> allergenInfo = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.String> dietaryInfo = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String claimedBy = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long claimedAt = null;
    private final long createdAt = 0L;
    private final long updatedAt = 0L;
    @org.jetbrains.annotations.Nullable
    private final java.lang.Long expiresAt = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.yourcompany.excesseats.data.model.Item copy(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String description, @org.jetbrains.annotations.NotNull
    com.yourcompany.excesseats.data.model.FoodType foodType, @org.jetbrains.annotations.NotNull
    java.lang.String quantity, int servings, @org.jetbrains.annotations.NotNull
    java.lang.String location, double latitude, double longitude, @org.jetbrains.annotations.Nullable
    java.lang.String imageUrl, @org.jetbrains.annotations.NotNull
    java.lang.String providerId, @org.jetbrains.annotations.NotNull
    java.lang.String providerName, float providerRating, @org.jetbrains.annotations.NotNull
    com.yourcompany.excesseats.data.model.ItemStatus status, @org.jetbrains.annotations.NotNull
    java.lang.String pickupInstructions, long pickupTimeStart, long pickupTimeEnd, boolean containerAvailable, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> allergenInfo, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> dietaryInfo, @org.jetbrains.annotations.Nullable
    java.lang.String claimedBy, @org.jetbrains.annotations.Nullable
    java.lang.Long claimedAt, long createdAt, long updatedAt, @org.jetbrains.annotations.Nullable
    java.lang.Long expiresAt) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public Item() {
        super();
    }
    
    public Item(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String description, @org.jetbrains.annotations.NotNull
    com.yourcompany.excesseats.data.model.FoodType foodType, @org.jetbrains.annotations.NotNull
    java.lang.String quantity, int servings, @org.jetbrains.annotations.NotNull
    java.lang.String location, double latitude, double longitude, @org.jetbrains.annotations.Nullable
    java.lang.String imageUrl, @org.jetbrains.annotations.NotNull
    java.lang.String providerId, @org.jetbrains.annotations.NotNull
    java.lang.String providerName, float providerRating, @org.jetbrains.annotations.NotNull
    com.yourcompany.excesseats.data.model.ItemStatus status, @org.jetbrains.annotations.NotNull
    java.lang.String pickupInstructions, long pickupTimeStart, long pickupTimeEnd, boolean containerAvailable, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> allergenInfo, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> dietaryInfo, @org.jetbrains.annotations.Nullable
    java.lang.String claimedBy, @org.jetbrains.annotations.Nullable
    java.lang.Long claimedAt, long createdAt, long updatedAt, @org.jetbrains.annotations.Nullable
    java.lang.Long expiresAt) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.yourcompany.excesseats.data.model.FoodType component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.yourcompany.excesseats.data.model.FoodType getFoodType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getQuantity() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int getServings() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLocation() {
        return null;
    }
    
    public final double component8() {
        return 0.0;
    }
    
    public final double getLatitude() {
        return 0.0;
    }
    
    public final double component9() {
        return 0.0;
    }
    
    public final double getLongitude() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getImageUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getProviderId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getProviderName() {
        return null;
    }
    
    public final float component13() {
        return 0.0F;
    }
    
    public final float getProviderRating() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.yourcompany.excesseats.data.model.ItemStatus component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.yourcompany.excesseats.data.model.ItemStatus getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPickupInstructions() {
        return null;
    }
    
    public final long component16() {
        return 0L;
    }
    
    public final long getPickupTimeStart() {
        return 0L;
    }
    
    public final long component17() {
        return 0L;
    }
    
    public final long getPickupTimeEnd() {
        return 0L;
    }
    
    public final boolean component18() {
        return false;
    }
    
    public final boolean getContainerAvailable() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getAllergenInfo() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> component20() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getDietaryInfo() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component21() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getClaimedBy() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long component22() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getClaimedAt() {
        return null;
    }
    
    public final long component23() {
        return 0L;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    public final long component24() {
        return 0L;
    }
    
    public final long getUpdatedAt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long component25() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Long getExpiresAt() {
        return null;
    }
}