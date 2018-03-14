package com.wxl.wechart.wechart.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.wxl.wechart.wechart.dto.wx.request.TextMessage;
import com.wxl.wechart.wechart.dto.wx.response.Article;
import com.wxl.wechart.wechart.dto.wx.response.NewsMessage;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {
    //请求消息类型：文本
    public static final String MESSAGE_TYPE_TEXT="text";
    ////请求消息类型：图片
    public static final String MESSAGE_TYPE_IMAGE="image";
    //请求消息类型：语音
    public static final String MESSAGE_TYPE_VOICE="voice";
    //请求消息类型：视频
    public static final String MESSAGE_TYPE_VIDEO="video";
    //小视频
    public static final String MESSAGE_TYPE_SHORT_VIDEO="shortvideo";
    public static final String MESSAGE_TYPE_LOCATION="location";
    public static final String MESSAGE_TYPE_LINK="link";
    public static final String MESSAGE_TYPE_EVENT="event";

    //事件类型
    public static final String EVENT_TYPE_SUBSCRIBE="subscribe";
    public static final String EVENT_TYPE_UNSUBSCRIBE="unsubscribe";
    public static final String EVENT_TYPE_SCAN="SCAN";
    public static final String EVENT_TYPE_LOCATION="LOCATION";
    public static final String EVENT_TYPE_CLICK="CLICK";
    public static final String EVENT_TYPE_VIEW="VIEW";



    //响应消息类型
    public static final String RESP_MESSAGE_TYPE_TEXT="text";
    public static final String RESP_MESSAGE_TYPE_IMAGE="image";
    public static final String RESP_MESSAGE_TYPE_VOICE="voice";
    public static final String RESP_MESSAGE_TYPE_VIDEO="video";
    public static final String RESP_MESSAGE_TYPE_MUSIC="music";
    public static final String RESP_MESSAGE_TYPE_NEWS="news";

    /**
     * 解析微信发送来的请求XML
     * @return
     */
    public static Map<String,String> parseXML(HttpServletRequest request) throws Exception{
        //将解析结果存储到HashMap中
        Map<String,String> map = new HashMap<String, String>();
        //从request中获取输入流
        InputStream inputStream = request.getInputStream();
        //读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        //得到XML根元素
        Element root = document.getRootElement();
        //得到根元素的所有子节点
        List<Element> elementList = root.elements();

        //遍历所有的子节点
        for(Element e:elementList){
            map.put(e.getName(),e.getText());
        }

        //释放资源
        inputStream.close();
        inputStream = null;
        return map;
    }
    /**
     * 扩展xstream使其支持CDATA
     */
    private static XStream xStream = new XStream(new XppDriver(){
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out){
                //对所有的XML转换添加CDATA
                boolean cdata = true;
                public void startNode(String name,Class clazz){
                    super.startNode(name,clazz);
                }
                protected void writeText(QuickWriter writer, String text){
                    if(cdata){
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }else{
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * 文本消息转换为XML
     */
    public static String messageToXML(TextMessage textMessage){
        xStream.alias("xml",textMessage.getClass());
        return xStream.toXML(textMessage);
    }

//    /**
//     * 图文消息转换为XML
//     */
    public static String messageToXML(NewsMessage newsMessage){
        xStream.alias("xml",newsMessage.getClass());
        xStream.alias("item",new Article().getClass());
        return xStream.toXML(newsMessage);
    }


}
