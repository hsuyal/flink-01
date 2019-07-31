package myflink.water;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * Created by Eva on 2019-07-30.
 */
public class KafkaMaskExt implements AssignerWithPeriodicWatermarks<JSONObject> {

    private long curMaxTimeStamp;


    /**
     * 周期性的调用，如果获取的watermark不等null而且比上一个最新的watermark大就会向下游发射
     * @return
     */
    @Nullable
    @Override
    public Watermark getCurrentWatermark() {

        //在最大的时间戳上延迟多少ms
        return new Watermark(curMaxTimeStamp - 3500);
    }

    /**
     * 每一条数据都会调用该方法
     * @return
     */
    @Override
    public long extractTimestamp(JSONObject element, long previousElementTimestamp) {

        long timestamp = System.currentTimeMillis();
//        if(element != null
//                && element.getJSONObject("report") != null
//                && element.getJSONObject("report").getJSONObject("identifiers") != null) {
//
//            JSONObject object = element.getJSONObject("report").getJSONObject("identifiers");
//
//            if(object != null && object.getString("locusStartTime") != null) {
//                System.out.println("object value is:" + object);
//                timestamp = new Date(object.getString("locusStartTime")).getTime();
//
//            }
//        }

        //因为可能乱序到达，所以需要持有最大的时间戳
        curMaxTimeStamp = Math.max(curMaxTimeStamp, timestamp);
        return timestamp;


    }
}
