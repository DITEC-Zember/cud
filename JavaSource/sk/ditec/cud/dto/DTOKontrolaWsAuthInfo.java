package sk.ditec.cud.dto;

public class DTOKontrolaWsAuthInfo {

	String url;
	String login;
	String passwd;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOKontrolaWsAuthInfo: {";
		s += "\n url=" + url;
		s += "\n login=" + login;
		s += "\n passwd=" + passwd;
		s += "}";
		return s;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
