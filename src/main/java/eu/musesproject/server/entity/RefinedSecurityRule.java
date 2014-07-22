package eu.musesproject.server.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the refined_security_rules database table.
 * 
 */
@Entity
@Table(name="refined_security_rules")
@NamedQuery(name="RefinedSecurityRule.findAll", query="SELECT r FROM RefinedSecurityRule r")
public class RefinedSecurityRule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="refined_security_rules_id")
	private String refinedSecurityRulesId;

	@Lob
	private byte[] file;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modification;

	private String name;

	private String status;

	//bi-directional many-to-one association to SecurityRule
	@ManyToOne
	@JoinColumn(name="original_security_rule_id")
	private SecurityRule securityRule;

	public RefinedSecurityRule() {
	}

	public String getRefinedSecurityRulesId() {
		return this.refinedSecurityRulesId;
	}

	public void setRefinedSecurityRulesId(String refinedSecurityRulesId) {
		this.refinedSecurityRulesId = refinedSecurityRulesId;
	}

	public byte[] getFile() {
		return this.file;
	}

	public void setFile(byte[] file) {
		this.file = file;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SecurityRule getSecurityRule() {
		return this.securityRule;
	}

	public void setSecurityRule(SecurityRule securityRule) {
		this.securityRule = securityRule;
	}

}