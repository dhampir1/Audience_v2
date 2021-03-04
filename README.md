This solution is based on MultiValuedMap.

Build the Project:
mvn package

Run application by:
java -cp target/Audience-2.0.jar AudienceApp {statementPath}
where {statementPath} is an optional parameter which should contain Path to the input statements.

the results will be saved into output.txt file.