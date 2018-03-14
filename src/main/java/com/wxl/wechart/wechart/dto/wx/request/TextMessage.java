package com.wxl.wechart.wechart.dto.wx.request;

import com.wxl.wechart.wechart.dto.wx.BaseMessage;
import lombok.Data;

@Data
public class TextMessage extends BaseMessage {
    private String Content;
}
