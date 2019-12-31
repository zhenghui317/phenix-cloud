//package com.phenix.admin.listener;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.google.common.collect.Lists;
//import com.phenix.admin.entity.SysApi;
//import com.phenix.admin.service.ISysApiService;
//import com.phenix.admin.service.ISysAuthorityService;
//import com.opencloud.common.constants.QueueConstants;
//import com.opencloud.common.security.http.OpenRestTemplate;
//import com.opencloud.common.utils.BeanConvertUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.messaging.handler.annotation.Payload;
//
//import java.time.Duration;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
///**
// * mq消息接收者
// *
// * @author zhenghui
// */
//@Configuration
//@Slf4j
//public class ResourceScanHandler {
//    @Autowired
//    private ISysApiService baseIApiService;
//    @Autowired
//    private ISysAuthorityService baseIAuthorityService;
//    @Autowired
//    private OpenRestTemplate restTemplate;
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    private final static String SCAN_API_RESOURCE_KEY_PREFIX = "scan_api_resource:";
//
//    /**
//     * 接收API资源扫描消息
//     */
//    @RabbitListener(queues = QueueConstants.QUEUE_SCAN_API_RESOURCE)
//    public void ScanApiResourceQueue(@Payload JSONObject resource) {
//        try {
//            String serviceId = resource.getString("application");
//            String key = SCAN_API_RESOURCE_KEY_PREFIX + serviceId;
//            Object object = redisTemplate.opsForValue().get(key);
//            if (object != null) {
//                // 3分钟内未失效,不再更新资源
//                return;
//            }
//            JSONArray array = resource.getJSONArray("mapping");
//            Iterator iterator = array.iterator();
//            List<String> codes = Lists.newArrayList();
//            while (iterator.hasNext()) {
//                Map map = (Map) iterator.next();
//                try {
//                    SysApi api = BeanConvertUtils.mapToObject(map,SysApi.class);
//                    codes.add(api.getApiCode());
//                    SysApi save = baseIApiService.getApi(api.getApiCode());
//                    if (save == null) {
//                        api.setIsOpen(0);
//                        api.setIsPersist(1);
//                        baseIApiService.addApi(api);
//                    } else {
//                        api.setApiId(save.getApiId());
//                        baseIApiService.updateApi(api);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.error("添加资源error:", e.getMessage());
//                }
//
//            }
//            if (array != null && array.size() > 0) {
//                // 清理无效权限数据
//                baseIAuthorityService.clearInvalidApi(serviceId, codes);
//                restTemplate.refreshGateway();
//                redisTemplate.opsForValue().set(key, array.size(), Duration.ofMinutes(3));
//            }
//
//        } catch (Exception e) {
//            log.error("error:", e);
//        }
//    }
//
//}