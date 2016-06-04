# Security
mvn clean compile -Djetty.port=8082 jetty:run

mvn clean compile -Dspring.profiles.active=test-derby -Dtest=TypeTaskTest test

-XX:MaxPermSize=512M


mvn clean compile package -Dmaven.test.skip=true -P deploy-k-local
