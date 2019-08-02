package myflink.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import myflink.water.KafkaMaskExt;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.table.api.StreamTableEnvironment;
import org.apache.flink.table.api.TableConfig;
import org.apache.flink.table.api.TableEnvironment;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eva on 2019-08-01.
 */
public class TestKafkaSql {

    public static final String TOPIC = "sj1_mqa_telemetry_wmequality_report";


    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();
        org.apache.flink.table.api.java.StreamTableEnvironment tEnv = TableEnvironment.getTableEnvironment(env);


        env.setParallelism(8);

        TableConfig tc = new TableConfig();

        StateBackend backend = new FsStateBackend(
                "file:///Users/eva/work/study/flink/state_bk");

        env.setStateBackend(backend);

        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                3, // number of restart attempts
                Time.of(10, TimeUnit.SECONDS).toMilliseconds() // delay
        ));


        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "rpsj1hsn017.webex.com:9092");
        properties.setProperty("group.id", "pda");
        properties.setProperty("auto.offset.reset", "latest"); //earliest ,latest


        FlinkKafkaConsumer011 consumer011 = new FlinkKafkaConsumer011<>(TOPIC, new SimpleStringSchema(), properties);
        DataStream<JSONObject> rawData = env.addSource(consumer011).map(new MapFunction<String, JSONObject>(){
            @Override
            public JSONObject map(String value) throws Exception {
                return JSON.parseObject(value);
            }
        }).assignTimestampsAndWatermarks(new KafkaMaskExt())
                .filter(new FilterFunction<JSONObject>() {

                    @Override
                    public boolean filter(JSONObject value) throws Exception {

                        if(value != null
                                && value.getJSONObject("report") != null
                                && value.getJSONObject("report").getJSONObject("identifiers") != null) {

                            JSONObject object = value.getJSONObject("report").getJSONObject("identifiers");

                            if(object != null && object.getString("locusStartTime") != null) {
//                                long timestamp = new Date(object.getString("locusStartTime")).getTime();
                                return true;

                            }
                        }

                        return false;
                    }
                });


        DataStream<String> stream = env.addSource(consumer011);

        DataStream<Tuple5<String, String, String, String, Long>> map = stream.map(new MapFunction<String, Tuple5<String, String, String, String,Long>>() {

            private static final long serialVersionUID = 1471936326697828381L;

            @Override
            public Tuple5<String, String, String, String,Long> map(String value) throws Exception {

                JSONObject object = JSONObject.parseObject(value);
                object.getString("report");
                return new Tuple5<String, String, String, String,Long>(object.getString("report"),
                        object.getString("reportId"),
                        object.getString("reportTime"),
                        object.getString("reportType"),
                        Long.valueOf(object.getString("reportVersion")));
            }
        });

        map.print(); //打印流数据


        //注册为user表
        tEnv.registerDataStream("raw_table", map, "userId,itemId,categoryId,behavior,timestampin,proctime.proctime");

        //执行sql查询     滚动窗口 10秒    计算10秒窗口内用户点击次数
//        Table sqlQuery = tableEnv.sqlQuery("SELECT TUMBLE_END(proctime, INTERVAL '10' SECOND) as processtime,"
//                + "userId,count(*) as pvcount "
//                + "FROM Users "
//                + "GROUP BY TUMBLE(proctime, INTERVAL '10' SECOND), userId");
//
//
//        //Table 转化为 DataStream
//        DataStream<Tuple3<Timestamp, String, Long>> appendStream = tableEnv.toAppendStream(sqlQuery,Types.TUPLE(Types.SQL_TIMESTAMP,Types.STRING,Types.LONG));

//        appendStream.print();
        env.execute();

    }
}
