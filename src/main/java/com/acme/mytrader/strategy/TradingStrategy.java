package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */

@Getter
@EqualsAndHashCode
public class TradingStrategy implements PriceListener {

    /**
     * This map keepsTrackOfPriceMovementsMap is designed to store the current stock prices of companies.
     * The PriceSource interface must update the value of companies' stock prices in real time and
     * save this info in keepsTrackOfPriceMovementsMap.
     */
    private static HashMap<String, Double> keepsTrackOfPriceMovementsMap = new HashMap<>();

    private ExecutionService executionService;
    private Integer value;

    public TradingStrategy(ExecutionService executionService, Integer value) {
        this.executionService = executionService;
        this.value = value;
    }

    /**
     * The method responsible for calling the buying strategy
     * @param security
     * @param price
     */
    public void priceUpdate(String security, double price) {
        try {
            startBuyingStrategy(security, price);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method responsible for buying the stock at the price we want. Imitates the flow of days for more clarity.
     * When PriceSource is running all the time, prices should change and bidding can end on any of the days.
     * @param security
     * @param priceForBuying
     * @throws InterruptedException
     */
    private void startBuyingStrategy(String security, Double priceForBuying) throws InterruptedException {
        boolean biddingContinues = true;
        int dayCount = 0;
        while (biddingContinues) {
            Thread.sleep(2000);
            dayCount++;
            System.out.printf("\n Day № %s gone", dayCount);
            Double currentPrice = keepsTrackOfPriceMovementsMap.get(security);
            if (currentPrice != null && currentPrice < priceForBuying) {
                executionService.buy(security, priceForBuying, value);
                System.out.println("\nThe shares are bought at the price we want. The auto-buy is complete.");
                biddingContinues = false;
            }
            if (dayCount > 10) {
                biddingContinues = false;
                System.out.println("\nIt was last auto-buy day at this price. Schedule auto-buy again if you want to continue.");
            }
        }
    }

    /**
     * Method needed to make changes to the list with stock prices.
     * @return
     */
    public static HashMap<String, Double> getKeepsTrackOfPriceMovementsMap() {
        return keepsTrackOfPriceMovementsMap;
    }
}