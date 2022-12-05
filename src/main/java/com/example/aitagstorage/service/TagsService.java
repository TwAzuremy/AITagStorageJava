package com.example.aitagstorage.service;

import com.example.aitagstorage.entity.Tags;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagsService {
    List<Tags> getTags(String table);

    int addTags(Tags tags);

    int delTags(@Param("table") String table, @Param("tagId") String tagId);

    List<String> fuzzySearch(@Param("table") String table, @Param("fuzzy") String fuzzy, @Param("mode") String mode);

    int bulkAdd(@Param("tags") List<Tags> tags);

    int getTables();

    String getPreImg(@Param("table") String table, @Param("id") String id);

    String getConfig(@Param("key") String key);

    int setConfig(@Param("key") String key, @Param("value") String value);
}
