package com.nolouser.config.algorithm;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class MonthTableShardingAlgorithm implements StandardShardingAlgorithm<Long> {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long value = shardingValue.getValue();
        String tableSuffix = simpleDateFormat.format(new Date(value));

        for (String availableTableName : availableTargetNames) {
            if (availableTableName.endsWith(tableSuffix)) {
                return availableTableName;
            }
        }

        // 默认取当月的分表
        return availableTargetNames.stream().filter(availableTargetName -> availableTargetName.endsWith(simpleDateFormat.format(new Date()))).findFirst().get();
    }

    /**
     * 范围分片，用于处理含有 BETWEEN AND 的分片处理
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        return availableTargetNames;
    }

    @Override
    public void init() {

    }

    @Override
    public String getType() {
        return "CLASS_BASED_MONTH";
    }
}
