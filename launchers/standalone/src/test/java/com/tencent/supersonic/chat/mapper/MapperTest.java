package com.tencent.supersonic.chat.mapper;

import com.tencent.supersonic.chat.api.pojo.SchemaElement;
import com.tencent.supersonic.chat.api.pojo.SemanticParseInfo;
import com.tencent.supersonic.chat.api.pojo.request.QueryFilter;
import com.tencent.supersonic.chat.api.pojo.request.QueryReq;
import com.tencent.supersonic.chat.api.pojo.response.QueryResult;
import com.tencent.supersonic.chat.core.query.rule.metric.MetricTagQuery;
import com.tencent.supersonic.chat.BaseTest;
import com.tencent.supersonic.util.DataUtils;
import com.tencent.supersonic.common.pojo.DateConf;
import com.tencent.supersonic.common.pojo.enums.FilterOperatorEnum;
import com.tencent.supersonic.common.pojo.enums.QueryType;
import org.junit.jupiter.api.Test;

import static com.tencent.supersonic.common.pojo.enums.AggregateTypeEnum.NONE;

public class MapperTest extends BaseTest {

    @Test
    public void hanlp() throws Exception {

        QueryReq queryContextReq = DataUtils.getQueryContextReq(10, "艺人周杰伦的播放量");
        queryContextReq.setAgentId(DataUtils.tagAgentId);

        QueryResult actualResult = submitNewChat("艺人周杰伦的播放量", DataUtils.tagAgentId);

        QueryResult expectedResult = new QueryResult();
        SemanticParseInfo expectedParseInfo = new SemanticParseInfo();
        expectedResult.setChatContext(expectedParseInfo);

        expectedResult.setQueryMode(MetricTagQuery.QUERY_MODE);
        expectedParseInfo.setAggType(NONE);

        QueryFilter dimensionFilter = DataUtils.getFilter("singer_name", FilterOperatorEnum.EQUALS, "周杰伦", "歌手名", 7L);
        expectedParseInfo.getDimensionFilters().add(dimensionFilter);

        SchemaElement metric = SchemaElement.builder().name("播放量").build();
        expectedParseInfo.getMetrics().add(metric);

        expectedParseInfo.setDateInfo(DataUtils.getDateConf(DateConf.DateMode.RECENT, 7, period, startDay, endDay));
        expectedParseInfo.setQueryType(QueryType.METRIC);

        assertQueryResult(expectedResult, actualResult);
    }

}
