package com.app2.app2t.web.security;
import com.app2.app2t.domain.security.AppMenu;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/appmenus")
@Controller
@RooWebScaffold(path = "appmenus", formBackingObject = AppMenu.class)
@RooWebJson(jsonObject = AppMenu.class)
public class AppMenuController {
}
