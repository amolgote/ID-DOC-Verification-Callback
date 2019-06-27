package com.jumio.callback.api.model.DocumentProcessor;
import io.swagger.annotations.ApiModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@ApiModel("User Document data model")
public class UserDocumentData {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "verif_doc_type_ID")
    private Integer docTypeId;

    @Column(name = "verif_data")
    private String data;

    @Column(name = "scan_Ref_num")
    private String scanReferenceNumber;

    @Column(name = "create_date")
    private Date createdDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDocTypeIdId() {
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

}

