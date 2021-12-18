package com.lyj.blog.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyj.blog.config.LoadConfig;
import com.lyj.blog.mapper.WordMapper;
import com.lyj.blog.model.Word;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateBatchRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateBatchResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Yingjie.Lu
 * @description
 * @date 2020/7/26 11:43 下午
 */
@Service
public class WordService {

    Pattern pattern = Pattern.compile("[A-Za-z]*");

    @Autowired
    WordMapper wordMapper;

    // 分词并翻译
    public Integer analyze(String name, String content) {
        // 分词
        String[] split = content.split("\\b");
        System.out.println("原始单词数：" + split.length);

        // 去重+统计
        Object[] objects = filterDuplicateAndStatistics(split);
        System.out.println("去重后单词数：" + objects.length);

        // 排序(倒序)
        Arrays.sort(objects, (o, t1) -> (int) ((Map.Entry) t1).getValue() - (int) ((Map.Entry) o).getValue());

        // 翻译
        List<String> list = new ArrayList<>();
        if (objects.length > 0) {
            translate(list, objects);
        }

        // 将结果保存到
        Word word = new Word().setName(name).setContext(JSON.toJSONString(list));
        wordMapper.insert(word);

        return word.getId();
    }


    // 去重+统计
    public Object[] filterDuplicateAndStatistics(String[] split) {
        HashMap<String, Integer> map = new HashMap<>(10240);

        for (String s : split) {
            // 去掉不符合条件的单词
            boolean matches = pattern.matcher(s).matches();
            if (!matches) {
                continue;
            }

            if (!map.containsKey(s)) {
                map.put(s, 1);
            } else {
                Integer count = map.get(s);
                map.put(s, count + 1);
            }
        }

        return map.entrySet().toArray(); // 返回去重复且包含统计的数组
    }

    // 批量翻译
    public void translate(List list, Object[] objects) {
        // 英文单词集合
        List<String> collect = Arrays.stream(objects).map((item) -> {
            return (String) ((Map.Entry) item).getKey();
        }).collect(Collectors.toList());

        int startIndex = 0, endIndex = 0;
        int size = 1000; //批量翻译的个数
        while (true) {
            if (startIndex > objects.length) {
                break;
            }

            // 限制最大值，防止数组越界
            if (startIndex + size < objects.length) {
                endIndex = startIndex + size;
            } else {
                endIndex = objects.length;
            }

            // 每一轮的批量翻译
            List<String> strings = collect.subList(startIndex, endIndex);
            TextTranslateBatchResponse transform = translateUtil("en", "zh", strings);
            String[] targetTextList = transform.getTargetTextList();// 获取翻译结果
            try {
                Thread.sleep(300); //限制调用频率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 打印输出
            for (int i = 0; i < endIndex - startIndex; i++) {
                Map.Entry word = (Map.Entry) objects[startIndex + i];

                list.add(word.getKey() + "," + targetTextList[i] + "," + word.getValue());// 单词+翻译+出现次数
//                System.out.println(word.getKey()+":"+targetTextList[i]+","+word.getValue()+","+"https://fanyi.baidu.com/#en/zh/"+word.getKey());
            }

            // 移动指针
            startIndex += size;
        }
    }

    // 翻译工具
    public TextTranslateBatchResponse translateUtil(String source, String target, List list) {
        TextTranslateBatchResponse resp = null;
        try {
            Credential cred = new Credential(LoadConfig.getInstance().getSecretId(), LoadConfig.getInstance().getSecretKey());

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tmt.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            TmtClient client = new TmtClient(cred, "ap-beijing", clientProfile);

            String params = String.format("{\"Source\":\"%s\",\"Target\":\"%s\",\"ProjectId\":0,\"SourceTextList\":%s}", source, target, JSON.toJSONString(list.toArray()));
            TextTranslateBatchRequest req = TextTranslateBatchRequest.fromJsonString(params, TextTranslateBatchRequest.class);

            resp = client.TextTranslateBatch(req);
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

        return resp;
    }


    public List<Word> list() {
        List<Word> words = wordMapper.selectList(new QueryWrapper<Word>().select("id,name"));
        return words;
    }

    public Word content(int id) {
        return wordMapper.selectById(id);
    }

    public void save(int id, Integer[] index) {
        Word word = new Word().setId(id).setIndex(JSON.toJSONString(index));
        wordMapper.updateById(word);
    }

    public void delete(int id) {
        wordMapper.deleteById(id);
    }
}
