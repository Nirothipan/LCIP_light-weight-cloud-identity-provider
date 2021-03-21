# key-generator

CS5242 group project

#### cURL for custom License key

```shell script
curl POST http://localhost:9090/keygenerator/key -H 'authorization: Basic YWJjOmFiYw=='  -H 'content-type: application/json' \
      --data '{
                "username" : "user@gmail.com", 
                "creator_username" : "user@gmail.com", 
                "apis" : ["api1", "api2"], 
                "expiry-date" : "2020-11-11 00:00:00"}' \
      -v
```
