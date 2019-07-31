package myflink.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import myflink.keyedstate.kafkaTestProcess;
import myflink.water.KafkaMaskExt;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eva on 2019-07-30.
 */
public class TestKafka {

    public static final String TOPIC = "sj1_mqa_telemetry_wmequality_report";

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setRestartStrategy(RestartStrategies.fallBackRestart());


        env.enableCheckpointing(5000);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        checkpointConfig.setMinPauseBetweenCheckpoints(5000);
        checkpointConfig.setCheckpointTimeout(6000);
        checkpointConfig.setMaxConcurrentCheckpoints(1);
        checkpointConfig.setFailOnCheckpointingErrors(true);
        checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        StateBackend backend=new FsStateBackend(


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

        DataStream<String> mapData = rawData.map(new MapFunction<JSONObject, String>() {
            @Override
            public String map(JSONObject value) throws Exception {
                String s = value.getString("reportId");

                return s;
            }
        });



        KeyedStream<JSONObject, String> keyStream = rawData.keyBy(new KeySelector<JSONObject, String>() {
            @Override
            public String getKey(JSONObject obj) throws Exception {
                JSONObject obj0 = obj.getJSONObject("report");
//                System.out.println("obj___" + obj0.getString("name"));

                return obj0.getString("name");
            }
        });

//        DataStream<Tuple3<String, Long, Long>> res =
//        .timeWindow((Time.seconds(2)),Time.seconds(1))
//        .process(new kafkaTestProcess());

        DataStream<Tuple3<String, Long ,Long>> res =
                keyStream.timeWindow(Time.seconds(2)).process(new kafkaTestProcess());

        res.print();
        env.execute("flink kafka test");

    }
}
