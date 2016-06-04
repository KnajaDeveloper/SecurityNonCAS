package com.app2.app2t.web.security;
import com.app2.app2t.domain.security.AppRoleMenu;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/approlemenus")
@Controller
@RooWebScaffold(path = "approlemenus", formBackingObject = AppRoleMenu.class)
@RooWebJson(jsonObject = AppRoleMenu.class)
public class AppRoleMenuController {
}
