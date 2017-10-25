package org.egov.inv.web.controller;import org.egov.common.contract.request.RequestInfo;import org.egov.common.contract.response.ResponseInfo;import org.egov.inv.domain.model.Material;import org.egov.inv.domain.service.MaterialService;import org.egov.inv.web.contract.MaterialRequest;import org.egov.inv.web.contract.MaterialResponse;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.web.bind.annotation.*;import javax.validation.Valid;import java.util.List;@RestController@RequestMapping("/materials")public class MaterialController {    private MaterialService materialService;    @Autowired    public MaterialController(MaterialService materialService) {        this.materialService = materialService;    }    @PostMapping("/_create")    public MaterialResponse create(@RequestBody @Valid MaterialRequest materialRequest,                                   @RequestParam(value = "tenantId") String tenantId) {        List<Material> materials = materialService.save(materialRequest, tenantId);        return buildMaterialResponse(materials, materialRequest.getRequestInfo());    }    private MaterialResponse buildMaterialResponse(List<Material> materials, RequestInfo requestInfo) {        return MaterialResponse.builder()                .responseInfo(getResponseInfo(requestInfo))                .materials(materials)                .build();    }    private ResponseInfo getResponseInfo(RequestInfo requestInfo) {        return ResponseInfo.builder()                .apiId(requestInfo.getApiId())                .ver(requestInfo.getVer())                .resMsgId(requestInfo.getMsgId())                .resMsgId("placeholder")                .status("placeholder")                .build();    }}