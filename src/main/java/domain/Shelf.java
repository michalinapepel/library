package domain;

import domain.base.AbstractLocation;

/**
 * Reprezentuje pojedynczą półkę (blat) wewnątrz regału.
 * Dziedziczy po {@code AbstractLocation} i może być używana do przypisania
 * książek do konkretnej pozycji w regale.
 */
public class Shelf extends AbstractLocation {
	public Shelf() {};
	public Shelf(Integer id, Integer bookcaseId, String name) {
		super.init(id, bookcaseId, name);
	};
}
