package com.jumio.callback.api.model.document_processor;
import io.swagger.annotations.ApiModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@ApiModel("User Document data model")
public class UserDocumentData implements Serializable {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "doc_type_ID")
    private Integer docTypeId;

    @Column(name = "verif_data")
    private String data;

    @Column(name = "scan_Ref_num")
    private String scanReferenceNumber;

    @Column(name = "create_date")
    private Date createdDate;

    @Column(name = "doc_expiration_date")
    private Date expirationDate;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(int docTypeId) {
        this.docTypeId = docTypeId;
    }

    public String getScanReferenceNumber() {
        return scanReferenceNumber;
    }

    public void setScanReferenceNumber(String scanReferenceNumber) {
        this.scanReferenceNumber = scanReferenceNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}

