package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {

    TradingStrategy tradingStrategy;
    private static Integer defaultValue = 100;
    @Mock
    ExecutionService executionService;

    @Before
    public void init() {
        tradingStrategy = new TradingStrategy(executionService, defaultValue);
        TradingStrategy.getKeepsTrackOfPriceMovementsMap().put("IBM", 55.0);
    }

    @Test
    public void testBuyingStrategyWork() {
        tradingStrategy.priceUpdate("IBM", 56.0);
        Mockito.verify(executionService, Mockito.times(1)).buy(anyString(), anyDouble(), anyInt());
    }

    @Test
    public void testBuyingStrategyFalse(){
        tradingStrategy.priceUpdate("IBM", 51.0);
        Mockito.verify(executionService, Mockito.times(0)).buy(anyString(), anyDouble(), anyInt());
    }
}
