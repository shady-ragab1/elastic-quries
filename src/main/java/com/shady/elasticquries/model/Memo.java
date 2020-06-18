package com.shady.elasticquries.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.VersionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "memo",shards = 1, replicas = 1,versionType = VersionType.INTERNAL)
//@TypeAlias("memo-alias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Memo {

    @Id
    private String id;

    @Field(store = true)
    private String desc;
    @Field(store = false)
    private String message;
}
