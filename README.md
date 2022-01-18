## Assumption

* The assumption is that the external API to get the passenger data would not change and would be seen as a source to
  provide the data for this application. I will address how to handle changing data in the Design Thinking section

## Set up

* Since on the backend, we use the elastic search to store and search data we need to spin up an elastic search
  container:

```shell 
docker run -d --name elasticsearch -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.16.3
```

* Run the spring boot application

```shell
mvn spring-boot:run
```

* When the spring application starts it would start the data ingestion, by fetching the data from the external API
  provided and store the data into the elasticsearch container, and it would show on the spring log message as below
  which means that the data ingestion is done and we are able to run the subsequent query API

> ***************** Passenger data ingestion completed! *********************

## API Documentation

* Swagger UI would is available via: http://localhost:8080/swagger-ui/index.html
* API documentation is available via: http://localhost:8080/v3/api-docs/

## Design Thinking

### Data Base Choice

* In our application we used ElasticSearch as the persistent layer to store and retrieve data, there are couple reasons
  for this
    * **Search Flexibility**: The main function of this application is to provide api for client to do flexible search,
      and elastic could provide every flexible search options, comparing to relational database
    * **Performance consideration**: if the application's main function is to search client's record then elastic search
      would best suits its purpose in this case for its faster performance.

### API Endpoint

* We have some static endpoints like ```/passenger_id/<id>``` for some very common and simple search request, the ones
  in the
* We also provide endpoints using query parameters like ```/filter?passenger_name=Erik&airlineName=Quarter```, for more
  complex and flexible queries
* Pagination is also provided for the client to fetch data the size of their choice

> These endpoints are just demonstration of subset of possible use cases, to show convenience as well as flexibility in search

### Performance

* For the initial data ingestion part, we are using
  the [Bulk API](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-bulk.html)
  from elastic search, which enable us to do bulk data ingestion in one network request.
* Also since to fetch the data from an external API is IO bounded, we use async task(CompletableFuture) to concurrently
  fetching different pages, to speed up the ingestion process

### Addressing Changing API

* As we have made the assumption that the data returned by the external API would not change, we could have done one
  time ingestion and then just use the data in elastic search to serve the client, but what if the data from the
  external API would change? There are possible solutions as below:
    * **Rolling Index**: we could have set up a cron job where it would go and fetch all the data from the API again and
      store the data in the new index, while doing this the old index would still serve the coming traffic. Once the new
      index is done ingestion, we could switch the query index to the new index
    * **Event Driven**: this would require some additional functionality of the external API. We could probably have a
      subscriber in the application that subscribe to the topic that the external API would publish. If there is any
      data changes in the external API like new data added or old data deleted it would be streamed to the consumer in
      the application. With this stream the application could update the record in the elastic search record accordingly
      while keeping up to date.

## Testing

* In the requirement it is mentioned that unit test is required, however in our configuration, the only component could
  use unit testing is the AviationRepository class, however within this class we are using method query to query the
  elastic search, all the internal method implementation would be according to the elastic search client documentation,
  so there is not much sense to test the elastic search client
* So I think in this case more appropriate way to do testing is to do an integration test, where we could test different
  searching criteria and find out that if the result would return the right passenger record
* **Note**: as stated in the beginning that the assumption is that we assume the data returned by external api would not
  change so if the data does change then some test would fail as well.

## Deployment

* Due to time limitation, this application is not being deployed into the cloud, but I could briefly discuss some
  potential deployment solutions
    * **Containerization**: we could package the application in a container and upload into a container repository like
      enterprise artifactory store for secure and centralised distribution
    * **EKS**: we could deploy the application using EKS in the aws for scalability and reliability
    * **Elasticsearch**: the elastic search could be connected to the EKS cluster which itself is also highly scalable
      and fault-tolerant

* CDK could be used for configuring all the aws resources and saved in a separate repo, so that infrastructure
  configuration is version controlled and repeatable

## Limitation and Improvement

* Due to time limitation there are not much security in place in the application. If this application is used as an
  internal API then we could configure the machine to machine authentication. If this faced the user directly we could
  restrict the user access using authentication framework like Oauth2
* Observability for logging and monitoring could be more sophisticated, like tracing , tracking slow query, or set up
  metric to log the percentage of failed requests.

