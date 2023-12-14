 # major changes
 
- added an Api to get employees currently not in a department.
- automatic database initialization.
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
To run the application, create a configuration file with the following required properties and provide their values;
- spring.mail.username=(the username of your mail server)
- spring.mail.password=(password of your mail server)
- app.company.name=(name of your company)
- app.company.end-tag=(end statement of all the emails)
- app.init.db-url=(url of your mysql database)
- app.ini.db-username=(username of your MySql database)
- app.ini.db-password=(password of your MySql database)

then execute the command;

`java -jar (jar filename) --spring.config.name=(name of your configuration file) --spring.config.location(path to your configuration file)`
