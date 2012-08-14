package org.eclipse.scout.tutorial.jaxws.server.services.ws.consumer;

import java.util.List;

import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.eclipse.scout.jaxws.annotation.ScoutWebServiceClient;
import org.eclipse.scout.jaxws.service.AbstractWebServiceClient;
import org.eclipse.scout.service.IService2;
import org.eclipse.scout.tutorial.jaxws.server.services.ws.handler.DatabaseLogHandler;

import com.nexus6studio.services.StockQuoteService;
import com.nexus6studio.services.StockQuoteServiceSoap;

@ScoutWebServiceClient
public class StockQuoteServiceSoapWebServiceClient extends AbstractWebServiceClient<StockQuoteService, StockQuoteServiceSoap> implements IService2 {

  @Override
  protected String getConfiguredUrl() {
    return "http://services.nexus6studio.com/StockQuoteService.asmx";
  }

  @Override
  protected void execInstallHandlers(List<SOAPHandler<SOAPMessageContext>> handlers) {
    handlers.add(new DatabaseLogHandler());
  }
}