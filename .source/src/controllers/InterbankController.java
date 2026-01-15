package controllers;

import entities.CreditCard;
import entities.PaymentTransaction;

import java.io.IOException;
import exceptions.ecobike.InvalidEcoBikeInformationException;

public class InterbankController {	
	public InterbankController() {
		super();
	}

	public PaymentTransaction payRental(CreditCard card, double amount, String content) throws IOException, InvalidEcoBikeInformationException {
		PaymentTransaction transaction = new PaymentTransaction(123, "ict_group6_2021", amount, content);
		
		return transaction;
	}
	

	public PaymentTransaction payDeposit(CreditCard card, double amount, String content) throws IOException, InvalidEcoBikeInformationException {
		PaymentTransaction transaction = new PaymentTransaction(123, "ict_group6_2021", amount, content);
		
		return transaction;
	}
	
	public PaymentTransaction refund(CreditCard card, double amount, String content) throws IOException, InvalidEcoBikeInformationException {
		PaymentTransaction transaction = new PaymentTransaction(123, "ict_group6_2021", amount, content);
		
		return transaction;
	}
}