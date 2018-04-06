package itcast.research.util;

import itcast.research.entity.other.Log;
import itcast.research.service.other.ILogService;
import itcast.research.service.other.impl.LogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 14:11
 * Description:
 */
@Slf4j
public class DBLogUtil {
    private static DBLogUtil dbLog = null;
    private ILogService logService;

    public static DBLogUtil getInstance() {
        if (dbLog == null) {
            dbLog = new DBLogUtil();
        }
        return dbLog;
    }

    public ILogService getLogService() {
        return logService;
    }

    public DBLogUtil setLogService(ILogService logService) {
        this.logService = logService;
        return this;
    }

    public void write(Log l) {
        Mono.fromCallable(() -> {
            logService.save(l);
            return true;
        }).subscribeOn(Schedulers.elastic()).subscribe(isSuc -> {
            if (!isSuc) {
                log.error("日志写入失败！");
            }
        });
    }
}
