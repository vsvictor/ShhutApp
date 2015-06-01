package com.shhutapp.social.twitter;

interface TwitterListener {

	void onStatusUpdateComplete();

	void onStatusUpdateFailed(Exception e);
}
