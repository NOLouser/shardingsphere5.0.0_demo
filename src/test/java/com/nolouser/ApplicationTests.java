package com.nolouser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTests.class);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * If you put the order of configuring(rule sharding configuration) t_order_hint after t_order, this will cause Exception.
     * eg:
     *      t_order:
     *         actualDataNodes: ds.t_order_${2021}0${1..9}, ds.t_order_${2021}${10..12}
     *         tableStrategy:
     *           standard:
     *             shardingColumn: order_id
     *             shardingAlgorithmName: monthTableShardingAlgorithm
     *       t_order_hint:
     *         actualDataNodes: ds.t_order_${2021}0${1..9}, ds.t_order_${2021}${10..12}
     *         tableStrategy:
     *           hint:
     *             shardingAlgorithmName: hintMonthTableShardingAlgorithm
     *
     *  This seems to be caused by the order in which the rule shards(same actualDataNodes, but different logicTable) are processed.
     */
    @Test
    public void selectCountWithHint() {
        try (HintManager hintManager = HintManager.getInstance()) {
            hintManager.addTableShardingValue("t_order_hint", "202110");
            hintManager.addTableShardingValue("t_order_hint", "202111");
            List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select count(1) from t_order_hint");
            LOGGER.info(GSON.toJson(mapList));
        }
    }

    /**
     * If you put the order of configuring(rule sharding configuration) t_order after t_order_hint, this will cause Exception.
     */
    @Test
    public void selectCountWithId() {
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select count(1) from t_order where order_id in (1637728778000,1634877578000)");
        LOGGER.info(GSON.toJson(mapList));
    }

    @Test
    public void insert() {
        // December
        jdbcTemplate.update("insert into t_order(order_id)values(?)", System.currentTimeMillis());
        // November
        jdbcTemplate.update("insert into t_order(order_id)values(?)", 1637728778000L);
        jdbcTemplate.update("insert into t_order(order_id)values(?)", 1637555978000L);
        // October
        jdbcTemplate.update("insert into t_order(order_id)values(?)", 1634877578000L);
    }

}
