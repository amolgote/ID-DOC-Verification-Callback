package com.jumio.callback.api.model.document_processor;


import io.swagger.annotations.ApiModel;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@ApiModel("User Attribute Verification Result data model")
public class UserAttributeVerificationResult implements Serializable {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "user_attrib_id")
    private Integer userAttributeId;

    @Id
    @Column(name = "doc_type_ID")
    private Integer docTypeId;

    @Column(name = "verif_result")
    private Boolean result;

    @Column(name = "verif_date")
    private Date verificationDatetime;

    @Column(name = "verif_notes")
    private String verificationNotes;

    /*@Column(name = "attrib_name")
    private String attributeName;

    @Column(name = "doc_type")
    private String documentType;*/

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserAttributeId() {
        return this.userAttributeId;
    }
    public void setUserAttributeId(int userAttributeId) {
        this.userAttributeId = userAttributeId;
    }
    public int getDocTypeId() {
        return docTypeId;
    }
    public void setDocTypeId(int docTypeId) {
        this.docTypeId = docTypeId;
    }
    public Date getVerificationDatetime() {
        return verificationDatetime;
    }
    public void setVerificationDatetime(Date verificationDatetime) {
        this.verificationDatetime = verificationDatetime;
    }
    public Boolean getResult() {
        return result;
    }
    public void setResult(Boolean result) {
        this.result = result;
    }

    /*public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDocumentType() {
        return attributeName;
    }
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }*/

    public String getVerificationNotes() {
        return verificationNotes;
    }

    public void setVerificationNotes(String verificationNotes) {
        this.verificationNotes = verificationNotes;
    }
}
