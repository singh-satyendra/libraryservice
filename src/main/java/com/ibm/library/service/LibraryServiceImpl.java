package com.ibm.library.service;

import java.util.Collection;
import com.ibm.library.model.AppProperties;
import com.ibm.library.model.BookData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ibm.library.endpoint.BookInventoryEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
//@RefreshScope
public class LibraryServiceImpl implements LibraryService {
	
	private final Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);
	
	@Autowired
	AppProperties appProperties;
	
	
	//@Value("${demo.message}")
	//private String hellowMessageFromProperty;
	
	@Autowired
	private BookInventoryEndpoint bookInventoryEndpoint;

	public LibraryServiceImpl() {
	}
	
	@Override
	public BookData getBook(String isbn) {
		logger.info("Entered LibraryServiceImpl.getBook().  isbn=" + isbn);
		BookData book = this.bookInventoryEndpoint.getBook(isbn);
		logger.info("Leaving LibraryServiceImpl.getBook().  isbn=" + isbn);
		return book;
	}

	@Override
	public Collection<BookData> getBooks() {
		logger.info("Entered LibraryServiceImpl.getBooks()");
		Collection<BookData> books = this.bookInventoryEndpoint.getBooks();
		logger.info("Leaving LibraryServiceImpl.getBooks()");
		return books;
	}
	
	@Override
	public ResponseEntity<?> createBook(BookData book) {
		ResponseEntity<?> bookres = this.bookInventoryEndpoint.createBook(book);
		return bookres;
	}
	
	@Override
	public void updateOneBookData(BookData book) {
		this.bookInventoryEndpoint.updateOneBookData(book);
	}

	@Override
	public void deleteBook(String isbn) {
		this.bookInventoryEndpoint.deleteBook(isbn);
	}
	
	@Override
	public Collection<BookData> findBooksByProperties(String bookType, String isbn, String title, String author, int pageNo, int pageSize) {
		return this.bookInventoryEndpoint.findBooksByProperties(bookType, isbn, title, author, pageNo, pageSize);
	}
	
	
	@Override
	public BookData sayHello() {
		logger.info("Entered LibraryServiceImpl.sayHello()");
		BookData book = this.bookInventoryEndpoint.sayHello();
		logger.info("Leaving LibraryServiceImpl.sayHello()");
		return book;
	}
	

	@Override
	public String demo() {
		logger.info("Entered LibraryServiceImpl.demo().  demoPropertySomeProperty {} ",appProperties.getMessage());
		String demoData = "This is a demo message from property." + appProperties.getMessage() +" : "+appProperties.getName();
		logger.info("Leaving LibraryServiceImpl.demo().  demoData=" + demoData);
		return demoData;
	}
}
