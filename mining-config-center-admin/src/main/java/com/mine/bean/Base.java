package com.mine.bean;

/**
 * 用来封装 model的公共属性
 */
public class Base {

    private int page;

    private int limit;

    public int getOffset(){
            return (page == 0 ? 1 : page - 1) * limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
