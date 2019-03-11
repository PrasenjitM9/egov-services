package org.egov.encryption.producer;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.egov.encryption.config.EncProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Component
public class Producer {

    @Autowired
    private EncProperties encProperties;

    private KafkaProducer kafkaProducer;

    @PostConstruct
    public void init() {
        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, encProperties.getKafkaBootstrapServerConfig());

        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.connect.json.JsonSerializer");

        kafkaProducer = new KafkaProducer(configProperties);

    }

    public void push(String topic, String key, Long timestamp, JsonNode data) {
        ProducerRecord<String, JsonNode> record = new ProducerRecord<>(topic, null, timestamp, key, data);
        kafkaProducer.send(record);
    }

}
