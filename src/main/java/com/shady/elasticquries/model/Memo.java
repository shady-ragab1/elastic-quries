package com.shady.elasticquries.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.VersionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "memo",shards = 1, replicas = 1,versionType = VersionType.INTERNAL)
//@TypeAlias("memo-alias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Memo {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String desc;
    @Field(type = FieldType.Text)
    private String message;
}
