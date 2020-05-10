package com.juquren.netty.client;

import com.juquren.netty.handler.ClientLogicHandler;
import lombok.Data;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Data
public class Client {
    private Channel channel = null;
    private ClientBootstrap bootstrap;

    public void doConnect() {
        ChannelFactory factory = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool(),
                8
        );
        bootstrap = new ClientBootstrap(factory);
        bootstrap.setOption("keepAlive", true);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("handler", new ClientLogicHandler());
                return pipeline;
            }
        });

        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8077));
        System.out.println("client start success!");
        boolean ret = future.awaitUninterruptibly(3000, TimeUnit.MILLISECONDS);
        if (ret && future.isSuccess()) {
            this.channel = future.getChannel();
        }
    }
}
