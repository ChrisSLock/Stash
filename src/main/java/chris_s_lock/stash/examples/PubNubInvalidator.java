//Copyright 2017 Chris Lock.  All Rights Reserved.
package chris_s_lock.stash.examples;

import java.util.Arrays;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import chris_s_lock.stash.Invalidation;
import chris_s_lock.stash.Invalidator;

/**
 * An Example of a true distributed Invalidator which uses the publish/subscribe  
 * service PubNub to pass messages.  Useful when your caches are distributed on mutliple 
 * machines, but still need to respond to each other's invalidations.
 * 
 * disclaimer: only works with Cache Key types that can be serialized with Gson.
 * 
 * @author chrislock
 */
public class PubNubInvalidator<K> extends Invalidator<K> {
	private static final String INVALIDATION_CHANNEL = "INVALIDATION";
	private final PubNub pubNub;
	private final Gson gson = new GsonBuilder().create();
	
	public PubNubInvalidator(final String pubKey, final String subKey) {
		super(PubNubInvalidator.class.getSimpleName());
		PNConfiguration config = new PNConfiguration();
		config.setPublishKey(pubKey);
		config.setSubscribeKey(subKey);
		pubNub = new PubNub(config);
		pubNub.subscribe().channels(Arrays.asList(INVALIDATION_CHANNEL+".*")).execute();
		
		pubNub.addListener(new SubscribeCallback() {
			@Override
			public void status(PubNub p, PNStatus status) {
				switch (status.getCategory()) {
				case PNUnexpectedDisconnectCategory:
				case PNTimeoutCategory:
					p.reconnect();
					break;
				default:
					System.out.println(status.getCategory().toString());
				}
			}

			@SuppressWarnings("serial")
			@Override
			public void message(PubNub p, PNMessageResult messageResult) {
				String message = messageResult.getMessage().asText();
				System.out.println("message: " + message);
				Invalidation<K> invalidation = gson.fromJson(message, new TypeToken<Invalidation<K>>(getClass()){}.getType());
				invalidationRequired(invalidation);
			}

			@Override
			public void presence(PubNub arg0, PNPresenceEventResult arg1) {}
		});
	}

	@Override
	public void invalidate(Invalidation<K> invalidation) {
		String message = gson.toJson(invalidation);
		pubNub.publish().channel(INVALIDATION_CHANNEL + "." + invalidation.getContext()).message(message).async( new PNCallback<PNPublishResult>() {
			@Override
			public void onResponse(PNPublishResult result, PNStatus status) {
				if (status.isError()) throw new RuntimeException(status.getCategory().toString());
			}
		});
	}
}
