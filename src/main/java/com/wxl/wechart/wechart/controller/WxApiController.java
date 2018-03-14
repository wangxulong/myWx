package com.wxl.wechart.wechart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wxl.wechart.wechart.dto.wx.request.TextMessage;
import com.wxl.wechart.wechart.dto.wx.response.Article;
import com.wxl.wechart.wechart.dto.wx.response.NewsMessage;
import com.wxl.wechart.wechart.util.MessageUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/wx")
public class WxApiController {
    private static String TOKEN = "ces";

    @GetMapping(value = "/api")
    @ResponseBody
    public String sign(@RequestParam(name = "signature") String signature,
                       @RequestParam(name = "timestamp") String timestamp,
                       @RequestParam(name = "nonce") String nonce,
                       @RequestParam(name = "echostr") String echostr) {
        System.out.println("-----------------------开始校验------------------------");
        //排序
        String[] params = new String[]{TOKEN, timestamp, nonce};
        Arrays.sort(params);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            content.append(params[i]);
        }
        String myString = new String(DigestUtils.sha1Hex(content.toString().getBytes()));
        //校验
        if (myString != null && myString != "" && myString.equals(signature)) {
            System.out.println("签名校验通过");
            //如果检验成功原样返回echostr，微信服务器接收到此输出，才会确认检验完成。
            return echostr;
        } else {
            return "error";
        }
    }

    @PostMapping(value = "/api")
    @ResponseBody
    public String message(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,String> map = MessageUtil.parseXML(request);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(map));
        String fromUserName = map.get("FromUserName");
        String toUserName = map.get("ToUserName");
        String msgType = map.get("MsgType");
        String content = map.get("Content");
        String message = null;
        if(MessageUtil.MESSAGE_TYPE_TEXT.equals(msgType)){
            TextMessage textMessage = new TextMessage();
            textMessage.setFromUserName(toUserName);
            textMessage.setToUserName(fromUserName);
            textMessage.setMsgType(msgType);
            textMessage.setContent("您发送的小系："+content);
            if(content.equals("单图文")){
              //  textMessage.setMsgType(MessageUtil.);
                String picUrl = "http://duanxian0402.6655.la/ces/1.jpg";
                String url = "http://www.cesgroup.com.cn/zxxx/zxxx20zn/index.html";
                Article article = getArticle("信息发展","中国打的费打发",picUrl,url);

                List<Article> articleList = new ArrayList<>();
                articleList.add(article);

                NewsMessage singleMessage = new NewsMessage();
                singleMessage.setFromUserName(toUserName);
                singleMessage.setToUserName(fromUserName);
                singleMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                singleMessage.setArticleCount(articleList.size());
                singleMessage.setArticles(articleList);
                singleMessage.setCreateTime(new Date().getTime());

                return MessageUtil.messageToXML(singleMessage);

            }else if(content.equals("多图文")){

                String picUrl1 = "http://duanxian0402.6655.la/ces/1.jpg";
                String picUrl2 = "http://duanxian0402.6655.la/ces/2.jpg";
                String url1 = "http://www.cesgroup.com.cn/zxxx/zxxx20zn/index.html";
                String url2="http://www.baidu.com";
                Article article1 =getArticle("信息发展","中国打的费打发",picUrl1,url1);
                Article article2 =getArticle("baidu","百度",picUrl2,url2);
                List<Article> articleList = new ArrayList<>();
                articleList.add(article1);
                articleList.add(article2);

                NewsMessage singleMessage = new NewsMessage();
                singleMessage.setFromUserName(toUserName);
                singleMessage.setToUserName(fromUserName);
                singleMessage.setCreateTime(new Date().getTime());
                singleMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                singleMessage.setArticleCount(articleList.size());
                singleMessage.setArticles(articleList);
                return MessageUtil.messageToXML(singleMessage);


            }
            message = MessageUtil.messageToXML(textMessage);
        }else if (MessageUtil.MESSAGE_TYPE_EVENT.equals(msgType)){
            //事件消息
            String eventType = map.get("Event");
            if(MessageUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)){
                System.out.println("进入关注。。。");
                TextMessage textMessage = new TextMessage();
                textMessage.setFromUserName(toUserName);
                textMessage.setToUserName(fromUserName);
                textMessage.setMsgType("text");
                textMessage.setContent("谢谢关注");
                textMessage.setCreateTime(new Date().getTime());
                message = MessageUtil.messageToXML(textMessage);
            }
        }
        return message;
    }
    //测试构建图文信息
    private static Article getArticle(String title,String desc,String picUrl,String url){
        Article article = new Article();
        article.setTitle(title);
        article.setDescription(desc);
        article.setPicUrl(picUrl);
        article.setUrl(url);
        return article;
    }
}