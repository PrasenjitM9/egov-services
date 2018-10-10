package org.egov.tl.service.notification;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tl.config.TLConfiguration;
import org.egov.tl.repository.TLRepository;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.util.NotificationUtil;
import org.egov.tl.web.models.*;
import org.egov.tracer.model.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Service
public class PaymentNotificationService {


    private TLConfiguration config;

    private TradeLicenseService tradeLicenseService;

    private NotificationUtil util;


    @Autowired
    public PaymentNotificationService(TLConfiguration config, TradeLicenseService tradeLicenseService,
                                      NotificationUtil util) {
        this.config = config;
        this.tradeLicenseService = tradeLicenseService;
        this.util = util;
    }





    final String tenantIdKey = "tenantId";

    final String businessServiceKey = "businessService";

    final String consumerCodeKey = "consumerCode";

    final String payerMobileNumberKey = "mobileNumber";

    final String paidByKey = "paidBy";

    final String amountPaidKey = "amountPaid";

    final String receiptNumberKey = "receiptNumber";



    public void process(HashMap<String, Object> record){
        try{
            String jsonString = new JSONObject(record).toString();
            DocumentContext documentContext = JsonPath.parse(jsonString);
            Map<String,String> valMap = enrichValMap(documentContext);
            RequestInfo requestInfo = new RequestInfo();

            if(valMap.get(businessServiceKey).equalsIgnoreCase(config.getBusinessService())){
                TradeLicense license = getTradeLicenseFromConsumerCode(valMap.get(tenantIdKey),valMap.get(consumerCodeKey),
                                                                       requestInfo);
                String localizationMessages = util.getLocalizationMessages(license.getTenantId(),requestInfo);
                List<SMSRequest> smsRequests = getSMSRequests(license,valMap,localizationMessages);
                util.sendSMS(smsRequests);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    private List<SMSRequest> getSMSRequests(TradeLicense license, Map<String,String> valMap,String localizationMessages){
            List<SMSRequest> ownersSMSRequest = getOwnerSMSRequest(license,valMap,localizationMessages);
            SMSRequest payerSMSRequest = getPayerSMSRequest(valMap,localizationMessages);

            List<SMSRequest> totalSMS = new LinkedList<>();
            totalSMS.addAll(ownersSMSRequest);
            totalSMS.add(payerSMSRequest);

            return totalSMS;
    }


    private List<SMSRequest> getOwnerSMSRequest(TradeLicense license, Map<String,String> valMap,String localizationMessages){
        String message = util.getOwnerPaymentMsg(valMap,localizationMessages);

        HashMap<String,String> mobileNumberToOwnerName = new HashMap<>();
        license.getTradeLicenseDetail().getOwners().forEach(owner -> {
            if(owner.getMobileNumber()!=null)
                mobileNumberToOwnerName.put(owner.getMobileNumber(),owner.getName());
        });

        List<SMSRequest> smsRequests = new LinkedList<>();

        for(Map.Entry<String,String> entrySet : mobileNumberToOwnerName.entrySet()){
            String customizedMsg = message.replace("<1>",entrySet.getValue());
            smsRequests.add(new SMSRequest(entrySet.getKey(),customizedMsg));
        }
        return smsRequests;
    }


    private SMSRequest getPayerSMSRequest(Map<String,String> valMap,String localizationMessages){
        String message = util.getPayerPaymentMsg(valMap,localizationMessages);
        String customizedMsg = message.replace("<1>",valMap.get(paidByKey));
        SMSRequest smsRequest = new SMSRequest(valMap.get(payerMobileNumberKey),customizedMsg);
        return smsRequest;
    }






    private Map<String,String> enrichValMap(DocumentContext context){
        Map<String,String> valMap = new HashMap<>();
        try{
            valMap.put(businessServiceKey,context.read("$.Receipt[0].Bill[0].billDetails[0].businessService"));
            valMap.put(consumerCodeKey,context.read("$.Receipt[0].Bill[0].billDetails[0].consumerCode"));
            valMap.put(tenantIdKey,context.read("$.Receipt[0].tenantId"));
            valMap.put(payerMobileNumberKey,context.read("$.Receipt[0].Bill[0].mobileNumber"));
            valMap.put(paidByKey,context.read("$.Receipt[0].Bill[0].paidBy"));
            Integer amountPaid = context.read("$.Receipt[0].instrument.amount");
            valMap.put(amountPaidKey,amountPaid.toString());
            valMap.put(receiptNumberKey,context.read("$.Receipt[0].Bill[0].billDetails[0].receiptNumber"));

        }
        catch (Exception e){
            e.printStackTrace();
            throw new CustomException("RECEIPT ERROR","Unable to fetch values from receipt");
        }
        return valMap;
    }





    private TradeLicense getTradeLicenseFromConsumerCode(String tenantId,String consumerCode,RequestInfo requestInfo){

        TradeLicenseSearchCriteria searchCriteria = new TradeLicenseSearchCriteria();
        searchCriteria.setApplicationNumber(consumerCode);
        searchCriteria.setTenantId(tenantId);
        List<TradeLicense> licenses = tradeLicenseService.getLicensesWithOwnerInfo(searchCriteria,requestInfo);

        if(CollectionUtils.isEmpty(licenses))
            throw new CustomException("INVALID RECEIPT","No license found for the consumerCode: "
                    +consumerCode+" and tenantId: "+tenantId);

        if(licenses.size()!=1)
            throw new CustomException("INVALID RECEIPT","Multiple license found for the consumerCode: "
                    +consumerCode+" and tenantId: "+tenantId);

        return licenses.get(0);

    }







}
