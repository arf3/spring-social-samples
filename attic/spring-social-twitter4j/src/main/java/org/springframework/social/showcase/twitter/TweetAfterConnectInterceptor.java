/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.showcase.twitter;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TweetAfterConnectInterceptor implements ConnectInterceptor<Twitter> {

	@Override
	public void preConnect(ConnectionFactory<Twitter> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {
		if (StringUtils.hasText(request.getParameter(POST_TWEET_PARAMETER))) {
			request.setAttribute(POST_TWEET_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	@Override
	public void postConnect(Connection<Twitter> connection, WebRequest request) {
		if (request.getAttribute(POST_TWEET_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
			try {
				connection.getApi().updateStatus("I've connected with the Spring Social Showcase!");
			} catch (TwitterException e) {
			}
			request.removeAttribute(POST_TWEET_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		}
	}

	private static final String POST_TWEET_PARAMETER = "postTweet";

	private static final String POST_TWEET_ATTRIBUTE = "twitterConnect." + POST_TWEET_PARAMETER;
}
