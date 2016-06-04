package com.app2.app2t.domain.security;
import com.app2.app2t.base.BaseEntity;
import org.apache.log4j.Logger;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@RooJson
public class AppRoleMenu extends BaseEntity {
    private static Logger LOGGER = Logger.getLogger(AppRoleMenu.class);
    /**
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appRole")
    private AppRole appRole;

    /**
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appMenu")
    private AppMenu appMenu;
}
