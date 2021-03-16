package com.ibm.library.service;

import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ibm.library.model.BookData;

public interface LibraryService {
	String demo();
	
	Collection<BookData> getBooks();
	BookData getBook(String isbn);
	BookData sayHello();
	
	ResponseEntity<?> createBook(BookData book);
	void deleteBook(String isbn);
	void updateOneBookData(BookData bookData);
	
	//Methods to be used for generic search with pagination 
	Collection<BookData> findBooksByProperties(String bookType, String isbn, String title, String author, int pageNo, int pageSize);

}
