package com.ppk.hystrix;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;

import com.netflix.ribbon.ServerError;
import com.netflix.ribbon.UnsuccessfulResponseException;
import com.netflix.ribbon.http.HttpResponseValidator;

public class ResponseValidator implements HttpResponseValidator {

	@Override
	public void validate(HttpClientResponse<ByteBuf> response)
			throws UnsuccessfulResponseException, ServerError {
		if (response.getStatus().code() / 100 >= 4) {
			throw new ServerError("Unexpected HTTP status code "
					+ response.getStatus());
		}

	}

}