/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
var srn = getUrlParameter('srn');
var lat, lng, myCenter, AV_status, serviceCode, keyword,  serviceDefinition= [];
var updateResponse = {};
var loadDD = new $.loadDD();
var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
var requestInfo = {};
requestInfo['RequestInfo'] = RequestInfo.requestInfo;
$(document).ready(function()
{

	if(localStorage.getItem('type') == 'CITIZEN'){
		$('.employee-action').remove();
	}else if(localStorage.getItem('type') == 'EMPLOYEE'){
		$('.citizen-action').remove();
	}else{
		$('.action-section').remove();
	}

	getComplaint();
	
	$('.slide-history-menu').click(function(){
		$('.history-slide').slideToggle();
		if($('#toggle-his-icon').hasClass('fa fa-angle-down'))
		{
			$('#toggle-his-icon').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
			//$('#see-more-link').hide();
			}else{
			$('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
			//$('#see-more-link').show();
		}
	});

	$('#status').change(function(){
		if($(this).val() == 'FORWARDED')
			$('#approvalDepartment, #approvalDesignation, #approvalPosition').attr('required', 'required');
		else
			$('#approvalDepartment, #approvalDesignation, #approvalPosition').removeAttr('required');
	})

	$('#ct-sel-jurisd').change(function(){
		getLocality($(this).val(),'');
	});
	
	$('#approvalDepartment').change(function(){
		getDesignation($(this).val());
	});
	
	$('#approvalDesignation').change(function(){
		getUser($('#approvalDepartment').val(),$(this).val())
	});

	$('#complaint-locate').on('show.bs.modal', function() {
		//Must wait until the render of the modal appear, thats why we use the resizeMap and NOT resizingMap!! ;-)
		//$('#show_address_in_map').html($('#address_locate').html());
		myCenter=new google.maps.LatLng(lat, lng);
		initialize();
		resizeMap();
	});

	$('#update-complaint').click(function(){
		if($('form#complaintUpdate').valid()){
			var obj = $(this);
			obj.attr("disabled", "disabled");
			complaintUpdate(obj);
		}
	});

});

function getComplaint(){
	$.ajax({
		url: "/pgr/seva/_search?tenantId=default&serviceRequestId="+srn,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo),
		beforeSend : function(){
			showLoader();
		},
		success : function(response){
			//console.log('Get complaint done!'+JSON.stringify(response));
			updateResponse = JSON.parse(JSON.stringify(response));

			if(response.serviceRequests.length == 0){
				bootbox.alert(translate('pgr.error.notvalid.srn'));
				hideLoader();
				return;
			}

			serviceCode = response.serviceRequests[0].serviceCode;

			var AV_locationId, AV_childLocationId, AV_receivingcenter, AV_departmentId, AV_stateId;

			for (var item of response.serviceRequests[0].attribValues) {
			    if(item['key']=='receivingCenter')
			    	AV_receivingcenter = item['name'];
			    else if(item['key']=='departmentId')
			    	AV_departmentId = item['name'];
		    	else if(item['key']=='locationId')
		    		AV_locationId = item['name'];
	    		else if(item['key']=='childLocationId')
	    			AV_childLocationId = item['name'];
	    		else if(item['key']=='stateId')
	    			AV_stateId = item['name'];
	    		else if(item['key']=='status'){
	    			AV_status = item['name'];
	    		}else if(item['key']=='keyword'){
	    			keyword = item['name'];
	    		}
			}

			if(localStorage.getItem('type') == 'EMPLOYEE'){
				if(AV_status == 'COMPLETED' || AV_status == 'REJECTED')
					$('.action-section').remove();
			}else if(localStorage.getItem('type') == 'CITIZEN'){
				if(AV_status == 'WITHDRAWN')
					$('.action-section').remove();
			}

			$.ajax({
				url: "/filestore/v1/files/tag?tenantId=default&tag="+srn,
				type : 'GET',
				success : function(fileresponse){
					//console.log(fileresponse.files)

					//History
					$.ajax({
						url : "/workflow/history?tenantId=default&workflowId="+AV_stateId,
						type : 'GET',
						success : function(work_response){

							//console.log('Work flow histry',JSON.stringify(work_response));

							var wf_response = {};
							wf_response['workflow'] = work_response;

							var wf_source   = $("#wfcomplaint-script").html();
							var wf_template = Handlebars.compile(wf_source);
							$('.wfcomplaint').append(wf_template(wf_response));

							if(localStorage.getItem('type') == 'CITIZEN' && AV_status != 'COMPLETED')
								$('.feedback').remove();

							lat = response.serviceRequests[0].lat;
							lng = response.serviceRequests[0].lng;

							if (lat != '0' && lng != '0')
								getAddressbyLatLng(lat, lng, response);

							getDepartmentbyId(AV_departmentId, response);

							if(AV_receivingcenter)
								getReceivingCenterbyId(AV_receivingcenter, response);

							if(AV_locationId)
								getBoundarybyId(AV_locationId,'LocationName', response);

							if(AV_childLocationId)
								getBoundarybyId(AV_childLocationId, 'ChildLocationName', response);

							response['files'] = fileresponse.files;
							var source   = $("#viewcomplaint-script").html();
							var template = Handlebars.compile(source);
							$('.viewcomplaint').append(template(response));
							translate();
							///console.log('response with files',JSON.stringify(response));

							if(keyword != 'Complaint')
								loadServiceDefinition(response);

							if(localStorage.getItem('type') == 'CITIZEN' && keyword != 'Complaint'){
								
							}else{
								var loadDD = new $.loadDD();
								nextStatus(loadDD);
							}

							if(keyword != 'Complaint' && localStorage.getItem('type') == 'CITIZEN')
								$('.action-section').remove();

							if(localStorage.getItem('type') == 'EMPLOYEE'){
								$('#status').attr('required','required');
								var wardId = AV_locationId;
								var localityid = AV_childLocationId;
								var serviceName = response.serviceRequests[0].serviceName;
								complaintType(loadDD, serviceName);
								getWard(loadDD, wardId);
								getLocality(wardId, localityid);
								getDepartment(loadDD);
							}

						},
						error:function(){
							bootbox.alert('Workflow Error!');
						},
						complete : function(){
							hideLoader();
						}
					});
				},
				error : function(){
					bootbox.alert('Media file Error!');
					hideLoader();
				},
				complete : function(){
					//console.log('Media Complete function called!');
				}
			});
		},
		error : function(jqXHR, textStatus){
			bootbox.alert( "Request failed!");
			hideLoader();
		},
		complete : function(){
			//console.log('Main complete called')
		}
	});
}

