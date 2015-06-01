package com.shhutapp.social.twitter;

import android.util.Log;

class TwitterPostListener implements TwitterListener {

	private static final String TAG = TwitterPostListener.class.getSimpleName();

	@Override
	public void onStatusUpdateComplete() {
		TwitterEvents.onPostPublished();
	}

	@Override
	public void onStatusUpdateFailed(Exception e) {
		Log.e(TAG, e.getMessage(), e);
		TwitterEvents.onPostPublishingFailed();
	}
};
