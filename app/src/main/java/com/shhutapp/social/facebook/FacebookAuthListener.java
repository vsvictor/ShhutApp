package com.shhutapp.social.facebook;

import android.os.Bundle;
import android.util.Log;

import com.shhutapp.social.facebook.extpack.com.facebook.android.DialogError;
import com.shhutapp.social.facebook.extpack.com.facebook.android.FacebookError;
import com.shhutapp.social.facebook.extpack.com.facebook.android.Facebook.DialogListener;


class FacebookAuthListener implements DialogListener {

	private static final String TAG = FacebookAuthListener.class.getSimpleName();

	@Override
	public void onFacebookError(FacebookError e) {
		Log.e(TAG, e.getMessage(), e);
		FacebookEvents.onLoginError(e.getMessage());
	}

	@Override
	public void onError(DialogError e) {
		Log.e(TAG, e.getMessage(), e);
		FacebookEvents.onLoginError(e.getMessage());
	}

	@Override
	public void onComplete(Bundle values) {
		FacebookEvents.onLoginSuccess();
	}

	@Override
	public void onCancel() {
		// Do nothing
	}
}
