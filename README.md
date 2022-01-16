# airline_api_spring
airline REST API


## Prerequisite Dependencies
* install elasticsearch docker image and run locally
```shell
docker run -d --name elasticsearch -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2
```



# TODO
* swagger
* API Endpoint
* resilient4j

## Bonus
* spring security
* metric
* deployment to aws
 
## REST API
/id
/passengenr_name
/airline_id



## questions 
* search depends on the external API is not possible since the external API
would only provide the pagination not the live search. if very app api needs to
go to the external API and get all the data and the do search in memory which
would be very slow
the way could be used is to use the external API for the initial data ingestion
into some kind of data store, with indexing and all for later search. but with every time
the application start then it would take some time to ingest the data which would takes
some time is that allowed?
* even with the method mentioned above the result that returned by the external API
could change if you query next time that means that the initial ingested data could be
staled, but there is no mentioning that there would be some kind of streaming data that
would push the update the information
* so for the search criteria, if I search client then the application api should
return all the record about the client and if it is by the airline then it should return
all the records about the airline? like a free text search but would return the whole item
as required in the spec?
* there is no limitation about what kind of search we need to perform? as long as it involves
passenger name, number of trips and/or airline name?
