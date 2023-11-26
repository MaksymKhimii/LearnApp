package com.epam.reportMicroservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.epam.reportMicroservice.repository")
public class DynamoDBConfig {

  @Value("${cloud.aws.region.static}")
  private String region;
  @Bean
  public AmazonDynamoDB amazonDynamoDB(AWSCredentials AWSCredentials,
      @Value("${cloud.aws.dynamodb.endpoint}") String dynamoDBURl) {

    AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(dynamoDBURl, "eu-west-1"))
        .withCredentials(new AWSStaticCredentialsProvider(AWSCredentials));

    return builder.build();
  }

  @Bean
  public AWSCredentials awsCredentials(@Value("${cloud.aws.credentials.access-key}")
  String accessKey,
      @Value("${cloud.aws.credentials.secret-key}")
      String secretKey) {
    return new BasicAWSCredentials(accessKey, secretKey);
  }
}
