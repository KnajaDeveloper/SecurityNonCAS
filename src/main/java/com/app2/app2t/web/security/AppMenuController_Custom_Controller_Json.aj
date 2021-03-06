// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.app2.app2t.web.security;

import com.app2.app2t.domain.security.AppMenu;
import com.app2.app2t.domain.security.AppRole;
import com.app2.app2t.domain.security.AppRoleMenu;
import com.google.gson.JsonArray;
import flexjson.JSONSerializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

privileged aspect AppMenuController_Custom_Controller_Json {

    @RequestMapping(value = "/insertAppMenu", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String>  AppMenuController.insertAppMenu(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            JSONObject obj = new JSONObject(json);
            String link = obj.getString("link");
            String menuTh = obj.getString("menuTh");
            String menuEn = obj.getString("menuEn");
            String controller = obj.getString("controller");
            int level = obj.getInt("level");
            int sequent = obj.getInt("sequent");
            long parent = obj.getLong("parent");
            String menuIcon = obj.getString("menuIcon");
            JSONArray arrRoleId = obj.getJSONArray("arrRoleId");

            // check duplicate link, controller, sequent same level
            List<AppMenu> appMenuByLink = AppMenu.findAppMenuByLink(link);
            List<AppMenu> appMenuByController = AppMenu.findAppMenuByController(controller);
            List<AppMenu> appMenuBySequent = AppMenu.findAppMenuBySequent(sequent, level, parent);

            int rowCountSameLink = appMenuByLink.size();
            int rowCountSameController = appMenuByController.size();
            int rowCountSameSequent = appMenuBySequent.size();
            int rowCount = rowCountSameLink + rowCountSameController + rowCountSameSequent;

            if (rowCount == 0) {
                AppMenu appMenu = AppMenu.insertAppMenu(link, menuTh, menuEn, controller, level, sequent, parent, menuIcon);
                for (int i = 0; i < arrRoleId.length(); i++) {
                    AppRole appRole = AppRole.findAppRole(arrRoleId.getLong(i));
                    AppRoleMenu.insertAppRoleMenu(appMenu, appRole);
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("rowCountSameLink", rowCountSameLink);
            map.put("rowCountSameController", rowCountSameController);
            map.put("rowCountSameSequent", rowCountSameSequent);

            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(map), headers, HttpStatus.CREATED);

        } catch (Exception e) {
//            LOGGER.debug("error " + e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/updateAppMenu", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String>  AppMenuController.updateAppMenu(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            JSONObject obj = new JSONObject(json);
            Long menuId = obj.getLong("menuId");
            String link = obj.getString("link");
            String menuTh = obj.getString("menuTh");
            String menuEn = obj.getString("menuEn");
            String controller = obj.getString("controller");
            int level = obj.getInt("level");
            int sequent = obj.getInt("sequent");
            long parent = obj.getLong("parent");
            String menuIcon = obj.getString("menuIcon");
            JSONArray arrRoles = obj.getJSONArray("roles");

            // check duplicate link, controller, sequent same level
            List<AppMenu> appMenuByLink = AppMenu.findAppMenuByLink(link);
            List<AppMenu> appMenuByController = AppMenu.findAppMenuByController(controller);
            List<AppMenu> appMenuBySequent = AppMenu.findAppMenuBySequent(sequent, level, parent);

            for (int i = 0; i < appMenuByLink.size(); i++) {
                if (appMenuByLink.get(i).getId().equals(menuId))
                    appMenuByLink.remove(i);
            }

            for (int i = 0; i < appMenuByController.size(); i++) {
                if (appMenuByController.get(i).getId().equals(menuId))
                    appMenuByController.remove(i);
            }

            for (int i = 0; i < appMenuBySequent.size(); i++) {
                if (appMenuBySequent.get(i).getId().equals(menuId))
                    appMenuBySequent.remove(i);
            }

            int rowCountSameLink = appMenuByLink.size();
            int rowCountSameController = appMenuByController.size();
            int rowCountSameSequent = appMenuBySequent.size();
            int rowCount = rowCountSameLink + rowCountSameController + rowCountSameSequent;

            if (rowCount == 0) {
                AppMenu appMenu = AppMenu.updateAppMenu(menuId, link, menuTh, menuEn, controller, level, sequent, parent, menuIcon);

                for (int i = 0; i < arrRoles.length(); i++) {
                    JSONObject role = arrRoles.getJSONObject(i);
                    Long roleId = role.getLong("roleId");
                    Boolean isCheck = role.getBoolean("used");

                    AppRole appRole = AppRole.findAppRole(roleId);
                    AppRoleMenu appRoleMenu = AppRoleMenu.findAppRoleMenuByMenuIdAndRoleId(menuId, roleId);

                    if (isCheck && appRoleMenu == null)     // create
                        AppRoleMenu.insertAppRoleMenu(appMenu, appRole);
                    
                    if (!isCheck && appRoleMenu != null)    // delete
                        AppRoleMenu.deleteAppRoleMenu(menuId, roleId);
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("rowCountSameLink", rowCountSameLink);
            map.put("rowCountSameController", rowCountSameController);
            map.put("rowCountSameSequent", rowCountSameSequent);

            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(map), headers, HttpStatus.OK);

        } catch (Exception e) {
//            LOGGER.debug("error " + e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/deleteAppMenu", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String>  AppMenuController.deleteAppMenu(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            int count = 0;
            JSONArray arrMenuId = new JSONArray(json);
            for (int i = 0; i < arrMenuId.length(); i++) {
                Long menuId = arrMenuId.getLong(i);
                if (AppMenu.deleteMenu(menuId)) {
                    count++;
                }
            }
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(count), headers, HttpStatus.OK);
        } catch (Exception e) {
//            LOGGER.debug("error " + e);
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findMenuParent", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> AppMenuController.findMenuParent(
            @RequestParam(value = "level", required = false) Integer level
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            List<AppMenu> appMenuList = AppMenu.findMenuParent(level);
            List<Map<String, Object>> list = new ArrayList<>();
            for (AppMenu appMenu : appMenuList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", appMenu.getId());
                map.put("menu_th", appMenu.getMenu_t_name());
                map.put("menu_en", appMenu.getMenu_e_name());

                list.add(map);
            }

            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(list), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findAllRole", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> AppMenuController.findAllRole() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            List<AppRole> appRoleList = AppRole.findAllAppRoles();
            List<Map<String, Object>> list = new ArrayList<>();
            for (AppRole appRole : appRoleList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", appRole.getId());
                map.put("roleName", appRole.getRoleName());

                list.add(map);
            }

            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(list), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/findMenu", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> AppMenuController.findMenu(
            @RequestParam(value = "id", required = false) Long menuId
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            AppMenu appMenu = AppMenu.findAppMenu(menuId);
            Map<String, Object> result = new HashMap<>();
            result.put("id", appMenu.getId());
            result.put("controller", appMenu.getController());
            result.put("link", appMenu.getLink());
            result.put("menuLevel", appMenu.getMenuLevel());
            result.put("menu_e_name", appMenu.getMenu_e_name());
            result.put("menu_t_name", appMenu.getMenu_t_name());
            result.put("parent", appMenu.getParent());
            result.put("menuIcon", appMenu.getMenuIcon());
            result.put("sequent", appMenu.getSegment());

            List<AppRoleMenu> appRoleMenuList = AppRoleMenu.findAppRoleMenuByMenuId(menuId);
            List<Long> listRoleId = new ArrayList<>();
            for (AppRoleMenu appRoleMenu : appRoleMenuList) {
                listRoleId.add(appRoleMenu.getAppRole().getId());
            }
            result.put("role", listRoleId);

            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findMenuByAppRoleCode", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> AppMenuController.findMenuByAppRoleCode(
            @RequestParam(value = "appRoleCode", required = false) String appRoleCode
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try {
            List<AppRole> listAppRole = AppRole.findByRoleCode(appRoleCode, null);
            AppRole appRole = listAppRole.get(0);
            List<AppMenu> listAppMenu = AppMenu.findAppMenuByRole(appRole.getId());

            List<Map<String, Object>> result = new ArrayList<>();

            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(listAppMenu), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findPaggingDataAppMenu", method = RequestMethod.GET, produces = "text/html", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> AppMenuController.findPaggingDataAppMenu(
            @RequestParam(value = "level", required = false) Integer level
            , @RequestParam(value = "maxResult", required = false) Integer maxResult
            , @RequestParam(value = "firstResult", required = false) Integer firstResult
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try{
            List<AppMenu> listMenuConstraint = AppRoleMenu.findConstraintMenu();
            long[] arrMenuContraint = new long[listMenuConstraint.size()];
            for (int i = 0; i < listMenuConstraint.size(); i++) {
                arrMenuContraint[i] = listMenuConstraint.get(i).getId();
            }

            List<AppMenu> result = AppMenu.findAppMenuByLevelPaggingData(level, firstResult, maxResult);
            List<Map<String, Object>> list = new ArrayList<>();
            for(int i = 0; i < result.size(); i++) {
                AppMenu appMenu = result.get(i);
                Map<String, Object> map = new HashMap<>();
                map.put("id", appMenu.getId());
                map.put("link", appMenu.getLink());
                map.put("menu_e_name", appMenu.getMenu_e_name());
                map.put("menu_t_name", appMenu.getMenu_t_name());
                map.put("controller", appMenu.getController());
                map.put("level", appMenu.getMenuLevel());
                map.put("segment", appMenu.getSegment());
                map.put("parent", appMenu.getParent());
                map.put("menuIcon", appMenu.getMenuIcon());

                if (Arrays.binarySearch(arrMenuContraint, appMenu.getId()) >= 0) {          // constraint
                    map.put("constraint", true);
                } else {
                    map.put("constraint", false);
                }

                if (appMenu.getParent() != 0) {                         // have parent
                    long parentId = appMenu.getParent();
                    AppMenu am = AppMenu.findAppMenu(parentId);
                    map.put("parent_t_name", am.getMenu_t_name());
                    map.put("parent_e_name", am.getMenu_e_name());
                }

                list.add(map);
            }

            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(list), headers, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/findPaggingSizeAppMenu", method = RequestMethod.GET, produces = "text/html", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> AppMenuController.findPaggingSizeAppMenu(
            @RequestParam(value = "level", required = false) Integer level
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        try{
            Long size = AppMenu.findAppMenuByLevelPaggingSize(level);
            Map dataSendToFront = new HashMap();
            dataSendToFront.put("size", size);
            return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").deepSerialize(dataSendToFront), headers, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
