package myflink.app;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.table.sources.CsvTableSource;
import org.apache.flink.table.sources.TableSource;
import org.apache.flink.types.Row;

/**
 * Created by Eva on 2019-07-31.
 */
public class TableTest {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tEnv = TableEnvironment.getTableEnvironment(env);

        String path = TableTest.class.getClassLoader().getResource("words.txt").getPath();
        TableSource csvSource = new CsvTableSource(path, new String[]{"word"},
                new TypeInformation[]{Types.STRING});

        tEnv.registerTableSource("fileSource", csvSource);

//        tEnv.connect(new FileSystem().path(path))
//                .withSchema(new Schema().field("word", Types.STRING))
//                .inAppendMode()
//                .registerTableSource("fileSource");

        Table result = tEnv.scan("fileSource")
                .groupBy("word")
                .select("word, count(1) as count");

        tEnv.toRetractStream(result, Row.class).print();
        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
