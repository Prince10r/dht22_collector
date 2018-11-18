package com.murphy.raspberry.dht.command;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessManager {
	
	private static ExecutorService fixedPool;
		
	private static final Logger LOG = LoggerFactory.getLogger(ProcessManager.class);
	
	public ProcessManager() {
		fixedPool = Executors.newFixedThreadPool(4);
	}
	
	public void send433Code(long code, int retry) {
		//FIXME: Move this to a static file
		String command = "./_433D -t9 -x" + retry + " " + code;
		String directory = "/home/pi/433_sniffer";
		fixedPool.execute(new InvokeCommand(directory , command));
	}
	
	public void queryDHT(CommandResultHandler commandResultHandler, int gpio) {
		String directory = "/home/pi/Adafruit_Python_DHT/examples";
		String command = "./AdafruitDHT.py 22 " + gpio;
		fixedPool.execute(new InvokeCommand(directory, command, commandResultHandler));
	}	
	
	public class InvokeCommand implements Runnable{
		
		private String command;
		private String directory;
		private boolean success;
		private CommandResultHandler cmdRstHndlr = null;
		
		public InvokeCommand(String directory, String command, CommandResultHandler cmdRstHndlr) {
			this.command = command;
			this.directory = directory;
		}
		
		public InvokeCommand(String directory, String command) {
			this.command = command;
			this.directory = directory;
		}
		
		public boolean wasSucessful() {
			return success;
		}
		
		
		@Override
		public void run() {
			Runtime rt = Runtime.getRuntime();

			Process process = null;
			// ProcessBuilder pb = new ProcessBuilder("CMD", "/C", "mvn.bat
			// dependency:tree");
			LOG.info("Invoking Command " + command + " @ directory: " + directory);
			ProcessBuilder pb = new ProcessBuilder("sh", "-c", command);			
			pb.directory(new File(directory));
			byte[] error = null;
			byte[] message = null;
			try {
				process = pb.start();				
				process.waitFor();
				error = process.getErrorStream().readAllBytes();
				message = process.getInputStream().readAllBytes();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (process != null && process.exitValue() == 0) {
					LOG.info("Command completed");
					if(cmdRstHndlr != null) {
						cmdRstHndlr.result(true, new String(message));
					}
				}else {
					String errorString = new String(error);
					LOG.info("Command failed, " + errorString);
					if(cmdRstHndlr != null) {
						cmdRstHndlr.result(true, errorString);
					}					
				}
				
			}
			
			success = process.exitValue() == 0;
		}
		
	}
		     
}
