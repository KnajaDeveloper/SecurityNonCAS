// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.app2.app2t.web.security;


import com.app2.app2t.domain.security.AppRole;
import com.app2.app2t.domain.security.AppRoleMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import flexjson.JSONSerializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

privileged aspect AppRoleController_Custom_Controller_Json {

    protected static Logger LOGGER = LoggerFactory.getLogger(AppRoleController_Custom_Controller_Json.class);
    @RequestMapping(value = "/findSecurityAppRoleName", method = RequestMethod.GET, produces = "text/html", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> AppRoleController.findSecurityAppRoleName() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            List<AppRole> result = AppRole.findNameApprole();
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(result), headers, HttpStatus.OK);


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/insertAppRole", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String>  AppRoleController.insertAppRole(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            JSONObject jsonPlan = new JSONObject(json);
            String roleCode = jsonPlan.getString("roleCode");
            String roleName = jsonPlan.getString("roleName");

            List<AppRole> appRoleList = AppRole.findByRoleCode(roleCode, null);
            int rows = appRoleList.size();
            if (rows == 0) {
                AppRole.insertAppRole(roleCode, roleName);
                return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(null), headers, HttpStatus.CREATED);
            }
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(null), headers, HttpStatus.OK);

        } catch (Exception e) {
//            LOGGER.debug("error " + e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findRole", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> AppRoleController.findRole(
            @RequestParam(value = "id", required = false) Long roleId
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            AppRole appRole = AppRole.findAppRole(roleId);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(appRole), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/updateAppRole", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String>  AppRoleController.updateAppRole(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            JSONObject obj = new JSONObject(json);
            long roleId = obj.getLong("roleId");
            String roleCode = obj.getString("roleCode");
            String roleName = obj.getString("roleName");

            List<AppRole> appRoleList = AppRole.findByRoleCode(roleCode, roleId);
            int size = appRoleList.size();
            if (size == 0) {
                AppRole.updateRole(roleId, roleCode, roleName);
                return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(null), headers, HttpStatus.CREATED);
            }
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(null), headers, HttpStatus.OK);

        } catch (Exception e) {
//            LOGGER.debug("error " + e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/deleteAppRole", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String>  AppRoleController.deleteAppRole(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            int count = 0;
            JSONArray arrRoleId = new JSONArray(json);
            for (int i = 0; i < arrRoleId.length(); i++) {
                Long roleId = arrRoleId.getLong(i);
                if (AppRole.deleteRole(roleId)) {
                    count++;
                }
            }
            Map<String, Integer> result = new HashMap<>();
            result.put("countRemove", count);

            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception e) {
//            LOGGER.debug("error " + e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findPaggingDataAppRole", method = RequestMethod.GET, produces = "text/html", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> AppRoleController.findPaggingDataAppRole(
            @RequestParam(value = "maxResult", required = false) Integer maxResult
            , @RequestParam(value = "firstResult", required = false) Integer firstResult
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            List<AppRole> listRoleConstraint = AppRoleMenu.findConstraintRole();
            long[] arrRoleContraint = new long[listRoleConstraint.size()];
            for (int i = 0; i < listRoleConstraint.size(); i++) {
                arrRoleContraint[i] = listRoleConstraint.get(i).getId();
            }

            List<AppRole> result = AppRole.findAllAppRoles();
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = firstResult; i < maxResult + firstResult && i < result.size(); i++) {
                AppRole appRole = result.get(i);

                Map<String, Object> map = new HashMap<>();
                map.put("id", appRole.getId());
                map.put("roleCode", appRole.getRoleCode());
                map.put("roleName", appRole.getRoleName());

                if (Arrays.binarySearch(arrRoleContraint, appRole.getId()) >= 0) {          // constraint
                    map.put("constraint", true);
                } else {
                    map.put("constraint", false);
                }

                list.add(map);
            }
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(list), headers, HttpStatus.OK);
        } catch (Exception e) {
//            LOGGER.error("findEvaPeriodTime :{}", e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findPaggingSizeAppRole", method = RequestMethod.GET, produces = "text/html", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> AppRoleController.findPaggingSizeAppRole(
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            List<AppRole> result = AppRole.findAllAppRoles();
            Map data = new HashMap();
            data.put("size", result.size());
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(data), headers, HttpStatus.OK);
        } catch (Exception e) {
//            LOGGER.error("findEvaPeriodTime :{}", e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findRoleCodeInUsed", method = RequestMethod.GET, produces = "text/html", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> AppRoleController.findRoleCodeInUsed(
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            List<String> result = emRestService.getRoleCodeInUsed();
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception e) {
//            LOGGER.error("findEvaPeriodTime :{}", e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

}