function complaintUpdate(obj){
	
	var duplicateResponse, req_obj = {};
	duplicateResponse =  JSON.parse(JSON.stringify(updateResponse));

	req_obj['RequestInfo'] = {'authToken':localStorage.getItem('auth')};
	req_obj['serviceRequest'] = duplicateResponse.serviceRequests[0];
	//req_obj['RequestInfo']['authToken'] = localStorage.getItem('auth');

	var dat = new Date().toLocaleDateString();
	var time = new Date().toLocaleTimeString();
	var date = dat.split("/").join("-");
	req_obj.serviceRequest['updatedDatetime'] = date+' '+time;
	req_obj.serviceRequest['tenantId'] = 'default';
	req_obj.serviceRequest['isAttribValuesPopulated'] = true;

	for (var i = 0, len = req_obj.serviceRequest.attribValues.length; i < len; i++) {
		if(req_obj.serviceRequest.attribValues[i]['key'] == 'status'){
			req_obj.serviceRequest.attribValues[i]['name'] = $('#status').val() ? $('#status').val() : AV_status;
		}
		else if(req_obj.serviceRequest.attribValues[i]['key'] == 'assigneeId'){
			req_obj.serviceRequest.attribValues[i]['key'] = 'assignmentId';
			if($("#approvalPosition").val())
				req_obj.serviceRequest.attribValues[i]['name'] = $("#approvalPosition").val();
		}
	}

	var finobj = {};
	finobj = {
	    key: 'approvalComments',
	    name: $('#approvalComment').val()
	};
	req_obj.serviceRequest.attribValues.push(finobj);

	if($("#approvalDepartment").val()){
		finobj = {
		    key: 'departmentName',
		    name: $("#approvalDepartment option:selected").text()
		};
		req_obj.serviceRequest.attribValues.push(finobj);
	}
	
	$.ajax({
		url: "/pgr/seva/_update",
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		beforeSend : function(){
			showLoader();
		},
		data : JSON.stringify(req_obj),
		success : function(response){

			var filefilledlength = Object.keys(filefilled).length;
			
			if(filefilledlength > 0){
				
				var formData=new FormData();
				formData.append('tenantId', 'default');
				formData.append('module', 'PGR');
				formData.append('tag', srn);
				// Main magic with files here

				$('input[name=file]').each(function(){
					var file = $(this)[0].files[0];
					formData.append('file', file); 
				});

				$.ajax({
					url: "/filestore/v1/files",
					type : 'POST',
					async : false,
					// THIS MUST BE DONE FOR FILE UPLOADING
					contentType: false,
					processData : false,
					data : formData,
					success: function(fileresponse){
						//console.log('file upload success');
						bootbox.alert(translate('pgr.msg.success.grievanceupdated'), function(){ 
							window.location.reload();
						});
					},
					error: function(){
						bootbox.alert('Media file failed!');
						obj.removeAttr("disabled");
						hideLoader();
					}
				});
			}else{
				bootbox.alert(translate('pgr.msg.success.grievanceupdated'), function(){ 
					window.location.reload();
				});
			}
		},
		error : function(){
			bootbox.alert('Error while update!');
		},
		complete : function(){
			obj.removeAttr("disabled");
			hideLoader();
		}
	});
}

