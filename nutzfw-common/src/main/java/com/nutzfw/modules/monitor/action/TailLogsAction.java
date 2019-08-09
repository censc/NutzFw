package com.nutzfw.modules.monitor.action;

import com.nutzfw.core.common.annotation.AutoCreateMenuAuth;
import com.nutzfw.core.common.cons.Cons;
import com.nutzfw.core.common.filter.CheckRoleAndSession;
import com.nutzfw.core.common.vo.AjaxResult;
import com.nutzfw.core.common.vo.LayuiTableDataListVO;
import com.nutzfw.modules.common.action.BaseAction;
import com.nutzfw.modules.monitor.websocket.TailLogsDebugWs;
import com.nutzfw.modules.monitor.websocket.TailLogsErrorWs;
import com.nutzfw.modules.monitor.websocket.TailLogsInfoWs;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄川 huchuc@vip.qq.com
 * @date: 2019/3/1
 * 描述此类：实时监控管理
 */
@IocBean
@At("/sysTailLogs")
@Filters(@By(type = CheckRoleAndSession.class, args = {Cons.SESSION_USER_KEY, Cons.SESSION_USER_ROLE}))
public class TailLogsAction extends BaseAction {


    @Inject
    TailLogsDebugWs debugWs;

    @Inject
    TailLogsErrorWs errorWs;

    @Inject
    TailLogsInfoWs infoWs;


    @Ok("btl:WEB-INF/view/sys/monitor/TailLogs/index.html")
    @GET
    @At("/index")
    @RequiresPermissions("sysTailLogs.index")
    @AutoCreateMenuAuth(name = "实时日志监控", icon = "fa-eye", parentPermission = "sys.monitor")
    public void index() {
    }


    @Ok("btl:WEB-INF/view/sys/monitor/TailLogs/console.html")
    @GET
    @At("/view")
    @RequiresPermissions("sysTailLogs.index")
    public NutMap view(@Param("name") String name, @Param("charSet") String charSet) {
        return NutMap.NEW().setv("name", name).setv("charSet", charSet);
    }


    @POST
    @At("/list")
    @Ok("json")
    @RequiresPermissions("sysTailLogs.index")
    public LayuiTableDataListVO loginLogListData() {
        List<NutMap> list = new ArrayList<>();
        list.add(NutMap.NEW().setv("name", "error").setv("status", errorWs.getStatus()).setv("command", debugWs.getCommand()));
        list.add(NutMap.NEW().setv("name", "info").setv("status", infoWs.getStatus()).setv("command", debugWs.getCommand()));
        list.add(NutMap.NEW().setv("name", "debug").setv("status", debugWs.getStatus()).setv("command", debugWs.getCommand()));
        return LayuiTableDataListVO.allData(list);
    }

    @POST
    @At("/stop")
    @Ok("json")
    @RequiresPermissions("sysTailLogs.index")
    public AjaxResult stop(@Param("name") String name) {
        if ("error".equals(name)) {
            errorWs.stop();
        }
        if ("info".equals(name)) {
            infoWs.stop();
        }
        if ("debug".equals(name)) {
            debugWs.stop();
        }
        return AjaxResult.sucess("操作成功");
    }

}