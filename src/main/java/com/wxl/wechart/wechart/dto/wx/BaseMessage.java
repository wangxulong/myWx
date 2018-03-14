package com.wxl.wechart.wechart.dto.wx;

import lombok.Data;

@Data
public class BaseMessage {
    //开发者微信账号
    private String ToUserName;
    //发送方账号（要给openId）
    private String FromUserName;
    //消息创建时间  整形 表示从1970到消息创建所间隔的秒数
    private long CreateTime;
    //消息类型（text/image/location/link/voice）
    private String MsgType;
    //消息ID 64位整数
    private long MsgId;
}
