package com.example.demo.validator;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import com.example.demo.Exception.CustomRequiredException;
import com.example.demo.Exception.LengthException;
import com.example.demo.dto.User;

@Component
public class AccDataValidator {

	public User validateAccountData(User account) throws CustomRequiredException, LengthException {
		account.setIsserMemberId(requiredValidate(account.getIsserMemberId() + ""));
		account.setOldPan(panValidator(account.getOldPan()));
		account.setEffectiveDate(effectiveDate(account.getEffectiveDate()));

		switch (account.getReasonCode()) {
		case "A": {
			account.setNewPan(newPanValidator(account.getNewPan(), 'A'));
			account.setOldExpirationDate(oldExpDatevalidator(account.getOldExpirationDate(), 'A'));
			account.setNewExpirationDate(newExpDatevalidator(account.getNewExpirationDate(), 'A'));
			break;
		}
		case "E": {
			account.setNewPan(newPanValidator(account.getNewPan(), 'E'));
			account.setOldExpirationDate(oldExpDatevalidator(account.getOldExpirationDate(), 'E'));
			account.setNewExpirationDate(newExpDatevalidator(account.getNewExpirationDate(), 'E'));
			break;
		}
		case "C": {
			account.setNewPan(newPanValidator(account.getNewPan(), 'C'));
			account.setOldExpirationDate(oldExpDatevalidator(account.getOldExpirationDate(), 'C'));
			account.setNewExpirationDate(newExpDatevalidator(account.getNewExpirationDate(), 'C'));
			break;
		}
		case "O": {
			account.setNewPan(newPanValidator(account.getNewPan(), 'O'));
			account.setOldExpirationDate(oldExpDatevalidator(account.getOldExpirationDate(), 'O'));
			account.setNewExpirationDate(newExpDatevalidator(account.getNewExpirationDate(), 'O'));
			break;
		}
		case "B": {
			account.setNewPan(newPanValidator(account.getNewPan(), 'B'));
			account.setOldExpirationDate(oldExpDatevalidator(account.getOldExpirationDate(), 'B'));
			account.setNewExpirationDate(newExpDatevalidator(account.getNewExpirationDate(), 'B'));
			break;
		}
		case "Q": {
			account.setNewPan(newPanValidator(account.getNewPan(), 'Q'));
			account.setOldExpirationDate(oldExpDatevalidator(account.getOldExpirationDate(), 'Q'));
			account.setNewExpirationDate(newExpDatevalidator(account.getNewExpirationDate(), 'Q'));
			break;
		}
		default:
			System.out.println("=============================Everything is fineeeeeeeeee============================");
			break;
		}
		return account;
	}

	private String effectiveDate(String effectiveDate) throws CustomRequiredException, LengthException {
		// TODO Auto-generated method stub
		if (effectiveDate != null && !effectiveDate.isEmpty()) {
			if (effectiveDate.length() != 8) {
				throw new LengthException("effectiveDate Length should be 8");
			} else {
				System.out.println("effectivedate id fine");
			}

		} else {
			throw new CustomRequiredException("Effective date Field is mandatory ");
		}
		return effectiveDate;
	}

	private int requiredValidate(String issuerMemberId) throws CustomRequiredException, LengthException {

		if (issuerMemberId != null && !issuerMemberId.isEmpty()) {
			System.out.println("User Id is not null");
			if (issuerMemberId.length() != 7) {
				throw new LengthException("Issuer Member Length should be 7");

			} else {
				System.out.println("Issuememberid is fine");
			}
		} else {
			throw new CustomRequiredException("Issue Member Id Field is mandatory ");
		}

		return Integer.parseInt(issuerMemberId);
	}

	private String panValidator(String oldPan) throws CustomRequiredException, LengthException {
		if (oldPan != null && !oldPan.isEmpty()) {
			if (oldPan.length() != 19) {
				throw new LengthException("Old Pan Length should be 19");
			}
			/*
			 * if(oldPan.length() == 14) { oldPan=StringUtils.rightPad(oldPan, 19," "); }
			 */
			else {
				System.out.println("oldpan is fine");
			}
		} else {
			throw new CustomRequiredException("Old Pan Field is mandatory ");
		}

		return oldPan;
	}

