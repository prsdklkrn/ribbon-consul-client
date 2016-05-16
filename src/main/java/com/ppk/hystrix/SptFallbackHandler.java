package com.ppk.hystrix;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.ribbon.hystrix.FallbackHandler;

public class SptFallbackHandler implements FallbackHandler<ByteBuf> {

	@Override
	public Observable<ByteBuf> getFallback(HystrixInvokableInfo<?> hystrixInfo,
			Map<String, Object> requestProperties) {
		System.out.println("Fallback exception : "
				+ hystrixInfo.getFailedExecutionException());
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> configMap = new HashMap<String, String>();
		configMap.put("error", "Service has responded with error");
		byte[] bytes = null;
		try {
			bytes = objectMapper.writeValueAsBytes(configMap);
		} catch (JsonProcessingException e) {
			System.out.println("Error on Json Processing" + e);
			throw new RuntimeException(e);
		}
		ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer(bytes.length);
		byteBuf.writeBytes(bytes);
		return Observable.just(byteBuf);
	}
}
