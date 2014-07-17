package eu.musesproject.server.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the user_authorization database table.
 * 
 */
@Entity
@Table(name="user_authorization")
@NamedQuery(name="UserAuthorization.findAll", query="SELECT u FROM UserAuthorization u")
public class UserAuthorization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_authorization_id")
	private int userAuthorizationId;

	@Column(name="role_id")
	private int roleId;

	@Column(name="user_id")
	private BigInteger userId;

	public UserAuthorization() {
	}

	public int getUserAuthorizationId() {
		return this.userAuthorizationId;
	}

	public void setUserAuthorizationId(int userAuthorizationId) {
		this.userAuthorizationId = userAuthorizationId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public BigInteger getUserId() {
		return this.userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

}