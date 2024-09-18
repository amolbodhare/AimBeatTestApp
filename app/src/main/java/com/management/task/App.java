package com.management.task;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class App  extends Application
{
    public static class LoadingDialog extends Dialog {

        public LoadingDialog(@NonNull Context context) {
            super(context);

            requestWindowFeature(Window.FEATURE_NO_TITLE);

            //setContentView(com.adoisstudio.helper.R.layout.helper_dialog_loading);
            setContentView(R.layout.helper_dialog_loading);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }//init

        public LoadingDialog(@NonNull Context context, boolean cancelable) {
            super(context);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setCancelable(cancelable);

            //setContentView(com.adoisstudio.helper.R.layout.helper_dialog_loading);
            setContentView(R.layout.helper_dialog_loading);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }//init

        public LoadingDialog(@NonNull Context context, String msg, boolean cancelable) {
            super(context);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setCancelable(cancelable);

            //setContentView(com.adoisstudio.helper.R.layout.helper_dialog_loading);
            setContentView(R.layout.helper_dialog_loading);

            //((TextView) findViewById(com.adoisstudio.helper.R.id.msg)).setText(msg);
            ((TextView) findViewById(R.id.msg)).setText(msg);

            getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }//init

        @Override
        public void show() {
            super.show();
        }

        public void show(String msg) {
            //((TextView) findViewById(com.adoisstudio.helper.R.id.msg)).setText(msg);
            ((TextView) findViewById(R.id.msg)).setText(msg);
            super.show();
        }

        public void setVisibility(boolean isVisible) {
            if (isVisible)
                show();
            else
                dismiss();
        }

        public void setVisibility(boolean isVisible, String msg) {
            if (isVisible)
                show(msg);
            else
                dismiss();
        }

    }
    public  static class Session
    {

        private SharedPreferences preferences;
        private SharedPreferences.Editor editor;

        public Session(Context context) {
            preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            editor = preferences.edit();
        }

        public Session(Context context, String text) {
            preferences = context.getSharedPreferences(text, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }


        public static Session newInstance(Context context) {
            return new Session(context);
        }

        public static Session newInstance(Context context, String text) {
            return new Session(context, text);
        }

        public void clear() {
            editor.clear();
            editor.commit();
        }

        public void addInt(String name, int val) {
            editor.putInt(name, val);
            editor.commit();
        }

        public void addBool(String name, boolean val) {
            editor.putBoolean(name, val);
            editor.commit();
        }

        public void addString(String name, String val) {
            editor.putString(name, val);
            editor.commit();
        }

        public boolean getBool(String name) {
            return preferences.getBoolean(name, false);
        }

        public int getInt(String name) {
            return preferences.getInt(name, 0);
        }

        public String getString(String name) {
            return preferences.getString(name, "");
        }

        public void addLong(String name, long val) {
            editor.putLong(name, val);
            editor.commit();
        }

        public long getLong(String name) {
            return preferences.getLong(name, 0);
        }

        public JSONObject getJson(String name) {

            try {
                return new JSONObject(getString(name));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new JSONObject();
        }

        public void addJson(String name, JSONObject json)
        {
            addString(name, json.toString());
        }

    }
}
