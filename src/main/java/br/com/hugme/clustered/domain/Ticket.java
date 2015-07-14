package br.com.hugme.clustered.domain;

import java.io.Serializable;

public class Ticket implements Serializable {

	private static final long serialVersionUID = -4412438564542273603L;

	private Long id;
	private String description;

	public Ticket() {
		super();
	}
	
	public Ticket(Long id, String description) {
		super();
		this.id = id;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", description=" + description + "]";
	}

}
