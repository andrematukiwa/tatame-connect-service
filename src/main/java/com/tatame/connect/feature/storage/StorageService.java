package com.tatame.connect.feature.storage;

import java.io.InputStream;

public interface StorageService {

    /** Envia o arquivo para o bucket e retorna a URL publica para exibi-lo. */
    String uploadFile(String path, InputStream content, long contentLength, String contentType);

    void deleteFile(String path);
}
