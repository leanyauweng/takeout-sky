package com.itheima.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ServerEndpoint("/ws/{sid}")
@Component
public class WebSocketServer {
    private static Map<String,Session> sessionMap = new HashMap();


    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("客户端"+ sid +"连接成功");
        sessionMap.put(sid, session);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("收到客户端：" + sid +"的消息" + message);
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("连接断开：" + sid);
        sessionMap.remove(sid);
    }

    public void sendToAllClient(String message){
        Collection<Session> sessions = sessionMap.values();
        log.info("推送消息给所有客户端: {}", message);
        for (Session session : sessions) {
            try{
                session.getBasicRemote().sendText(message);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
