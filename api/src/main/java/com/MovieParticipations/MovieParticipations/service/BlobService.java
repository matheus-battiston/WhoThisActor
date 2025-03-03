package com.MovieParticipations.MovieParticipations.service;

import com.MovieParticipations.MovieParticipations.controller.response.SasResponse;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

import static com.MovieParticipations.MovieParticipations.mapper.SASTokenMapper.toResponse;

@Service
public class BlobService {

    @Value("${CONNECTIONSTRING}")
    private String connectionString;
    private static final String containerName = "blobs";

    public SasResponse gerarSas(){
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerSasPermission permissions = new BlobContainerSasPermission()
                .setReadPermission(true)
                .setWritePermission(true);

        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(
                OffsetDateTime.now().plusHours(1), permissions);

        String sasToken = blobServiceClient
                .getBlobContainerClient(containerName)
                .generateSas(sasValues);


        return toResponse(sasToken);
        }
    }
