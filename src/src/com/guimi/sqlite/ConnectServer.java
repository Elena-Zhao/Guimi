package com.guimi.sqlite;

import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.guimi.entities.Match;
import com.guimi.entities.Outfit;
import com.guimi.entities.PersonInfo;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class ConnectServer {
	private static String URL = "http://hyacinth067357.xicp.net:8080/GUIMIService.asmx";
	private static String NAMESPACE = "http://tempuri.org/";
	private static String METHOD_NAME;
	private static String SOAP_ACTION;
	public volatile static ConnectServer connect;

	// set ConnectServer as an singleton class
	public static ConnectServer getInstance() {
		if (connect == null)
			synchronized (GuiMiDB.class) {
				if (connect == null)
					connect = new ConnectServer();
			}
		return connect;
	}

	/*
	 * Log In login succeed, return 1 wrong password, return 2 userId doesn't
	 * exist, return 0 connect failed, return -1
	 */
	public int logIn(String userId, String password) {
		METHOD_NAME = "login";
		SOAP_ACTION = "http://tempuri.org/login";
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("userId", userId);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			SoapObject soap1 = (SoapObject) soapObject.getProperty(0);
			SoapObject soapChild = (SoapObject) soap1.getProperty(1);
			if (soapChild.getPropertyCount() != 0) {
				SoapObject soap2 = (SoapObject) soapChild.getProperty(0);
				SoapObject soap3 = (SoapObject) soap2.getProperty(0);
				if (soap3.getProperty("password").toString().equals(password)) {
					PersonInfo.userId = soap3.getProperty("userId").toString();
					PersonInfo.userName = soap3.getProperty("password")
							.toString();
					PersonInfo.password = soap3.getProperty("userName")
							.toString();
					return 0;
				} else
					return 2;
			} else
				return 1;
		} catch (Exception e) {
			Log.i("db", e.toString());
			return -1;
		}
	}

	/*
	 * sign up succeed, return 0; connect failed, return -1;
	 */
	public int SignUp(String userId, String password, String userName) {
		METHOD_NAME = "signup";
		SOAP_ACTION = "http://tempuri.org/signup";
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("userId", userId);
			rpc.addProperty("password", password);
			rpc.addProperty("userName", userName);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			if (Boolean.valueOf(soapObject.getProperty(0).toString()))
				return 0;
			else
				return 1;
		} catch (Exception e) {
			Log.i("db", e.toString());
			return -1;
		}
	}

	// upload an outfit
	public int uploadOutfit(String outfitDescription,
			String clothDescription, String pictureUrl, String uploadTime) {
		METHOD_NAME = "uploadOutfit";
		SOAP_ACTION = "http://tempuri.org/uploadOutfit";
		try {
			String[] temp = pictureUrl.split("/");
			String fileName = temp[temp.length - 1];

			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("uploaderId", PersonInfo.userId);
			rpc.addProperty("outfitDescription", outfitDescription);
			rpc.addProperty("clothesDescription", clothDescription);
			rpc.addProperty("fileName", fileName);
			rpc.addProperty("uploadTime", uploadTime);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			return Integer.parseInt(soapObject.getProperty(0).toString());
		} catch (Exception e) {
			Log.i("db", e.toString());
			return -1;
		}
	}

	public Vector<Match> getMatchs(int orderType, int flagOutfitId,
			boolean beforeOrAfter) {
		Vector<Match> matchs = new Vector<Match>();
		METHOD_NAME = "getOutfits";
		SOAP_ACTION = "http://tempuri.org/getOutfits";
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("type", orderType);
			rpc.addProperty("flagOutfitId", flagOutfitId);
			rpc.addProperty("beforeOrAfter", beforeOrAfter);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			SoapObject soap1 = (SoapObject) soapObject.getProperty(0);
			SoapObject soapChild = (SoapObject) soap1.getProperty(1);
			if (soapChild.getPropertyCount() != 0) {
				SoapObject soap2 = (SoapObject) soapChild.getProperty(0);
				for (int i = 0; i < soap2.getPropertyCount(); i++) {
					SoapObject soap3 = (SoapObject) soap2.getProperty(i);
					Match match = new Match(
							Integer.parseInt(soap3.getProperty("outfitId")
									.toString()),
							soap3.getProperty("image").toString(),
							Integer.parseInt(soap3.getProperty("imageWidth")
									.toString()),
							Integer.parseInt(soap3.getProperty("imageHeight")
									.toString()),
							soap3.getProperty("uploaderId").toString(),
							soap3.getProperty("uploaderName").toString(),
							Integer.parseInt(soap3.getProperty("up").toString()),
							Integer.parseInt(soap3.getProperty("down")
									.toString()));
					matchs.add(match);
				}
			}
			matchs.trimToSize();
			return matchs;
		} catch (Exception e) {
			Log.i("db", e.toString());
			return null;
		}
	}

	public Outfit getOutfit(int outfitId) {
		METHOD_NAME = "getOutfit";
		SOAP_ACTION = "http://tempuri.org/getOutfit";
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("outfitId", outfitId);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			if (soapObject == null) {
				Outfit outfit = new Outfit(0, "", "", "", "", 0, 0, "", "");
				return outfit;
			} else {
				SoapObject soap1 = (SoapObject) soapObject.getProperty(0);
				SoapObject soapChild = (SoapObject) soap1.getProperty(1);
				SoapObject soap2 = (SoapObject) soapChild.getProperty(0);
				SoapObject soap3 = (SoapObject) soap2.getProperty(0);
				Outfit outfit = new Outfit(
						Integer.parseInt(soap3.getProperty("outfitId").toString()), 
						soap3.getProperty("image").toString(),
						soap3.getProperty("uploaderId").toString(), 
						soap3.getProperty("uploaderName").toString(),
						soap3.getProperty("uploadTime").toString(),
						Integer.parseInt(soap3.getProperty("up").toString()),
						Integer.parseInt(soap3.getProperty("down").toString()),
						soap3.getProperty("outfitDescription").toString(),
						soap3.getProperty("clothesDescription").toString());
				outfit.setPictureSize(Integer.parseInt(soap3.getProperty(
						"imageWidth").toString()), Integer.parseInt(soap3
						.getProperty("imageHeight").toString()));
				return outfit;
			}

		} catch (Exception e) {
			Log.i("db", e.toString());
			return null;
		}
	}

	public int likeOrDislike(int outfitId, int type) {
		METHOD_NAME = "likeOrDislike";
		SOAP_ACTION = "http://tempuri.org/likeOrDislike";
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("type", type);
			rpc.addProperty("outfitId", outfitId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			Log.i("db", "success");
			if (Boolean.valueOf(soapObject.getProperty(0).toString()))
				return 0;
			else
				return 1;
		} catch (Exception e) {
			Log.i("db", e.toString());
			return -1;
		}
	}

	public int deletePublish(int outfitId) {
		METHOD_NAME = "deleteOutfit";
		SOAP_ACTION = "http://tempuri.org/deleteOutfit";
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("outfitId", outfitId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			Log.i("db", "success");
			if (Boolean.valueOf(soapObject.getProperty(0).toString()))
				return 0;
			else
				return 1;
		} catch (Exception e) {
			Log.i("db", e.toString());
			return -1;
		}
	}

	public int getLikes(int outfitId) {
		METHOD_NAME = "getLikes";
		SOAP_ACTION = "http://tempuri.org/getLikes";
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			rpc.addProperty("outfitId", outfitId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			Log.i("db", "success");
			return Integer.parseInt(soapObject.getProperty(0).toString());
		} catch (Exception e) {
			Log.i("db", e.toString());
			return -1;
		}
	}
}
