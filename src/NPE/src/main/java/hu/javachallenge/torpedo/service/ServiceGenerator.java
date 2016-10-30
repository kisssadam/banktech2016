package hu.javachallenge.torpedo.service;

import java.lang.annotation.Annotation;
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

import hu.javachallenge.torpedo.response.CommonResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

	private static final Logger log = LoggerFactory.getLogger(ServiceGenerator.class);

	private final String serverAddress;
	private final String teamToken;
	private final HttpLoggingInterceptor.Level httpLoggingLevel;
	private final Gson gson;
	private final TorpedoApi torpedoApi;
	private final Converter<ResponseBody, CommonResponse> converter;
	
	public ServiceGenerator(String serverAddress, String teamToken, HttpLoggingInterceptor.Level httpLoggingLevel) {
		log.debug("serverAddress: '{}', teamToken: '{}', httpLoggingLevel: '{}'", serverAddress, teamToken, httpLoggingLevel);
		
		this.serverAddress = serverAddress;
		this.teamToken = teamToken;
		this.httpLoggingLevel = httpLoggingLevel;
		
		HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

			@Override
			public void log(String message) {
				log.trace(message);
			}
			
		});
		httpLoggingInterceptor.setLevel(this.httpLoggingLevel);
		
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(chain -> chain.proceed(chain.request()
						.newBuilder()
						.header("Accept", MediaType.APPLICATION_JSON)
						.header("Content-Type", MediaType.APPLICATION_JSON)
						.addHeader("TEAMTOKEN", teamToken)
						.build()))
				.addInterceptor(httpLoggingInterceptor)
				.build();
		
		this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
			
			@Override
			public LocalDateTime deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context) throws JsonParseException {
				ZoneId budapestZoneId = TimeZone.getTimeZone("Europe/Budapest").toZoneId();
				return LocalDateTime.ofInstant(Instant.ofEpochMilli(json.getAsLong()), budapestZoneId);
			}
			
		}).create();
		
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(HttpUrl.parse(serverAddress))
				.addConverterFactory(GsonConverterFactory.create(this.gson))
				.client(okHttpClient)
				.build();
		
		this.torpedoApi = retrofit.create(TorpedoApi.class);
		this.converter = retrofit.responseBodyConverter(CommonResponse.class, new Annotation[0]);
	}
	
	public String getServerAddress() {
		return serverAddress;
	}
	
	public String getTeamToken() {
		return teamToken;
	}
	
	public HttpLoggingInterceptor.Level getHttpLoggingLevel() {
		return httpLoggingLevel;
	}
	
	public Gson getGson() {
		return gson;
	}
	
	public TorpedoApi getTorpedoApi() {
		return torpedoApi;
	}
	
	public Converter<ResponseBody, CommonResponse> getConverter() {
		return converter;
	}
	
}
