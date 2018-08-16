package com.shijie.constant;

/**
 * 常量类
 */
public class Constants {

    /**
     * 请求跟地址URL
     */
    public enum requestRootURL {
        RootURL("http://www.haogedada.top/api/");
        private final String url;
        requestRootURL(String url) {
            this.url = url;
        }
        public String getName() {
            return url;
        }

    }

    /**
     * 朋友类型
     * 1.粉丝
     * 2.关注
     */
    public enum friendType {
        FANS(1, "fans"), FOLLOW(2, "follow");
        private final Integer value;
        private final String name;

        friendType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * url类型
     * 1.视频url
     * 2.视频封面图片url
     * 3.头像图片url
     */
    public enum urlType {
        VIDEOURL(1, "http://www.haogedada.top/api/upLoadFile/videoFile/"),
        VIDEOCOVERURL(2, "http://www.haogedada.top/api/upLoadFile/videoCover/"),
        HEADURL(3, "http://www.haogedada.top/api/upLoadFile/headImage/");
        private final Integer value;
        private final String name;

        urlType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }



    /**
     * 视频类型
     */
    public enum videoType {
        SOCIOLOGY(1, "sociology", "社会"),
        WORLD(2, "world", "世界"),
        SPORTS(3, "sports", "体育"),
        LIFE(4, "life", "生活"),
        TECH(5, "tech", "科技"),
        ENTERTAINMENT(6, "entertainment", "娱乐"),
        MOVIE(7, "movie", "电影"),
        AUTO(8, "auto", "汽车"),
        TASTE(9, "taste", "美食"),
        MUSIC(10, "music", "音乐"),
        BUSINESS(11, "business", "商业"),
        HOT(12, "hot", "热门");
        private final Integer index;
        private final String name;
        private final String value;

        videoType(Integer index, String name, String value) {
            this.index = index;
            this.name = name;
            this.value = value;
        }

       public static videoType getVideoType(String typeName) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i].getName().equals(typeName)) {
                    return values()[i];
                }
            }
          return null;
        }
        public Integer getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }


}
