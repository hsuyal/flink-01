package myflink.model;

/**
 * Created by xumingyang on 2019/7/12.
 */
public class OptLog {
        /**
 * 用户名
 */
private String userName;

        @Override
        public String toString() {
            return "OptLog{" +
                    "userName='" + userName + '\'' +
                    ", opType=" + opType +
                    ", opTs=" + opTs +
                    ", pageName='" + pageName + '\'' +
                    '}';
        }

        /**
         * 操作类型
         */
        private int opType;
        /**
         * 时间戳
         */
        private long opTs;

        public String getPageName() {
            return pageName;
        }

        public void setPageName(String pageName) {
            this.pageName = pageName;
        }

        private String pageName;

        public OptLog(String userName, int opType, long opTsm, String pageName) {
            this.userName = userName;
            this.opType = opType;
            this.opTs = opTsm;
            this.pageName = pageName;
        }

        public static OptLog of(String userName, int opType, long opTs, String pageName){
            return new OptLog(userName,opType,opTs, pageName);
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getOpType() {
            return opType;
        }

        public void setOpType(int opType) {
            this.opType = opType;
        }

        public long getOpTs() {
            return opTs;
        }

        public void setOpTs(long opTs) {
            this.opTs = opTs;
        }

}