function complaintType(loadDD, serviceName){
	$.ajax({
		url: "/pgr/services/_search?type=ALL&tenantId=default",
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json"
	}).done(function(data) {
		loadDD.load({
			element:$('#complaintType'),
			data:data.complaintTypes,
			keyValue:'serviceCode',
			keyDisplayName:'serviceName'
		});
		$('#complaintType option').each(function(){
			if($(this).text() == serviceName){
				$(this).attr('selected', 'selected');
			}
		});
	});
}

function nextStatus(loadDD){
	var appendURL = '';
	
	if(keyword != 'Complaint')
		appendURL = '&keyword=Deliverable_service';
	else
		appendURL = '';

	//List of all status and get the code respective status
	$.ajax({
		url : '/workflow/v1/nextstatuses/_search?tenantId=default&currentStatusCode='+AV_status+appendURL,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#status'),
			data:data.statuses,
			keyValue:'code',
			keyDisplayName:'name'
		});
		$('#status').val(AV_status);
		$('#status').val() ? $('#status').val() : $('#status').val('');
	});

}

function getWard(loadDD, wardId){
	$.ajax({
		url: "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?tenantId=default&boundaryTypeName=Ward&hierarchyTypeName=Administration",
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#ct-sel-jurisd'),
			data:data.Boundary,
			keyValue:'id',
			keyDisplayName:'name'
		});
		$('#ct-sel-jurisd').val(wardId);
		$('#ct-sel-jurisd').val() ? $('#ct-sel-jurisd').val() : $('#ct-sel-jurisd').val('');
	});
}

function getLocality(boundaryId, localityid){
	$.ajax({
		url: "/egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId="+boundaryId,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#location'),
			data:data.Boundary,
			keyValue:'id',
			keyDisplayName:'name'
		});
		if(localityid){
			$('#location').val(localityid);
			$('#location').val() ? $('#location').val() : $('#location').val('');	
		}
	});
}

function getDepartment(loadDD){
	$.ajax({
		url: "/eis/departments?tenantId=default",
		type : 'GET'
	}).done(function(data) {
		loadDD.load({
			element:$('#approvalDepartment'),
			placeholder : 'Select Deparment', // default - Select(optional)
			data:data.Department,
			keyValue:'id',
			keyDisplayName:'name'
		});
	});
}

function getDesignation(depId){
	$.ajax({
		url: "/eis/designationByDepartmentId?tenantId=default&id="+depId,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#approvalDesignation'),
			placeholder : 'Select Designation', // default - Select(optional)
			data:data.Designation,
			keyValue:'id',
			keyDisplayName:'name'
		});
	});
}

function getUser(depId, desId){
	//console.log(depId, desId);
	$.ajax({
		url: "/eis/assignmentsByDeptOrDesignId?tenantId=default&deptId="+depId+"&desgnId="+desId,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo),
	}).done(function(data) {
		loadDD.load({
			element:$('#approvalPosition'),
			placeholder : 'Select Position', // default - Select(optional)
			data:data.Assignment,
			keyValue:'position',
			keyDisplayName:'employee'
		});
	});
}

function getDepartmentbyId(departmentId, response){
	$.ajax({
		url : '/eis/departments?tenantId=default&id='+departmentId,
		async : false,
		success : function(depresponse){
			var obj = {};
			obj['key'] = 'departmentName';
			obj['name'] = depresponse.Department[0].name;
			response.serviceRequests[0].attribValues.push(obj);
		},
		error : function(){
			bootbox.alert('Loading departmentName failed');
		}
	});
}

function getBoundarybyId(id, name, response){
	$.ajax({
		url : '/egov-location/boundarys?tenantId=default&boundary='+id,
		async : false,
		success : function(lresponse){
			var obj = {};
			obj['key'] = name;
			obj['name'] = lresponse.Boundary[0].name;
			response.serviceRequests[0].attribValues.push(obj);
		},
		error : function(){
			bootbox.alert('Loading location failed');
		}
	});
}

function getReceivingCenterbyId(receivingcenter, response){
	$.ajax({
		url : "/pgr/receivingcenter/_search?tenantId=default&id="+receivingcenter,
		type: 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
,		async : false,
		success : function(centerReponse){
			var obj = {};
			obj['key'] = 'recCenterText';
			obj['name'] = centerReponse.receivingCenters[0].name; 
			response.serviceRequests[0].attribValues.push(obj);
		}
	});
}

