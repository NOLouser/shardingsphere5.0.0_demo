package com.nolouser.config.algorithm;

import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <pre>
 * 强制路由算法
 * </pre>
 *
 * @author nolouser
 * @version 1.00.00
 *
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */
public class HintMonthTableShardingAlgorithm implements HintShardingAlgorithm<String> {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, HintShardingValue<String> shardingValue) {
        Collection<String> result = new LinkedList<>();
        for (String each : availableTargetNames) {
            for (String value : shardingValue.getValues()) {
                if (each.endsWith(value)) {
                    result.add(each);
                }
            }
        }
        return result;
    }

    @Override
    public void init() {

    }

    @Override
    public String getType() {
        return "CLASS_BASED_HINT";
    }
}
