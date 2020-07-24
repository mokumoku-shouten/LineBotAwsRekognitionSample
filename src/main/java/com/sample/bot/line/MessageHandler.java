package com.sample.bot.line;

import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.sample.bot.line.service.RekognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * LINEメッセージハンドラ
 */
@LineMessageHandler
public class MessageHandler {

	@Autowired
	private LineMessagingClient lineMessagingClient;

	@Autowired
	private RekognitionService rekognitionService;

	public static void main(String[] args) {
		SpringApplication.run(MessageHandler.class, args);
	}

	/**
	 * デフォルト
	 *
	 * @param event
	 */
	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
	}

	/**
	 * テキスト
	 * @param event
	 * @return
	 */
	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		lineMessagingClient.replyMessage(
				new ReplyMessage(
						event.getReplyToken(),
						Arrays.asList(new TextMessage("テスト"))
				)
		);
	}

	/**
	 * 画像
	 * @param event
	 * @return
	 */
	@EventMapping
	public void handleImageMessage(MessageEvent<ImageMessageContent> event) throws IOException, ExecutionException, InterruptedException {
		String messageId = event.getMessage().getId();
		InputStream is = lineMessagingClient.getMessageContent(messageId).get().getStream();
		String result = rekognitionService.analyze(is);

		lineMessagingClient.replyMessage(
				new ReplyMessage(
						event.getReplyToken(),
						Arrays.asList(new TextMessage(result))
				)
		);
	}


}

