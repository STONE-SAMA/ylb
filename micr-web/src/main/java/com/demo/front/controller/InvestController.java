package com.demo.front.controller;

import com.demo.api.model.User;
import com.demo.front.view.RespResult;
import com.demo.front.view.invest.RankView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.constants.RedisKeys;
import org.example.common.util.CommonUtil;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    //购买理财产品，更新排行榜
    @ApiOperation(value = "投资理财产品")
    @GetMapping("/invest/product")
    public RespResult investRecord(@RequestHeader("uid") Integer uid,
                                   @RequestParam("productId") Integer productId,
                                   @RequestParam("money") BigDecimal money) {
        RespResult result = RespResult.fail();
        if ((uid != null && uid > 0) && (productId != null && productId > 0)
                && (money != null && money.intValue() % 100 == 0 && money.intValue() >= 100)) {
            int investResult = investService.investProduct(uid, productId, money);
            //根据investResult的值确定失败的具体原因
            switch (investResult) {
                case 0:
                    result.setMsg("投资数据不正确");
                    break;
                case 1:
                    result = RespResult.ok();
                    modifyInvestRank(uid, money);
                    break;
                case 2:
                    result.setMsg("资金账号不存在");
                    break;
                case 3:
                    result.setMsg("资金不足");
                    break;
                case 4:
                    result.setMsg("理财产品不存在");
                    break;
            }
        }
        return result;
    }

    //更新投资排行榜
    private void modifyInvestRank(Integer uid, BigDecimal money) {
        User user = userService.queryById(uid);
        if (user != null) {
            //更新redis中的投资排行榜
            String key = RedisKeys.REDIS_KEY_INVEST_RANK;
            stringRedisTemplate.boundZSetOps(key).incrementScore(user.getPhone(), money.doubleValue());
        }
    }

}
