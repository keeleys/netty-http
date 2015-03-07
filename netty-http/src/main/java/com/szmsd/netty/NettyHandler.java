/*
 * @author TianJun
 * netty 处理类
 */
package com.szmsd.netty;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szmsd.core.DefaultProcess;

public class NettyHandler extends SimpleChannelInboundHandler<HttpObject> {
	private static Logger log = LoggerFactory.getLogger(NettyHandler.class);

	private final StringBuilder responseContent = new StringBuilder();

	private static final HttpDataFactory factory = new DefaultHttpDataFactory(
			DefaultHttpDataFactory.MINSIZE); // Disk if size exceed
	private HttpPostRequestDecoder decoder;
	
	private DefaultProcess nettyHttpProcess;
	
	
	public NettyHandler() {
		super();
		this.nettyHttpProcess = new DefaultProcess();
	}
	Map<String ,String> postMap = new HashMap<String ,String> ();
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HttpObject msg)
			throws Exception {
		if (msg instanceof HttpRequest) {
			
			postMap = new HashMap<String ,String> ();
			
			HttpRequest request = (HttpRequest) msg;

			if (HttpHeaders.is100ContinueExpected(request)) {
				ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, CONTINUE));
			}
			for (Entry<String, String> entry : request.headers()) {
				log.debug("HEADER: " + entry.getKey() + '=' + entry.getValue()+ "\r\n");
			}
			postMap.put("uri", request.getUri());
			postMap.put("method", request.getMethod().name());
			
			QueryStringDecoder decoderQuery = new QueryStringDecoder(request.getUri());
			Map<String, List<String>> uriAttributes = decoderQuery.parameters();
			for (Entry<String, List<String>> attr : uriAttributes.entrySet()) {
				for (String attrVal : attr.getValue()) {
					postMap.put(attr.getKey(), attrVal);
					log.debug("URI: " + attr.getKey() + '=' + attrVal + "\r\n");
				}
			}
			decoder = new HttpPostRequestDecoder(factory, request);
		}
		
		if (decoder != null) {
			if (msg instanceof HttpContent) {
				HttpContent chunk = (HttpContent) msg;
				decoder.offer(chunk);
				initPostParam();
				if ( chunk instanceof LastHttpContent) {
					String result = nettyHttpProcess.process(postMap);
					log.info("process result: "+result);
					responseContent.append(result);
					writeResponse(ctx);
					reset();
				}
			}
		}else{
			writeResponse(ctx);
		}

	}

	private void writeResponse(ChannelHandlerContext ctx) throws InterruptedException {
		ByteBuf buf =Unpooled.copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK,buf);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plan; charset=UTF-8");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
        ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private void initPostParam() throws IOException {
		try {
			while (decoder.hasNext()) {
				InterfaceHttpData data = decoder.next();
				if (data != null) {
					try {
						if (data.getHttpDataType() == HttpDataType.Attribute) {
							Attribute attribute = (Attribute) data;
							postMap.put(attribute.getName(),
									attribute.getValue());
							log.debug("post: " + attribute.getName() + '=' + attribute.getValue() + "\r\n");
						}
					} finally {
						data.release();
					}
				}
			}
		} catch (EndOfDataDecoderException e1) {
			log.debug("END OF CONTENT CHUNK BY CHUNK\r\n\r\n");
		}

	}
	private void reset() {
		decoder.destroy();
		decoder = null;
	}
	
}
