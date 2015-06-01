package com.shhutapp.social.common;

public interface AuthListener {

	public void onAuthSucceed();

	public void onAuthFail(String error);
}
