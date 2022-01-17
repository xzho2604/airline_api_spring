package com.example.demo.ingestion;

import com.example.demo.config.PassengerClient;
import com.example.demo.model.DataDto;
import com.example.demo.model.PassengerRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.IMMEDIATE;

@Component
@Slf4j
public class PassengerIngestion implements ApplicationRunner {

    private static final Integer PAGE_SIZE = 2000;

    private final PassengerClient passengerClient;
    private final ObjectMapper objectMapper;
    private final RestHighLevelClient esClient;

    static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {
    };

    public PassengerIngestion(PassengerClient passengerClient,
                              ObjectMapper objectMapper,
                              RestHighLevelClient esClient) {
        this.passengerClient = passengerClient;
        this.objectMapper = objectMapper;
        this.esClient = esClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        var dataDto = passengerClient.getPassengerData(0, PAGE_SIZE);
        var totalPage = dataDto.getTotalPages();
        var executor = Executors.newCachedThreadPool();
        List<CompletableFuture<Void>> jobs = new ArrayList<>();

        for (int i = 0; i < totalPage; i++) {
            int finalI = i;
            var job = CompletableFuture
                    .supplyAsync(() -> passengerClient.getPassengerData(finalI, PAGE_SIZE), executor)
                    .thenAccept(this::bulkIndex);

            jobs.add(job);
//            break; //TODO: remove
        }

        // block until all records saved
        jobs.forEach(CompletableFuture::join);
        log.info("***************** Passenger data ingestion completed! *********************");
    }

    private void bulkIndex(DataDto dataDto) {
        var bulkRequest = new BulkRequest()
                .setRefreshPolicy(IMMEDIATE);

        dataDto.getData().stream()
                .map(this::toIndexRequest)
                .forEach(bulkRequest::add);

        try {
            esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IndexRequest toIndexRequest(PassengerRecord passengerRecord) {
        var source = objectMapper.convertValue(passengerRecord, MAP_TYPE_REFERENCE);
        source.put("id", passengerRecord.getId());
        source.remove("_id");

        return new IndexRequest("aviation")
                .id(passengerRecord.getId())
                .timeout("5s")
                .source(source);
    }

}
