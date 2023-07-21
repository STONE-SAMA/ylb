package com.demo.front.controller;

import com.demo.front.view.RespResult;
import com.demo.front.view.invest.RankView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.constants.RedisKeys;
import org.example.common.util.CommonUtil;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin
@Api(tags = "投资理财产品")
@RestController
@RequestMapping("/v1")
public class InvestController extends BaseController {

    //投资排行榜
    @ApiOperation(value = "投资排行榜", tags = "显示投资金额最高的三位用户信息")
    @GetMapping("/invest/rank")
    public RespResult showInvestRank() {
        //从redis查询数据
        Set<ZSetOperations.TypedTuple<String>> tupleSet = stringRedisTemplate
                .boundZSetOps(RedisKeys.REDIS_KEY_INVEST_RANK)
                .reverseRangeWithScores(0, 2);

        List<RankView> rankViewList = new ArrayList<>();
        //遍历set集合
        tupleSet.forEach(tuple -> {
            rankViewList.add(new RankView(CommonUtil.privacyPhone(tuple.getValue()), tuple.getScore()));
        });

        RespResult result = RespResult.ok();
        result.setList(rankViewList);

        return result;
    }

}
