// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.app2.app2t.web.security;

import com.app2.app2t.domain.security.AppMenu;
import com.app2.app2t.domain.security.AppRole;
import com.app2.app2t.domain.security.AppRoleMenu;
import com.app2.app2t.web.security.AppRoleMenuController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect AppRoleMenuController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String AppRoleMenuController.create(@Valid AppRoleMenu appRoleMenu, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, appRoleMenu);
            return "approlemenus/create";
        }
        uiModel.asMap().clear();
        appRoleMenu.persist();
        return "redirect:/approlemenus/" + encodeUrlPathSegment(appRoleMenu.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String AppRoleMenuController.createForm(Model uiModel) {
        populateEditForm(uiModel, new AppRoleMenu());
        return "approlemenus/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String AppRoleMenuController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("approlemenu", AppRoleMenu.findAppRoleMenu(id));
        uiModel.addAttribute("itemId", id);
        return "approlemenus/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String AppRoleMenuController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("approlemenus", AppRoleMenu.findAppRoleMenuEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) AppRoleMenu.countAppRoleMenus() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("approlemenus", AppRoleMenu.findAllAppRoleMenus(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "approlemenus/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String AppRoleMenuController.update(@Valid AppRoleMenu appRoleMenu, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, appRoleMenu);
            return "approlemenus/update";
        }
        uiModel.asMap().clear();
        appRoleMenu.merge();
        return "redirect:/approlemenus/" + encodeUrlPathSegment(appRoleMenu.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String AppRoleMenuController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AppRoleMenu.findAppRoleMenu(id));
        return "approlemenus/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String AppRoleMenuController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AppRoleMenu appRoleMenu = AppRoleMenu.findAppRoleMenu(id);
        appRoleMenu.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/approlemenus";
    }
    
    void AppRoleMenuController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("appRoleMenu_createddate_date_format", "dd/MM/yyyy");
        uiModel.addAttribute("appRoleMenu_updateddate_date_format", "dd/MM/yyyy");
    }
    
    void AppRoleMenuController.populateEditForm(Model uiModel, AppRoleMenu appRoleMenu) {
        uiModel.addAttribute("appRoleMenu", appRoleMenu);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("appmenus", AppMenu.findAllAppMenus());
        uiModel.addAttribute("approles", AppRole.findAllAppRoles());
    }
    
    String AppRoleMenuController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}