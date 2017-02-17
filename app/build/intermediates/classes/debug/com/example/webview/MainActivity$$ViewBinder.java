// Generated code from Butter Knife. Do not modify!
package com.example.webview;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.example.webview.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165191, "field 'btn_01' and method 'onClickEvent'");
    target.btn_01 = finder.castView(view, 2131165191, "field 'btn_01'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickEvent(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165192, "field 'btn_02' and method 'onClickEvent'");
    target.btn_02 = finder.castView(view, 2131165192, "field 'btn_02'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickEvent(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165193, "field 'btn_03' and method 'onClickEvent'");
    target.btn_03 = finder.castView(view, 2131165193, "field 'btn_03'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickEvent(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165194, "field 'btn_04' and method 'onClickEvent'");
    target.btn_04 = finder.castView(view, 2131165194, "field 'btn_04'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickEvent(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.btn_01 = null;
    target.btn_02 = null;
    target.btn_03 = null;
    target.btn_04 = null;
  }
}