function getAddressbyLatLng(lat, lng, response){
	$.ajax({ 
        type: "POST",
        async : false,
        url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lng,
        dataType: 'json',
        success : function(data){
        	var obj = {};
			obj['key'] = 'latlngAddress'
			obj['name'] = data.results[0].formatted_address;
			response.serviceRequests[0].attribValues.push(obj);
        }
	});
}

function loadServiceDefinition(searchResponse){
	$.ajax({
		url: "/pgr/services/_search?type=all&tenantId=default",
		type : 'POST',
		async : false,
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		beforeSend : function(){
			showLoader();
		},
		success : function(data) {
			serviceResult = (data.complaintTypes).filter(function( obj ) {
				return (obj.keywords).indexOf('deliverable') > -1;
			});
			for(var i=0;i<serviceResult.length;i++){
				if(serviceResult[i].serviceCode == serviceCode){
					callToLoadDefinition(searchResponse);
					return;
				}
			}
		}
	});
}

function callToLoadDefinition(searchResponse){
	$.ajax({
		url: "/pgr/servicedefinition/_search?tenantId=default&serviceCode="+serviceCode,
		type : 'POST',
		data : JSON.stringify(requestInfo),
		async : false,
		dataType: 'json',
		processData : false,
		beforeSend : function(){
			showLoader();
		},
		contentType: "application/json",
		success : function(data){

			//console.log('OR',JSON.stringify(searchResponse.serviceRequests[0].attribValues));
			serviceDefinition = searchResponse.serviceRequests[0].attribValues;

			var renderFields = new $.renderFields();
			var finTemplate = renderFields.render({'data':data.attributes,'create':false});

			if(finTemplate.formFields){
				$('#servicesBlock').parents('.panel-primary').removeClass('hide');
				$('#servicesBlock').prepend(finTemplate.formFields);
			}
			else
				$('#servicesBlock').parents('.panel-primary').hide();

			if(finTemplate.checklist){
				$('#servicesBlockClist').parents('.panel-primary').removeClass('hide');
				$('#servicesBlockClist').prepend(finTemplate.checklist);
			}
			else
				$('#servicesBlockClist').parents('.panel-primary').hide();

			if(finTemplate.documents){
				$('#servicesBlockDocs').parents('.panel-primary').removeClass('hide');
				$('#servicesBlockDocs').prepend(finTemplate.documents);
			}
			else
				$('#servicesBlockDocs').parents('.panel-primary').hide();

			patternvalidation();
			translate();

			$('.appForm *').filter(':input').each(function(){
			    var obj  = getObjFromArray(serviceDefinition, $(this).attr('name'));
			    if(obj)
			    	$(this).val(obj.name);
			});

			$('.checkForm *').filter(':input').each(function(){
				var obj  = getObjFromArray(serviceDefinition, $(this).attr('name'));
				if(obj){
					if(JSON.parse(obj.name))
				    	$(this).prop('checked', JSON.parse(obj.name)).attr('disabled', "disabled");
				    else
				    	$(this).prop('checked', JSON.parse(obj.name));
				}
			});

			$('#servicesBlockDocs *').filter(':input').each(function(){
				var obj  = getObjFromArray(serviceDefinition, $(this).attr('name'));
				//console.log($(this).attr('name'), obj ? obj.name : 'nothing')
				if(obj){
					$(this).parent('div').html('<a href="/filestore/v1/files/id?fileStoreId='+obj.name+'&tenantId=default" download>Download</a>')
					$(this).remove();
				}
			});
			

		},
		error: function(){
			//bootbox.alert('Error!');
		},
		complete : function(){
			hideLoader();
		}
	});
}

function resizeMap() {
	if(typeof map =="undefined") return;
	setTimeout( function(){resizingMap();} , 400);
}

function resizingMap() {
	if(typeof map =="undefined") return;
	var center = map.getCenter();
	google.maps.event.trigger(map, "resize");
	map.setCenter(center); 
}

function initialize() {
	
	marker=new google.maps.Marker({
		position:myCenter
	});
	
	var mapProp = {
		center:myCenter,
		mapTypeControl: true,
		zoom:12,
		mapTypeId:google.maps.MapTypeId.ROADMAP
	};
	
	map=new google.maps.Map(document.getElementById("normal"),mapProp);
	
	marker.setMap(map);
	
};

google.maps.event.addDomListener(window, 'load', initialize);

google.maps.event.addDomListener(window, "resize", resizingMap());

Handlebars.registerHelper('contains', function(string, checkString) {
	var n = string.includes(checkString);
	return n;
});

function getObjFromArray(arr, value) {
  var result  = arr.filter(function(o){return o.key == value;} );
  return result? result[0] : null; // or undefined
}