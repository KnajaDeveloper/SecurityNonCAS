package com.app2.app2t.manualtest;

import com.app2.app2t.domain.security.AppMenu;
import com.app2.app2t.domain.security.AppRole;
import com.app2.app2t.domain.security.AppRoleMenu;
import com.app2.app2t.util.AuthorizeUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration({"classpath:META-INF/spring/applicationContext*.xml", "file:src/main/webapp/WEB-INF/spring/webmvc-config.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AppMenuTest {

    private Logger LOGGER = LoggerFactory.getLogger(AppMenuTest.class);
    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        AuthorizeUtil.setUserName("admin");

        AppRole appRole1 = insertDataToAppRole("ADMIN", "Administrator");
        AppRole appRole2 = insertDataToAppRole("EM", "Employee");

        AppMenu appMenu1 = insertDataToAppMenu("Controller - 1", "Link - 1", "Icon - 1", 0, "Menu 1", "เมนู 1", 0L, 1);
        AppMenu appMenu2 = insertDataToAppMenu("Controller - 2", "Link - 2", "Icon - 2", 0, "Menu 2", "เมนู 2", 0L, 2);

        AppMenu appMenu11 = insertDataToAppMenu("Controller - 1_1", "Link - 1_1", "", 1, "Menu 1_1", "เมนู 1_1", 1L, 1);
        AppMenu appMenu12 = insertDataToAppMenu("Controller - 1_2", "Link - 1_2", "", 1, "Menu 1_2", "เมนู 1_2", 1L, 2);
        AppMenu appMenu13 = insertDataToAppMenu("Controller - 1_3", "Link - 1_3", "", 1, "Menu 1_3", "เมนู 1_3", 1L, 3);
        AppMenu appMenu21 = insertDataToAppMenu("Controller - 2_1", "Link - 2_1", "", 1, "Menu 2_1", "เมนู 2_1", 2L, 4);
        AppMenu appMenu22 = insertDataToAppMenu("Controller - 2_2", "Link - 2_2", "", 1, "Menu 2_2", "เมนู 2_2", 2L, 5);
        AppMenu appMenu23 = insertDataToAppMenu("Controller - 2_3", "Link - 2_3", "", 1, "Menu 2_3", "เมนู 2_3", 2L, 6);

        AppMenu appMenu111 = insertDataToAppMenu("Controller - 1_1_1", "Link - 1_1_1", "", 2, "Menu 1_1_1", "เมนู 1_1_1", 3L, 1);
        AppMenu appMenu112 = insertDataToAppMenu("Controller - 1_1_2", "Link - 1_1_2", "", 2, "Menu 1_1_2", "เมนู 1_1_2", 3L, 2);
        AppMenu appMenu121 = insertDataToAppMenu("Controller - 1_2_1", "Link - 1_2_1", "", 2, "Menu 1_2_1", "เมนู 1_2_1", 4L, 1);
        AppMenu appMenu122 = insertDataToAppMenu("Controller - 1_2_2", "Link - 1_2_2", "", 2, "Menu 1_2_2", "เมนู 1_2_2", 4L, 2);
        AppMenu appMenu131 = insertDataToAppMenu("Controller - 1_3_1", "Link - 1_3_1", "", 2, "Menu 1_3_1", "เมนู 1_3_1", 5L, 1);
        AppMenu appMenu132 = insertDataToAppMenu("Controller - 1_3_2", "Link - 1_3_2", "", 2, "Menu 1_3_2", "เมนู 1_3_2", 5L, 2);

        insertDataToAppRoleMenu(appMenu1, appRole2);
        insertDataToAppRoleMenu(appMenu2, appRole2);

        insertDataToAppRoleMenu(appMenu11, appRole2);
        insertDataToAppRoleMenu(appMenu12, appRole2);
        insertDataToAppRoleMenu(appMenu13, appRole2);
        insertDataToAppRoleMenu(appMenu21, appRole2);
        insertDataToAppRoleMenu(appMenu22, appRole2);
        insertDataToAppRoleMenu(appMenu23, appRole2);

        insertDataToAppRoleMenu(appMenu111, appRole2);
        insertDataToAppRoleMenu(appMenu112, appRole2);
        insertDataToAppRoleMenu(appMenu121, appRole2);
        insertDataToAppRoleMenu(appMenu122, appRole2);
        insertDataToAppRoleMenu(appMenu131, appRole2);
        insertDataToAppRoleMenu(appMenu132, appRole2);

        LOGGER.debug("================================ Before ===========================================");
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findPaggingDataAppMenuLv0Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findPaggingDataAppMenu")
                        .param("level", "0")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 1")))
                .andExpect(jsonPath("$[1].controller", is("Controller - 2")))
                .andExpect(jsonPath("$[0].link", is("Link - 1")))
                .andExpect(jsonPath("$[1].link", is("Link - 2")))
                .andReturn();
    }
    @Test
    public void findPaggingDataAppMenuLv1Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findPaggingDataAppMenu")
                        .param("level", "1")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 1_1")))
                .andExpect(jsonPath("$[1].controller", is("Controller - 1_2")))
                .andExpect(jsonPath("$[2].controller", is("Controller - 1_3")))
                .andExpect(jsonPath("$[3].controller", is("Controller - 2_1")))
                .andExpect(jsonPath("$[4].controller", is("Controller - 2_2")))
                .andExpect(jsonPath("$[5].controller", is("Controller - 2_3")))
                .andExpect(jsonPath("$[0].link", is("Link - 1_1")))
                .andExpect(jsonPath("$[1].link", is("Link - 1_2")))
                .andExpect(jsonPath("$[2].link", is("Link - 1_3")))
                .andExpect(jsonPath("$[3].link", is("Link - 2_1")))
                .andExpect(jsonPath("$[4].link", is("Link - 2_2")))
                .andExpect(jsonPath("$[5].link", is("Link - 2_3")))
                .andReturn();
    }
    @Test
    public void findPaggingDataAppMenuLv2Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findPaggingDataAppMenu")
                        .param("level", "2")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 1_1_1")))
                .andExpect(jsonPath("$[1].controller", is("Controller - 1_1_2")))
                .andExpect(jsonPath("$[2].controller", is("Controller - 1_2_1")))
                .andExpect(jsonPath("$[3].controller", is("Controller - 1_2_2")))
                .andExpect(jsonPath("$[4].controller", is("Controller - 1_3_1")))
                .andExpect(jsonPath("$[5].controller", is("Controller - 1_3_2")))
                .andExpect(jsonPath("$[0].link", is("Link - 1_1_1")))
                .andExpect(jsonPath("$[1].link", is("Link - 1_1_2")))
                .andExpect(jsonPath("$[2].link", is("Link - 1_2_1")))
                .andExpect(jsonPath("$[3].link", is("Link - 1_2_2")))
                .andExpect(jsonPath("$[4].link", is("Link - 1_3_1")))
                .andExpect(jsonPath("$[5].link", is("Link - 1_3_2")))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findPaggingSizeAppMenuLv0Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "0")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(2)))
                .andReturn();
    }
    @Test
    public void findPaggingSizeAppMenuLv1Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(6)))
                .andReturn();
    }
    @Test
    public void findPaggingSizeAppMenuLv2Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(6)))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findMenuByIdTest() throws Exception     {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findMenu")
                        .param("id", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.controller", is("Controller - 1")))
                .andExpect(jsonPath("$.link", is("Link - 1")))
                .andExpect(jsonPath("$.menuIcon", is("Icon - 1")))
                .andExpect(jsonPath("$.menuLevel", is(0)))
                .andExpect(jsonPath("$.menu_e_name", is("Menu 1")))
                .andExpect(jsonPath("$.menu_t_name", is("เมนู 1")))
                .andExpect(jsonPath("$.parent", is(0)))
                .andExpect(jsonPath("$.sequent", is(1)))
                .andExpect(jsonPath("$.role[0]", is(2)))
                .andReturn();

        MvcResult mvcResult2 = this.mockMvc.perform(get("/appmenus/findMenu")
                        .param("id", "3")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.controller", is("Controller - 1_1")))
                .andExpect(jsonPath("$.link", is("Link - 1_1")))
                .andExpect(jsonPath("$.menuIcon", is("")))
                .andExpect(jsonPath("$.menuLevel", is(1)))
                .andExpect(jsonPath("$.menu_e_name", is("Menu 1_1")))
                .andExpect(jsonPath("$.menu_t_name", is("เมนู 1_1")))
                .andExpect(jsonPath("$.parent", is(1)))
                .andExpect(jsonPath("$.sequent", is(1)))
                .andExpect(jsonPath("$.role[0]", is(2)))
                .andReturn();

        MvcResult mvcResult3 = this.mockMvc.perform(get("/appmenus/findMenu")
                        .param("id", "9")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.controller", is("Controller - 1_1_1")))
                .andExpect(jsonPath("$.link", is("Link - 1_1_1")))
                .andExpect(jsonPath("$.menuIcon", is("")))
                .andExpect(jsonPath("$.menuLevel", is(2)))
                .andExpect(jsonPath("$.menu_e_name", is("Menu 1_1_1")))
                .andExpect(jsonPath("$.menu_t_name", is("เมนู 1_1_1")))
                .andExpect(jsonPath("$.parent", is(3)))
                .andExpect(jsonPath("$.sequent", is(1)))
                .andExpect(jsonPath("$.role[0]", is(2)))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findMenuByAppRoleCodeTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findMenuByAppRoleCode")
                        .param("appRoleCode", "EM")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 1")))
                .andExpect(jsonPath("$[1].controller", is("Controller - 2")))
                .andExpect(jsonPath("$[0].link", is("Link - 1")))
                .andExpect(jsonPath("$[1].link", is("Link - 2")))
                .andReturn();

        MvcResult mvcResult2 = this.mockMvc.perform(get("/appmenus/findMenuByAppRoleCode")
                        .param("appRoleCode", "ADMIN")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Assert.assertEquals(mvcResult2.getResponse().getContentAsString(), "[]");
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findAllRoleTest() throws Exception     {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findAllRole")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].roleName", is("Administrator")))
                .andExpect(jsonPath("$[1].roleName", is("Employee")))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findMenuParentTest() throws Exception     {
        MvcResult mvcResult = this.mockMvc.perform(get("/appmenus/findMenuParent")
                        .param("level", "0")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].menu_th", is("เมนู 1")))
                .andExpect(jsonPath("$[1].menu_th", is("เมนู 2")))
                .andExpect(jsonPath("$[0].menu_en", is("Menu 1")))
                .andExpect(jsonPath("$[1].menu_en", is("Menu 2")))
                .andReturn();

        MvcResult mvcResult2 = this.mockMvc.perform(get("/appmenus/findMenuParent")
                        .param("level", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].menu_th", is("เมนู 1_1")))
                .andExpect(jsonPath("$[1].menu_th", is("เมนู 1_2")))
                .andExpect(jsonPath("$[2].menu_th", is("เมนู 1_3")))
                .andExpect(jsonPath("$[3].menu_th", is("เมนู 2_1")))
                .andExpect(jsonPath("$[4].menu_th", is("เมนู 2_2")))
                .andExpect(jsonPath("$[5].menu_th", is("เมนู 2_3")))
                .andExpect(jsonPath("$[0].menu_en", is("Menu 1_1")))
                .andExpect(jsonPath("$[1].menu_en", is("Menu 1_2")))
                .andExpect(jsonPath("$[2].menu_en", is("Menu 1_3")))
                .andExpect(jsonPath("$[3].menu_en", is("Menu 2_1")))
                .andExpect(jsonPath("$[4].menu_en", is("Menu 2_2")))
                .andExpect(jsonPath("$[5].menu_en", is("Menu 2_3")))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void deleteAppMenuLv0Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/deleteAppMenu")
                        .content("[\"1\"]")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Assert.assertEquals(mvcResult.getResponse().getContentAsString(), "1");

        // Check menu level 0
        this.mockMvc.perform(get("/appmenus/findPaggingDataAppMenu")
                        .param("level", "0")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 2")))
                .andExpect(jsonPath("$[0].link", is("Link - 2")))
                .andReturn();

        this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "0")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(1)))
                .andReturn();

        // Check menu level 1
        this.mockMvc.perform(get("/appmenus/findPaggingDataAppMenu")
                        .param("level", "1")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 2_1")))
                .andExpect(jsonPath("$[1].controller", is("Controller - 2_2")))
                .andExpect(jsonPath("$[2].controller", is("Controller - 2_3")))
                .andExpect(jsonPath("$[0].link", is("Link - 2_1")))
                .andExpect(jsonPath("$[1].link", is("Link - 2_2")))
                .andExpect(jsonPath("$[2].link", is("Link - 2_3")))
                .andReturn();

        this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(3)))
                .andReturn();

        // Check menu level 2
        this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "2")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(0)))
                .andReturn();
    }
    @Test
    public void deleteAppMenuLv1Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/deleteAppMenu")
                        .content("[\"3\"]")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Assert.assertEquals(mvcResult.getResponse().getContentAsString(), "1");

        // Check menu level 1
        this.mockMvc.perform(get("/appmenus/findPaggingDataAppMenu")
                        .param("level", "1")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 1_2")))
                .andExpect(jsonPath("$[1].controller", is("Controller - 1_3")))
                .andExpect(jsonPath("$[2].controller", is("Controller - 2_1")))
                .andExpect(jsonPath("$[3].controller", is("Controller - 2_2")))
                .andExpect(jsonPath("$[4].controller", is("Controller - 2_3")))
                .andExpect(jsonPath("$[0].link", is("Link - 1_2")))
                .andExpect(jsonPath("$[1].link", is("Link - 1_3")))
                .andExpect(jsonPath("$[2].link", is("Link - 2_1")))
                .andExpect(jsonPath("$[3].link", is("Link - 2_2")))
                .andExpect(jsonPath("$[4].link", is("Link - 2_3")))
                .andReturn();

        this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(5)))
                .andReturn();

        // Check menu level 2
        this.mockMvc.perform(get("/appmenus/findPaggingDataAppMenu")
                        .param("level", "2")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 1_2_1")))
                .andExpect(jsonPath("$[1].controller", is("Controller - 1_2_2")))
                .andExpect(jsonPath("$[2].controller", is("Controller - 1_3_1")))
                .andExpect(jsonPath("$[3].controller", is("Controller - 1_3_2")))
                .andExpect(jsonPath("$[0].link", is("Link - 1_2_1")))
                .andExpect(jsonPath("$[1].link", is("Link - 1_2_2")))
                .andExpect(jsonPath("$[2].link", is("Link - 1_3_1")))
                .andExpect(jsonPath("$[3].link", is("Link - 1_3_2")))
                .andReturn();

        this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "2")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(4)))
                .andReturn();
    }
    @Test
    public void deleteAppMenuLv2Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/deleteAppMenu")
                        .content("[\"13\"]")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Assert.assertEquals(mvcResult.getResponse().getContentAsString(), "1");

        // Check menu level 2
        this.mockMvc.perform(get("/appmenus/findPaggingDataAppMenu")
                        .param("level", "2")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 1_1_1")))
                .andExpect(jsonPath("$[1].controller", is("Controller - 1_1_2")))
                .andExpect(jsonPath("$[2].controller", is("Controller - 1_2_1")))
                .andExpect(jsonPath("$[3].controller", is("Controller - 1_2_2")))
                .andExpect(jsonPath("$[4].controller", is("Controller - 1_3_2")))
                .andExpect(jsonPath("$[0].link", is("Link - 1_1_1")))
                .andExpect(jsonPath("$[1].link", is("Link - 1_1_2")))
                .andExpect(jsonPath("$[2].link", is("Link - 1_2_1")))
                .andExpect(jsonPath("$[3].link", is("Link - 1_2_2")))
                .andExpect(jsonPath("$[4].link", is("Link - 1_3_2")))
                .andReturn();

        this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "2")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(5)))
                .andReturn();
    }
    @Test
    public void deleteAppMenuLv2MultipleTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/deleteAppMenu")
                        .content("[\"13\", \"14\"]")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Assert.assertEquals(mvcResult.getResponse().getContentAsString(), "2");

        // Check menu level 2
        this.mockMvc.perform(get("/appmenus/findPaggingDataAppMenu")
                        .param("level", "2")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].controller", is("Controller - 1_1_1")))
                .andExpect(jsonPath("$[1].controller", is("Controller - 1_1_2")))
                .andExpect(jsonPath("$[2].controller", is("Controller - 1_2_1")))
                .andExpect(jsonPath("$[3].controller", is("Controller - 1_2_2")))
                .andExpect(jsonPath("$[0].link", is("Link - 1_1_1")))
                .andExpect(jsonPath("$[1].link", is("Link - 1_1_2")))
                .andExpect(jsonPath("$[2].link", is("Link - 1_2_1")))
                .andExpect(jsonPath("$[3].link", is("Link - 1_2_2")))
                .andReturn();

        this.mockMvc.perform(get("/appmenus/findPaggingSizeAppMenu")
                        .param("level", "2")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(4)))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void insertAppMenuLv0Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/insertAppMenu")
                        .content("{\"link\":\"Link - 3\",\"menuTh\":\"menu th 3\",\"menuEn\":\"Menu 3\",\"controller\":\"Controller - 3\",\"level\":\"0\",\"sequent\":\"3\",\"parent\":\"0\",\"menuIcon\":\"Icon - 3\",\"arrRoleId\":[\"1\", \"2\"]}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.rowCountSameSequent", is(0)))
                .andExpect(jsonPath("$.rowCountSameLink", is(0)))
                .andExpect(jsonPath("$.rowCountSameController", is(0)))
                .andReturn();

        this.mockMvc.perform(get("/appmenus/findMenu")
                        .param("id", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.controller", is("Controller - 3")))
                .andExpect(jsonPath("$.link", is("Link - 3")))
                .andExpect(jsonPath("$.menuIcon", is("Icon - 3")))
                .andExpect(jsonPath("$.menuLevel", is(0)))
                .andExpect(jsonPath("$.menu_e_name", is("Menu 3")))
                .andExpect(jsonPath("$.menu_t_name", is("menu th 3")))
                .andExpect(jsonPath("$.parent", is(0)))
                .andExpect(jsonPath("$.sequent", is(3)))
                .andExpect(jsonPath("$.role[0]", is(1)))
                .andExpect(jsonPath("$.role[1]", is(2)))
                .andReturn();
    }
    @Test
    public void insertAppMenuLv0DuplicateLinkTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/insertAppMenu")
                        .content("{\"link\":\"Link - 1\",\"menuTh\":\"menu th 3\",\"menuEn\":\"Menu 3\",\"controller\":\"Controller - 3\",\"level\":\"0\",\"sequent\":\"3\",\"parent\":\"0\",\"menuIcon\":\"Icon - 3\",\"arrRoleId\":[\"1\", \"2\"]}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.rowCountSameSequent", is(0)))
                .andExpect(jsonPath("$.rowCountSameLink", is(1)))
                .andExpect(jsonPath("$.rowCountSameController", is(0)))
                .andReturn();
    }
    @Test
    public void insertAppMenuLv0DuplicateControllerTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/insertAppMenu")
                        .content("{\"link\":\"Link - 3\",\"menuTh\":\"menu th 3\",\"menuEn\":\"Menu 3\",\"controller\":\"Controller - 1\",\"level\":\"0\",\"sequent\":\"3\",\"parent\":\"0\",\"menuIcon\":\"Icon - 3\",\"arrRoleId\":[\"1\", \"2\"]}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.rowCountSameSequent", is(0)))
                .andExpect(jsonPath("$.rowCountSameLink", is(0)))
                .andExpect(jsonPath("$.rowCountSameController", is(1)))
                .andReturn();
    }
    @Test
    public void insertAppMenuLv0DuplicateSequentTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/insertAppMenu")
                        .content("{\"link\":\"Link - 3\",\"menuTh\":\"menu th 3\",\"menuEn\":\"Menu 3\",\"controller\":\"Controller - 3\",\"level\":\"0\",\"sequent\":\"1\",\"parent\":\"0\",\"menuIcon\":\"Icon - 3\",\"arrRoleId\":[\"1\", \"2\"]}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.rowCountSameSequent", is(1)))
                .andExpect(jsonPath("$.rowCountSameLink", is(0)))
                .andExpect(jsonPath("$.rowCountSameController", is(0)))
                .andReturn();
    }
    @Test
    public void insertAppMenuLv1Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/insertAppMenu")
                        .content("{\"link\":\"Link - 1_4\",\"menuTh\":\"menu th 1_4\",\"menuEn\":\"Menu 1_4\",\"controller\":\"Controller - 1_4\",\"level\":\"1\",\"sequent\":\"4\",\"parent\":\"1\",\"menuIcon\":\"\",\"arrRoleId\":[\"1\", \"2\"]}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.rowCountSameSequent", is(0)))
                .andExpect(jsonPath("$.rowCountSameLink", is(0)))
                .andExpect(jsonPath("$.rowCountSameController", is(0)))
                .andReturn();

        this.mockMvc.perform(get("/appmenus/findMenu")
                        .param("id", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.controller", is("Controller - 1_4")))
                .andExpect(jsonPath("$.link", is("Link - 1_4")))
                .andExpect(jsonPath("$.menuIcon", is("")))
                .andExpect(jsonPath("$.menuLevel", is(1)))
                .andExpect(jsonPath("$.menu_e_name", is("Menu 1_4")))
                .andExpect(jsonPath("$.menu_t_name", is("menu th 1_4")))
                .andExpect(jsonPath("$.parent", is(1)))
                .andExpect(jsonPath("$.sequent", is(4)))
                .andExpect(jsonPath("$.role[0]", is(1)))
                .andExpect(jsonPath("$.role[1]", is(2)))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void DeleteAppMenuLv0Test() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/appmenus/deleteAppMenu")
                        .content("[\"1\"]")
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertEquals(mvcResult.getResponse().getContentAsString(), "1");
    }
    //-------------------------------------------------------------------------------------------
    @After
    public void afterTest() {

    }

    public AppRole insertDataToAppRole(String roleCode, String roleName) throws Exception {
        AppRole appRole = new AppRole();
        appRole.setRoleCode(roleCode);
        appRole.setRoleName(roleName);
        appRole.persist();

        return appRole;
    }

    public AppMenu insertDataToAppMenu(String controller, String link, String menuIcon, int menuLevel, String menuEn, String menuTh, Long parent, int sequent) throws Exception {
        AppMenu appMenu = new AppMenu();
        appMenu.setController(controller);
        appMenu.setLink(link);
        appMenu.setMenuIcon(menuIcon);
        appMenu.setMenuLevel(menuLevel);
        appMenu.setMenu_e_name(menuEn);
        appMenu.setMenu_t_name(menuTh);
        appMenu.setParent(parent);
        appMenu.setSegment(sequent);
        appMenu.setVersion(0);
        appMenu.persist();

        return appMenu;
    }

    public void insertDataToAppRoleMenu(AppMenu appMenu, AppRole appRole) throws Exception {
        AppRoleMenu appRoleMenu = new AppRoleMenu();
        appRoleMenu.setAppMenu(appMenu);
        appRoleMenu.setAppRole(appRole);
        appRoleMenu.setVersion(0);
        appRoleMenu.persist();
    }
}
