package com.yourcompany.excesseats.ui.posting;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\nH\u0002J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\nH\u0002J$\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010 \u001a\u00020\u0011H\u0016J\u001a\u0010!\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020\u00192\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010#\u001a\u00020\u0011H\u0002J\b\u0010$\u001a\u00020\u0011H\u0002J\b\u0010%\u001a\u00020\u0011H\u0002J\b\u0010&\u001a\u00020\u0011H\u0002J0\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\n2\u0006\u0010*\u001a\u00020\n2\u0006\u0010+\u001a\u00020\n2\u0006\u0010,\u001a\u00020\nH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001c\u0010\b\u001a\u0010\u0012\f\u0012\n \u000b*\u0004\u0018\u00010\n0\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006-"}, d2 = {"Lcom/yourcompany/excesseats/ui/posting/ItemPostingFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/yourcompany/excesseats/databinding/FragmentItemPostingBinding;", "binding", "getBinding", "()Lcom/yourcompany/excesseats/databinding/FragmentItemPostingBinding;", "getContent", "Landroidx/activity/result/ActivityResultLauncher;", "", "kotlin.jvm.PlatformType", "itemRepository", "Lcom/yourcompany/excesseats/data/repository/ItemRepository;", "selectedImageUri", "Landroid/net/Uri;", "clearForm", "", "convertTimeToMillis", "", "time", "getFoodType", "Lcom/yourcompany/excesseats/data/model/FoodType;", "foodType", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "setupFoodTypeDropdown", "setupImageUpload", "setupSubmitButton", "setupTimePickerDialog", "validateInput", "", "title", "quantity", "location", "pickupTime", "app_debug"})
public final class ItemPostingFragment extends androidx.fragment.app.Fragment {
    private com.yourcompany.excesseats.databinding.FragmentItemPostingBinding _binding;
    private final com.yourcompany.excesseats.data.repository.ItemRepository itemRepository = null;
    private android.net.Uri selectedImageUri;
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> getContent = null;
    
    public ItemPostingFragment() {
        super();
    }
    
    private final com.yourcompany.excesseats.databinding.FragmentItemPostingBinding getBinding() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupFoodTypeDropdown() {
    }
    
    private final void setupTimePickerDialog() {
    }
    
    private final void setupImageUpload() {
    }
    
    private final void setupSubmitButton() {
    }
    
    private final boolean validateInput(java.lang.String title, java.lang.String foodType, java.lang.String quantity, java.lang.String location, java.lang.String pickupTime) {
        return false;
    }
    
    private final com.yourcompany.excesseats.data.model.FoodType getFoodType(java.lang.String foodType) {
        return null;
    }
    
    private final long convertTimeToMillis(java.lang.String time) {
        return 0L;
    }
    
    private final void clearForm() {
    }
    
    @java.lang.Override
    public void onDestroyView() {
    }
}