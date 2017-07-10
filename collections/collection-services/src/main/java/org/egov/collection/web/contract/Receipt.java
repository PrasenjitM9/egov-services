package org.egov.collection.web.contract;


import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.WorkflowDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class Receipt   {

  private String tenantId;
  
  private String instrumentType;

  private String instrumentHeader;
  
  private BillInfo bill; //for billing-service
  
  @JsonProperty("BillInfoWrapper")
  private BillInfoWrapper billInfoWrapper; //for collection-service

  @JsonProperty("Bank")
  private Bank bank;
  
  @JsonProperty("BankAccount")
  private BankAccount bankAccount;  
  
  private AuditDetails auditDetails;
  
  transient private WorkflowDetails workflowDetails;

  }

