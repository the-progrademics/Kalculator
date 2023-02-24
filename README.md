coverage
----------------------
	https://github.com/the-progrademics/Kalculator/main/TEST_COVERAGE.PNG?raw=true)

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
