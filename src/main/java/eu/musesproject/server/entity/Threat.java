package eu.musesproject.server.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the threat database table.
 * 
 */
@Entity
@Table(name="threat")
@NamedQueries ({
	@NamedQuery(name="Threat.findAll", 
		    	query="SELECT t FROM Threat t"),
	@NamedQuery(name="Threat.findThreatById", 
		    	query="SELECT t FROM Threat t where t.threatId = :threat_id"),
	@NamedQuery(name="Threat.findThreatbyDescription", 
 				query="SELECT t FROM Threat t where t.description = :description"),
})
public class Threat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="threat_id", unique=true, nullable=false)
	private String threatId;

	private int badOutcomeCount;

	@Lob
	@Column(nullable=false)
	private String description;

	private int occurences;

	@Column(nullable=false)
	private double probability;

	private int ttl;

	//bi-directional many-to-one association to Outcome
	@OneToMany(mappedBy="threat")
	private List<Outcome> outcomes;

	public Threat() {
	}

	public String getThreatId() {
		return this.threatId;
	}

	public void setThreatId(String threatId) {
		this.threatId = threatId;
	}

	public int getBadOutcomeCount() {
		return this.badOutcomeCount;
	}

	public void setBadOutcomeCount(int badOutcomeCount) {
		this.badOutcomeCount = badOutcomeCount;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOccurences() {
		return this.occurences;
	}

	public void setOccurences(int occurences) {
		this.occurences = occurences;
	}

	public double getProbability() {
		return this.probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public int getTtl() {
		return this.ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public List<Outcome> getOutcomes() {
		return this.outcomes;
	}

	public void setOutcomes(List<Outcome> outcomes) {
		this.outcomes = outcomes;
	}

	public Outcome addOutcome(Outcome outcome) {
		getOutcomes().add(outcome);
		outcome.setThreat(this);

		return outcome;
	}

	public Outcome removeOutcome(Outcome outcome) {
		getOutcomes().remove(outcome);
		outcome.setThreat(null);

		return outcome;
	}

}