// Generated by view binder compiler. Do not edit!
package com.yourcompany.excesseats.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.yourcompany.excesseats.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentNotificationsBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final TextView emptyView;

  @NonNull
  public final RecyclerView notificationsRecyclerView;

  private FragmentNotificationsBinding(@NonNull FrameLayout rootView, @NonNull TextView emptyView,
      @NonNull RecyclerView notificationsRecyclerView) {
    this.rootView = rootView;
    this.emptyView = emptyView;
    this.notificationsRecyclerView = notificationsRecyclerView;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentNotificationsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentNotificationsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_notifications, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentNotificationsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.emptyView;
      TextView emptyView = ViewBindings.findChildViewById(rootView, id);
      if (emptyView == null) {
        break missingId;
      }

      id = R.id.notificationsRecyclerView;
      RecyclerView notificationsRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (notificationsRecyclerView == null) {
        break missingId;
      }

      return new FragmentNotificationsBinding((FrameLayout) rootView, emptyView,
          notificationsRecyclerView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
