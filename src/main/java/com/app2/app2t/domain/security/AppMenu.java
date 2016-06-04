package com.app2.app2t.domain.security;
import com.app2.app2t.base.BaseEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.json.RooJson;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@RooJson
public class AppMenu extends BaseEntity {

    /**
     */
    @NotNull
    @Column(unique = true)
    @Size(max = 255)
    private String link;

    /**
     */
    @NotNull
    @Column(unique = true)
    @Size(max = 255)
    private String controller;

    /**
     */
    @Digits(integer= 1, fraction = 0)
    @NotNull
    private Integer menuLevel;

    /**
     * front end use "sequent"
     */
    @Digits(integer= 2, fraction = 0)
    @NotNull
    private Integer segment;

    /**
     */
    @Digits(integer= 19, fraction = 0)
    @NotNull
    private Long parent;

    /**
     */
    @Size(max = 255)
    @NotNull
    private String menu_t_name;

    /**
     */
    @Size(max = 255)
    @NotNull
    private String menu_e_name;

    /**
     */
    @Size(max = 255)
    private String menuIcon;
}
