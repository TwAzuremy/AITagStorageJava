package com.example.aitagstorage.service.impl;

import com.example.aitagstorage.entity.Tags;
import com.example.aitagstorage.mapper.TagsMapper;
import com.example.aitagstorage.service.TagsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TagsServiceImpl implements TagsService {
    @Resource
    private TagsMapper tagsMapper;

    @Override
    public List<Tags> getTags(String table) {
        return tagsMapper.getTags(table);
    }

    @Override
    public int addTags(Tags tags) {
        return tagsMapper.addTags(tags);
    }

    @Override
    public int delTags(String table, String tagId) {
        return tagsMapper.delTags(table, tagId);
    }

    @Override
    public List<String> fuzzySearch(String table, String fuzzy, String mode) {
        return tagsMapper.fuzzySearch(table, fuzzy, mode);
    }

    @Override
    public int bulkAdd(List<Tags> tags) {
        int rows = 0;
        for (Tags t : tags) {
            rows += tagsMapper.bulkAdd(t);
        }
        return rows;
    }

    @Override
    public int getTables() {
        return tagsMapper.getTables();
    }

    @Override
    public String getPreImg(String table, String id) {
        return tagsMapper.getPreImg(table, id);
    }

    @Override
    public String getConfig(String key) {
        return tagsMapper.getConfig(key);
    }

    @Override
    public int setConfig(String key, String value) {
        return tagsMapper.setConfig(key, value);
    }
}
