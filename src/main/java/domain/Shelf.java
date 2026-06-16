package domain;

import domain.base.AbstractLocation;

//klasa definiująca PÓŁKĘ (pojedynczy blat w większym regale)
public class Shelf extends AbstractLocation {
	public Shelf() {};
	public Shelf(Integer id, Integer bookcaseId, String name) {
		super.init(id, bookcaseId, name);
	};
}
