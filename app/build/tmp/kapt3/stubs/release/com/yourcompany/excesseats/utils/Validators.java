package com.yourcompany.excesseats.utils;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\r\n\u0002\u0010\u0007\n\u0002\b\f\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0004J\u000e\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\rJ\u0016\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015J\u000e\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u0004J\u000e\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u0004J\u000e\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\t\u001a\u00020\u0004J\u000e\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\rJ\u000e\u0010\u001d\u001a\u00020\u000b2\u0006\u0010\u001e\u001a\u00020\u0004J\u000e\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020\u0004J\u000e\u0010!\u001a\u00020\u000b2\u0006\u0010\"\u001a\u00020#J\u000e\u0010$\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020\u0015J\u0016\u0010&\u001a\u00020\u000b2\u0006\u0010\'\u001a\u00020\r2\u0006\u0010(\u001a\u00020\rJ\u000e\u0010)\u001a\u00020\u000b2\u0006\u0010*\u001a\u00020\u0004J\u000e\u0010+\u001a\u00020\u000b2\u0006\u0010,\u001a\u00020\u0004J\u000e\u0010-\u001a\u00020\u00042\u0006\u0010.\u001a\u00020\u0004\u00a8\u0006/"}, d2 = {"Lcom/yourcompany/excesseats/utils/Validators;", "", "()V", "getEmailErrorMessage", "", "email", "getPasswordErrorMessage", "password", "getPhoneErrorMessage", "phone", "isValidDate", "", "date", "", "isValidDescription", "description", "isValidEmail", "isValidImageSize", "sizeInBytes", "isValidLocation", "latitude", "", "longitude", "isValidName", "name", "isValidPassword", "isValidPhoneNumber", "isValidPickupTime", "pickupTime", "isValidPostalCode", "postalCode", "isValidQuantity", "quantity", "isValidRating", "rating", "", "isValidSearchRadius", "radius", "isValidTimeRange", "startTime", "endTime", "isValidTitle", "title", "isValidUrl", "url", "sanitizeInput", "input", "app_release"})
public final class Validators {
    @org.jetbrains.annotations.NotNull
    public static final com.yourcompany.excesseats.utils.Validators INSTANCE = null;
    
    private Validators() {
        super();
    }
    
    public final boolean isValidEmail(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
        return false;
    }
    
    public final boolean isValidPassword(@org.jetbrains.annotations.NotNull
    java.lang.String password) {
        return false;
    }
    
    public final boolean isValidPhoneNumber(@org.jetbrains.annotations.NotNull
    java.lang.String phone) {
        return false;
    }
    
    public final boolean isValidTitle(@org.jetbrains.annotations.NotNull
    java.lang.String title) {
        return false;
    }
    
    public final boolean isValidDescription(@org.jetbrains.annotations.NotNull
    java.lang.String description) {
        return false;
    }
    
    public final boolean isValidQuantity(@org.jetbrains.annotations.NotNull
    java.lang.String quantity) {
        return false;
    }
    
    public final boolean isValidLocation(double latitude, double longitude) {
        return false;
    }
    
    public final boolean isValidPickupTime(long pickupTime) {
        return false;
    }
    
    public final boolean isValidImageSize(long sizeInBytes) {
        return false;
    }
    
    public final boolean isValidName(@org.jetbrains.annotations.NotNull
    java.lang.String name) {
        return false;
    }
    
    public final boolean isValidUrl(@org.jetbrains.annotations.NotNull
    java.lang.String url) {
        return false;
    }
    
    public final boolean isValidPostalCode(@org.jetbrains.annotations.NotNull
    java.lang.String postalCode) {
        return false;
    }
    
    public final boolean isValidDate(long date) {
        return false;
    }
    
    public final boolean isValidTimeRange(long startTime, long endTime) {
        return false;
    }
    
    public final boolean isValidSearchRadius(double radius) {
        return false;
    }
    
    public final boolean isValidRating(float rating) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String sanitizeInput(@org.jetbrains.annotations.NotNull
    java.lang.String input) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPasswordErrorMessage(@org.jetbrains.annotations.NotNull
    java.lang.String password) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getEmailErrorMessage(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPhoneErrorMessage(@org.jetbrains.annotations.NotNull
    java.lang.String phone) {
        return null;
    }
}