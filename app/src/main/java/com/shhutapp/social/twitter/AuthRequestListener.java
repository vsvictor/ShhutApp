package com.shhutapp.social.twitter;

public interface AuthRequestListener {

	void onAuthRequestComplete(String requestUrl);

	void onAuthRequestFailed(Exception e);
}
