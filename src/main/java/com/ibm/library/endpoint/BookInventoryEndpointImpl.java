package com.ibm.library.endpoint;

import java.util.Collection;
import java.time.Duration;
import java.util.Arrays;
import com.ibm.library.model.BookData;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

import org.springframework.web.reactive.function.client.WebClient;



@Component
public class BookInventoryEndpointImpl implements BookInventoryEndpoint {
	
	private final Logger logger = LoggerFactory.getLogger(BookInventoryEndpointImpl.class);
	
	// Notice that we're not using @Autowired
	// Spring creates a bean for RestTemplate in one of its jars ... we're not going to use it.
	// We could also create a bean for RestTemplate by using a class w/ @Configuration & method w/ @Bean
	//private RestTemplate restTemplate = new RestTemplate();
	
	//Used @Autowired to support Jaegar
	@Autowired
	private RestTemplate restTemplate;
	
	private WebClient webClient;
	
	//By default, RestTemplate has infinite timeouts. But we can change this behavior by using 
	//RestTemplateBuilder class for setting the connection and read timeouts:
	public BookInventoryEndpointImpl(RestTemplateBuilder restTemplateBuilder, WebClient.Builder webClientBuilder) {
        // set connection and read timeouts
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(500))
                .setReadTimeout(Duration.ofSeconds(500))
                .build();
        
        this.webClient = webClientBuilder.baseUrl("http://").build();
	
    }
	
		
	//Structure coaching
	
	@Value("${bookinventory.endpoint}")
	private String bookInventoryEndpoint;
	
	@Override
	//@HystrixCommand(fallbackMethod = "getBookfallBack", commandKey = "endpointGetBook", commandProperties = {
	//@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
	//@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
	//@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
	//@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75") })
		
	@HystrixCommand(fallbackMethod = "getBookfallBack", commandKey = "endpointGetBook", threadPoolKey = "prim", threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "30")})
	public BookData getBook(String isbn) {
		
		if (logger.isInfoEnabled()) {
		  logger.info("Entered BookInventoryEndpointImpl.getBook() isbn=[" + isbn + "]");	
		}
		String bookInventoryRESTRequestURL = "http://" + bookInventoryEndpoint + "/bookinventory/book/" + isbn;
		BookData book = this.restTemplate.getForObject(bookInventoryRESTRequestURL, BookData.class);
		logger.info("Exited BookInventoryEndpointImpl.getBook() isbn=[" + isbn + "]");	
		return book;
	}
	
	//If you've set up the read timeout for a REST API call to be 3000 milliseconds, then you should set execution.isolation.thread.timeoutInMilliseconds to something like 4000 milliseconds
	public BookData getBookfallBack(String isbn) {
		logger.info("Entered BookInventoryEndpointImpl.getBookfallBack() isbn=[" + isbn + "]");	
		BookData book = new BookData("FICTION", "12345", "Satyendra", "Some Author, getBook_fallBack.");
		return book;
    }
	
	@Override
	public BookData getBookWebClient(String isbn) {
		BookData book= webClient.get().uri("http://" +bookInventoryEndpoint+"/bookinventory/book/{isbn}" , isbn)
				.retrieve().bodyToMono(BookData.class).block();
		return book;
	}

	@Override
	public Collection<BookData> getBooks() {
		Collection<BookData> books = null;
		String bookInventoryRESTRequestURL = "http://" + bookInventoryEndpoint + "/bookinventory/books";
		BookData[] bookDataArray = this.restTemplate.getForObject(bookInventoryRESTRequestURL, BookData[].class);
		if ((bookDataArray != null) && (bookDataArray.length != 0)) { 
			books = Arrays.asList(bookDataArray); 
		}
		return books;
	}
	
	
	 //http://localhost:9002/bookinventory/books/generic?title=SpringBoot&bookType=FICTION&page=1&size=10
		//bookType, isbn, title, author
	
	public Collection<BookData> findBooksByProperties(String bookType, String isbn, String title, String author, int page, int size) {
		Collection<BookData> books = null;

		StringBuilder sb = new StringBuilder("http://");
		sb.append(bookInventoryEndpoint);
		sb.append("/bookinventory/books/generic?");
		sb.append("page=");
		sb.append(page);
		sb.append("&size=");
		sb.append(size);
		
		if (bookType != null && !bookType.isEmpty()) {
			sb.append("&bookType=");
			sb.append(bookType);
		}
		
		if (isbn != null && !isbn.isEmpty()) {
			sb.append("&isbn=");
			sb.append(isbn);
		}
		
		if (title != null && !title.isEmpty()) {
			sb.append("&title=");
			sb.append(title);
		}
		
		if (author != null && !author.isEmpty()) {
			sb.append("&author=");
			sb.append(author);
		}
		
		logger.info("Entered BookInventoryEndpointImpl backend url {} ",sb.toString());
		
		BookData[] bookDataArray = this.restTemplate.getForObject(sb.toString(), BookData[].class);
		
		logger.info("Entered BookInventoryEndpointImpl count {} ",bookDataArray.length);
		
		if ((bookDataArray != null) && (bookDataArray.length != 0)) { 
			books = Arrays.asList(bookDataArray); 
		}
		return books;
	}

	
	
	@Override
    public ResponseEntity<?> createBook(BookData book) {
		logger.info("Entered BookInventoryEndpointImpl : createBook");
		String bookInventoryRESTRequestURL = "http://" + bookInventoryEndpoint + "/bookinventory/book";
		ResponseEntity<?> bookreponse = this.restTemplate.postForEntity(bookInventoryRESTRequestURL, book, BookData.class);
		return bookreponse;
	}
	
	@Override
    public void updateOneBookData(BookData book) {
		String bookInventoryRESTRequestURL = "http://" + bookInventoryEndpoint + "/bookinventory/book";
		this.restTemplate.put(bookInventoryRESTRequestURL, book);
	}
	
	@Override
    public void deleteBook(String isbn) {
		String bookInventoryRESTRequestURL = "http://" + bookInventoryEndpoint + "/bookinventory/book/" + isbn;
		this.restTemplate.delete(bookInventoryRESTRequestURL);
	}
	
	
	@Override
	public BookData sayHello() {
		String bookInventoryRESTRequestURL = "http://" + bookInventoryEndpoint + "/bookinventory/hello";
		BookData book = this.restTemplate.getForObject(bookInventoryRESTRequestURL, BookData.class);
		return book;
	}
}
