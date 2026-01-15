package entities;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import exceptions.interbank.InvalidCardException;
import views.screen.popup.ToastContainer;

public class CreditCard {
	private String cardHolderName;

	private String cardNumber;
	
	private String cardSecurity;

	private double balance;

	private java.util.Date expirationDate;
	
	public CreditCard(String cardHolderName, String cardNumber, String cardSecurity, String expirationDate) throws IOException {
		super();
		this.setCardHolderName(cardHolderName);
		this.setCardNumber(cardNumber);
		this.setCardSecurity(cardSecurity);
		this.setExpirationDate(expirationDate);
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	
	public void setCardNumber(String cardNumber) throws InvalidCardException {
		if (cardNumber == null || cardNumber.length() == 0) {
			throw new InvalidCardException("Card number must not be null");
		}
				
		this.cardNumber = cardNumber;
	}
	
	public String getCardHolderName() {
		return cardHolderName;
	}
	
	public void setCardHolderName(String cardHolderName) throws IOException {
		if (cardHolderName == null || cardHolderName.length() == 0) {
			ToastContainer.error("INVALID_CARD_NAME!");
		}
		
		this.cardHolderName = cardHolderName;
	}
	
	public String getExpirationDate() {
		DateFormat dateFormatter = new SimpleDateFormat("mm/yy");
		return dateFormatter.format(expirationDate);
	}
	
	public void setExpirationDate(String expirationDate) {
		try {
			this.expirationDate = new SimpleDateFormat("mm/yy").parse(expirationDate);
		} catch (Exception e) {
			throw new InvalidCardException("Invalid expiration date");
		}
	}
	
	public String getCardSecurity() {
		return cardSecurity;
	}
	
	public void setCardSecurity(String cardSecurity) {
		this.cardSecurity = cardSecurity;
	}
	
	public double getBalance() {
		return balance;
	}
}
