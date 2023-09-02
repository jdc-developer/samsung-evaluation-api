package com.ibm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {

    private Integer documentId;
    private String documentNumber;
    private String notaFiscal;
    private String documentDate;
    private Double documentValue;
    private String currencyCode;
}
