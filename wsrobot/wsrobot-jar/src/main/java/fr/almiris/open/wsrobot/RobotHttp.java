package fr.almiris.open.wsrobot;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class RobotHttp {

	private final DefaultHttpClient httpclient = new DefaultHttpClient();

	private HttpResponse response;
	
	private String content;
	
	public RobotHttp() {	
	}
	
	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void post(String url, Map<String, String> headers, String data) {
		HttpPost httpPost = new HttpPost(url);

		try {
			if (headers != null) {
				for (String header : headers.keySet()) {				
					httpPost.addHeader(header, headers.get(header));
				}
			}

			if (data != null) {
				httpPost.setEntity(new StringEntity(data, Charset.forName("utf-8")));
			}

			response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				content = IOUtils.toString(entity.getContent(),"UTF-8");
				EntityUtils.consume(entity);
			}
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		} 
		finally {
			httpPost.releaseConnection();
		}
	}
	
	public void get(String url, Map<String, String> headers) {
		HttpGet httpGet = new HttpGet(url);

		try {
			if (headers != null) {
				for (String header : headers.keySet()) {				
					httpGet.addHeader(header, headers.get(header));
				}
			}

			response = httpclient.execute(httpGet);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				content = IOUtils.toString(entity.getContent(),"UTF-8");
				EntityUtils.consume(entity);
			}			
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		} 
		finally {
			httpGet.releaseConnection();
		}
	}
	
	public void delete(String url, Map<String, String> headers) {
		HttpDelete httpDelete = new HttpDelete(url);

		try {
			if (headers != null) {
				for (String header : headers.keySet()) {				
					httpDelete.addHeader(header, headers.get(header));
				}
			}

			response = httpclient.execute(httpDelete);

			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				content = IOUtils.toString(entity.getContent(),"UTF-8");
				EntityUtils.consume(entity);
			}
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		} 
		finally {
			httpDelete.releaseConnection();
		}
	}
	
}
