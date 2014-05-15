package lab.s2jh.sys.service;

import java.util.List;

import lab.s2jh.sys.entity.PubPost;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SystemSechduleService {

    private final Logger logger = LoggerFactory.getLogger(SystemSechduleService.class);

    @Autowired
    private PubPostService pubPostService;

    public void pubPostCacheRefreshTimely() {
        logger.debug("Timely check and refresh PubPost spring cache...");
        //设置当前应用加载ClassLoader以适应共享部署模式
        ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
        logger.trace("Original ClassLoader: {}", originalLoader);

        List<PubPost> items = pubPostService.findPublished();
        for (PubPost pubPost : items) {
            if (new DateTime(pubPost.getPublishTime()).isAfterNow()) {
                pubPostService.evictCache();
                return;
            }
            if (new DateTime(pubPost.getExpireTime()).isBeforeNow()) {
                pubPostService.evictCache();
                return;
            }
        }
    }
}
