package eu.musesproject.server.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the security_incident database table.
 * 
 */
@Entity
@Table(name="security_incident")
@NamedQuery(name="SecurityIncident.findAll", query="SELECT s FROM SecurityIncident s")
public class SecurityIncident implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="security_incident_id", unique=true, nullable=false)
	private String securityIncidentId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modification;

	@Column(nullable=false, length=50)
	private String name;

	//bi-directional many-to-one association to Decision
	@ManyToOne
	@JoinColumn(name="decision_id")
	private Decision decision;

	//bi-directional many-to-one association to Devices
	@ManyToOne
	@JoinColumn(name="device_id")
	private Devices device;

	//bi-directional many-to-one association to SimpleEvents
	@ManyToOne
	@JoinColumn(name="event_id")
	private SimpleEvents simpleEvent;

	//bi-directional many-to-one association to Users
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users user;

	public SecurityIncident() {
	}

	public String getSecurityIncidentId() {
		return this.securityIncidentId;
	}

	public void setSecurityIncidentId(String securityIncidentId) {
		this.securityIncidentId = securityIncidentId;
	}

	public Date getModification() {
		return this.modification;
	}

	public void setModification(Date modification) {
		this.modification = modification;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Decision getDecision() {
		return this.decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}

	public Devices getDevice() {
		return this.device;
	}

	public void setDevice(Devices device) {
		this.device = device;
	}

	public SimpleEvents getSimpleEvent() {
		return this.simpleEvent;
	}

	public void setSimpleEvent(SimpleEvents simpleEvent) {
		this.simpleEvent = simpleEvent;
	}

	public Users getUser() {
		return this.user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}