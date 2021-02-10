package herobrine99dan.heropanel.webserver.features;

import java.security.GeneralSecurityException;

public class AuthenticationHandler {
		
	private String authenticated = "";
	private final String base32Secret;
	private String lastCodeUsed = "";
	
	public AuthenticationHandler(String totpKey) {
		this.base32Secret = totpKey;
	}
	
	public boolean isAuthenticated(String ip) {
		if(authenticated.isEmpty() || ip.isEmpty()) {
			return false;
		}
		return ip.equals(authenticated);
	}
	
	public boolean logoutHandler() {
		if(authenticated.isEmpty()) {
			return false;
		}
		authenticated = "";
		return true;
	}
	
	public boolean loginHandler(String ip, String code) throws GeneralSecurityException {
		if(!authenticated.isEmpty()) {
			return false;
		}
		if(!code.matches("[0-9]+")) {
			return false;
		}
		return isCodeCorrect(code, ip);
	}
	
	private boolean isCodeCorrect(String code, String ip) throws GeneralSecurityException {
		if(code.isEmpty()) {
			return false;
		}
		String secret = Long.toString(TOTP.generateCurrentNumber(base32Secret));
		if(secret.equals(code) && !lastCodeUsed.equals(secret)) {
			authenticated = ip;
			lastCodeUsed = secret;
			return true;
		}
		return false;
	}

	public String getBase32Secret() {
		return base32Secret;
	}

}