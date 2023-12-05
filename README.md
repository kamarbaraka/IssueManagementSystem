 # major changes

- Optimized the ticket assignment process and Api.
- Improved the service logging.
- Optimized the ticket feedback process and Api.
- added an Api to get employees currently not in a department.
- changed the feedback api to accept an attachment.
- Optimized the sorting.
- changed the ticket submission Api to require a written solution for the ticket.
- changed the ticket referral Api to require a reason for referring the ticket.

# Run
To run the application, execute the command;

`java -jar <jar filename> --spring.mail.username=<your email server username> --spring.mail.password=<your mail server password> --spring.datasource.url=<your database url> --spring.datasource.pusername=<your database username> --spring.datasource.password=<your database password>`
