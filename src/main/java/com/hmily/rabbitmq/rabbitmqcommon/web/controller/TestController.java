package com.hmily.rabbitmq.rabbitmqcommon.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hmily.dubbo.common.service.ISnowFlakeServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hmily.rabbitmq.rabbitmqcommon.entity.MessageFailed;
import com.hmily.rabbitmq.rabbitmqcommon.service.IMessageFailedService;
import com.hmily.rabbitmq.rabbitmqcommon.util.SnowFlake;

@RestController
public class TestController {
	
	private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Reference(version = "${snowFlakeServiceApi.version}",
            application = "${dubbo.application.id}",
            interfaceName = "com.hmily.dubbo.common.service.ISnowFlakeServiceApi",
            check = false,
            timeout = 1000,
            retries = 0
    )
    private ISnowFlakeServiceApi snowFlakeServiceApi;

	@GetMapping("/test")
	public String test() {
		return "hello";
	}
	
	@GetMapping("/test/longid")
	public String testIdByLocal() {
        Long id = SnowFlake.getId();
        log.info("id: {}", id);
        return id.toString();
	}
	
	@GetMapping("/test/longid/rpc")
	public String testIdByRPC() {
        Long id = snowFlakeServiceApi.getSnowFlakeID();
        log.info("id: {}", id);
        return id.toString();

	}
	
	@Autowired
	private IMessageFailedService messageFailedService;
	
	@GetMapping("/test/save")
	public String testSave() {
		MessageFailed failed = new MessageFailed(1001L, "test_title", "test_desc");
		int row = messageFailedService.add(failed);
        log.info("row: {}", row);
        return row + "";

	}
	
	
	
}
