package com.yiqzq.miaoshasystem.controller;

import com.yiqzq.miaoshasystem.annotation.AccessLimit;
import com.yiqzq.miaoshasystem.common.RedisCacheExpire;
import com.yiqzq.miaoshasystem.common.StringConstant;
import com.yiqzq.miaoshasystem.exception.MyException;
import com.yiqzq.miaoshasystem.pojo.GoodsWIthStatusWithUser;
import com.yiqzq.miaoshasystem.pojo.SeckillGoods;
import com.yiqzq.miaoshasystem.pojo.User;
import com.yiqzq.miaoshasystem.result.CodeMsg;
import com.yiqzq.miaoshasystem.result.Result;
import com.yiqzq.miaoshasystem.service.GoodsService;
import com.yiqzq.miaoshasystem.utils.CookieUtil;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author yiqzq
 * @date 2020/6/4 18:32
 */
@Slf4j
@Controller
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    ApplicationContext applicationContext;

//    @RequestMapping("/goods/list")
//    public String goodsList(Model model, HttpSession session) {
//        log.info("进入/goods/list");
//        String sessionId = session.getId();
//        User u = (User) RedisUtil.get("user:" + sessionId);
//        if (u == null) throw new MyException(CodeMsg.SESSION_EXPIRE);
//        log.info("通过User认证");
//        List<SeckillGoodds> allGoods = goodsService.getAllGoods();
//        log.info("数据库返回信息");
//        model.addAttribute("user", u);
//        model.addAttribute("allGoods", allGoods);
//        return "goods_list";
//    }

    //页面静态化
    @RequestMapping("/goods/list")
    @ResponseBody
    public String goodsList2(Model model, HttpServletResponse response, HttpServletRequest request) {

        String html = (String) RedisUtil.get(StringConstant.GOOGS_LIST);
        if (!StringUtils.isEmpty(html)) {
            log.info("从Redis中获得静态页面");
            return html;
        }
        List<SeckillGoods> allGoods = goodsService.getAllGoods();
        model.addAttribute("allGoods", allGoods);
        IWebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            RedisUtil.set(StringConstant.GOOGS_LIST, html, RedisCacheExpire.GOODS_LIST);
        }
        return html;
    }

    //页面静态化,提供接口,返回数据
    @ResponseBody
    @RequestMapping("/goods/detail/{goodsId}")
    public Result<GoodsWIthStatusWithUser> detail(Model model, @PathVariable("goodsId") Integer goodsId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
//        log.info("loginToken"+loginToken);
        User user = (User) RedisUtil.get("user:" + loginToken);

        SeckillGoods goods = goodsService.getSeckillGoodsByGoodsId(goodsId);
        if (goods == null) {
            throw new MyException(CodeMsg.NO_GOODS);
        }
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        /*
        0秒杀还没开始
        1秒杀进行中
        2秒杀已经结束
        * */
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        GoodsWIthStatusWithUser goodsWIthStatusWithUser = new GoodsWIthStatusWithUser();
        goodsWIthStatusWithUser.setGoods(goods);
        goodsWIthStatusWithUser.setUser(user);
        goodsWIthStatusWithUser.setRemainSeconds(remainSeconds);
        goodsWIthStatusWithUser.setMiaoshaStatus(miaoshaStatus);
        return Result.success(goodsWIthStatusWithUser);
    }

    /*************************************Test接口**********************/
    @RequestMapping("/goods/getjsonlist")
    @ResponseBody
    public Result<Object> getGoodsJson() {
        List<SeckillGoods> allGoods = goodsService.getAllGoods();
        return Result.success(allGoods);
    }


    @RequestMapping("/goods/jmeterlist")
    @ResponseBody
    public Result<Object> JmetergoodsList(Model model, HttpSession session) {
        log.info("进入/goods/list");
//        String sessionId = session.getId();
//        User u = (User) RedisUtil.get("user" + sessionId);
//        if (u == null) throw new MyException(CodeMsg.SESSION_EXPIRE);
        log.info("通过User认证");
        List<SeckillGoods> allGoods = goodsService.getAllGoods();
        log.info("数据库返回信息");
//        model.addAttribute("user", u);
        return Result.success(allGoods);
    }
}
