package myflink.model;

/**
 * Created by xumingyang on 2019/7/12.
 */
public class OptLog {
    public OptLog() {
    }

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
    private String pageName0;
    private String pageName1;
    private String pageName2;
    private String pageName3;
    private String pageName4;
    private String pageName5;
    private String pageName6;
    private String pageName7;

    public String getPageName0() {
        return pageName0;
    }

    public void setPageName0(String pageName0) {
        this.pageName0 = pageName0;
    }

    public String getPageName1() {
        return pageName1;
    }

    public void setPageName1(String pageName1) {
        this.pageName1 = pageName1;
    }

    public String getPageName2() {
        return pageName2;
    }

    public void setPageName2(String pageName2) {
        this.pageName2 = pageName2;
    }

    public String getPageName3() {
        return pageName3;
    }

    public void setPageName3(String pageName3) {
        this.pageName3 = pageName3;
    }

    public String getPageName4() {
        return pageName4;
    }

    public void setPageName4(String pageName4) {
        this.pageName4 = pageName4;
    }

    public String getPageName5() {
        return pageName5;
    }

    public void setPageName5(String pageName5) {
        this.pageName5 = pageName5;
    }

    public String getPageName6() {
        return pageName6;
    }

    public void setPageName6(String pageName6) {
        this.pageName6 = pageName6;
    }

    public String getPageName7() {
        return pageName7;
    }

    public void setPageName7(String pageName7) {
        this.pageName7 = pageName7;
    }

    public String getPageName8() {
        return pageName8;
    }

    public void setPageName8(String pageName8) {
        this.pageName8 = pageName8;
    }

    public String getPageName9() {
        return pageName9;
    }

    public void setPageName9(String pageName9) {
        this.pageName9 = pageName9;
    }

    public String getPageName10() {
        return pageName10;
    }

    public void setPageName10(String pageName10) {
        this.pageName10 = pageName10;
    }

    public String getPageName11() {
        return pageName11;
    }

    public void setPageName11(String pageName11) {
        this.pageName11 = pageName11;
    }

    public String getPageName12() {
        return pageName12;
    }

    public void setPageName12(String pageName12) {
        this.pageName12 = pageName12;
    }

    public String getPageName13() {
        return pageName13;
    }

    public void setPageName13(String pageName13) {
        this.pageName13 = pageName13;
    }

    public String getPageName14() {
        return pageName14;
    }

    public void setPageName14(String pageName14) {
        this.pageName14 = pageName14;
    }

    private String pageName8;
    private String pageName9;
    private String pageName10;
    private String pageName11;
    private String pageName12;
    private String pageName13;
    private String pageName14;



    public OptLog(String userName, int opType, long opTsm, String pageName,String pageName0,
                  String pageName1,
                  String pageName2,
                  String pageName3,
                  String pageName4,
                  String pageName5,
                  String pageName6,
                  String pageName7,
                  String pageName8,
                  String pageName9,
                  String pageName10,
                  String pageName11,
                  String pageName12,
                  String pageName13,
                  String pageName14) {
            this.userName = userName;
            this.opType = opType;
            this.opTs = opTsm;
            this.pageName = pageName;
            this.pageName0 = pageName0;
        this.pageName0 = pageName0;
        this.pageName1 = pageName0;
        this.pageName2 = pageName0;
        this.pageName3 = pageName0;
        this.pageName4 = pageName0;
        this.pageName5 = pageName0;
        this.pageName6 = pageName0;
        this.pageName7 = pageName0;
        this.pageName8 = pageName0;
        this.pageName9 = pageName0;
        this.pageName10 = pageName0;
        this.pageName11 = pageName0;
        this.pageName12 = pageName0;
        this.pageName13 = pageName0;
        this.pageName14 = pageName0;
    }

        public static OptLog of(String userName, int opType, long opTs, String pageName,
                                String pageName0,
                                String pageName1,
                                String pageName2,
                                String pageName3,
                                String pageName4,
                                String pageName5,
                                String pageName6,
                                String pageName7,
                                String pageName8,
                                String pageName9,
                                String pageName10,
                                String pageName11,
                                String pageName12,
                                String pageName13,
                                String pageName14
                                ){
            return new OptLog(userName,opType,opTs, pageName,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0,
                    pageName0
                    );
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