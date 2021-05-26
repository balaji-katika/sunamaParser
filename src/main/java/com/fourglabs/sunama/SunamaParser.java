package com.fourglabs.sunama;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fourglabs.sunama.utils.Constants;
import com.fourglabs.sunama.utils.SunamaUtils;

public class SunamaParser implements Constants {

	private final static String DELIM_COMMA = ",";
	private static final String DELIM_SPACE = " ";
	private static final String CONST_SALUTATION_FATHER = "Shree";

	public static void main(String[] args) throws IOException {
		SunamaParser sp = new SunamaParser();
		sp.process();
	}

	public String process() throws IOException {

		String csvFileIn = "/Users/bkatika/in.csv";
		String csvFileOut = "/Users/bkatika/out4.csv";
		String line = null;

		BufferedWriter bw = new BufferedWriter(new FileWriter(csvFileOut));
		try (BufferedReader br = new BufferedReader(new FileReader(csvFileIn))) {
			while ((line = br.readLine()) != null) {

				String[] profile = line.split(DELIM_COMMA);
				StringBuffer sb = new StringBuffer();
				constructDisplayString(profile);

				sb.append(SunamaUtils.correctName(profile[Constants.INT_NAME]) + DELIM_COMMA
				        + profile[Constants.INT_GENDER] + DELIM_COMMA + profile[Constants.INT_AGE] + DELIM_COMMA
				        + SunamaUtils.correctName(profile[INT_GOTRAM]) + DELIM_COMMA + profile[INT_PHONE] + DELIM_COMMA
				        + profile[INT_QUAL] + DELIM_COMMA
				        + (profile[INT_JOB].isEmpty() ? Constants.STR_NA : profile[INT_JOB]) + DELIM_COMMA
				        + profile[INT_PLACE] + DELIM_COMMA
				        + (profile[INT_PREFS].isEmpty() ? Constants.STR_NA : profile[INT_PREFS]) + DELIM_COMMA
				        + SunamaUtils.correctName(profile[INT_FATHER]) + DELIM_COMMA
				        + (profile[INT_INCOME].isEmpty() ? Constants.STR_NA : profile[INT_INCOME]) + DELIM_COMMA
				        + ((profile[INT_EMAIL].isEmpty()) ? Constants.STR_NA : profile[INT_EMAIL]));

				bw.write(sb.toString() + "\n");

				// System.out.println(sb.toString());
				invokePUTtoFirebase(profile);
			}
		}
		catch (IOException ioException) {
			System.err.println(ioException);
		}
		finally {
			bw.close();
		}
		return line;

	}

	private void invokePUTtoFirebase(String[] profile) {
		StringBuffer sb = new StringBuffer();
		sb.append("[{" + "\"na\":\"" + SunamaUtils.correctName(profile[INT_NAME]) + "\",");
		sb.append("\"ge\":\"" + profile[Constants.INT_GENDER] + "\",");
		sb.append("\"ag\":" + profile[Constants.INT_AGE] + ",");
		sb.append("\"go\":\"" + SunamaUtils.correctName(profile[Constants.INT_GOTRAM]) + "\",");
		sb.append("\"ph\":" + profile[Constants.INT_PHONE] + ",");
		sb.append("\"ed\":\"" + SunamaUtils.getStrOrEmpty(profile[Constants.INT_QUAL]) + "\",");
		sb.append("\"oc\":\"" + SunamaUtils.getStrOrEmpty(profile[INT_JOB]) + "\",");
		sb.append("\"ad\":\"" + profile[INT_PLACE] + "\",");
		sb.append("\"pr\":\"" + SunamaUtils.getStrOrEmpty(profile[INT_PREFS]) + "\",");
		sb.append("\"fa\":\"" + SunamaUtils.correctName(profile[INT_FATHER]) + "\",");
		sb.append("\"in\":\"" + SunamaUtils.getStrOrEmpty(profile[INT_INCOME]) + "\",");
		sb.append("\"em,\":\"" + SunamaUtils.getStrOrEmpty(profile[INT_EMAIL]) + "\"");
		sb.append("}]");
		//System.out.println(sb.toString());
		triggerAPI(sb.toString(), profile[Constants.INT_PHONE]);

	}

	private void constructDisplayString(String[] profile) {
		StringBuffer sb = new StringBuffer();

		sb.append(SunamaUtils.correctName(profile[Constants.INT_NAME])).append(DELIM_SPACE)
		        .append(SunamaUtils.correctName(profile[Constants.INT_GOTRAM])).append(DELIM_SPACE)
		        // .append(Constants.INT_NAME).append(DELIM_SPACE)
		        .append(SunamaUtils.getPrefix(profile[Constants.INT_GENDER])).append(DELIM_SPACE)
		        .append(CONST_SALUTATION_FATHER).append(DELIM_SPACE)
		        .append(SunamaUtils.correctName(profile[Constants.INT_FATHER])).append(Constants.DELIM_SPACE)
		        .append(Constants.CONST_STR_FROM).append(Constants.DELIM_SPACE).append(profile[Constants.INT_PLACE])
		        .append(Constants.DELIM_FULL_STOP).append(" Completed ").append(profile[Constants.INT_QUAL])
		        .append(SunamaUtils.getOccupation(profile[Constants.INT_JOB])).append(" (")
		        .append(profile[Constants.INT_PHONE]).append(" )");

		System.out.println(sb.toString());
	}

	private static void triggerAPI(String input, String phone) {
		HttpsURLConnection conn = null;
		try {
			/* Start of Fix */
	        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) { }

	        } };

	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) { return true; }
	        };
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	        
			URL url = new URL("https://sunama-jakini.firebaseio.com/profiles/0/" + phone + ".json");
			conn = (HttpsURLConnection) url.openConnection();
			

				  
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output=null;
			// System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
			}
			br.close();

			conn.disconnect();
		}
		catch (Exception e) {
			System.err.println(e);
		}
		finally {
			if (conn != null) {
				conn.disconnect();
			}

		}
	}

}
