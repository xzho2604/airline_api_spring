package com.example.demo.ingestion;

import com.example.demo.config.PassengerClient;
import com.example.demo.repository.AviationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PassengerIngestion implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(PassengerIngestion.class);

    private PassengerClient passengerClient;
    private AviationRepository aviationRepository;

    public PassengerIngestion(PassengerClient passengerClient, AviationRepository aviationRepository) {
        this.passengerClient = passengerClient;
        this.aviationRepository = aviationRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var dataDto = passengerClient.getPassengerData(0, 10);

        log.info(dataDto.toString());

        // TODO: use pagination for bulk saving
        for (var passengerRecord : dataDto.getData()) {
            aviationRepository.save(passengerRecord);
        }

        log.info("***************** Passenger data ingestion completed! *********************");
    }


}
