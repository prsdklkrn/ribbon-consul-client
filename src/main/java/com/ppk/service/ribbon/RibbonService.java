package com.ppk.service.ribbon;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ppk.entities.SptRibbonRequest;

import rx.Observable;

public interface RibbonService {
	<T> Observable<T> getRibbonRequestObservable(final SptRibbonRequest sptRibbonRequest, final TypeReference<T> type);
}
