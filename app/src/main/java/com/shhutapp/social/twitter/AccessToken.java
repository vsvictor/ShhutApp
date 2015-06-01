package com.shhutapp.social.twitter;

class AccessToken {

	private String token;
	private String tokenSecret;

	AccessToken(String token, String tokenSecret) {
		this.token = token;
		this.tokenSecret = tokenSecret;
	}

	public String getToken() {
		return token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}
}
