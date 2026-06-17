package domain.base;

    /**
     * Klasa reprezentująca ogólną lokalizację w bibliotece.
     * Może być używana jako klasa bazowa dla bardziej konkretnych typów lokalizacji,
     * takich jak regały czy półki. Zawiera podstawowe pola wspólne dla lokalizacji.
     */
    abstract public class AbstractLocation {
        /**
         * Nazwa lokalizacji (np. nazwa regału lub półki).
         * Może być {@code null}, jeśli nieustawiona.
         */
        protected String name;

        /**
         * Unikalny identyfikator lokalizacji.
         * Służy do odróżniania obiektów w kolekcjach/ bazie danych.
         */
        protected int id;

        protected int bookcaseId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getBookcaseId() {
            return bookcaseId;
        }

        public void setBookcaseId(int bookcaseId) {
            this.bookcaseId = bookcaseId;
        }

        public void init(Integer id, Integer bookcaseId, String name) {
            this.id = id;
            this.bookcaseId = bookcaseId;
            this.name = name;

        }

        public void init(Integer id, String name) {
            this.id = id;
            this.name = name;

        }
    }
