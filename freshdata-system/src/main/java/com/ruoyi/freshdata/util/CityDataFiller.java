package com.ruoyi.freshdata.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 城市数据填充工具类
 * 用于为CSV文件填充城市数据
 * 
 * @author ruoyi
 */
public class CityDataFiller {
    
    // 中国主要城市及其权重
    private static final Map<String, Integer> CITIES = new HashMap<>();
    
    static {
        // 一线城市（权重高）
        CITIES.put("北京", 15);
        CITIES.put("上海", 15);
        CITIES.put("广州", 12);
        CITIES.put("深圳", 12);
        
        // 新一线城市
        CITIES.put("成都", 8);
        CITIES.put("杭州", 8);
        CITIES.put("重庆", 8);
        CITIES.put("西安", 7);
        CITIES.put("武汉", 7);
        CITIES.put("苏州", 7);
        CITIES.put("郑州", 6);
        CITIES.put("南京", 6);
        CITIES.put("天津", 6);
        CITIES.put("长沙", 5);
        CITIES.put("东莞", 5);
        CITIES.put("宁波", 5);
        CITIES.put("佛山", 5);
        
        // 二线城市
        CITIES.put("合肥", 4);
        CITIES.put("福州", 4);
        CITIES.put("厦门", 4);
        CITIES.put("哈尔滨", 3);
        CITIES.put("济南", 3);
        CITIES.put("青岛", 3);
        CITIES.put("大连", 3);
        CITIES.put("长春", 2);
        CITIES.put("石家庄", 2);
        CITIES.put("南昌", 2);
        CITIES.put("贵阳", 2);
        CITIES.put("南宁", 2);
        CITIES.put("昆明", 2);
        CITIES.put("太原", 2);
        CITIES.put("兰州", 1);
        CITIES.put("呼和浩特", 1);
        CITIES.put("乌鲁木齐", 1);
        CITIES.put("银川", 1);
    }
    
    /**
     * 创建带权重的城市列表
     */
    private static List<String> createWeightedCityList() {
        List<String> weightedCities = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : CITIES.entrySet()) {
            String city = entry.getKey();
            int weight = entry.getValue();
            for (int i = 0; i < weight; i++) {
                weightedCities.add(city);
            }
        }
        return weightedCities;
    }
    
    /**
     * 填充CSV文件的城市数据
     */
    public static void fillCityData(String inputFile, String outputFile) throws IOException {
        List<String> weightedCities = createWeightedCityList();
        Random random = new Random(42); // 使用固定种子保证可重复性
        
        List<String[]> rows = new ArrayList<>();
        String[] header = null;
        int cityIndex = -1;
        
        // 读取CSV文件
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8))) {
            
            // 读取表头
            String line = reader.readLine();
            if (line != null) {
                header = line.split(",", -1);
                // 找到city_num列的索引
                for (int i = 0; i < header.length; i++) {
                    if ("city_num".equals(header[i])) {
                        cityIndex = i;
                        break;
                    }
                }
            }
            
            if (cityIndex == -1) {
                throw new IllegalArgumentException("找不到city_num列");
            }
            
            System.out.println("表头: " + String.join(",", header));
            System.out.println("city_num列索引: " + cityIndex);
            
            // 读取数据行
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                // 随机选择一个城市
                String city = weightedCities.get(random.nextInt(weightedCities.size()));
                fields[cityIndex] = city;
                rows.add(fields);
            }
        }
        
        System.out.println("总记录数: " + rows.size());
        
        // 写入新文件
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
            
            // 写入表头
            writer.write(String.join(",", header));
            writer.newLine();
            
            // 写入数据行
            for (String[] row : rows) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
        
        System.out.println("已成功填充城市数据并保存到: " + outputFile);
        
        // 统计城市分布
        Map<String, Integer> cityCount = new HashMap<>();
        for (String[] row : rows) {
            String city = row[cityIndex];
            cityCount.put(city, cityCount.getOrDefault(city, 0) + 1);
        }
        
        // 排序并显示TOP 15
        System.out.println("\n城市分布统计（TOP 15）:");
        cityCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(15)
                .forEach(entry -> {
                    double percentage = (entry.getValue() * 100.0) / rows.size();
                    System.out.printf("%s: %d (%.2f%%)\n", entry.getKey(), entry.getValue(), percentage);
                });
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        try {
            String inputFile = "app_data_cleaned.csv";
            String outputFile = "app_data_cleaned.csv";
            
            System.out.println("开始填充城市数据...");
            fillCityData(inputFile, outputFile);
            System.out.println("\n完成！");
            
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

