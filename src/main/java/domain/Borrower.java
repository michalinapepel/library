package domain;

import domain.base.AbstractPerson;

public class Borrower extends AbstractPerson {
    private String addressCity;
    private String addressStreet;
    private Byte addressNumber;
    private String addressZip;
    private int cardNumber; //numer karty bibliotecznej albo legimki jak on chcial
}
