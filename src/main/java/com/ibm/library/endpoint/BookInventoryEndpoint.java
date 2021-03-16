package com.ibm.library.endpoint;

import java.util.Collection;
import org.springframework.http.ResponseEntity;
import com.ibm.library.model.BookData;

public interface BookInventoryEndpoint {

	Collection<BookData> getBooks();
	BookData getBook(String isbn);
	BookData sayHello();
	
	BookData getBookWebClient(String isbn);
	
	ResponseEntity<?> createBook(BookData book);
	void deleteBook(String isbn);
	void updateOneBookData(BookData bookData);
	Collection<BookData> findBooksByProperties(String bookType, String isbn, String title, String author, int page, int size);
}
