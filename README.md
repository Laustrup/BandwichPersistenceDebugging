# BandwichPersistenceDebugging

### This is a project for further debugging the persistence project of bandwich, while it is being judged. Meaning there will be pushed commits here between the 6th and the 16th of january.

The original project can be found at [BandwichPersistence](https://github.com/Laustrup/BandwichPersistence)

## Changes made from original:

* The controller has got an endpoint directory field.
* Dependencies have changed to Spring Boot starter dependencies to make it able to compile.
* LEFT JOIN SELECTS for if users doesn't have anything in table.
* Extra test methods added.
* Fixed a stackoverflow error of continuing reads of relation fields.
* JDBC connection URL has ?allowMultiQueries=true added.
* DTO objects have been created for better converting to and from application.
* Unique keys for passwords of either username or email added.