	private String newPanValidator(String newPan, char cas) throws LengthException, CustomRequiredException {
		List<Character> condCases = Arrays.asList('O', 'B', 'Q');
		List<Character> omittedCases = Arrays.asList('E', 'C');
		if (condCases.contains(cas)) {
			if (newPan != null && !newPan.isEmpty()) {
				if (newPan.length() != 19) {
					throw new LengthException("New Pan Length should be 19");
				} else {
					System.out.println("NewPan is fine");
				}
				/*
				 * if(newPan.length() == 14) { newPan=StringUtils.rightPad(newPan, 19," "); }
				 */

			} else {
				//newPan = StringUtils.rightPad(newPan, 19, " ");
				System.out.println("No change in new Pan and it is conditional");
			}
		}
		if (omittedCases.contains(cas)) {
			/*
			 * if(newPan != null && newPan.length() != 19) { if(newPan.length() == 14) {
			 * newPan=StringUtils.rightPad(newPan, 19," "); } else {
			 * System.out.println("No spaces required"); }
			 * 
			 * }
			 */
			/* else { */
			//newPan = StringUtils.rightPad(newPan, 19, " ");
			System.out.println("NewPan is omitted ");
			/* } */
		}
		/*
		 * else { if(newPan != null) { if(newPan != null && newPan.length() != 19 &&
		 * !newPan.isEmpty()) {
		 * 
		 * if(newPan.length() == 14) { newPan=StringUtils.rightPad(newPan, 19," "); }
		 * else { System.out.println("No spaces required"); }
		 * 
		 * throw new LengthException("New Pan Length should be 19");
		 * 
		 * } } else { throw new CustomRequiredException("new Pan Field is mandatory ");
		 * } }
		 */
		else {
			if(newPan != null && !newPan.isEmpty()) {
				if(newPan.length() != 19) {
					throw new LengthException("New Pan Length should be 19");
				}
				else {
					System.out.println("NewPan is fine");
				}
			}
			else {
				throw new CustomRequiredException("new Pan Field is mandatory ");
			}
		}

		return newPan;

	}

	private String oldExpDatevalidator(String oldExpdate, char cas) throws CustomRequiredException, LengthException {
		List<Character> reqCases = Arrays.asList('A', 'B', 'E', 'O', 'Q');
		// List<Character> condCases=Arrays.asList('O','B','Q');
		List<Character> omittedCases = Arrays.asList('C');
		if (reqCases.contains(cas)) {
			if (oldExpdate != null && oldExpdate.length() != 0 && !oldExpdate.isEmpty()) {
				if(oldExpdate.length() !=4) {
					throw new LengthException("Oldexpdate Length should be 4");
				}
				else {
					System.out.println("oldExpdate is fine");
				}
				
			} else {
				throw new CustomRequiredException("Old exp date Field is mandatory ");
			}
		} else if (omittedCases.contains(cas)) {
			System.out.println("OldExpdate is omitted ");
			/*
			 * if(oldExpdate!=null && oldExpdate.length()!=0) {
			 * System.out.println("No change in oldExpdate"); } else {
			 */
			//oldExpdate = StringUtils.rightPad(oldExpdate, 4, " ");
			//;
			/* } */
		}
		return oldExpdate;

	}

	private String newExpDatevalidator(String newExpdate, char cas) throws CustomRequiredException, LengthException {
		List<Character> reqCases = Arrays.asList('A', 'B', 'E', 'O', 'Q');
		// List<Character> condCases=Arrays.asList('O','B','Q');
		List<Character> omittedCases = Arrays.asList('C');
		if (reqCases.contains(cas)) {
			if (newExpdate != null && newExpdate.length() != 0 && !newExpdate.isEmpty()) {
				if(newExpdate.length() !=4) {
					throw new LengthException("Newexpdate Length should be 4");
				}
				else {
					System.out.println("NewExpdate is fine");
				}
			} else {
				throw new CustomRequiredException("new exp date Field is mandatory ");
			}
		} else if (omittedCases.contains(cas)) {
			System.out.println("NewExpdate is omitted ");
			/*
			 * if(newExpdate!=null && newExpdate.length()!=0) {
			 * System.out.println("No change in oldExpdate"); } else {
			 */
			//newExpdate = StringUtils.rightPad(newExpdate, 4, " ");
			/* } */
		}
		return newExpdate;

	}

}
