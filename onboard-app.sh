mvn clean compile assembly:single
java -jar target/app*.jar $1
go run TfcClient.go $1