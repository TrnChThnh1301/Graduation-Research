## Use Case 1: RENT BIKE

### Basic flow:

1. The user authenticates himself/herself to use the software

2. Call _View Dock_ use case.

3. The user needs to use the feature for bike renting in the EcoBikeRental app to scan the barcode on the lock.

4. The software scans and decodes the barcode on the lock.

5. Call _View Bike_ use case.

6. The information of the bike is shown (e.g., license plate, current battery percentag e of electric bicycle, etc.).

7. Deposit fee calculated by the software, the user get a notification of choosing payment method.

8. The user chooses a payment method and confirms to make deposit.

9. The software connects to the Interbank and asks to deduct the deposit amount.

10. The Interbank proceeds the transaction.

11. The transaction recognized and saved in the software.

12. The lock automatically opens, allowing the user to use the bike.

13. The software starts time recording.

### Alternate flow(s):

- At step 1: Invalid username/password. The user have to enter credencials again.

- At step 4: Invalid (unrecognized or currently in used) barcode. The user gets a notification to scan the barcode again. Continuing Step 3.

- At step 10: Transaction timed out. The user gets a notification. The use case then resume at step 9.

- At step 10: Balance not sufficient. The user gets a notification. The use case then resume at step 9.
