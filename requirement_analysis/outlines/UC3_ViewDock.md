## Use Case 3: VIEW DOCK

### Basic flow:

1. Software provides a list of random docks on the screen and also a search box to find a specific dock.

2. Customer selects a dock from the list provided by the software.

3. Software gets the dock’s detailed information from database.

4. Software shows detailed information of the selected dock.

### Alternative flows:

- At step 2: If the customer types in the search box, the software updated the list of docks displayed. Resume at step 3.

- At step 3: The database doesn't have information about the dock selected, the software will notify that there's no information about that dock. The use case then resume at step 1.
