## Use Case 2: RETURN BIKE

### Basic flow:

1. Customer returns the bike to an empty docking point.

2. The locker closes the lock.

3. Locker scans the code of the bike.

4. Locker sends signals to the software.

5. Rental fee calculated by the software.

6. Software asks interbank to deduct rental fee.

7. Interbank proceeds the transaction.

8. The transaction recognized and saved in the software.

9. Success notification shown.

### Alternate flow(s):

- At step 3: Invalid (unrecognized) barcode. The user gets a notification to scan the barcode again. Continuing Step 3.

- At step 5: The rental fee is greater than the deposit money, the software asks interbank to deduct money from the bank balance. The use case then resume at step 7.

- At step 7: Timed out. The use case then resume at step 3.
