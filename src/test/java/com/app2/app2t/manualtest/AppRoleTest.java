package com.app2.app2t.manualtest;

import com.app2.app2t.domain.security.AppRole;
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
public class AppRoleTest {

    private Logger LOGGER = LoggerFactory.getLogger(AppRoleTest.class);
    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        AuthorizeUtil.setUserName("admin");

        insertDataToAppRole("ADMIN", "Administrator");
        insertDataToAppRole("EM", "Employee");
        insertDataToAppRole("PM", "Project manager");
        insertDataToAppRole("SA", "Software analysis");

        LOGGER.debug("================================ Before ===========================================");
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findRoleNameTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/approles/findSecurityAppRoleName")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].roleName", is("Administrator")))
                .andExpect(jsonPath("$[1].roleName", is("Employee")))
                .andExpect(jsonPath("$[2].roleName", is("Project manager")))
                .andExpect(jsonPath("$[3].roleName", is("Software analysis")))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findRoleByIdTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/approles/findRole")
                        .param("id", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.roleCode", is("ADMIN")))
                .andExpect(jsonPath("$.roleName", is("Administrator")))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findPaggingDataAppRoleTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/approles/findPaggingDataAppRole")
                        .param("firstResult", "0")
                        .param("maxResult", "15")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].roleCode", is("ADMIN")))
                .andExpect(jsonPath("$[0].roleName", is("Administrator")))
                .andExpect(jsonPath("$[1].roleCode", is("EM")))
                .andExpect(jsonPath("$[1].roleName", is("Employee")))
                .andExpect(jsonPath("$[2].roleCode", is("PM")))
                .andExpect(jsonPath("$[2].roleName", is("Project manager")))
                .andExpect(jsonPath("$[3].roleCode", is("SA")))
                .andExpect(jsonPath("$[3].roleName", is("Software analysis")))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void findPaggingSizeAppRoleTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/approles/findPaggingSizeAppRole")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.size", is(4)))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void insertAppRoleTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/approles/insertAppRole")
                        .content("{\"roleCode\":\"DEV\",\"roleName\":\"Developer\"}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        this.mockMvc.perform(get("/approles/findRole")
                        .param("id", "5")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.roleCode", is("DEV")))
                .andExpect(jsonPath("$.roleName", is("Developer")))
                .andReturn();
    }
    @Test
    public void insertAppRoleDuplicateCodeTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/approles/insertAppRole")
                        .content("{\"roleCode\":\"ADMIN\",\"roleName\":\"Developer\"}")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void updateAppRoleCodeAndNameTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/approles/updateAppRole")
                        .content("{\"roleId\":\"1\",\"roleCode\":\"ADMIN2\",\"roleName\":\"Administrator2\"}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        this.mockMvc.perform(get("/approles/findRole")
                        .param("id", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.roleCode", is("ADMIN2")))
                .andExpect(jsonPath("$.roleName", is("Administrator2")))
                .andReturn();
    }
    @Test
    public void updateAppRoleCodeOnlyTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/approles/updateAppRole")
                        .content("{\"roleId\":\"1\",\"roleCode\":\"Admin\",\"roleName\":\"Administrator\"}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        this.mockMvc.perform(get("/approles/findRole")
                        .param("id", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.roleCode", is("Admin")))
                .andExpect(jsonPath("$.roleName", is("Administrator")))
                .andReturn();
    }
    @Test
    public void updateAppRoleNameOnlyTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/approles/updateAppRole")
                        .content("{\"roleId\":\"1\",\"roleCode\":\"ADMIN\",\"roleName\":\"Administrator2\"}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        this.mockMvc.perform(get("/approles/findRole")
                        .param("id", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.roleCode", is("ADMIN")))
                .andExpect(jsonPath("$.roleName", is("Administrator2")))
                .andReturn();
    }
    @Test
    public void updateAppRoleDuplicateCodeTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/approles/updateAppRole")
                        .content("{\"roleId\":\"1\",\"roleCode\":\"EM\",\"roleName\":\"Administrator\"}")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();

        this.mockMvc.perform(get("/approles/findRole")
                        .param("id", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.roleCode", is("ADMIN")))
                .andExpect(jsonPath("$.roleName", is("Administrator")))
                .andReturn();
    }
    //-------------------------------------------------------------------------------------------
    @Test
    public void deleteRoleTest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/approles/deleteAppRole")
                        .content("[\"1\", \"2\"]")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.countRemove", is(2)))
                .andReturn();

        MvcResult mvcResult2 = this.mockMvc.perform(get("/approles/findRole")
                        .param("id", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Assert.assertEquals(mvcResult2.getResponse().getContentAsString(), "null");

        MvcResult mvcResult3 = this.mockMvc.perform(get("/approles/findRole")
                        .param("id", "2")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Assert.assertEquals(mvcResult3.getResponse().getContentAsString(), "null");
    }
    //-------------------------------------------------------------------------------------------
    @After
    public void afterTest() {
        EntityManager ent = AppRole.entityManager();
        Criteria criteria = ((Session) ent.getDelegate()).createCriteria(AppRole.class);
        List<AppRole> appRoleList = criteria.list();
        for (AppRole appRole : appRoleList) {
            LOGGER.debug("================> Id: {} Role Code {} Role Name: {}", appRole.getId(), appRole.getRoleCode(), appRole.getRoleName());
        }
    }

    public void insertDataToAppRole(String roleCode, String roleName) throws Exception {
        AppRole appRole = new AppRole();
        appRole.setRoleCode(roleCode);
        appRole.setRoleName(roleName);
        appRole.persist();
    }

}