/*
 * Copyright (C) 2014 Li Cong, forlong401@163.com http://www.360qihoo.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kesetel.mobile.helper;

import android.content.Context;

import com.forlong401.log.transaction.log.manager.ILogManager;
import com.forlong401.log.transaction.log.manager.LogManagerImpl;
import com.forlong401.log.transaction.utils.LogTool;

/**
 * Log manager.
 * 
 * @author Li Cong
 * @date 2014-3-23
 */
public class LogHelper {
	private static ILogManager sManager;

	/**
	 * Get singleton manager. DO NOT GET MANAGER BY THE CONTEXT OF ACTIVITY.
	 * 
	 * @param context
	 *            The context of application.
	 * @return
	 */
	public synchronized static void init(Context context) {
		if (context == null) {
			return;
		}

		if (sManager == null) {
			sManager = new LogManagerImpl(context);
		}
		sManager.registerCrashHandler();
	}

	public static ILogManager getLogMgr(){
		return sManager;
	}

	public static void logFile(String tag, String msg){
		sManager.log(tag, msg, LogTool.LOG_TYPE_2_FILE);
	}

	public static void logFileAndLogcat(String tag, String msg){
		sManager.log(tag, msg, LogTool.LOG_TYPE_2_FILE_AND_LOGCAT);
	}

	public static void logcat(String tag, String msg){
		sManager.log(tag, msg, LogTool.LOG_TYPE_2_LOGCAT);
	}
}
