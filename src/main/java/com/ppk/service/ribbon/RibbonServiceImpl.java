package com.ppk.service.ribbon;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.ribbon.ClientOptions;
import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.http.HttpRequestBuilder;
import com.netflix.ribbon.http.HttpRequestTemplate;
import com.netflix.ribbon.http.HttpRequestTemplate.Builder;
import com.ppk.entities.SptRibbonRequest;
import com.ppk.hystrix.ResponseValidator;
import com.ppk.hystrix.SptFallbackHandler;
import com.netflix.ribbon.http.HttpResourceGroup;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.reactivex.netty.channel.ContentTransformer;
import io.reactivex.netty.channel.StringTransformer;
import rx.Observable;
import rx.Subscriber;

@Service
public class RibbonServiceImpl implements RibbonService {

	@Override
	public <T> Observable<T> getRibbonRequestObservable(final SptRibbonRequest sptRibbonRequest,
			final TypeReference<T> type) {
		Observable<T> ribbonObservable = Observable.create(new Observable.OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> observer) {
				try {
					if (!observer.isUnsubscribed()) {
						RibbonRequest<ByteBuf> ribbonRequest = getRibbonRequest(sptRibbonRequest);
						ByteBuf responseBuf = ribbonRequest.execute();
						T responseMap = convertByteBufToMap(responseBuf, type);
						observer.onNext(responseMap);
						observer.onCompleted();
					}
				} catch (Exception e) {
					observer.onError(e);
				}
			}

			@SuppressWarnings("unchecked")
			private T convertByteBufToMap(ByteBuf responseBuf, TypeReference<T> type) throws IOException {
				T map = null;
				ObjectMapper mapper = new ObjectMapper();
				try {
					InputStream is = new ByteBufInputStream(responseBuf);
					map = (T) mapper.readValue(is, type);
				} catch (IOException jsonParseException) {
					System.out.println("error parsing json response" + jsonParseException);
					throw jsonParseException;
				} finally {
					responseBuf.release();
				}
				return map;
			}
		});

		return ribbonObservable;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HttpRequestTemplate<ByteBuf> httpGETLoadBalancer(SptRibbonRequest sptRibbonRequest) {
		HttpResourceGroup httpResourceGroup = Ribbon.createHttpResourceGroup(sptRibbonRequest.getResourceGroupName(),
				ClientOptions.create()
						.withMaxAutoRetriesNextServer(Integer.valueOf(sptRibbonRequest.getMaxAutoRetryForNextServe()))
						.withConfigurationBasedServerList(sptRibbonRequest.getCommaSeparatedServerList()));
		Builder builder = httpResourceGroup.newTemplateBuilder(sptRibbonRequest.getTemplateName(), ByteBuf.class)
				.withMethod(sptRibbonRequest.getHttpMethod()).withUriTemplate(sptRibbonRequest.getTemplateURI());
		updateHeaders(sptRibbonRequest.getHeaderMap(), builder);

		HttpRequestTemplate<ByteBuf> requestTemplate = builder.withFallbackProvider(new SptFallbackHandler())
				.withResponseValidator(new ResponseValidator()).build();
		return requestTemplate;

	}

	public RibbonRequest<ByteBuf> getRibbonRequest(SptRibbonRequest sptRibbonRequest) throws JsonProcessingException {
		HttpRequestTemplate<ByteBuf> ribbonRequestTemplate = httpGETLoadBalancer(sptRibbonRequest);
		HttpRequestBuilder<ByteBuf> ribbonRequestBuilder = ribbonRequestTemplate.requestBuilder();
		updateRequestProperties(ribbonRequestBuilder, sptRibbonRequest);
		RibbonRequest<ByteBuf> ribbonRequest = ribbonRequestBuilder.build();
		return ribbonRequest;
	}

	private void updateRequestProperties(HttpRequestBuilder<ByteBuf> ribbonRequestBuilder,
			SptRibbonRequest sptRibbonRequest) throws JsonProcessingException {
		if (sptRibbonRequest != null && (sptRibbonRequest.getPathVariablesMap() != null
				&& sptRibbonRequest.getPathVariablesMap().size() > 0)) {
			for (Map.Entry<String, String> entry : sptRibbonRequest.getPathVariablesMap().entrySet()) {
				ribbonRequestBuilder.withRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		if (sptRibbonRequest != null && sptRibbonRequest.getPostBody() != null) {
			ObjectMapper mapper = new ObjectMapper();
			String value = mapper.writeValueAsString(sptRibbonRequest.getPostBody());
			Observable<String> raw = Observable.just(value);
			ContentTransformer<String> transformer = new StringTransformer(Charset.forName("UTF-8"));
			ribbonRequestBuilder.withRawContentSource(raw, transformer);
		}
	}

	@SuppressWarnings("rawtypes")
	private void updateHeaders(Map<String, String> headerMap, Builder builder) {
		builder.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8")
				.withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8");
		if (headerMap != null && headerMap.size() > 0) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				builder.withHeader(key, value);
			}
		}
	}

}
