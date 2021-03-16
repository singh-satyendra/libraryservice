package com.ibm.library.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ibm.library.service.LibraryService;

import java.util.Collection;
import java.util.List;

import com.ibm.library.model.BookData;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//http://169.51.194.138:30081/actuator/health
//http://localhost:9001/library/hellolib


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/library")
public class LibraryController
{
	private final Logger logger = LoggerFactory.getLogger(LibraryController.class);
	
	@Autowired
	private LibraryService libraryService;
	
	public LibraryController() {
	}
	
	//Get book by isbn
    @RequestMapping(value = "/book/{isbn}")
	public ResponseEntity<?> getBook(@PathVariable(value="isbn") String isbn) {
    	BookData book = this.libraryService.getBook(isbn);
    	ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.OK);
     	return responseEntity;
	}

    //Get all books 
    @RequestMapping(value = "/books")
	public ResponseEntity<?> getBooks() {
    	Collection<BookData> books = this.libraryService.getBooks();
    	ResponseEntity<Collection<BookData>> responseEntity = new ResponseEntity<Collection<BookData>>(books, HttpStatus.OK);
 		return responseEntity;
	}
    
    
    //Create a new book
    @RequestMapping(value = "/book", method = RequestMethod.POST)
	public ResponseEntity<?> createBook(@RequestBody BookData book) {
    	ResponseEntity<?> responseEntity = this.libraryService.createBook(book);
		//ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.CREATED);
		return responseEntity;
	}
	
    //Update an existing book
	@RequestMapping(value = "/book", method = RequestMethod.PUT)
	public ResponseEntity<?> updateBook(@RequestBody BookData book) {
		this.libraryService.updateOneBookData(book);
		ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.CREATED);
		return responseEntity;
	}
	
	//Delete the book
	@RequestMapping(value = "/book/{isbn}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBook(@PathVariable(value="isbn") String isbn) {
    	this.libraryService.deleteBook(isbn);
    	ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(HttpStatus.NO_CONTENT);
		return responseEntity;
	}
	
	// http://localhost:9001/library/books/generic?title=SpringBoot&bookType=FICTION&isbn=7&author=Author_1&page=0&size=10
	//Generic Search with pagination 
	@RequestMapping(value = "/books/generic", method = RequestMethod.GET) 
	public ResponseEntity<?> getBooksByProperties(@RequestParam(required = false) String bookType,
			@RequestParam(required = false) String isbn, @RequestParam(required = false) String title,
			@RequestParam(required = false) String author, @RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "3") int size) {
			
		Collection<BookData> books = this.libraryService.findBooksByProperties(bookType, isbn, title, author, page, size);
		ResponseEntity<Collection<BookData>> responseEntity = null;
		if (null == books) {
			logger.info("Entered LibraryController no data.");
			responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND); 
		}
		else {
			responseEntity = new ResponseEntity<Collection<BookData>>(books, HttpStatus.OK); 
		}
			return responseEntity;
	}	
	
    
    //This is Dummy method to test the Library Service only
    @RequestMapping(value = "/hellolib")
   	public ResponseEntity<?> sayLibraryHello() {
       	//logger.info("Entered LibraryController.sayLibraryHello()");
        BookData book = new BookData("FICTION", "12345", "Satyendra", "Some Author, returns from Library Service.");
       	ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.OK);
       	//logger.info("Leaving LibraryController.sayHello()");
   		return responseEntity;
   	}
    
    
    //This is Dummy method to test the Book Inventory Service. Corresponding Book Inventory API will return the hard coded value.
    //It will be used to end to end integration testing purpose only.
    @RequestMapping(value = "/hellobook")
   	public ResponseEntity<?> sayHello() {
       	//logger.info("Entered LibraryController.sayHello()");
       	BookData book = this.libraryService.sayHello();
       	ResponseEntity<BookData> responseEntity = new ResponseEntity<BookData>(book, HttpStatus.OK);
       	//logger.info("Leaving LibraryController.sayHello()");
   		return responseEntity;
   	}
    
    @RequestMapping(value = "/demo")
 	public ResponseEntity<?> demo() {
     	logger.info("Entered DemoProjectController.demo()");
     	String demoData = this.libraryService.demo();
     	ResponseEntity<String> responseEntity = new ResponseEntity<String>(demoData, HttpStatus.OK); 
     	logger.info("Leaving DemoProjectController.demo()");
     	return responseEntity;

     }
}
