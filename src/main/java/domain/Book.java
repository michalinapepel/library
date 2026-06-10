package domain;

public class Book {
	static private int id;
	static private String title = "";//
	static private Author[] authors = null;
	static private String publisher = "";//
	static private int yearOfPublishing = 0;//
	static private String isbn = "";//
	static private int shelfId;

	// przechowywanie jak???
	public Book() {
	};

	public Book(String title, String publisher, int publicationYear, String isbn, Integer shelfId) {
		this.title = title;
		this.publisher = publisher;
		this.yearOfPublishing = publicationYear;
		this.isbn = isbn;
		this.shelfId = shelfId;
	}

	public Book(int id, String title, String publisher, int publicationYear, String isbn, Integer shelfId) {
		this.id = id;
		this.title = title;
		this.publisher = publisher;
		this.yearOfPublishing = publicationYear;
		this.isbn = isbn;
		this.shelfId = shelfId;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getPublisher() {
		return publisher;
	}

	public int getPublicationYear() {
		return yearOfPublishing;
	}

	public String getIsbn() {
		return isbn;
	}

	public Integer getShelfId() {
		return shelfId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setPublicationYear(int publicationYear) {
		this.yearOfPublishing = publicationYear;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setShelfId(Integer shelfId) {
		this.shelfId = shelfId;
	}

}
