package com.wxl.wechart.wechart.dto.wx.response;

import com.wxl.wechart.wechart.dto.wx.BaseMessage;
import lombok.Data;

import java.util.List;

/**
 * 回复图文信息
 */
@Data
public class NewsMessage extends BaseMessage {
    //图文消息个数，限制为10条以内
    private int ArticleCount;
    //多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
    private List<Article> Articles;

}
