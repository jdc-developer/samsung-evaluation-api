package com.ibm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

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

    public DocumentDTO(JSONObject jsonObject) {
        documentId = jsonObject.getInt("documentId");
        documentNumber = jsonObject.getString("documentNumber");
        notaFiscal = jsonObject.getString("notaFiscal");
        documentDate = jsonObject.getString("documentDate");
        documentValue = jsonObject.getDouble("documentValue");
        currencyCode = jsonObject.getString("currencyCode");
    }
}
