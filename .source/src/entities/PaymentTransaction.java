package entities;

import java.util.Calendar;
import java.util.Date;

import exceptions.ecobike.InvalidEcoBikeInformationException;
import exceptions.interbank.InvalidCardException;
import utils.FunctionalUtils;

public class PaymentTransaction {
	private String transactionId;
	
	private String creditCardNumber;

	private double amount;

	private Date transactionTime;

	private String content;

	private String errorMessage;
	
	private int rentID;

	public PaymentTransaction() {}

	public PaymentTransaction(int transactionId, String creditCardNumber, double amount, String content) throws InvalidEcoBikeInformationException {
		this.setAmount(amount);
		this.setTransactionId(transactionId);
		this.setCreditCardNumber(creditCardNumber);
		this.setContent(content);
		this.setTransactionTime("");
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = Integer.toString(transactionId);
	}

	public String getCreditCardNumber() {
		return this.creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		if (creditCardNumber == null || creditCardNumber.length() == 0) {
			throw new InvalidCardException("NULL_CARD");
		}
		
		if (FunctionalUtils.contains(creditCardNumber, "^[0-9 ]")) {
			throw new InvalidCardException("CARD NUMBER MUST NOT CONTAINS LETTERS OR SPECIAL CHARACTER");
		}

		this.creditCardNumber = creditCardNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) throws InvalidEcoBikeInformationException {
		try {			
			if (transactionTime == null || transactionTime.length() == 0) {
				this.transactionTime = FunctionalUtils.stringToDate(Calendar.getInstance().getTime().toString());			
			} else {
				this.transactionTime = FunctionalUtils.stringToDate(transactionTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidEcoBikeInformationException("INVALID TIME FORMAT");
		}
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public void setRentID(int rentID) {
		this.rentID = rentID;
	}
	
	public int getRentID() { 
		return this.rentID;
	}
	
}