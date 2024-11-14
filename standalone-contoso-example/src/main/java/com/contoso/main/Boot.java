package com.contoso.main;

import com.contoso.config.SeimiConfig;
import com.contoso.core.Seimi;

/**
 * @author github.com/zhegexiaohuozi contmaster@gmail.com
 * @since 2015/10/21.
 */
public class Boot {
    public static void main(String[] args){
        SeimiConfig config = new SeimiConfig();
//        config.setSeimiAgentHost("127.0.0.1");
//        config.redisSingleServer().setAddress("redis://127.0.0.1:6379");
        Seimi s = new Seimi(config);
        s.goRun("basic");
    }
}
