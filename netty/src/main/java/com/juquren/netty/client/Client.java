package com.juquren.netty.client;

import com.juquren.netty.handler.ClientLogicHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Client {
    public static void main(String[] args) {
        ChannelFactory factory = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool(),
                8
        );
        ClientBootstrap bootstrap = new ClientBootstrap(factory);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("handler", new ClientLogicHandler());
                return pipeline;
            }
        });

        bootstrap.connect(new InetSocketAddress("127.0.0.1", 8077));
        System.out.println("client start success!");
    }
}
