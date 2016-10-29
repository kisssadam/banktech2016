package hu.javachallenge.service;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

					Gson gson = new GsonBuilder().create();
					
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
