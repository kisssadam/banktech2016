package hu.javachallenge.service;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

	private static final Logger log = LoggerFactory.getLogger(ServiceGenerator.class);
	
	private static volatile TorpedoApi torpedoApi;
	
	private ServiceGenerator() {
		throw new RuntimeException();
	}
	
	public static TorpedoApi getClient(final String serverAddress, final String teamToken) {
		if (torpedoApi == null) {
			synchronized (ServiceGenerator.class) {
				if (torpedoApi == null) {
					OkHttpClient okHttpClient = new OkHttpClient()
							.newBuilder()
							.addInterceptor(chain -> chain.proceed(chain.request()
									.newBuilder()
									.addHeader("TEAMTOKEN", teamToken)
									.addHeader("Accept", MediaType.APPLICATION_JSON)
									.addHeader("Content-Type", MediaType.APPLICATION_JSON)
									.build()))
							.build();

					Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
						
						@Override
						public LocalDateTime deserialize(JsonElement json, Type typeOfT,
								JsonDeserializationContext context) throws JsonParseException {
							ZoneId budapestZoneId = TimeZone.getTimeZone("Europe/Budapest").toZoneId();
							return LocalDateTime.ofInstant(Instant.ofEpochMilli(json.getAsLong()), budapestZoneId);
						}
						
					}).create();
					
					Retrofit retrofit = new Retrofit.Builder()
							.baseUrl(HttpUrl.parse(serverAddress))
							.addConverterFactory(GsonConverterFactory.create(gson))
							.client(okHttpClient)
							.build();

					torpedoApi = retrofit.create(TorpedoApi.class);
				}
			}
		}
		return torpedoApi;
	}
	
}
