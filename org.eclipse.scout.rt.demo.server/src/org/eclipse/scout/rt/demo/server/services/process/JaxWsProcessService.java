package org.eclipse.scout.rt.demo.server.services.process;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.demo.server.services.ws.consumer.StockQuoteServiceSoapWebServiceClient;
import org.eclipse.scout.rt.demo.shared.services.process.IJaxWsProcessService;
import org.eclipse.scout.service.AbstractService;
import org.eclipse.scout.service.SERVICES;

import com.nexus6studio.services.StockQuote;
import com.nexus6studio.services.StockQuoteServiceSoap;

public class JaxWsProcessService extends AbstractService implements IJaxWsProcessService {

  @Override
  public double getCompanyLastValue(String symbol) throws ProcessingException {
    StockQuoteServiceSoapWebServiceClient service = SERVICES.getService(StockQuoteServiceSoapWebServiceClient.class);
    StockQuoteServiceSoap portType = service.getPortType();
    StockQuote stockQuote = portType.getStockQuote(symbol);

    return parseDouble(stockQuote.getLastTrade());
  }

  private Double parseDouble(String number) {
    if (number != null && number.equals("N/A")) {
      return null;
    }
    try {
      return Double.parseDouble(number);
    }
    catch (Exception e) {
      ScoutLogManager.getLogger(JaxWsProcessService.class).error("failed to parse double value '" + number + "'", e);
    }
    return null;
  }
}
