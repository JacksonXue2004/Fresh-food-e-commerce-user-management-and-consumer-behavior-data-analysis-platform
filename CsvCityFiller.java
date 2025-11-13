import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvCityFiller {
    
    private static final String[] CITIES = {
        "110000", "310000", "440100", "440300", "330100", "510100", "500000", "420100",
        "610100", "320100", "330200", "370200", "350200", "440400", "430100", "320500",
        "210200", "350100", "450100", "230100", "220100", "370100", "340100", "410100",
        "130100", "140100", "530100", "520100", "450300", "320600"
    };
    
    public static void main(String[] args) {
        String inputFile = "app_data_cleaned.csv";
        String outputFile = "app_data_cleaned_new.csv";
        
        try {
            fillCityData(inputFile, outputFile);
            System.out.println("城市数据填充完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void fillCityData(String inputFile, String outputFile) throws IOException {
        Random random = new Random();
        int count = 0;
        
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8)
        );
        
        BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8)
        );
        
        // 读取并写入表头
        String header = reader.readLine();
        writer.write(header);
        writer.newLine();
        
        // 处理数据行
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",", -1);
            
            // 检查city_num字段（索引4）是否为空
            if (fields.length > 4 && (fields[4] == null || fields[4].trim().isEmpty())) {
                fields[4] = CITIES[random.nextInt(CITIES.length)];
                count++;
            }
            
            // 重新组装行
            writer.write(String.join(",", fields));
            writer.newLine();
        }
        
        reader.close();
        writer.close();
        
        System.out.println("共填充了 " + count + " 条城市数据");
    }
}

