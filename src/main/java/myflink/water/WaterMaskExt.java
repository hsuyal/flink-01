package myflink.water;

import myflink.model.OptLog;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

/**
 * Created by xumingyang on 2019/7/12.
 * 周期性水印产生
 */
public class WaterMaskExt implements AssignerWithPeriodicWatermarks<OptLog> {

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
    public long extractTimestamp(OptLog element, long previousElementTimestamp) {
        long timestamp = element.getOpTs();

        //因为可能乱序到达，所以需要持有最大的时间戳
        curMaxTimeStamp = Math.max(curMaxTimeStamp, timestamp);
        return timestamp;
    }
}
