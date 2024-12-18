// Generated by view binder compiler. Do not edit!
package com.yourcompany.excesseats.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.yourcompany.excesseats.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegisterBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final MaterialButton btnLogin;

  @NonNull
  public final MaterialButton btnRegister;

  @NonNull
  public final TextInputEditText etDisplayName;

  @NonNull
  public final TextInputEditText etEmail;

  @NonNull
  public final TextInputEditText etPassword;

  @NonNull
  public final TextInputEditText etPhone;

  @NonNull
  public final ProgressBar progressBar;

  private ActivityRegisterBinding(@NonNull LinearLayout rootView, @NonNull MaterialButton btnLogin,
      @NonNull MaterialButton btnRegister, @NonNull TextInputEditText etDisplayName,
      @NonNull TextInputEditText etEmail, @NonNull TextInputEditText etPassword,
      @NonNull TextInputEditText etPhone, @NonNull ProgressBar progressBar) {
    this.rootView = rootView;
    this.btnLogin = btnLogin;
    this.btnRegister = btnRegister;
    this.etDisplayName = etDisplayName;
    this.etEmail = etEmail;
    this.etPassword = etPassword;
    this.etPhone = etPhone;
    this.progressBar = progressBar;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_register, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegisterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnLogin;
      MaterialButton btnLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnLogin == null) {
        break missingId;
      }

      id = R.id.btnRegister;
      MaterialButton btnRegister = ViewBindings.findChildViewById(rootView, id);
      if (btnRegister == null) {
        break missingId;
      }

      id = R.id.etDisplayName;
      TextInputEditText etDisplayName = ViewBindings.findChildViewById(rootView, id);
      if (etDisplayName == null) {
        break missingId;
      }

      id = R.id.etEmail;
      TextInputEditText etEmail = ViewBindings.findChildViewById(rootView, id);
      if (etEmail == null) {
        break missingId;
      }

      id = R.id.etPassword;
      TextInputEditText etPassword = ViewBindings.findChildViewById(rootView, id);
      if (etPassword == null) {
        break missingId;
      }

      id = R.id.etPhone;
      TextInputEditText etPhone = ViewBindings.findChildViewById(rootView, id);
      if (etPhone == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      return new ActivityRegisterBinding((LinearLayout) rootView, btnLogin, btnRegister,
          etDisplayName, etEmail, etPassword, etPhone, progressBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
