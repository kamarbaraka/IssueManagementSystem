 # major changes
 
- added an Api to get employees currently not in a department.
- changed the feedback api to accept an attachment.
- changed the ticket submission Api to require a written solution for the ticket.
- changed the ticket referral Api to require a reason for referring the ticket.

# Fixes

- Fixed an issue where the ticket solution is not reelecting when getting the ticket.
- Fixed an issue where an employee can be assigned a ticket more than once.
- Fixed an issue where an employee can refer a ticket to himself.

# Improvements

- Optimized the ticket assignment process and Api.
- Improved the service logging.
- Optimized the sorting.
- Optimized the ticket feedback process and Api.
- Made a ticket solution an independent entity.

# Requirements

- Java 21
- MySQL 8

# Run
To run the application, execute the command;

`java -jar <jar filename> --spring.mail.username=<your email server username> --spring.mail.password=<your mail server password> --spring.datasource.url=<your database url> --spring.datasource.username=<your database username> --spring.datasource.password=<your database password> --app.company.name=<your company name> --app.company.end-tag=<custom end word to be placed after an email> --app.init.db
-username=<your database username> --app.init.db-password=<your database password>`
