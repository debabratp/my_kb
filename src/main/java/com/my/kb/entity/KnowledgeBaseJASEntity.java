package com.my.kb.entity;

import com.oberasoftware.jasdb.api.entitymapper.annotations.Id;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBEntity;
import com.oberasoftware.jasdb.api.entitymapper.annotations.JasDBProperty;

import java.time.LocalDateTime;
import java.util.Date;

@JasDBEntity(bagName = "knowledge_base")
public class KnowledgeBaseJASEntity {
    private String id;
    private String title;
    // private String type;
    private String description;
    private String splunkQuery;
    private String tag;
    //private String reference;
    private String createDate;
    //private String application;
    private long mostSearched;

    @Id
    @JasDBProperty
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JasDBProperty
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /* @JasDBProperty
     public String getType() {
         return type;
     }

     public void setType(String type) {
         this.type = type;
     }
 */
    @JasDBProperty
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JasDBProperty
    public String getSplunk_query() {
        return splunkQuery;
    }

    public void setSplunk_query(String splunk_query) {
        this.splunkQuery = splunk_query;
    }

    @JasDBProperty
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /*  @JasDBProperty
      public String getReference() {
          return reference;
      }

      public void setReference(String reference) {
          this.reference = reference;
      }
  */
    @JasDBProperty
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String create_date) {
        this.createDate = create_date;
    }

    /*@JasDBProperty
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
*/
    @JasDBProperty
    public long getMostSearched() {
        return mostSearched;
    }

    public void setMostSearched(long most_searched) {
        this.mostSearched = most_searched;
    }
}
