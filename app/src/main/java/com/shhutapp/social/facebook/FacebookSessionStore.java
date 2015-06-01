package com.shhutapp.social.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.shhutapp.social.Constants.Preferences;
import com.shhutapp.social.facebook.extpack.com.facebook.android.Facebook;

final class FacebookSessionStore {

	private FacebookSessionStore() {
	}

	public static boolean save(Facebook session, Context context) {
		Editor editor = context.getSharedPreferences(Preferences.FACEBOOK_KEY, Context.MODE_PRIVATE).edit();
		editor.putString(Preferences.FACEBOOK_TOKEN, session.getAccessToken());
		editor.putLong(Preferences.FACEBOOK_EXPIRES, session.getAccessExpires());
		return editor.commit();
	}

	public static boolean restore(Facebook session, Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(Preferences.FACEBOOK_KEY, Context.MODE_PRIVATE);
		session.setAccessToken(savedSession.getString(Preferences.FACEBOOK_TOKEN, null));
		session.setAccessExpires(savedSession.getLong(Preferences.FACEBOOK_EXPIRES, 0));
		return session.isSessionValid();
	}

	public static void clear(Context context) {
		Editor editor = context.getSharedPreferences(Preferences.FACEBOOK_KEY, Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
	}
}
