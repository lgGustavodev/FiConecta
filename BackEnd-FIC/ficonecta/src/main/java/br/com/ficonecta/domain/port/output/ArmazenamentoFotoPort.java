package br.com.ficonecta.domain.port.output;

import org.springframework.web.multipart.MultipartFile;

public interface ArmazenamentoFotoPort {

    /**
     * Envia o arquivo para o serviço de armazenamento
     * e devolve a URL pública da imagem salva.
     */
    String armazenar(MultipartFile arquivo);
}
