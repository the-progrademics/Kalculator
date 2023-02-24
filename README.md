coverage
----------------------
![TEST_COVERAGE](https://user-images.githubusercontent.com/120630403/221122934-7ccf1e24-0684-4f36-b6f5-bae0676edd59.png)

Database prerequisets to running the program or tests:

table name : cmeals
-------------------------
	columns: 
		entryid serial primary_key
		username character_varying 150
		foodname character_varying 150
		cals numeric 5

table name : mealseaten
-------------------------
	columns: 
		entryid serial primary_key
		username character_varying 150
		foodname character_varying 150
		cals numeric 5
		date date

table name : signinup
-------------------------
	columns: 
		username character_varying 150 primary_key
		password character_varying 150
