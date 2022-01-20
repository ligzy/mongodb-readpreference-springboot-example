package com.soundai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.soundai.repository.Customer;
import com.soundai.repository.primary.PrimaryCustomerRepository;
import com.soundai.repository.secondary.SecondaryCustomerRepository;
import com.soundai.repository.standalone.CustomerRepository;

@SpringBootApplication
public class PrimarySecondaryApplication implements CommandLineRunner {
	@Autowired
	private CustomerRepository repository;
	@Autowired
	private PrimaryCustomerRepository primaryRepository;
	@Autowired
	private SecondaryCustomerRepository secondRepository;

	public static void main(String[] args) {
		SpringApplication.run(PrimarySecondaryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();
		System.out.println("Deleted existing data");
		System.out.println("-------------------------------");

		// save a couple of customers
//
	   new Thread(new Runnable() {

			@Override
			public void run() {
				for(int i=0;i<1000;i++)
				{
					repository.save(new Customer("Alice "+i, "Smith"));
					repository.save(new Customer("Bob "+ i, "Smith"));
				}
			}			   
		   }).start();	   
		
		// fetch all customers
		System.out.println("Customers found with findAll():");
//		System.out.println("-------------------------------");
//		for (Customer customer : repository.findAll()) {
//			System.out.println(customer);
//		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int i = 0;
					while (i++ < 100) {
						long total = repository.count();
						System.out.println("total:" + total);
						Thread.currentThread().sleep(100);
					}
				} catch (InterruptedException e) {
 					e.printStackTrace();
				}
			}
			
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int i = 0;
					while (i++ < 100) {
						long total = primaryRepository.count();
						System.out.println("total primaryRepository:" + total);
						Thread.currentThread().sleep(100);
					}
				} catch (InterruptedException e) {
 					e.printStackTrace();
				}
			}
			
		}).start();		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int i = 0;
					while (i++ < 100) {
						long total = secondRepository.count();
						System.out.println("total  secondRepository:" + total);
						Thread.currentThread().sleep(100);
					}
				} catch (InterruptedException e) {
 					e.printStackTrace();
				}
			}
			
		}).start();

//		// fetch an individual customer
//		System.out.println("Customer found with findByFirstName('Alice'):");
//		System.out.println("--------------------------------");
//		System.out.println(repository.findByFirstName("Alice"));
//
//		System.out.println("Customers found with findByLastName('Smith'):");
//		System.out.println("--------------------------------");
//		for (Customer customer : repository.findByLastName("Smith")) {
//			System.out.println(customer);
//		}

	}

}
