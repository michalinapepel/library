package domain;

import domain.base.AbstractPerson;

public class Author extends AbstractPerson {
	private String pseudonym;
	private String nationality;
    //moze napisane przez niego ksiazki w tablicy?
	
	public Author() {};
	public Author(Integer id, String firstName, String lastName, String pseudonym, String nationality) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.pseudonym = pseudonym;
		this.nationality = nationality;
	};
	
	public String getPseudonym() {
		return pseudonym;
	}
	public void setPseudonym(String pseudonym) {
		this.pseudonym = pseudonym;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
}
