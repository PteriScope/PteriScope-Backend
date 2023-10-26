package com.pteriscope.pteriscopebackend.review.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sagemakerruntime.SageMakerRuntimeClient;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointRequest;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointResponse;

import java.util.Base64;

@Service
public class SageMakerService {

    private final SageMakerRuntimeClient sageMakerRuntimeClient;

    public SageMakerService() {
        this.sageMakerRuntimeClient = SageMakerRuntimeClient.builder()
                .region(Region.US_EAST_1)  // replace with your AWS region
                .build();
    }

    public String processImage(String imageBase64) {
        // Decode the base64 image
        byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

        // Create the request object
        InvokeEndpointRequest request = InvokeEndpointRequest.builder()
                .endpointName("your-endpoint-name")
                .body(SdkBytes.fromByteArray(imageBytes))
                .build();

        // Invoke the SageMaker endpoint
        InvokeEndpointResponse response = sageMakerRuntimeClient.invokeEndpoint(request);

        // Process the response
        return new String(response.body().asByteArray());
    }
}
