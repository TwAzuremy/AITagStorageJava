package com.example.aitagstorage.controller;

import com.alibaba.fastjson2.JSON;
import com.example.aitagstorage.entity.Tags;
import com.example.aitagstorage.service.TagsService;
import com.example.aitagstorage.tools.MD5;
import com.example.aitagstorage.tools.PATH;
import com.example.aitagstorage.tools.TagTables;
import com.example.aitagstorage.tools.TagsThreadPoolExecutor;
import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@Controller
@CrossOrigin
public class TagsController {
    @Resource
    private TagsService tagsService;

    @PostMapping("/getData")
    @ResponseBody
    public String getData(String table) {
        return JSON.toJSONString(tagsService.getTags(table));
    }

    @PostMapping("/addTag")
    @ResponseBody
    public String addTag(MultipartFile file, HttpServletRequest request) {
        Map<String, String[]> query = request.getParameterMap();

        String uploadPath = PATH.getUploadPath(request);
        String fileSuffixName = "";
        String newFileName = "";

        if (file != null) {
            fileSuffixName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
            newFileName = MD5.Encryption(
                    Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0],
                    String.valueOf(System.currentTimeMillis())
            );
        }

        Tags tags = new Tags();
        tags.setTable(query.get("table")[0]);
        tags.setTitle(query.get("title")[0]);
        tags.setFront(query.get("front")[0]);
        tags.setReverse(query.get("reverse")[0]);
        tags.setRemark(query.get("remark")[0]);
        if (!(newFileName.isEmpty() || fileSuffixName.isEmpty())) {
            tags.setPreImg(newFileName + "." + fileSuffixName);
        }

        int rows = tagsService.addTags(tags);
        if (rows > 0 && !(newFileName.isEmpty() || fileSuffixName.isEmpty())) {
            try {
                file.transferTo(new File(uploadPath, newFileName + "." + fileSuffixName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return JSON.toJSONString(rows);
    }

    @PostMapping("/delTag")
    @ResponseBody
    public String delTag(@NotNull String table, @NotNull String id, HttpServletRequest request) {
        String fileName = tagsService.getPreImg(table, id);
        String uploadPath = PATH.getUploadPath(request);

        int rows = tagsService.delTags(table, id);

        if (fileName != null && rows > 0) {
            Path path = Paths.get(uploadPath + "\\" + fileName);
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return JSON.toJSONString(rows);
    }

    @PostMapping("fuzzySearch")
    @ResponseBody
    public String fuzzySearch(@NotNull String fuzzy, @NotNull String mode) {
        Map<String, String> result_map = new HashMap<>();
        if (fuzzy.isEmpty()) {
            return null;
        }

        int tables = tagsService.getTables();
        CountDownLatch latch = new CountDownLatch(tables - 1);
        ExecutorService service = TagsThreadPoolExecutor.fuzzyThreadPoolExecutor(
                tables - 1,
                Thread::new /* Thread Factory */
        );

        for (TagTables table : TagTables.values()) {
            service.submit(() -> {
                List<String> result_list = tagsService.fuzzySearch(String.valueOf(table), fuzzy, mode.toLowerCase());

                for (String title : result_list) {
                    if (!CollectionUtils.isEmpty(result_list)) {
                        result_map.put(title, String.valueOf(table));
                    }
                }

                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        service.shutdown();
        return JSON.toJSONString(result_map);
    }

    @PostMapping("bulkAdd")
    @ResponseBody
    public String bulkAdd(@RequestParam Map<String, String> data) {
        List<Tags> tags_list = new ArrayList<>();

        for (int i = 1; i <= data.keySet().size() / 6; i++) {
            Tags tag = new Tags();
            tag.setTable(data.get("data[" + i + "][table_eng]"));
            tag.setTitle(data.get("data[" + i + "][title]"));
            tag.setFront(data.get("data[" + i + "][front]"));
            tag.setReverse(data.get("data[" + i + "][reverse]"));
            tag.setRemark(data.get("data[" + i + "][remark]"));

            tags_list.add(tag);
        }

        return JSON.toJSONString(tagsService.bulkAdd(tags_list));
    }

    @PostMapping("/configOperation")
    @ResponseBody
    public String configOperation(String key, String value, @NotNull String operation) {
        if (operation.equals("SET")) {
            int update_rows = tagsService.setConfig(key, value);

            if (update_rows > 0) {
                return JSON.toJSONString(value);
            } else {
                // update failed
                return JSON.toJSONString(-1);
            }
        }

        // operation == GET
        return JSON.toJSONString(tagsService.getConfig(key));
    }
}
