package com.fear1ess.reyunaditoolcontroller.cmd;

public class WSConnectCmd {
    public class ClientCmd {
        public final static int START_OPERATE_APP = 1;
        public final static int STOP_SERVICE = 2;
    }

    public class ServerCmd {
        public final static int NEW_PUSH_MSG = 101;
    }
}
