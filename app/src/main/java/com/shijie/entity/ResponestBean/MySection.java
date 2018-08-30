package com.shijie.entity.ResponestBean;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.shijie.entity.tableBean.VideoBean;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MySection extends SectionEntity<VideoBean> {
    private boolean isMore;
    public MySection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public MySection(VideoBean t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}
