This solution is based on MultiValuedMap.

Build the Project:
mvn package

Run application by:
java -cp target/Audience-2.1.jar AudienceApp {statementPath}
e.g java -cp target/Audience-2.1.jar AudienceApp src\main\resources\example2.txt
where {statementPath} is an optional parameter which should contain Path to the input statements.

If {statementPath} is not passed then application will generate example file with 5 millions statements.
the results will be saved into output.txt file.