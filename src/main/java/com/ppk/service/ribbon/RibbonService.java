package com.ppk.service.ribbon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ppk.entities.RibbonClientRequest;

import rx.Observable;

public interface RibbonService {
	<T> Observable<T> getRibbonRequestObservable(final RibbonClientRequest sptRibbonRequest, final TypeReference<T> type);
}
