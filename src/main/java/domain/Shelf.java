package domain;

import domain.base.AbstractLocation;

/**
 * Reprezentuje pojedynczą półkę (blat) wewnątrz regału.
 * Dziedziczy po {@code AbstractLocation} i może być używana do przypisania
 * książek do konkretnej pozycji w regale.
 */
public class Shelf extends AbstractLocation {
	private String bookcaseName;

	public Shelf() {};
	public Shelf(Integer id, Integer bookcaseId, String name) {
		super.init(id, bookcaseId, name);
	};

	public String getBookcaseName() {
		return bookcaseName;
	}

	public void setBookcaseName(String name) {
		this.bookcaseName = name;
	}
}
