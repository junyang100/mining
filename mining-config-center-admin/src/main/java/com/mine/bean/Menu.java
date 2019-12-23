package com.mine.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class Menu extends Base{

    private Integer id;

    private String menuName;

    private Integer menuParent;

    private String menuAction;

    private Integer menuOrder;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private String menuDisplayname;

    private Menu parentMenu;

    private Set<Menu> childrenMenu = new LinkedHashSet<>(8);


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public Integer getMenuParent() {
        return menuParent;
    }

    public void setMenuParent(Integer menuParent) {
        this.menuParent = menuParent;
    }

    public String getMenuAction() {
        return menuAction;
    }

    public void setMenuAction(String menuAction) {
        this.menuAction = menuAction == null ? null : menuAction.trim();
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMenuDisplayname() {
        return menuDisplayname;
    }

    public void setMenuDisplayname(String menuDisplayname) {
        this.menuDisplayname = menuDisplayname == null ? null : menuDisplayname.trim();
    }

    public Set<Menu> getChildrenMenu() {
        return childrenMenu;
    }

    public void setChildrenMenu(Set<Menu> childrenMenu) {
        this.childrenMenu = childrenMenu;
    }

    public Menu getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }
}