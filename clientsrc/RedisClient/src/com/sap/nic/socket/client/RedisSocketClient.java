/**
 * 
 */
package com.sap.nic.socket.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import redis.clients.jedis.Jedis;

/**
 * @author llv22
 * 
 */
public class RedisSocketClient {

	/**
	 * scriptKey for Redis Socket Client http://redis.io/commands/keys and
	 * delete all keys
	 */
	static String scriptKey = "ed59d31dfe8aa712b1d2b49a1319fb9733da42e6";

	/**
	 * jedis client proxy
	 */
	static protected Jedis jedis;

	/**
	 * simple date format
	 */
	static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSSS");

	/**
	 * @param args
	 * @see redis.clients.jedis.tests.commands.ScriptingCommandsTest
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		jedis = new Jedis("localhost", 6379, 500);
		jedis.connect();
		jedis.auth("foobared");
		jedis.configSet("timeout", "300");
		// TODO : flush all is used to clear-up all keys
		// jedis.flushAll();
		Boolean exists = jedis.scriptExists(scriptKey);
		if (exists) {
			// TODO: script query and returning results
			String strAccessToken = UUID.randomUUID().toString();
			strAccessToken = "Item";
			String strTokenSecret = UUID.randomUUID().toString();
			strTokenSecret = strTokenSecret.replace("-", "");
			Date now = new Date();
			String creationTime = format.format(now);

			// TODO : must convert lua response as string type
			@SuppressWarnings("unchecked")
			List<String> response = (List<String>) jedis.evalsha(scriptKey, 3,
					strAccessToken, strTokenSecret, creationTime);
			int i = 0;
			for (String resItem : response) {
				if ((i & 01) == 0) {
					System.out.printf("%s", resItem);
				} else {
					System.out.printf("-%s\n", resItem);
				}
				i++;
			}
		}
	}

}
